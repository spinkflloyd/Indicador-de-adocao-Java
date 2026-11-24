package adocao.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelefoneTest {

    @Test
    void removeFormatacaoEOCodigoDoPais() {
        assertEquals("87999999999", Telefone.normalizar("(87) 99999-9999"));
        assertEquals("87999999999", Telefone.normalizar("+55 87 99999-9999"));
        assertEquals("87999999999", Telefone.normalizar("5587999999999"));
        assertEquals("8733334444", Telefone.normalizar("558733334444"));
    }

    @Test
    void naoConfundeODddCinquentaECincoComOCodigoDoPais() {
        assertEquals("55999999999", Telefone.normalizar("55999999999"));
    }

    @Test
    void rejeitaNumerosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> Telefone.normalizar("9999-9999"));
        assertThrows(IllegalArgumentException.class, () -> Telefone.normalizar("1234567890123456"));
        assertThrows(IllegalArgumentException.class, () -> Telefone.normalizar("87899999999"));
        assertThrows(IllegalArgumentException.class, () -> Telefone.normalizar("0799999999"));
        assertThrows(IllegalArgumentException.class, () -> Telefone.normalizar(null));
    }
}
