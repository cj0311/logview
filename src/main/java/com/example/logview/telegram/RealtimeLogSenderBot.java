package com.example.logview.telegram;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RealtimeLogSenderBot extends TelegramLongPollingBot {

    private static final String LOG_FILE_PATH = "logs/app.log";
    private static final String BOT_TOKEN = "7269136902:AAE9qzC37hoyVe5enZtrfvf_E4nXZdG5JlQ";
    private static final String CHAT_ID = "517970343";

    @Override
    public String getBotUsername() {
        return "loggrepbot";
    }

    @Override
    public String getBotToken() {
        return "7269136902:AAE9qzC37hoyVe5enZtrfvf_E4nXZdG5JlQ";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // 이 메서드는 봇이 메시지를 받았을 때 호출됩니다.
        // 여기서는 사용하지 않지만, 구현은 해야 합니다.
    }

    public void monitorAndSendLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
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
        sendMessage.setChatId(CHAT_ID);
        sendMessage.setText(log);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            RealtimeLogSenderBot bot = new RealtimeLogSenderBot();
            botsApi.registerBot(bot);
            bot.monitorAndSendLogs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}