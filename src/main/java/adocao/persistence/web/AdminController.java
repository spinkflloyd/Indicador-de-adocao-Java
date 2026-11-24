package adocao.persistence.web;

import adocao.model.*;
import adocao.persistence.dto.*;
import adocao.persistence.entity.*;
import adocao.persistence.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AnimalRepository animais; private final PessoaRepository pessoas; private final InteresseRepository interesses;
    private final AnuncioRepository anuncios;
    private final String chave;
    public AdminController(AnimalRepository animais, PessoaRepository pessoas, InteresseRepository interesses,
                           AnuncioRepository anuncios, @Value("${adocao.chave-admin:}") String chave) {
        this.animais=animais; this.pessoas=pessoas; this.interesses=interesses; this.anuncios=anuncios; this.chave=chave;
    }
    @ModelAttribute
    public void autorizar(@RequestHeader(value="X-Admin-Key", required=false) String enviada) {
        if (chave.isBlank() || enviada==null || !java.security.MessageDigest.isEqual(chave.getBytes(java.nio.charset.StandardCharsets.UTF_8),enviada.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Chave administrativa inválida ou não configurada.");
    }

    @GetMapping("/animais") public List<AnimalAdminDto> listarAnimais(){return animais.findAllByOrderByIdAsc().stream().map(AnimalAdminDto::de).toList();}
    @PostMapping("/animais") @ResponseStatus(HttpStatus.CREATED) public AnimalAdminDto criarAnimal(@RequestBody AnimalRequest r){
        String nome=texto(r.nome()); if(animais.existsByNomeIgnoreCase(nome)) throw conflito("Já existe um animal com esse nome.");
        return AnimalAdminDto.de(animais.save(novoAnimal(r,nome)));
    }
    @PutMapping("/animais/{id}") public AnimalAdminDto atualizarAnimal(@PathVariable Long id,@RequestBody AnimalRequest r){
        AnimalEntity a=animais.findById(id).orElseThrow(()->naoEncontrado("Animal não encontrado."));
        String nome=texto(r.nome()); if(animais.findByNomeIgnoreCase(nome).filter(x->!x.getId().equals(id)).isPresent()) throw conflito("Já existe outro animal com esse nome.");
        preencher(a,r,nome); return AnimalAdminDto.de(animais.save(a));
    }
    @DeleteMapping("/animais/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional public void excluirAnimal(@PathVariable Long id){
        if(!animais.existsById(id)) throw naoEncontrado("Animal não encontrado.");
        if(interesses.existsByAnimalId(id)) throw conflito("Este animal possui pedidos de interesse. Exclua ou altere esses pedidos antes.");
        animais.deleteById(id);
    }

    @GetMapping("/pessoas") public List<PessoaAdminDto> listarPessoas(){return pessoas.findAllByOrderByIdAsc().stream().map(PessoaAdminDto::de).toList();}
    @PostMapping("/pessoas") @ResponseStatus(HttpStatus.CREATED) public PessoaAdminDto criarPessoa(@RequestBody PessoaRequest r){
        String nome=texto(r.nome()); if(pessoas.existsByNomeIgnoreCase(nome)) throw conflito("Já existe uma pessoa com esse nome.");
        return PessoaAdminDto.de(pessoas.save(novaPessoa(r,nome)));
    }
    @PutMapping("/pessoas/{id}") public PessoaAdminDto atualizarPessoa(@PathVariable Long id,@RequestBody PessoaRequest r){
        PessoaEntity p=pessoas.findById(id).orElseThrow(()->naoEncontrado("Pessoa não encontrada."));
        String nome=texto(r.nome()); if(pessoas.findByNomeIgnoreCase(nome).filter(x->!x.getId().equals(id)).isPresent()) throw conflito("Já existe outra pessoa com esse nome.");
        preencher(p,r,nome); return PessoaAdminDto.de(pessoas.save(p));
    }
    @DeleteMapping("/pessoas/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluirPessoa(@PathVariable Long id){if(!pessoas.existsById(id))throw naoEncontrado("Pessoa não encontrada."); pessoas.deleteById(id);}

    @GetMapping("/interesses") public List<InteresseAdminDto> listarInteresses(){return interesses.findAll().stream().sorted(java.util.Comparator.comparing(InteresseEntity::getQuando)).map(InteresseAdminDto::de).toList();}
    @PostMapping("/interesses") @ResponseStatus(HttpStatus.CREATED) public InteresseAdminDto criarInteresse(@RequestBody InteresseRequestAdmin r){return InteresseAdminDto.de(interesses.save(novoInteresse(r)));}
    @PutMapping("/interesses/{id}") public InteresseAdminDto atualizarInteresse(@PathVariable Long id,@RequestBody InteresseRequestAdmin r){
        InteresseEntity i=interesses.findById(id).orElseThrow(()->naoEncontrado("Interesse não encontrado."));
        preencher(i,r); return InteresseAdminDto.de(interesses.save(i));
    }
    @DeleteMapping("/interesses/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluirInteresse(@PathVariable Long id){if(!interesses.existsById(id))throw naoEncontrado("Interesse não encontrado."); interesses.deleteById(id);}

    // ---------- Anúncios da comunidade ----------
    @GetMapping("/anuncios") public List<AnuncioAdminDto> listarAnuncios(){return anuncios.listarParaAdmin();}
    @PutMapping("/anuncios/{id}/status") public Map<String,Object> alterarStatusAnuncio(@PathVariable Long id,@RequestBody Map<String,String> corpo){
        String status=corpo.getOrDefault("status","").trim().toUpperCase(Locale.ROOT);
        if(!status.equals("APROVADO") && !status.equals("PENDENTE")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status inválido. Use APROVADO ou PENDENTE.");
        if(anuncios.atualizarStatus(id,status)==0) throw naoEncontrado("Anúncio não encontrado.");
        return Map.of("id",id,"status",status);
    }
    @DeleteMapping("/anuncios/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluirAnuncio(@PathVariable Long id){
        if(!anuncios.existsById(id)) throw naoEncontrado("Anúncio não encontrado.");
        anuncios.deleteById(id);
    }
    /** A foto de qualquer anúncio (inclusive pendente), para a moderação. */
    @GetMapping("/anuncios/{id}/foto") public ResponseEntity<byte[]> fotoAnuncio(@PathVariable Long id){
        AnuncioEntity a=anuncios.findById(id).orElseThrow(()->naoEncontrado("Anúncio não encontrado."));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(a.getFotoTipo())).header("X-Content-Type-Options","nosniff").cacheControl(CacheControl.noStore()).body(a.getFoto());
    }

    private AnimalEntity novoAnimal(AnimalRequest r,String n){return new AnimalEntity(n,enumVal(Especie.class,r.especie()),texto(r.raca()),enumVal(Porte.class,r.porte()),enumVal(FaixaEtaria.class,r.faixaEtaria()),enumVal(Energia.class,r.energia()),r.sociavelCriancas(),r.sociavelAnimais(),r.vacinado(),r.hipoalergenico());}
    private void preencher(AnimalEntity a,AnimalRequest r,String n){a.setNome(n);a.setEspecie(enumVal(Especie.class,r.especie()));a.setRaca(texto(r.raca()));a.setPorte(enumVal(Porte.class,r.porte()));a.setFaixaEtaria(enumVal(FaixaEtaria.class,r.faixaEtaria()));a.setEnergia(enumVal(Energia.class,r.energia()));a.setSociavelCriancas(r.sociavelCriancas());a.setSociavelAnimais(r.sociavelAnimais());a.setVacinado(r.vacinado());a.setHipoalergenico(r.hipoalergenico());}
    private PessoaEntity novaPessoa(PessoaRequest r,String n){return new PessoaEntity(n,enumVal(Moradia.class,r.moradia()),r.criancas(),r.animais(),enumVal(Tempo.class,r.tempo()),r.alergico(),r.iniciante());}
    private void preencher(PessoaEntity p,PessoaRequest r,String n){p.setNome(n);p.setMoradia(enumVal(Moradia.class,r.moradia()));p.setCriancas(r.criancas());p.setAnimais(r.animais());p.setTempo(enumVal(Tempo.class,r.tempo()));p.setAlergico(r.alergico());p.setIniciante(r.iniciante());}
    private InteresseEntity novoInteresse(InteresseRequestAdmin r){AnimalEntity a=animais.findById(r.animalId()).orElseThrow(()->naoEncontrado("Animal do interesse não encontrado."));return new InteresseEntity(java.time.Instant.now(),a,texto(r.nome()),texto(r.contato()),r.mensagem()==null?"":r.mensagem().trim());}
    private void preencher(InteresseEntity i,InteresseRequestAdmin r){AnimalEntity a=animais.findById(r.animalId()).orElseThrow(()->naoEncontrado("Animal do interesse não encontrado."));i.setAnimal(a);i.setNome(texto(r.nome()));i.setContato(texto(r.contato()));i.setMensagem(r.mensagem()==null?"":r.mensagem().trim());}
    private static String texto(String s){if(s==null||s.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Preencha todos os campos de texto obrigatórios.");return s.trim();}
    private static <E extends Enum<E>> E enumVal(Class<E> c,String s){try{return Enum.valueOf(c,s.trim().toUpperCase(Locale.ROOT));}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Valor inválido para "+c.getSimpleName()+": "+s);}}
    private static ResponseStatusException naoEncontrado(String m){return new ResponseStatusException(HttpStatus.NOT_FOUND,m);}
    private static ResponseStatusException conflito(String m){return new ResponseStatusException(HttpStatus.CONFLICT,m);}
}
