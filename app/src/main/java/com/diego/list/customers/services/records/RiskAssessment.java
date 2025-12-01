package com.diego.list.customers.services.records;

import java.util.List;

public record RiskAssessment(int score, boolean requiresMFA, List<String> factors) {
    public boolean isRequiresMFA() {
        return requiresMFA;
    }

    public int getScore() {
        return score;
    }

    public List<String> getFactors() {
        return factors;
    }
}

