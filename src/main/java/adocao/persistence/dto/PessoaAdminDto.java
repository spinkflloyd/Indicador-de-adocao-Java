package adocao.persistence.dto;
import adocao.persistence.entity.PessoaEntity;
public record PessoaAdminDto(Long id, String nome, String moradia, boolean criancas, boolean animais,
                             String tempo, boolean alergico, boolean iniciante) {
    public static PessoaAdminDto de(PessoaEntity p) { return new PessoaAdminDto(p.getId(),p.getNome(),p.getMoradia().toString(),p.isCriancas(),p.isAnimais(),p.getTempo().toString(),p.isAlergico(),p.isIniciante()); }
}
