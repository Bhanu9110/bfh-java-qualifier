package com.example.bfh.service;

import com.example.bfh.dto.GenerateWebhookRequest;
import com.example.bfh.dto.GenerateWebhookResponse;
import com.example.bfh.dto.SubmitPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebhookClient {

    private static final Logger log = LoggerFactory.getLogger(WebhookClient.class);
    private final WebClient webClient;

    public WebhookClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public GenerateWebhookResponse generateWebhook(String url, GenerateWebhookRequest request) {
        log.info("Generating webhook from: {}", url);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(status -> status.isError(), this::handleError)
                .bodyToMono(GenerateWebhookResponse.class)
                .block();
    }

    public void submitFinalQuery(String webhookUrl, String accessToken, SubmitPayload payload) {
        log.info("Submitting final query to: {}", webhookUrl);
        webClient.post()
                .uri(webhookUrl)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .onStatus(status -> status.isError(), this::handleError)
                .toBodilessEntity()
                .block();
        log.info("Submission completed.");
    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> {
                    String msg = "HTTP " + response.statusCode().value() + " error. Body: " + body;
                    log.error(msg);
                    return Mono.error(new RuntimeException(msg));
                });
    }
}