package adocao.service;

import adocao.model.*;
import adocao.service.ResultadoBusca.Situacao;

import java.util.ArrayList;
import java.util.List;

/**
 * As regras do sistema. Cada método corresponde a um predicado do adocao.pl.
 * Como no Prolog, recomendado/2 e explicar/2 usam as mesmas condições:
 * um animal é recomendado quando não há nenhum motivo de recusa.
 */
public class RegrasService {

    private final Abrigo abrigo;

    public RegrasService(Abrigo abrigo) {
        this.abrigo = abrigo;
    }

    // ---------- Regras 1 a 6 ----------

    /** 1. O animal cabe na moradia? */
    public boolean cabeNoEspaco(Animal a, Pessoa p) {
        return p.moradia().nivel() >= a.porte().nivel();
    }

    /** 2. A pessoa tem tempo para a energia do animal? */
    public boolean tempoAdequado(Animal a, Pessoa p) {
        return p.tempo().nivel() >= a.energia().nivel();
    }

    /** 3. Com crianças em casa, o animal precisa ser sociável com elas. */
    public boolean compativelCriancas(Animal a, Pessoa p) {
        return !p.criancas() || a.sociavelCriancas();
    }

    /** 4. Com outros animais em casa, o animal precisa conviver bem com eles. */
    public boolean compativelAnimais(Animal a, Pessoa p) {
        return !p.animais() || a.sociavelAnimais();
    }

    /** 5. Pessoa alérgica só pode receber animal hipoalergênico. */
    public boolean alergiaOk(Animal a, Pessoa p) {
        return !p.alergico() || a.hipoalergenico();
    }

    /** 6. Iniciante não recebe animal de energia alta. */
    public boolean experienciaAdequada(Animal a, Pessoa p) {
        return !p.iniciante() || a.energia() != Energia.ALTA;
    }

    // ---------- Regra 7: recomendação ----------

    /** 7. Junta as regras anteriores e exige vacinação. */
    public boolean recomendado(Animal a, Pessoa p) {
        return cabeNoEspaco(a, p)
            && tempoAdequado(a, p)
            && compativelCriancas(a, p)
            && compativelAnimais(a, p)
            && alergiaOk(a, p)
            && experienciaAdequada(a, p)
            && a.vacinado();
    }

    /** findall(A, recomendado(A, P), L). */
    public List<Animal> recomendados(Pessoa p) {
        return abrigo.animais().stream().filter(a -> recomendado(a, p)).toList();
    }

    /** 8. Recomendação filtrada por espécie. */
    public List<Animal> recomendadosPorEspecie(Pessoa p, Especie especie) {
        return recomendados(p).stream().filter(a -> a.especie() == especie).toList();
    }

    /** findall(P, recomendado(A, P), L): para quem o animal é recomendado. */
    public List<Pessoa> pessoasParaQuem(Animal a) {
        return abrigo.pessoas().stream().filter(p -> recomendado(a, p)).toList();
    }

    // ---------- Regras 9 a 11 ----------

    /** 9. Animais idosos têm prioridade. */
    public List<Animal> prioritarios() {
        return abrigo.animais().stream().filter(a -> a.faixaEtaria() == FaixaEtaria.IDOSO).toList();
    }

    /** 10. Animais não vacinados precisam de atenção. */
    public List<Animal> precisaAtencaoSaude() {
        return abrigo.animais().stream().filter(a -> !a.vacinado()).toList();
    }

    /** 11. Dois animais que podem ser adotados juntos pela mesma pessoa. */
    public List<Dupla> duplasCompativeis(Pessoa p) {
        List<Animal> rec = recomendados(p);
        List<Dupla> duplas = new ArrayList<>();
        for (Animal a1 : rec) {
            for (Animal a2 : rec) {
                if (a1.nome().compareTo(a2.nome()) < 0 && a1.sociavelAnimais() && a2.sociavelAnimais()) {
                    duplas.add(new Dupla(a1.nome(), a2.nome()));
                }
            }
        }
        return duplas;
    }

    // ---------- Regra 12: explicação ----------

