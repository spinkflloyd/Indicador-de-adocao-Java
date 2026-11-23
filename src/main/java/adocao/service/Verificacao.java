package adocao.service;

/** Resultado de uma regra para um animal e uma pessoa: passou ou não, e o motivo. */
public record Verificacao(String regra, boolean ok, String detalhe) {}
