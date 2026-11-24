package adocao.persistence.web;

import adocao.persistence.dto.AnuncioResumoDto;
import adocao.persistence.entity.AnuncioEntity;
import adocao.persistence.repository.AnuncioRepository;
import adocao.service.LimiteDeEnvios;
import adocao.service.Telefone;
import adocao.service.TipoImagem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/anuncios")
public class AnuncioPublicoController {

    private static final long MAX_FOTO = 2L * 1024 * 1024;
    static final String APROVADO = "APROVADO";
    static final String PENDENTE = "PENDENTE";

    private final AnuncioRepository repository;
    private final LimiteDeEnvios limite;
    private final boolean moderacao;

    public AnuncioPublicoController(AnuncioRepository repository, LimiteDeEnvios limite,
                                    @Value("${adocao.moderacao:false}") boolean moderacao) {
        this.repository = repository;
        this.limite = limite;
        this.moderacao = moderacao;
    }

    /** Só anúncios aprovados, e sem carregar as fotos (cada uma é buscada em /{id}/foto). */
    @GetMapping
    public List<Map<String, Object>> listar() {
        return repository.listarAprovados().stream().map(AnuncioPublicoController::paraJson).toList();
    }

    private static Map<String, Object> paraJson(AnuncioResumoDto a) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", a.id());
        m.put("nome", a.nome());
        m.put("especie", a.especie());
        m.put("raca", a.raca());
        m.put("porte", a.porte());
        m.put("faixaEtaria", a.faixaEtaria());
        m.put("energia", a.energia());
        m.put("sociavelCriancas", a.sociavelCriancas());
        m.put("sociavelAnimais", a.sociavelAnimais());
        m.put("vacinado", a.vacinado());
        m.put("hipoalergenico", a.hipoalergenico());
        m.put("descricao", a.descricao());
        m.put("responsavel", a.responsavel());
        m.put("cidade", a.cidade());
        m.put("whatsapp", a.whatsapp());
        m.put("fotoUrl", "/api/anuncios/" + a.id() + "/foto");
        return m;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> criar(
            @RequestParam String nome, @RequestParam String especie, @RequestParam String raca,
            @RequestParam String porte, @RequestParam String faixaEtaria, @RequestParam String energia,
            @RequestParam(defaultValue = "false") boolean sociavelCriancas,
            @RequestParam(defaultValue = "false") boolean sociavelAnimais,
            @RequestParam(defaultValue = "false") boolean vacinado,
            @RequestParam(defaultValue = "false") boolean hipoalergenico,
            @RequestParam String descricao, @RequestParam String responsavel, @RequestParam String whatsapp,
            @RequestParam String cidade, @RequestParam MultipartFile foto,
            @RequestParam(defaultValue = "false") boolean consentimento,
            @RequestParam(defaultValue = "") String website,
            HttpServletRequest request) {

        // Campo escondido no formulário: pessoas não veem, robôs preenchem. Finge sucesso e não grava nada.
        if (!website.isBlank()) {
            return Map.of("mensagem", "Anúncio publicado com sucesso!", "status", APROVADO);
        }

        nome = texto(nome, 80, "nome");
        raca = texto(raca, 100, "raça");
        descricao = texto(descricao, 1200, "descrição");
        responsavel = texto(responsavel, 80, "responsável");
        cidade = texto(cidade, 100, "cidade");
        try {
            whatsapp = Telefone.normalizar(whatsapp);
        } catch (IllegalArgumentException e) {
            throw erro(e.getMessage());
        }
        especie = enumTexto(especie, "cachorro", "gato", "coelho");
        porte = enumTexto(porte, "pequeno", "medio", "grande");
        faixaEtaria = enumTexto(faixaEtaria, "filhote", "adulto", "idoso");
        energia = enumTexto(energia, "baixa", "media", "alta");
        if (!consentimento) {
            throw erro("É preciso autorizar a divulgação do seu nome e do seu WhatsApp para publicar o anúncio.");
        }

        if (foto.isEmpty()) throw erro("Envie uma foto do animal.");
        if (foto.getSize() > MAX_FOTO) throw erro("A foto deve ter no máximo 2 MB.");
        byte[] bytes;
        try {
            bytes = foto.getBytes();
        } catch (IOException e) {
            throw erro("Não foi possível ler a foto.");
        }
        // O tipo vem dos bytes do arquivo, e não do que o navegador informa.
        String tipo = TipoImagem.detectar(bytes);
        if (tipo == null) throw erro("Envie uma imagem JPG, PNG ou WEBP.");

        if (!limite.permitir(ipDe(request), Instant.now())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Muitos anúncios em pouco tempo. Tente novamente mais tarde.");
        }

        String status = moderacao ? PENDENTE : APROVADO;
        AnuncioEntity a = repository.save(new AnuncioEntity(nome, especie, raca, porte, faixaEtaria, energia,
            sociavelCriancas, sociavelAnimais, vacinado, hipoalergenico, descricao, responsavel, whatsapp, cidade,
            status, tipo, bytes));

        String mensagem = moderacao
            ? "Anúncio recebido! Ele aparecerá no site depois de aprovado pela administração."
            : "Anúncio publicado com sucesso!";
        return Map.of("mensagem", mensagem, "id", a.getId(), "status", status);
    }

    /** A foto de um anúncio. Anúncios pendentes não são servidos ao público. */
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> foto(@PathVariable Long id) {
        AnuncioEntity a = repository.findById(id)
            .filter(x -> APROVADO.equals(x.getStatus()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(a.getFotoTipo()))
            .header("X-Content-Type-Options", "nosniff")
            .cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic())
            .body(a.getFoto());
    }

    /**
     * Identifica o visitante pelo IP. Atrás do proxy do Render vale o primeiro endereço de X-Forwarded-For.
     * Esse cabeçalho pode ser forjado, por isso o limite geral é a proteção de fato; o limite por IP ajuda no uso normal.
     */
    private static String ipDe(HttpServletRequest request) {
        String encaminhado = request.getHeader("X-Forwarded-For");
        if (encaminhado != null && !encaminhado.isBlank()) return encaminhado.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private static String texto(String v, int max, String campo) {
        if (v == null || v.isBlank() || v.trim().length() > max) {
            throw erro("Confira o campo " + campo + " (máximo " + max + " caracteres).");
        }
        return v.trim();
    }

    private static String enumTexto(String v, String... permitidos) {
        String s = v == null ? "" : v.trim().toLowerCase(Locale.ROOT);
        if (!List.of(permitidos).contains(s)) throw erro("Valor inválido: " + s);
        return s;
    }

    private static ResponseStatusException erro(String m) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, m);
    }
}