    /** motivo_recusa/3: um motivo para cada condição de recomendado/2 que falha. */
    public List<String> motivosRecusa(Animal a, Pessoa p) {
        List<String> motivos = new ArrayList<>();
        if (!cabeNoEspaco(a, p))
            motivos.add("porte " + a.porte() + " nao cabe em " + p.moradia());
        if (!tempoAdequado(a, p))
            motivos.add("energia " + a.energia() + " exige mais tempo livre do que " + p.tempo());
        if (!compativelCriancas(a, p))
            motivos.add("nao convive bem com criancas");
        if (!compativelAnimais(a, p))
            motivos.add("nao convive bem com outros animais");
        if (!alergiaOk(a, p))
            motivos.add("a pessoa e alergica e o animal nao e hipoalergenico");
        if (!experienciaAdequada(a, p))
            motivos.add("animal de energia alta nao e indicado para iniciante");
        if (!a.vacinado())
            motivos.add("nao esta vacinado");
        return motivos;
    }

    /** 12. explicar/2: confirma a recomendação ou lista os motivos da recusa. */
    public Explicacao explicar(Animal a, Pessoa p) {
        List<String> motivos = motivosRecusa(a, p);
        if (motivos.isEmpty()) {
            return new Explicacao(true,
                a.nome() + " e recomendado para " + p.nome() + ": atende a todos os criterios.", motivos);
        }
        return new Explicacao(false,
            a.nome() + " NAO e recomendado para " + p.nome() + " pelos motivos:", motivos);
    }

    // ---------- Modo interativo (buscar_animal) ----------

    /** opcoes_raca/2: raças da espécie escolhida (ou de todas, se for null), sem repetição. */
    public List<String> racas(Especie especie) {
        return abrigo.animais().stream()
            .filter(a -> especie == null || a.especie() == especie)
            .map(Animal::raca)
            .distinct()
            .sorted()
            .toList();
    }

    /**
     * resultado_adocao/2: mostra os animais compatíveis que atendem à espécie e à raça
     * (null = sem preferência). Se o filtro não achar nada, sugere os demais compatíveis.
     */
    public ResultadoBusca buscar(Pessoa visitante, Especie especie, String raca) {
        List<Animal> compativeis = recomendados(visitante);
        boolean semRaca = raca == null || raca.isBlank();
        List<Animal> filtrados = compativeis.stream()
            .filter(a -> especie == null || a.especie() == especie)
            .filter(a -> semRaca || a.raca().equals(raca))
            .toList();

        if (!filtrados.isEmpty()) return new ResultadoBusca(Situacao.INDICADOS, filtrados);
        if (!compativeis.isEmpty()) return new ResultadoBusca(Situacao.SO_COMPATIVEIS, compativeis);
        return new ResultadoBusca(Situacao.NENHUM, List.of());
    }

    // ---------- Exemplos (exemplos/0) ----------

    public List<Exemplo> exemplos() {
        Pessoa ana = abrigo.pessoa("ana").orElseThrow();
        Pessoa bruno = abrigo.pessoa("bruno").orElseThrow();
        Pessoa diego = abrigo.pessoa("diego").orElseThrow();
        Animal rex = abrigo.animal("rex").orElseThrow();
        Animal simba = abrigo.animal("simba").orElseThrow();

        List<Exemplo> exemplos = new ArrayList<>();
        exemplos.add(new Exemplo("Quais animais são recomendados para a Ana?",
            nomes(recomendados(ana))));
        exemplos.add(new Exemplo("Quais gatos são recomendados para o Bruno?",
            nomes(recomendadosPorEspecie(bruno, Especie.GATO))));
        exemplos.add(new Exemplo("O Rex cabe no apartamento da Ana?",
            List.of(cabeNoEspaco(rex, ana) ? "sim" : "não")));
        exemplos.add(new Exemplo("Para quem o Simba é recomendado?",
            pessoasParaQuem(simba).stream().map(Pessoa::nome).toList()));
        exemplos.add(new Exemplo("Quais animais têm prioridade de adoção?",
            nomes(prioritarios())));
        exemplos.add(new Exemplo("Quais animais precisam de atenção de saúde?",
            nomes(precisaAtencaoSaude())));

        List<String> paraRex = pessoasParaQuem(rex).stream().map(Pessoa::nome).toList();
        exemplos.add(new Exemplo("O Rex é recomendado para alguém?",
            paraRex.isEmpty() ? List.of("ninguém") : paraRex));
        exemplos.add(new Exemplo("Quais animais são recomendados para o Diego (alérgico e iniciante)?",
            nomes(recomendados(diego))));
        exemplos.add(new Exemplo("Por que o Rex não é recomendado para a Ana?",
            explicar(rex, ana).linhas()));
        return exemplos;
    }

    private static List<String> nomes(List<Animal> animais) {
        return animais.stream().map(Animal::nome).toList();
    }
}
