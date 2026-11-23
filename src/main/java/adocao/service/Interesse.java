package adocao.service;

import java.time.Instant;

/** Um pedido de contato feito pelo botão "Tenho interesse". */
public record Interesse(Instant quando, String animal, String nome, String contato, String mensagem) {}
