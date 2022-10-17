package ru.artemev.dossier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("ru.artemev.dossier")
public class DossierApplication {

  public static void main(String[] args) {
    SpringApplication.run(DossierApplication.class, args);
  }
}
