package adocao.web;

import adocao.model.Animal;
import adocao.model.Pessoa;
import adocao.service.Verificacao;

import java.util.List;

/** Objetos trocados com a página web. Os valores de enum viajam em minúsculas, como no Prolog. */
public final class Dtos {

    private Dtos() {}

    public record AnimalDto(String nome, String especie, String raca, String porte,
                            String faixaEtaria, String energia,
                            boolean sociavelCriancas, boolean sociavelAnimais,
                            boolean vacinado, boolean hipoalergenico) {
        public static AnimalDto de(Animal a) {
            return new AnimalDto(a.nome(), a.especie().toString(), a.raca(), a.porte().toString(),
                a.faixaEtaria().toString(), a.energia().toString(),
                a.sociavelCriancas(), a.sociavelAnimais(), a.vacinado(), a.hipoalergenico());
        }
    }

    public record PessoaDto(String nome, String moradia, boolean criancas, boolean animais,
                            String tempo, boolean alergico, boolean iniciante) {
        public static PessoaDto de(Pessoa p) {
            return new PessoaDto(p.nome(), p.moradia().toString(), p.criancas(), p.animais(),
                p.tempo().toString(), p.alergico(), p.iniciante());
        }
    }

    /** Respostas do formulário de busca. especie e raca podem ser nulos ou "qualquer". */
    public record BuscaRequest(String especie, String raca, String moradia, boolean criancas,
                               boolean animais, String tempo, boolean alergico, boolean iniciante) {}

    public record BuscaResponse(String situacao, List<AnimalDto> animais) {}

    /** Um animal analisado para o perfil informado: veredito geral e resultado de cada regra. */
    public record AnaliseDto(AnimalDto animal, boolean recomendado, List<Verificacao> verificacoes) {}
}
