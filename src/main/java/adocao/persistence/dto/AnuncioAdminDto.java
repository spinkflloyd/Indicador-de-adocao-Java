package adocao.persistence.dto;

import java.time.Instant;

/** Anúncio na área administrativa (sem a foto). */
public record AnuncioAdminDto(Long id, String nome, String especie, String raca, String cidade, String responsavel,
                              String whatsapp, String descricao, String status, Instant criadoEm) {}
