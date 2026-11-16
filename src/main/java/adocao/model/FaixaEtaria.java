package adocao.model;

public enum FaixaEtaria {
    FILHOTE, ADULTO, IDOSO;

    @Override public String toString() { return name().toLowerCase(); }
}
