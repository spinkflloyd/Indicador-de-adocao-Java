package adocao.model;

/** tempo_nivel/2 do Prolog: tempo livre da pessoa. */
public enum Tempo {
    POUCO(1), MEDIO(2), MUITO(3);

    private final int nivel;
    Tempo(int nivel) { this.nivel = nivel; }
    public int nivel() { return nivel; }
    @Override public String toString() { return name().toLowerCase(); }
}
