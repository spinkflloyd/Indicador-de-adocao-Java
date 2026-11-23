package adocao.persistence.dto;
import adocao.persistence.entity.InteresseEntity;
import java.time.Instant;
public record InteresseAdminDto(Long id, Instant quando, Long animalId, String animal, String nome, String contato, String mensagem) {
    public static InteresseAdminDto de(InteresseEntity i) { return new InteresseAdminDto(i.getId(),i.getQuando(),i.getAnimal().getId(),i.getAnimal().getNome(),i.getNome(),i.getContato(),i.getMensagem()); }
}
