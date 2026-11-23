package adocao.persistence.service;

import adocao.model.*;
import adocao.persistence.entity.AnimalEntity;
import adocao.persistence.entity.PessoaEntity;
import adocao.persistence.repository.AnimalRepository;
import adocao.persistence.repository.PessoaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DadosIniciais implements CommandLineRunner {
    private final AnimalRepository animais; private final PessoaRepository pessoas;
    public DadosIniciais(AnimalRepository animais, PessoaRepository pessoas) { this.animais=animais; this.pessoas=pessoas; }
    @Override public void run(String... args) {
        if (animais.count()==0) {
            animais.save(new AnimalEntity("rex",Especie.CACHORRO,"pastor_alemao",Porte.GRANDE,FaixaEtaria.ADULTO,Energia.ALTA,true,false,true,false));
            animais.save(new AnimalEntity("mel",Especie.CACHORRO,"labrador",Porte.MEDIO,FaixaEtaria.FILHOTE,Energia.MEDIA,true,true,true,false));
            animais.save(new AnimalEntity("thor",Especie.CACHORRO,"poodle",Porte.PEQUENO,FaixaEtaria.IDOSO,Energia.BAIXA,false,true,true,true));
            animais.save(new AnimalEntity("luna",Especie.GATO,"siames",Porte.PEQUENO,FaixaEtaria.ADULTO,Energia.MEDIA,true,true,true,true));
            animais.save(new AnimalEntity("mimi",Especie.GATO,"srd",Porte.PEQUENO,FaixaEtaria.FILHOTE,Energia.ALTA,true,false,true,false));
            animais.save(new AnimalEntity("bob",Especie.CACHORRO,"srd",Porte.MEDIO,FaixaEtaria.ADULTO,Energia.ALTA,true,true,false,false));
            animais.save(new AnimalEntity("nina",Especie.COELHO,"mini_lop",Porte.PEQUENO,FaixaEtaria.ADULTO,Energia.BAIXA,false,false,true,false));
            animais.save(new AnimalEntity("simba",Especie.GATO,"persa",Porte.PEQUENO,FaixaEtaria.IDOSO,Energia.BAIXA,true,true,true,false));
        }
        if (pessoas.count()==0) {
            pessoas.save(new PessoaEntity("ana",Moradia.APARTAMENTO,false,false,Tempo.POUCO,false,false));
            pessoas.save(new PessoaEntity("bruno",Moradia.CASA_QUINTAL,true,true,Tempo.MUITO,false,false));
            pessoas.save(new PessoaEntity("carla",Moradia.CASA_PEQUENA,true,false,Tempo.MEDIO,false,false));
            pessoas.save(new PessoaEntity("diego",Moradia.APARTAMENTO,false,false,Tempo.POUCO,true,true));
        }
    }
}
