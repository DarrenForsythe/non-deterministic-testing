package com.darrenforsythe.non.deterministic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

@SpringBootApplication
public class NonDeterministicTestApplication implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(NonDeterministicTestApplication.class);

    @Autowired private HardToTestService hardToTestService;

    public static void main(String[] args) {
        SpringApplication.run(NonDeterministicTestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        log.info("UUID 1 - {}", hardToTestService.getUUID());
        log.info("UUID 2 - {}", hardToTestService.getUUID());
        log.info("Current Instant 1 - {}", hardToTestService.getInstant());
        log.info("Current Instant 2 - {}", hardToTestService.getInstant());
    }

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }

    @Bean
    public CurrentTimeGenerator currentTimeGenerator() {
        return () -> System.currentTimeMillis();
    }
}
