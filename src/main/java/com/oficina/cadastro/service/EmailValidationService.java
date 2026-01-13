package com.oficina.cadastro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class EmailValidationService {

    @Value("${mails.api.key:}")
    private String mailsApiKey;

    public boolean isDeliverable(String email) {
        if (mailsApiKey == null || mailsApiKey.isBlank()) {
            return true; // se não configurado, não bloqueia
        }
        try {
            String endpoint = "https://api.mails.so/v1/batch";
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("x-mails-api-key", mailsApiKey);
            conn.setDoOutput(true);

            String payload = "{\"emails\": [\"" + email + "\"]}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));
            StringBuilder resp = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) resp.append(line);
            br.close();

            String body = resp.toString().toLowerCase();
            if (status >= 200 && status < 300) {
                // heurísticas comuns em APIs de validação
                return body.contains("deliverable\":true") || body.contains("valid\":true") || body.contains("result\":\"deliverable\"");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

