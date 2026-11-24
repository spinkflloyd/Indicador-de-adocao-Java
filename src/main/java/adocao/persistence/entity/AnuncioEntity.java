package adocao.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "anuncios_adocao")
public class AnuncioEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=80) private String nome;
    @Column(nullable=false, length=20) private String especie;
    @Column(nullable=false, length=100) private String raca;
    @Column(nullable=false, length=20) private String porte;
    @Column(nullable=false, length=20) private String faixaEtaria;
    @Column(nullable=false, length=20) private String energia;
    @Column(nullable=false) private boolean sociavelCriancas;
    @Column(nullable=false) private boolean sociavelAnimais;
    @Column(nullable=false) private boolean vacinado;
    @Column(nullable=false) private boolean hipoalergenico;
    @Column(nullable=false, length=1200) private String descricao;
    @Column(nullable=false, length=80) private String responsavel;
    @Column(nullable=false, length=20) private String whatsapp;
    @Column(nullable=false, length=100) private String cidade;
    /** APROVADO aparece no site; PENDENTE fica escondido até a administração aprovar. */
    @Column(nullable=false, length=20, columnDefinition="varchar(20) not null default 'APROVADO'") private String status;
    @Column(nullable=false) private Instant criadoEm;
    @Column(nullable=false, length=100) private String fotoTipo;
    @Column(nullable=false, columnDefinition="bytea") private byte[] foto;

    protected AnuncioEntity() {}
    public AnuncioEntity(String nome,String especie,String raca,String porte,String faixaEtaria,String energia,
        boolean sociavelCriancas,boolean sociavelAnimais,boolean vacinado,boolean hipoalergenico,
        String descricao,String responsavel,String whatsapp,String cidade,String status,String fotoTipo,byte[] foto) {
        this.nome=nome;this.especie=especie;this.raca=raca;this.porte=porte;this.faixaEtaria=faixaEtaria;this.energia=energia;
        this.sociavelCriancas=sociavelCriancas;this.sociavelAnimais=sociavelAnimais;this.vacinado=vacinado;this.hipoalergenico=hipoalergenico;
        this.descricao=descricao;this.responsavel=responsavel;this.whatsapp=whatsapp;this.cidade=cidade;this.status=status;this.fotoTipo=fotoTipo;this.foto=foto;this.criadoEm=Instant.now();
    }
    public Long getId(){return id;} public String getNome(){return nome;} public String getEspecie(){return especie;} public String getRaca(){return raca;}
    public String getPorte(){return porte;} public String getFaixaEtaria(){return faixaEtaria;} public String getEnergia(){return energia;}
    public boolean isSociavelCriancas(){return sociavelCriancas;} public boolean isSociavelAnimais(){return sociavelAnimais;}
    public boolean isVacinado(){return vacinado;} public boolean isHipoalergenico(){return hipoalergenico;}
    public String getDescricao(){return descricao;} public String getResponsavel(){return responsavel;} public String getWhatsapp(){return whatsapp;}
    public String getCidade(){return cidade;} public String getStatus(){return status;} public Instant getCriadoEm(){return criadoEm;} public String getFotoTipo(){return fotoTipo;} public byte[] getFoto(){return foto;}
}
