package adocao.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TipoImagemTest {

    @Test
    void reconheceJpegPngEWebp() {
        assertEquals("image/jpeg", TipoImagem.detectar(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0}));
        assertEquals("image/png", TipoImagem.detectar(new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0}));
        assertEquals("image/webp", TipoImagem.detectar(new byte[]{'R', 'I', 'F', 'F', 1, 2, 3, 4, 'W', 'E', 'B', 'P'}));
    }

    @Test
    void recusaArquivoQueNaoEImagemMesmoComNomeDeImagem() {
        assertNull(TipoImagem.detectar("<html><script>alert(1)</script>".getBytes()));
        assertNull(TipoImagem.detectar(new byte[0]));
        assertNull(TipoImagem.detectar(null));
    }
}
