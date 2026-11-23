package adocao.persistence.dto;
public record AnimalRequest(String nome, String especie, String raca, String porte, String faixaEtaria,
                            String energia, boolean sociavelCriancas, boolean sociavelAnimais,
                            boolean vacinado, boolean hipoalergenico) {}
