package adocao.persistence.entity;

import adocao.model.Moradia;
import adocao.model.Tempo;
import jakarta.persistence.*;

@Entity
@Table(name = "pessoas")
public class PessoaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 80) private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private Moradia moradia;
    @Column(nullable = false) private boolean criancas;
    @Column(nullable = false) private boolean animais;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Tempo tempo;
    @Column(nullable = false) private boolean alergico;
    @Column(nullable = false) private boolean iniciante;
    protected PessoaEntity() {}
    public PessoaEntity(String nome, Moradia moradia, boolean criancas, boolean animais, Tempo tempo, boolean alergico, boolean iniciante) {
        this.nome=nome; this.moradia=moradia; this.criancas=criancas; this.animais=animais; this.tempo=tempo; this.alergico=alergico; this.iniciante=iniciante;
    }
    public Long getId(){return id;} public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public Moradia getMoradia(){return moradia;} public void setMoradia(Moradia v){moradia=v;}
    public boolean isCriancas(){return criancas;} public void setCriancas(boolean v){criancas=v;}
    public boolean isAnimais(){return animais;} public void setAnimais(boolean v){animais=v;}
    public Tempo getTempo(){return tempo;} public void setTempo(Tempo v){tempo=v;}
    public boolean isAlergico(){return alergico;} public void setAlergico(boolean v){alergico=v;}
    public boolean isIniciante(){return iniciante;} public void setIniciante(boolean v){iniciante=v;}
}
