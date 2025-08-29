package com.example.bfh.runner;

import com.example.bfh.config.AppProperties;
import com.example.bfh.dto.GenerateWebhookRequest;
import com.example.bfh.dto.GenerateWebhookResponse;
import com.example.bfh.dto.SubmitPayload;
import com.example.bfh.service.SqlSolver;
import com.example.bfh.service.WebhookClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private final AppProperties props;
    private final WebhookClient client;
    private final SqlSolver solver;

    public StartupRunner(AppProperties props, WebhookClient client, SqlSolver solver) {
        this.props = props;
        this.client = client;
        this.solver = solver;
    }

    @Override
    public void run(ApplicationArguments args) {
        var user = props.getUser();
        var endpoints = props.getEndpoints();
        var submission = props.getSubmission();

        // 1) Generate webhook + token
        GenerateWebhookRequest req = new GenerateWebhookRequest(user.getName(), user.getRegNo(), user.getEmail());
        GenerateWebhookResponse resp = client.generateWebhook(endpoints.getGenerate(), req);
        if (resp == null) {
            throw new IllegalStateException("No response from generateWebhook endpoint.");
        }
        String webhookUrl = StringUtils.hasText(resp.getWebhook()) ? resp.getWebhook() : submission.getFallbackWebhook();
        String token = resp.getAccessToken();
        log.info("Received webhook: {}", webhookUrl);
        log.info("Received accessToken: {}{}", (token != null && token.length() > 10) ? token.substring(0,10) : token, "...");

        // 2) Solve SQL
        String finalQuery = solver.solve(user.getRegNo());
        log.info("Final SQL Query ready: {}", finalQuery);

        // 3) Submit
        client.submitFinalQuery(webhookUrl, token, new SubmitPayload(finalQuery));
    }
}