package adocao.persistence.repository;
import adocao.persistence.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PessoaRepository extends JpaRepository<PessoaEntity, Long> {
    Optional<PessoaEntity> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
