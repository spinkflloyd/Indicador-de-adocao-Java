package adocao.service;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LimiteDeEnviosTest {

    private final Instant inicio = Instant.parse("2026-11-24T10:00:00Z");

    @Test
    void bloqueiaOQuartoEnvioDoMesmoVisitante() {
        LimiteDeEnvios limite = new LimiteDeEnvios(3, 30, Duration.ofHours(1));
        assertTrue(limite.permitir("A", inicio));
        assertTrue(limite.permitir("A", inicio.plusSeconds(10)));
        assertTrue(limite.permitir("A", inicio.plusSeconds(20)));
        assertFalse(limite.permitir("A", inicio.plusSeconds(30)));
        assertTrue(limite.permitir("B", inicio.plusSeconds(40)));
    }

    @Test
    void respeitaOLimiteGeralELiberaDepoisDaJanela() {
        LimiteDeEnvios limite = new LimiteDeEnvios(3, 2, Duration.ofHours(1));
        assertTrue(limite.permitir("A", inicio));
        assertTrue(limite.permitir("B", inicio.plusSeconds(1)));
        assertFalse(limite.permitir("C", inicio.plusSeconds(2)));
        assertTrue(limite.permitir("C", inicio.plusSeconds(3700)));
    }
}
