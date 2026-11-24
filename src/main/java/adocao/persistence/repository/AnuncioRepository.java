package adocao.persistence.repository;
import adocao.persistence.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {}
