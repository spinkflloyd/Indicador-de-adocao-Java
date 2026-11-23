package adocao.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InteressesTest {

    private static Interesse pedido(int n) {
        return new Interesse(Instant.now(), "rex", "pessoa" + n, "contato" + n, "");
    }

    @Test
    void guardaNaOrdemDeChegada() {
        Interesses interesses = new Interesses();
        interesses.registrar(pedido(1));
        interesses.registrar(pedido(2));
        assertEquals("pessoa1", interesses.todos().get(0).nome());
        assertEquals("pessoa2", interesses.todos().get(1).nome());
    }

    @Test
    void mantemSoOsMaisRecentesQuandoPassaDoLimite() {
        Interesses interesses = new Interesses();
        for (int i = 1; i <= Interesses.LIMITE + 5; i++) interesses.registrar(pedido(i));
        assertEquals(Interesses.LIMITE, interesses.todos().size());
        assertEquals("pessoa6", interesses.todos().get(0).nome());
    }
}
