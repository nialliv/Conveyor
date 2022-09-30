package ru.artemev.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("ru.artemev.deal")
public class DealApplication {

  public static void main(String[] args) {
    SpringApplication.run(DealApplication.class, args);
  }
}
