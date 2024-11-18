package hackathon.voice_cut_1.voice_cut_1.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class DiscordNotificationService {

    @Value("${discord.webhook.text-and-percent.url}")
    private String discordWebhookTextAndPercentUrl;

    @Value("${discord.webhook.exception.url}")
    private String discordWebhookExceptionUrl;

    public void sendTextAndPercentAsync(
            String text,
            int percent
    ) {
        String payload = String.format("{\"content\": \"Text: %s, Percent: %d%%\"}", text, percent);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(discordWebhookTextAndPercentUrl))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenAccept(response -> {
                    if (response.statusCode() != 204) {
                        log.error("Failed to send discord notification, HTTP status code: {}", response.statusCode());
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Failed to send discord notification: {}", throwable.getMessage());
                    return null;
                });
    }

    public void sendExceptionMessageAsync(
            String exceptionMessage
    ) {
        String payload = String.format("{\"content\": \"%s\"}", exceptionMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(discordWebhookExceptionUrl))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenAccept(response -> {
                    if (response.statusCode() != 204) {
                        log.error("Failed to send discord notification, HTTP status code: {}", response.statusCode());
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Failed to send discord notification: {}", throwable.getMessage());
                    return null;
                });
    }
}
