package adocao;

import adocao.service.LimiteDeEnvios;
import adocao.service.RegrasService;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class AdocaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdocaoApplication.class, args);
    }

    // As regras continuam em Java puro; os cadastros agora vêm do PostgreSQL.
    @Bean
    RegrasService regrasService(adocao.service.Abrigo abrigo) {
        return new RegrasService(abrigo);
    }

    /** Limite de anúncios por hora: por visitante e no total. Configurável pelas propriedades adocao.anuncios.*. */
    @Bean
    LimiteDeEnvios limiteDeEnvios(@Value("${adocao.anuncios.max-por-ip-hora:3}") int porIp,
                                  @Value("${adocao.anuncios.max-global-hora:30}") int global) {
        return new LimiteDeEnvios(porIp, global, Duration.ofHours(1));
    }
}
