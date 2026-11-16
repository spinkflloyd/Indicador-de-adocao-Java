package adocao.service;

import adocao.model.Animal;

import java.util.List;

/** Resultado do modo interativo (resultado_adocao/2 do Prolog). */
public record ResultadoBusca(Situacao situacao, List<Animal> animais) {

    public enum Situacao {
        /** Há animais compatíveis que atendem à espécie/raça pedidas. */
        INDICADOS,
        /** Nenhum atende ao filtro, mas outros animais combinam com o perfil. */
        SO_COMPATIVEIS,
        /** Nenhum animal do abrigo é compatível com o perfil. */
        NENHUM
    }
}
