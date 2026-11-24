package adocao.service;

/** Normalização do WhatsApp informado nos anúncios. */
public final class Telefone {

    private Telefone() {}

    /**
     * Devolve só os dígitos, sem o código do país: DDD + número (10 ou 11 dígitos).
     * Aceita "(87) 99999-9999", "87999999999" e "+55 87 99999-9999" (o 55 é removido).
     */
    public static String normalizar(String texto) {
        String d = texto == null ? "" : texto.replaceAll("\\D", "");
        if ((d.length() == 12 || d.length() == 13) && d.startsWith("55")) d = d.substring(2);
        boolean tamanhoOk = d.length() == 10 || d.length() == 11;
        boolean dddOk = tamanhoOk && d.charAt(0) != '0';
        boolean celularOk = d.length() != 11 || d.charAt(2) == '9';
        if (!dddOk || !celularOk) {
            throw new IllegalArgumentException("Informe um WhatsApp com DDD e número.");
        }
        return d;
    }
}
