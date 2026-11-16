package adocao.model;

/** energia_nivel/2 do Prolog. */
public enum Energia {
    BAIXA(1), MEDIA(2), ALTA(3);

    private final int nivel;
    Energia(int nivel) { this.nivel = nivel; }
    public int nivel() { return nivel; }
    @Override public String toString() { return name().toLowerCase(); }
}
