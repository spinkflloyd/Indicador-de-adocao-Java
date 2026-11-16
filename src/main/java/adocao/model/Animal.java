package adocao.model;

/**
 * Junta os fatos animal/5, raca/2, sociavel_criancas/1, sociavel_animais/1,
 * vacinado/1 e hipoalergenico/1 do Prolog.
 */
public record Animal(String nome, Especie especie, String raca, Porte porte,
                     FaixaEtaria faixaEtaria, Energia energia,
                     boolean sociavelCriancas, boolean sociavelAnimais,
                     boolean vacinado, boolean hipoalergenico) {}
