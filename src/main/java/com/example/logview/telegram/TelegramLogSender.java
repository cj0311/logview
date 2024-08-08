package com.example.logview.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class TelegramLogSender extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Value("${log.file.path}")
    private String logFilePath;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // 봇이 메시지를 받았을 때의 처리. 여기서는 사용하지 않습니다.
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startMonitoringLogs() {
        new Thread(this::monitorAndSendLogs).start();
    }

    private void monitorAndSendLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            reader.skip(reader.lines().count());  // 파일의 끝으로 이동

            String line;
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    sendLog(line);
                } else {
                    Thread.sleep(1000);  // 새 로그가 없으면 1초 대기
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLog(String log) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(log);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}