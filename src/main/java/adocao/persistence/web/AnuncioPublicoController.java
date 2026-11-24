package adocao.persistence.web;

import adocao.persistence.entity.AnuncioEntity;
import adocao.persistence.repository.AnuncioRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/anuncios")
public class AnuncioPublicoController {
    private static final long MAX_FOTO = 5L * 1024 * 1024;
    private final AnuncioRepository repository;
    public AnuncioPublicoController(AnuncioRepository repository){this.repository=repository;}

    @GetMapping
    public List<Map<String,Object>> listar(){
        return repository.findAll().stream().map(a -> Map.<String,Object>ofEntries(
            Map.entry("id",a.getId()), Map.entry("nome",a.getNome()), Map.entry("especie",a.getEspecie()),
            Map.entry("raca",a.getRaca()), Map.entry("porte",a.getPorte()), Map.entry("faixaEtaria",a.getFaixaEtaria()),
            Map.entry("energia",a.getEnergia()), Map.entry("sociavelCriancas",a.isSociavelCriancas()),
            Map.entry("sociavelAnimais",a.isSociavelAnimais()), Map.entry("vacinado",a.isVacinado()),
            Map.entry("hipoalergenico",a.isHipoalergenico()), Map.entry("descricao",a.getDescricao()),
            Map.entry("responsavel",a.getResponsavel()), Map.entry("cidade",a.getCidade()),
            Map.entry("whatsapp",a.getWhatsapp()), Map.entry("fotoUrl","/api/anuncios/"+a.getId()+"/foto")
        )).toList();
    }

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> criar(@RequestParam String nome,@RequestParam String especie,@RequestParam String raca,
        @RequestParam String porte,@RequestParam String faixaEtaria,@RequestParam String energia,
        @RequestParam(defaultValue="false") boolean sociavelCriancas,@RequestParam(defaultValue="false") boolean sociavelAnimais,
        @RequestParam(defaultValue="false") boolean vacinado,@RequestParam(defaultValue="false") boolean hipoalergenico,
        @RequestParam String descricao,@RequestParam String responsavel,@RequestParam String whatsapp,
        @RequestParam String cidade,@RequestParam MultipartFile foto) {
        nome=texto(nome,80,"nome"); raca=texto(raca,100,"raça"); descricao=texto(descricao,1200,"descrição");
        responsavel=texto(responsavel,80,"responsável"); cidade=texto(cidade,100,"cidade");
        whatsapp=whatsapp==null?"":whatsapp.replaceAll("\\D","");
        if(whatsapp.length()<10 || whatsapp.length()>13) throw erro("Informe um WhatsApp com DDD e número.");
        especie=enumTexto(especie,"cachorro","gato","coelho"); porte=enumTexto(porte,"pequeno","medio","grande");
        faixaEtaria=enumTexto(faixaEtaria,"filhote","adulto","idoso"); energia=enumTexto(energia,"baixa","media","alta");
        if(foto==null || foto.isEmpty()) throw erro("Envie uma foto do animal.");
        if(foto.getSize()>MAX_FOTO) throw erro("A foto deve ter no máximo 5 MB.");
        String tipo=foto.getContentType(); if(!List.of("image/jpeg","image/png","image/webp").contains(tipo)) throw erro("Envie uma imagem JPG, PNG ou WEBP.");
        try {
            AnuncioEntity a=repository.save(new AnuncioEntity(nome,especie,raca,porte,faixaEtaria,energia,sociavelCriancas,sociavelAnimais,vacinado,hipoalergenico,descricao,responsavel,whatsapp,cidade,tipo,foto.getBytes()));
            return Map.of("mensagem","Anúncio publicado com sucesso!","id",a.getId());
        } catch(IOException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Não foi possível ler a foto.");}
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> foto(@PathVariable Long id){
        AnuncioEntity a=repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(a.getFotoTipo())).cacheControl(CacheControl.maxAge(java.time.Duration.ofHours(1))).body(a.getFoto());
    }
    private static String texto(String v,int max,String campo){if(v==null||v.isBlank()||v.trim().length()>max)throw erro("Confira o campo "+campo+" (máximo "+max+" caracteres).");return v.trim();}
    private static String enumTexto(String v,String... permitidos){String s=v==null?"":v.trim().toLowerCase(Locale.ROOT);if(!List.of(permitidos).contains(s))throw erro("Valor inválido: "+s);return s;}
    private static ResponseStatusException erro(String m){return new ResponseStatusException(HttpStatus.BAD_REQUEST,m);}
}
