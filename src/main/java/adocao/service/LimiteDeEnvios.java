package adocao.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Limita quantos anúncios podem ser publicados por janela de tempo: um máximo por visitante (IP)
 * e um máximo geral. Fica em memória, então zera quando o servidor reinicia.
 */
public class LimiteDeEnvios {

    private final int porChave;
    private final int global;
    private final Duration janela;
    private final Map<String, Deque<Instant>> porVisitante = new HashMap<>();
    private final Deque<Instant> todos = new ArrayDeque<>();

    public LimiteDeEnvios(int porChave, int global, Duration janela) {
        this.porChave = porChave;
        this.global = global;
        this.janela = janela;
    }

    /** Registra um envio e devolve true, ou devolve false (sem registrar) se algum limite foi atingido. */
    public synchronized boolean permitir(String chave, Instant agora) {
        Instant corte = agora.minus(janela);
        descartarAntigos(todos, corte);
        porVisitante.values().removeIf(d -> {
            descartarAntigos(d, corte);
            return d.isEmpty();
        });

        Deque<Instant> meus = porVisitante.computeIfAbsent(chave == null ? "desconhecido" : chave, k -> new ArrayDeque<>());
        if (todos.size() >= global || meus.size() >= porChave) return false;

        meus.addLast(agora);
        todos.addLast(agora);
        return true;
    }

    private static void descartarAntigos(Deque<Instant> fila, Instant corte) {
        while (!fila.isEmpty() && fila.peekFirst().isBefore(corte)) fila.removeFirst();
    }
}
