package adocao.persistence.dto;
import adocao.persistence.entity.AnimalEntity;
public record AnimalAdminDto(Long id, String nome, String especie, String raca, String porte, String faixaEtaria,
                             String energia, boolean sociavelCriancas, boolean sociavelAnimais,
                             boolean vacinado, boolean hipoalergenico) {
    public static AnimalAdminDto de(AnimalEntity a) { return new AnimalAdminDto(a.getId(),a.getNome(),a.getEspecie().toString(),a.getRaca(),a.getPorte().toString(),a.getFaixaEtaria().toString(),a.getEnergia().toString(),a.isSociavelCriancas(),a.isSociavelAnimais(),a.isVacinado(),a.isHipoalergenico()); }
}
