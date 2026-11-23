package adocao.persistence.entity;

import adocao.model.*;
import jakarta.persistence.*;

@Entity
@Table(name = "animais")
public class AnimalEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80) private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Especie especie;
    @Column(nullable = false, length = 100) private String raca;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Porte porte;
    @Enumerated(EnumType.STRING) @Column(name = "faixa_etaria", nullable = false, length = 20) private FaixaEtaria faixaEtaria;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Energia energia;
    @Column(name = "sociavel_criancas", nullable = false) private boolean sociavelCriancas;
    @Column(name = "sociavel_animais", nullable = false) private boolean sociavelAnimais;
    @Column(nullable = false) private boolean vacinado;
    @Column(nullable = false) private boolean hipoalergenico;

    protected AnimalEntity() {}
    public AnimalEntity(String nome, Especie especie, String raca, Porte porte, FaixaEtaria faixaEtaria,
                        Energia energia, boolean sociavelCriancas, boolean sociavelAnimais,
                        boolean vacinado, boolean hipoalergenico) {
        this.nome=nome; this.especie=especie; this.raca=raca; this.porte=porte;
        this.faixaEtaria=faixaEtaria; this.energia=energia; this.sociavelCriancas=sociavelCriancas;
        this.sociavelAnimais=sociavelAnimais; this.vacinado=vacinado; this.hipoalergenico=hipoalergenico;
    }
    public Long getId(){return id;} public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public Especie getEspecie(){return especie;} public void setEspecie(Especie v){especie=v;}
    public String getRaca(){return raca;} public void setRaca(String v){raca=v;}
    public Porte getPorte(){return porte;} public void setPorte(Porte v){porte=v;}
    public FaixaEtaria getFaixaEtaria(){return faixaEtaria;} public void setFaixaEtaria(FaixaEtaria v){faixaEtaria=v;}
    public Energia getEnergia(){return energia;} public void setEnergia(Energia v){energia=v;}
    public boolean isSociavelCriancas(){return sociavelCriancas;} public void setSociavelCriancas(boolean v){sociavelCriancas=v;}
    public boolean isSociavelAnimais(){return sociavelAnimais;} public void setSociavelAnimais(boolean v){sociavelAnimais=v;}
    public boolean isVacinado(){return vacinado;} public void setVacinado(boolean v){vacinado=v;}
    public boolean isHipoalergenico(){return hipoalergenico;} public void setHipoalergenico(boolean v){hipoalergenico=v;}
}
