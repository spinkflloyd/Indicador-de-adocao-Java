package adocao.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "interesses")
public class InteresseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private Instant quando;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "animal_id", nullable = false) private AnimalEntity animal;
    @Column(nullable = false, length = 80) private String nome;
    @Column(nullable = false, length = 100) private String contato;
    @Column(length = 500) private String mensagem;
    protected InteresseEntity() {}
    public InteresseEntity(Instant quando, AnimalEntity animal, String nome, String contato, String mensagem) {
        this.quando=quando; this.animal=animal; this.nome=nome; this.contato=contato; this.mensagem=mensagem;
    }
    public Long getId(){return id;} public Instant getQuando(){return quando;} public void setQuando(Instant v){quando=v;}
    public AnimalEntity getAnimal(){return animal;} public void setAnimal(AnimalEntity v){animal=v;}
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public String getContato(){return contato;} public void setContato(String v){contato=v;}
    public String getMensagem(){return mensagem;} public void setMensagem(String v){mensagem=v;}
}
