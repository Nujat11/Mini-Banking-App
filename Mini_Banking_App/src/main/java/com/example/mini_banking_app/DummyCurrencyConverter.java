package com.example.mini_banking_app;

import java.util.HashMap;
import java.util.Map;

public class DummyCurrencyConverter {
    private final Map<String, Double> rates = new HashMap<>();

    public DummyCurrencyConverter() {

        rates.put("BDT", 1.00);
        rates.put("USD", 0.0082);
        rates.put("EUR", 0.0073);
    }

    public double convert(String from, String to, double amount) {
        if (rates.containsKey(from) && rates.containsKey(to)) {
            double rateFrom = rates.get(from);
            double rateTo = rates.get(to);
            return (amount / rateFrom) * rateTo;
        }
        return 0;
    }
}
