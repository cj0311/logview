package com.example.logview.service;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class LogService {

    private static final String LOG_FILE_PATH = "logs/app.log";
    private static final int LINES_TO_READ = 100;

    public List<String> getRecentLogs() throws IOException {
        List<String> lines = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (lines.size() >= LINES_TO_READ) {
                    lines.remove(0); // Remove the oldest line
                }
                lines.add(line);
            }
        }
        return lines;
    }
}