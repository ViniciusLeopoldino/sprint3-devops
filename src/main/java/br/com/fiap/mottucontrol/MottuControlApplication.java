package br.com.fiap.mottucontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.fiap.mottucontrol.repository")
@EntityScan("br.com.fiap.mottucontrol.model")
@ComponentScan("br.com.fiap.mottucontrol.controller")
public class MottuControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(MottuControlApplication.class, args);
    }

}