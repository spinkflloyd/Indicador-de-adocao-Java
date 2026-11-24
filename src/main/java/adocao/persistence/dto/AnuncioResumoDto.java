package adocao.persistence.dto;

/** Anúncio sem a foto: é o que a listagem pública carrega do banco (a foto é buscada à parte, uma por vez). */
public record AnuncioResumoDto(Long id, String nome, String especie, String raca, String porte, String faixaEtaria,
                               String energia, Boolean sociavelCriancas, Boolean sociavelAnimais, Boolean vacinado,
                               Boolean hipoalergenico, String descricao, String responsavel, String cidade,
                               String whatsapp) {}
