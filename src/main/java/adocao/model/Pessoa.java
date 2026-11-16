package adocao.model;

/** Junta pessoa/5, alergico/1 e iniciante/1 do Prolog. */
public record Pessoa(String nome, Moradia moradia, boolean criancas, boolean animais,
                     Tempo tempo, boolean alergico, boolean iniciante) {}
