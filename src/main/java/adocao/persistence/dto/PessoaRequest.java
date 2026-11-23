package adocao.persistence.dto;
public record PessoaRequest(String nome, String moradia, boolean criancas, boolean animais,
                            String tempo, boolean alergico, boolean iniciante) {}
