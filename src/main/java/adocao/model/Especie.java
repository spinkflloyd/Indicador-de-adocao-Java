package adocao.model;

public enum Especie {
    CACHORRO, GATO, COELHO;

    @Override public String toString() { return name().toLowerCase(); }
}
