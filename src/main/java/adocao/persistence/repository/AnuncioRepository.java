package adocao.persistence.repository;

import adocao.persistence.dto.AnuncioAdminDto;
import adocao.persistence.dto.AnuncioResumoDto;
import adocao.persistence.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {

    /** Só os aprovados, do mais novo para o mais antigo, sem carregar a coluna da foto. */
    @Query("select new adocao.persistence.dto.AnuncioResumoDto(a.id, a.nome, a.especie, a.raca, a.porte, a.faixaEtaria, "
         + "a.energia, a.sociavelCriancas, a.sociavelAnimais, a.vacinado, a.hipoalergenico, a.descricao, "
         + "a.responsavel, a.cidade, a.whatsapp) from AnuncioEntity a where a.status = 'APROVADO' order by a.id desc")
    List<AnuncioResumoDto> listarAprovados();

    /** Todos, para a área administrativa, sem carregar a coluna da foto. */
    @Query("select new adocao.persistence.dto.AnuncioAdminDto(a.id, a.nome, a.especie, a.raca, a.cidade, a.responsavel, "
         + "a.whatsapp, a.descricao, a.status, a.criadoEm) from AnuncioEntity a order by a.id desc")
    List<AnuncioAdminDto> listarParaAdmin();

    /** Troca o status sem carregar a foto na memória. Devolve quantas linhas mudaram. */
    @Modifying
    @Transactional
    @Query("update AnuncioEntity a set a.status = :status where a.id = :id")
    int atualizarStatus(@Param("id") Long id, @Param("status") String status);
}
