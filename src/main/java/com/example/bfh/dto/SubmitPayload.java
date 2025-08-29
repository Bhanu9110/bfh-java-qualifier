package com.example.bfh.dto;

public class SubmitPayload {
    private String finalQuery;

    public SubmitPayload() {}

    public SubmitPayload(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}