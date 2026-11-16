package adocao.service;

import java.util.ArrayList;
import java.util.List;

/** Resultado de explicar/2: confirma a recomendação ou lista os motivos da recusa. */
public record Explicacao(boolean recomendado, String mensagem, List<String> motivos) {

    /** As mesmas linhas que o explicar/2 do Prolog imprimia. */
    public List<String> linhas() {
        List<String> linhas = new ArrayList<>();
        linhas.add(mensagem);
        motivos.forEach(m -> linhas.add("  - " + m));
        return linhas;
    }
}
