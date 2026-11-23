package adocao.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Guarda em memória os pedidos de interesse. Não há banco de dados: os pedidos somem quando o
 * servidor reinicia e só os 200 mais recentes ficam guardados.
 */
public class Interesses {

    static final int LIMITE = 200;

    private final Deque<Interesse> lista = new ArrayDeque<>();

    public synchronized void registrar(Interesse interesse) {
        if (lista.size() >= LIMITE) lista.removeFirst();
        lista.addLast(interesse);
    }

    /** Do mais antigo para o mais recente. */
    public synchronized List<Interesse> todos() {
        return List.copyOf(lista);
    }
}
