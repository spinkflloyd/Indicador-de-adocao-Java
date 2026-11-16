package adocao.service;

import adocao.model.*;

import java.util.List;
import java.util.Optional;

/** Base de fatos: os animais do abrigo e os interessados cadastrados. */
public class Abrigo {

    // Campos: nome, espécie, raça, porte, faixa etária, energia,
    //         sociável com crianças, sociável com animais, vacinado, hipoalergênico
    private final List<Animal> animais = List.of(
        new Animal("rex",   Especie.CACHORRO, "pastor_alemao", Porte.GRANDE,  FaixaEtaria.ADULTO,  Energia.ALTA,  true,  false, true,  false),
        new Animal("mel",   Especie.CACHORRO, "labrador",      Porte.MEDIO,   FaixaEtaria.FILHOTE, Energia.MEDIA, true,  true,  true,  false),
        new Animal("thor",  Especie.CACHORRO, "poodle",        Porte.PEQUENO, FaixaEtaria.IDOSO,   Energia.BAIXA, false, true,  true,  true),
        new Animal("luna",  Especie.GATO,     "siames",        Porte.PEQUENO, FaixaEtaria.ADULTO,  Energia.MEDIA, true,  true,  true,  true),
        new Animal("mimi",  Especie.GATO,     "srd",           Porte.PEQUENO, FaixaEtaria.FILHOTE, Energia.ALTA,  true,  false, true,  false),
        new Animal("bob",   Especie.CACHORRO, "srd",           Porte.MEDIO,   FaixaEtaria.ADULTO,  Energia.ALTA,  true,  true,  false, false),
        new Animal("nina",  Especie.COELHO,   "mini_lop",      Porte.PEQUENO, FaixaEtaria.ADULTO,  Energia.BAIXA, false, false, true,  false),
        new Animal("simba", Especie.GATO,     "persa",         Porte.PEQUENO, FaixaEtaria.IDOSO,   Energia.BAIXA, true,  true,  true,  false)
    );

    // Campos: nome, moradia, tem crianças, tem animais, tempo livre, alérgico, iniciante
    private final List<Pessoa> pessoas = List.of(
        new Pessoa("ana",   Moradia.APARTAMENTO,  false, false, Tempo.POUCO, false, false),
        new Pessoa("bruno", Moradia.CASA_QUINTAL, true,  true,  Tempo.MUITO, false, false),
        new Pessoa("carla", Moradia.CASA_PEQUENA, true,  false, Tempo.MEDIO, false, false),
        new Pessoa("diego", Moradia.APARTAMENTO,  false, false, Tempo.POUCO, true,  true)
    );

    public List<Animal> animais() { return animais; }

    public List<Pessoa> pessoas() { return pessoas; }

    public Optional<Animal> animal(String nome) {
        return animais.stream().filter(a -> a.nome().equalsIgnoreCase(nome)).findFirst();
    }

    public Optional<Pessoa> pessoa(String nome) {
        return pessoas.stream().filter(p -> p.nome().equalsIgnoreCase(nome)).findFirst();
    }
}
