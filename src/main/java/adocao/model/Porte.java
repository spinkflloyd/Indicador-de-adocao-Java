package adocao.model;

/** porte_exige/2 do Prolog: quanto espaço o animal exige. */
public enum Porte {
    PEQUENO(1), MEDIO(2), GRANDE(3);

    private final int nivel;
    Porte(int nivel) { this.nivel = nivel; }
    public int nivel() { return nivel; }
    @Override public String toString() { return name().toLowerCase(); }
}
