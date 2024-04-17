package com.prox.passgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class PassGptApplication {

    public static void main(String[] args) {
        // set time zone gmt +7
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
        SpringApplication.run(PassGptApplication.class, args);
    }

    private static void setTimeZone(TimeZone timeZone) {
        TimeZone.setDefault(timeZone);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
