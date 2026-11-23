package adocao;

import adocao.service.RegrasService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
