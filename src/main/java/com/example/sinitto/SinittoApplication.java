package com.example.sinitto;

import com.example.sinitto.common.properties.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(KakaoProperties.class)
@EnableScheduling
public class SinittoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinittoApplication.class, args);
    }

}
