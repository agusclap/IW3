package ar.edu.iua.iw3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j // instancia una variable log
public class BackendApplication extends SpringBootServletInitializer implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void run(String... args) throws Exception {
        log.info("Perfil activo {}", profile);
    }
}
