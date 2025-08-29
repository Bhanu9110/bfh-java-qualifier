package com.example.bfh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private User user = new User();
    private Endpoints endpoints = new Endpoints();
    private Submission submission = new Submission();

    public User getUser() { return user; }
    public Endpoints getEndpoints() { return endpoints; }
    public Submission getSubmission() { return submission; }

    public static class User {
        private String name;
        private String regNo;
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRegNo() { return regNo; }
        public void setRegNo(String regNo) { this.regNo = regNo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class Endpoints {
        private String generate;

        public String getGenerate() { return generate; }
        public void setGenerate(String generate) { this.generate = generate; }
    }

    public static class Submission {
        private String fallbackWebhook;

        public String getFallbackWebhook() { return fallbackWebhook; }
        public void setFallbackWebhook(String fallbackWebhook) { this.fallbackWebhook = fallbackWebhook; }
    }
}