package adocao.service;

import adocao.model.Animal;
import adocao.model.Especie;
import adocao.model.Moradia;
import adocao.model.Pessoa;
import adocao.model.Tempo;
import adocao.service.ResultadoBusca.Situacao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Os casos esperados são os mesmos exemplos de consulta do README do projeto em Prolog. */
class RegrasServiceTest {

    private final Abrigo abrigo = new Abrigo();
    private final RegrasService regras = new RegrasService(abrigo);

    private Pessoa pessoa(String nome) { return abrigo.pessoa(nome).orElseThrow(); }
    private Animal animal(String nome) { return abrigo.animal(nome).orElseThrow(); }
    private static List<String> nomes(List<Animal> animais) { return animais.stream().map(Animal::nome).toList(); }

    @Test
    void recomendadosParaAna() {
        assertEquals(List.of("thor", "nina", "simba"), nomes(regras.recomendados(pessoa("ana"))));
    }

    @Test
    void recomendadosParaBruno() {
        assertEquals(List.of("mel", "luna", "simba"), nomes(regras.recomendados(pessoa("bruno"))));
    }

    @Test
    void recomendadosParaCarla() {
        assertEquals(List.of("mel", "luna", "simba"), nomes(regras.recomendados(pessoa("carla"))));
    }

    @Test
    void recomendadosParaDiegoAlergicoEIniciante() {
        assertEquals(List.of("thor"), nomes(regras.recomendados(pessoa("diego"))));
    }

    @Test
    void gatosParaBruno() {
        assertEquals(List.of("luna", "simba"),
            nomes(regras.recomendadosPorEspecie(pessoa("bruno"), Especie.GATO)));
    }

    @Test
    void rexNaoCabeNoApartamentoDaAna() {
        assertFalse(regras.cabeNoEspaco(animal("rex"), pessoa("ana")));
    }

    @Test
    void simbaEhRecomendadoParaAnaBrunoECarla() {
        assertEquals(List.of("ana", "bruno", "carla"),
            regras.pessoasParaQuem(animal("simba")).stream().map(Pessoa::nome).toList());
    }

    @Test
    void rexNaoEhRecomendadoParaNinguem() {
        assertTrue(regras.pessoasParaQuem(animal("rex")).isEmpty());
    }

    @Test
    void prioritariosSaoOsIdosos() {
        assertEquals(List.of("thor", "simba"), nomes(regras.prioritarios()));
    }

    @Test
    void bobPrecisaDeAtencaoDeSaude() {
        assertEquals(List.of("bob"), nomes(regras.precisaAtencaoSaude()));
    }

    @Test
    void explicaPorQueRexNaoServePraAna() {
        Explicacao e = regras.explicar(animal("rex"), pessoa("ana"));
        assertFalse(e.recomendado());
        assertEquals(List.of(
            "rex NAO e recomendado para ana pelos motivos:",
            "  - porte grande nao cabe em apartamento",
            "  - energia alta exige mais tempo livre do que pouco"), e.linhas());
    }

    @Test
    void explicaQuandoEhRecomendado() {
        Explicacao e = regras.explicar(animal("simba"), pessoa("ana"));
        assertTrue(e.recomendado());
        assertEquals("simba e recomendado para ana: atende a todos os criterios.", e.mensagem());
    }

    @Test
    void bobNaoVacinadoGeraMotivo() {
        assertTrue(regras.motivosRecusa(animal("bob"), pessoa("bruno")).contains("nao esta vacinado"));
    }

    @Test
    void duplasCompativeisDoBruno() {
        assertEquals(
            List.of(new Dupla("mel", "simba"), new Dupla("luna", "mel"), new Dupla("luna", "simba")),
            regras.duplasCompativeis(pessoa("bruno")));
    }

    @Test
    void racasDosGatosSemRepeticao() {
        assertEquals(List.of("persa", "siames", "srd"), regras.racas(Especie.GATO));
    }

    @Test
    void buscaComFiltroEncontraAnimais() {
        ResultadoBusca r = regras.buscar(pessoa("ana"), Especie.GATO, null);
        assertEquals(Situacao.INDICADOS, r.situacao());
        assertEquals(List.of("simba"), nomes(r.animais()));
    }

    @Test
    void buscaSemResultadoNoFiltroSugereOsCompativeis() {
        ResultadoBusca r = regras.buscar(pessoa("ana"), Especie.CACHORRO, "labrador");
        assertEquals(Situacao.SO_COMPATIVEIS, r.situacao());
        assertEquals(List.of("thor", "nina", "simba"), nomes(r.animais()));
    }

    @Test
    void buscaSemNenhumCompativel() {
        Pessoa dificil = new Pessoa("visitante", Moradia.APARTAMENTO, true, true, Tempo.POUCO, true, true);
        assertEquals(Situacao.NENHUM, regras.buscar(dificil, null, null).situacao());
    }

    @Test
    void geraOsNoveExemplos() {
        assertEquals(9, regras.exemplos().size());
    }
}
