package adocao.model;

/** espaco_nivel/2 do Prolog: quanto espaço a moradia oferece. */
public enum Moradia {
    APARTAMENTO(1), CASA_PEQUENA(2), CASA_QUINTAL(3);

    private final int nivel;
    Moradia(int nivel) { this.nivel = nivel; }
    public int nivel() { return nivel; }
    @Override public String toString() { return name().toLowerCase(); }
}
