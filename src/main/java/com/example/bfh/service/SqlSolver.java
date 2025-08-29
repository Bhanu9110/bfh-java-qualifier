package com.example.bfh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SqlSolver {

    private static final Logger log = LoggerFactory.getLogger(SqlSolver.class);

    /**
     * Returns the single final SQL query string to submit.
     * Replace the placeholders with your actual final SQL based on the assigned question.
     */
    public String solve(String regNo) {
        int lastTwo = extractLastTwoDigits(regNo);
        boolean isOdd = (lastTwo % 2) == 1;

        if (isOdd) {
            log.info("Detected ODD last two digits ({}). Using Question 1 SQL.", lastTwo);
            // TODO: Replace with your actual final SQL for Question 1
            return "SELECT 1 AS answer;";
        } else {
            log.info("Detected EVEN last two digits ({}). Using Question 2 SQL.", lastTwo);
            // TODO: Replace with your actual final SQL for Question 2
            return "SELECT 2 AS answer;";
        }
    }

    private int extractLastTwoDigits(String regNo) {
        if (regNo == null) return 0;
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() >= 2) {
            return Integer.parseInt(digits.substring(digits.length() - 2));
        }
        if (digits.isEmpty()) return 0;
        return Integer.parseInt(digits);
    }
}