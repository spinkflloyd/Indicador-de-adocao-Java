package adocao.service;

import adocao.persistence.entity.InteresseEntity;
import adocao.persistence.repository.AnimalRepository;
import adocao.persistence.repository.InteresseRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayDeque;
import java.util.Deque;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Persiste os pedidos de interesse no PostgreSQL. */
@Service
public class Interesses {
    static final int LIMITE = 200;
    private final InteresseRepository interesses;
    private final AnimalRepository animais;
    private final Deque<Interesse> interessesTeste;
    @Autowired
    public Interesses(InteresseRepository interesses, AnimalRepository animais) { this.interesses=interesses; this.animais=animais; this.interessesTeste=null; }
    /** Construtor usado somente pelos testes unitários antigos do serviço. */
    public Interesses() { this.interesses=null; this.animais=null; this.interessesTeste=new ArrayDeque<>(); }
    @Transactional
    public void registrar(Interesse interesse) {
        if (interesses==null) { if(interessesTeste.size()>=LIMITE) interessesTeste.removeFirst(); interessesTeste.addLast(interesse); return; }
        var animal = animais.findByNomeIgnoreCase(interesse.animal()).orElseThrow(() -> new IllegalArgumentException("Animal não encontrado."));
        interesses.save(new InteresseEntity(interesse.quando(),animal,interesse.nome(),interesse.contato(),interesse.mensagem()));
    }
    @Transactional(readOnly = true)
    public List<Interesse> todos() { return interesses==null ? List.copyOf(interessesTeste) : interesses.findAll().stream().sorted(java.util.Comparator.comparing(InteresseEntity::getQuando)).map(i -> new Interesse(i.getQuando(),i.getAnimal().getNome(),i.getNome(),i.getContato(),i.getMensagem())).toList(); }
}
