package asu.eng.gofund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("asu.eng.gofund.repo")
public class GoFundApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoFundApplication.class, args);
    }

}
