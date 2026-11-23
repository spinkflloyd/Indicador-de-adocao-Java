package adocao.persistence.repository;
import adocao.persistence.entity.InteresseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InteresseRepository extends JpaRepository<InteresseEntity, Long> {
    boolean existsByAnimalId(Long animalId);
}
