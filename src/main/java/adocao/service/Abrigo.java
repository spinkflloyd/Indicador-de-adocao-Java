package adocao.service;

import adocao.model.Animal;
import adocao.model.Pessoa;
import adocao.model.Especie;
import adocao.model.Porte;
import adocao.model.FaixaEtaria;
import adocao.model.Energia;
import adocao.model.Moradia;
import adocao.model.Tempo;
import adocao.persistence.entity.AnimalEntity;
import adocao.persistence.entity.PessoaEntity;
import adocao.persistence.repository.AnimalRepository;
import adocao.persistence.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

/** Consulta os cadastros persistidos no PostgreSQL e os converte para os modelos usados pelas regras. */
@Service
public class Abrigo {
    private final AnimalRepository animaisRepo;
    private final PessoaRepository pessoasRepo;
    private final List<Animal> animaisTeste;
    private final List<Pessoa> pessoasTeste;
    @Autowired
    public Abrigo(AnimalRepository animaisRepo, PessoaRepository pessoasRepo) { this.animaisRepo=animaisRepo; this.pessoasRepo=pessoasRepo; this.animaisTeste=null; this.pessoasTeste=null; }
    /** Construtor usado apenas pelos testes unitários das regras, sem subir o Spring ou o banco. */
    public Abrigo() {
        this.animaisRepo=null; this.pessoasRepo=null;
        this.animaisTeste=List.of(
            new Animal("rex",Especie.CACHORRO,"pastor_alemao",Porte.GRANDE,FaixaEtaria.ADULTO,Energia.ALTA,true,false,true,false),
            new Animal("mel",Especie.CACHORRO,"labrador",Porte.MEDIO,FaixaEtaria.FILHOTE,Energia.MEDIA,true,true,true,false),
            new Animal("thor",Especie.CACHORRO,"poodle",Porte.PEQUENO,FaixaEtaria.IDOSO,Energia.BAIXA,false,true,true,true),
            new Animal("luna",Especie.GATO,"siames",Porte.PEQUENO,FaixaEtaria.ADULTO,Energia.MEDIA,true,true,true,true),
            new Animal("mimi",Especie.GATO,"srd",Porte.PEQUENO,FaixaEtaria.FILHOTE,Energia.ALTA,true,false,true,false),
            new Animal("bob",Especie.CACHORRO,"srd",Porte.MEDIO,FaixaEtaria.ADULTO,Energia.ALTA,true,true,false,false),
            new Animal("nina",Especie.COELHO,"mini_lop",Porte.PEQUENO,FaixaEtaria.ADULTO,Energia.BAIXA,false,false,true,false),
            new Animal("simba",Especie.GATO,"persa",Porte.PEQUENO,FaixaEtaria.IDOSO,Energia.BAIXA,true,true,true,false));
        this.pessoasTeste=List.of(
            new Pessoa("ana",Moradia.APARTAMENTO,false,false,Tempo.POUCO,false,false),
            new Pessoa("bruno",Moradia.CASA_QUINTAL,true,true,Tempo.MUITO,false,false),
            new Pessoa("carla",Moradia.CASA_PEQUENA,true,false,Tempo.MEDIO,false,false),
            new Pessoa("diego",Moradia.APARTAMENTO,false,false,Tempo.POUCO,true,true));
    }
    public List<Animal> animais() { return animaisRepo==null ? animaisTeste : animaisRepo.findAllByOrderByIdAsc().stream().map(Abrigo::modelo).toList(); }
    public List<Pessoa> pessoas() { return pessoasRepo==null ? pessoasTeste : pessoasRepo.findAllByOrderByIdAsc().stream().map(Abrigo::modelo).toList(); }
    public Optional<Animal> animal(String nome) { return animaisRepo==null ? animaisTeste.stream().filter(a->a.nome().equalsIgnoreCase(nome)).findFirst() : animaisRepo.findByNomeIgnoreCase(nome).map(Abrigo::modelo); }
    public Optional<Pessoa> pessoa(String nome) { return pessoasRepo==null ? pessoasTeste.stream().filter(p->p.nome().equalsIgnoreCase(nome)).findFirst() : pessoasRepo.findByNomeIgnoreCase(nome).map(Abrigo::modelo); }
    public static Animal modelo(AnimalEntity a) { return new Animal(a.getNome(),a.getEspecie(),a.getRaca(),a.getPorte(),a.getFaixaEtaria(),a.getEnergia(),a.isSociavelCriancas(),a.isSociavelAnimais(),a.isVacinado(),a.isHipoalergenico()); }
    public static Pessoa modelo(PessoaEntity p) { return new Pessoa(p.getNome(),p.getMoradia(),p.isCriancas(),p.isAnimais(),p.getTempo(),p.isAlergico(),p.isIniciante()); }
}
