package adocao;

import adocao.service.Abrigo;
import adocao.service.RegrasService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdocaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdocaoApplication.class, args);
    }

    // O modelo e as regras são Java puro (sem anotações do Spring); aqui só os registramos.
    @Bean
    Abrigo abrigo() {
        return new Abrigo();
    }

    @Bean
    RegrasService regrasService(Abrigo abrigo) {
        return new RegrasService(abrigo);
    }
}
