package io.qdao.scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "io.qdao" })
@EnableAutoConfiguration
public class QDabScannerRunner {

    public static void main(String... args) {
        SpringApplication.run(QDabScannerRunner.class, args);
    }
}
