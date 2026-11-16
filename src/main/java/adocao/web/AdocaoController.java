package adocao.web;

import adocao.model.Animal;
import adocao.model.Especie;
import adocao.model.Moradia;
import adocao.model.Pessoa;
import adocao.model.Tempo;
import adocao.service.Abrigo;
import adocao.service.Dupla;
import adocao.service.Exemplo;
import adocao.service.Explicacao;
import adocao.service.RegrasService;
import adocao.service.ResultadoBusca;
import adocao.web.Dtos.AnimalDto;
import adocao.web.Dtos.BuscaRequest;
import adocao.web.Dtos.BuscaResponse;
import adocao.web.Dtos.PessoaDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdocaoController {

    private final Abrigo abrigo;
    private final RegrasService regras;

    public AdocaoController(Abrigo abrigo, RegrasService regras) {
        this.abrigo = abrigo;
        this.regras = regras;
    }

    // ---------- Consultas ----------

    /** Menu: "Ver os animais do abrigo". */
    @GetMapping("/animais")
    public List<AnimalDto> animais() {
        return abrigo.animais().stream().map(AnimalDto::de).toList();
    }

    /** Menu: "Ver animais com prioridade de adoção". */
    @GetMapping("/animais/prioritarios")
    public List<AnimalDto> prioritarios() {
        return regras.prioritarios().stream().map(AnimalDto::de).toList();
    }

    /** Menu: "Ver animais que precisam de atenção de saúde". */
    @GetMapping("/animais/saude")
    public List<AnimalDto> saude() {
        return regras.precisaAtencaoSaude().stream().map(AnimalDto::de).toList();
    }

    /** Raças da espécie (ou de todas, se a espécie for omitida ou "qualquer"). */
    @GetMapping("/racas")
    public List<String> racas(@RequestParam(required = false) String especie) {
        return regras.racas(lerOpcional(Especie.class, especie));
    }

    /** Menu: "Ver os perfis cadastrados". */
    @GetMapping("/pessoas")
    public List<PessoaDto> pessoas() {
        return abrigo.pessoas().stream().map(PessoaDto::de).toList();
    }

    /** recomendado/2 e recomendado_especie/3 para uma pessoa cadastrada. */
    @GetMapping("/pessoas/{nome}/recomendados")
    public List<AnimalDto> recomendados(@PathVariable String nome,
                                        @RequestParam(required = false) String especie) {
        Pessoa pessoa = pessoa(nome);
        Especie filtro = lerOpcional(Especie.class, especie);
        List<Animal> lista = filtro == null
            ? regras.recomendados(pessoa)
            : regras.recomendadosPorEspecie(pessoa, filtro);
        return lista.stream().map(AnimalDto::de).toList();
    }

    /** dupla_compativel/3. */
    @GetMapping("/pessoas/{nome}/duplas")
    public List<Dupla> duplas(@PathVariable String nome) {
        return regras.duplasCompativeis(pessoa(nome));
    }

    /** Menu: "Entender por que um animal não foi recomendado". */
    @GetMapping("/explicar")
    public Explicacao explicar(@RequestParam String animal, @RequestParam String pessoa) {
        return regras.explicar(animal(animal), pessoa(pessoa));
    }

    /** Menu: "Rodar as consultas de exemplo". */
    @GetMapping("/exemplos")
    public List<Exemplo> exemplos() {
        return regras.exemplos();
    }

    // ---------- Busca (modo interativo) ----------

    /** Menu: "Buscar um animal para mim". O perfil do visitante não é guardado. */
    @PostMapping("/buscar")
    public BuscaResponse buscar(@RequestBody BuscaRequest req) {
        Pessoa visitante = new Pessoa(
            "visitante",
            lerObrigatorio(Moradia.class, req.moradia(), "moradia"),
            req.criancas(),
            req.animais(),
            lerObrigatorio(Tempo.class, req.tempo(), "tempo"),
            req.alergico(),
            req.iniciante());

        String raca = (req.raca() == null || req.raca().equalsIgnoreCase("qualquer")) ? null : req.raca();
        ResultadoBusca resultado = regras.buscar(visitante, lerOpcional(Especie.class, req.especie()), raca);

        return new BuscaResponse(
            resultado.situacao().name(),
            resultado.animais().stream().map(AnimalDto::de).toList());
    }

    // ---------- Auxiliares ----------

    private Animal animal(String nome) {
        return abrigo.animal(nome).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal " + nome + " nao existe na base."));
    }

    private Pessoa pessoa(String nome) {
        return abrigo.pessoa(nome).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa " + nome + " nao esta cadastrada."));
    }

    /** Converte "gato" em Especie.GATO. Nulo, vazio ou "qualquer" significam sem restrição. */
    private static <E extends Enum<E>> E lerOpcional(Class<E> tipo, String valor) {
        if (valor == null || valor.isBlank() || valor.equalsIgnoreCase("qualquer")) return null;
        return converter(tipo, valor, tipo.getSimpleName().toLowerCase());
    }

    private static <E extends Enum<E>> E lerObrigatorio(Class<E> tipo, String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo obrigatorio: " + campo);
        }
        return converter(tipo, valor, campo);
    }

    private static <E extends Enum<E>> E converter(Class<E> tipo, String valor, String campo) {
        try {
            return Enum.valueOf(tipo, valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor invalido para " + campo + ": " + valor);
        }
    }
}
