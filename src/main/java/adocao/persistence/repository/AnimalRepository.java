package adocao.persistence.repository;
import adocao.persistence.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
    /** A ordem do id é a ordem de cadastro, da qual dependem os resultados das regras. */
    List<AnimalEntity> findAllByOrderByIdAsc();
    Optional<AnimalEntity> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
