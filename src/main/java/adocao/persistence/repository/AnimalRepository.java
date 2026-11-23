package adocao.persistence.repository;
import adocao.persistence.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
    Optional<AnimalEntity> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
