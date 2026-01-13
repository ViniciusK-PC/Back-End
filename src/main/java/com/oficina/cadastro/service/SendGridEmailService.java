package com.oficina.cadastro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email:}")
    private String fromEmail;

    @Value("${sendgrid.from.name:}")
    private String fromName;

    public boolean sendVerificationEmail(String toEmail, String subject, String contentHtml) {
        try {
            String json = "{" +
                    "\"personalizations\":[{\"to\":[{\"email\":\"" + toEmail + "\"}]}]," +
                    "\"from\":{\"email\":\"" + fromEmail + "\",\"name\":\"" + fromName + "\"}," +
                    "\"subject\":\"" + subject + "\"," +
                    "\"content\":[{\"type\":\"text/html\",\"value\":\"" + contentHtml.replace("\"", "\\\"") + "\"}]" +
                    "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
                    .header("Authorization", "Bearer " + sendGridApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            return status >= 200 && status < 300;
        } catch (Exception e) {
            return false;
        }
    }
}

