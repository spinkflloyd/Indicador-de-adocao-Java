package adocao.service;

import java.util.List;

/** Uma pergunta de exemplo e o resultado (equivalente ao exemplos/0 do Prolog). */
public record Exemplo(String pergunta, List<String> resultado) {}
