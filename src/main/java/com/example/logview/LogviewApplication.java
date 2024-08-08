package com.example.logview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

@SpringBootApplication
public class LogviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogviewApplication.class, args);
    }

}
