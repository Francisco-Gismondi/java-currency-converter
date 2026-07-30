package service;

import java.util.Map;

import api.ExchangeRateClient;
import model.ExchangeRateResponse;

public class ConversionService {

    private final ExchangeRateClient apiClient;
    private Map<String, Double> ratesCache;
    private final String BASE_CURRENCY = "USD";

    public ConversionService() {
        this.apiClient = new ExchangeRateClient();
        loadRates();
    }

    private void loadRates() {
        System.out.println("Downloading exchange rates (Base: " + BASE_CURRENCY + ")...");
        ExchangeRateResponse response = apiClient.getRates(BASE_CURRENCY);
        this.ratesCache = response.getConversionRates();
    }

    public double convert(String fromCurrency, String toCurrency, double amount) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        if (!ratesCache.containsKey(fromCurrency)) {
            throw new IllegalArgumentException("Unsupported source currency: " + fromCurrency);
        }
        if (!ratesCache.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported target currency: " + toCurrency);
        }

        double fromRate = ratesCache.get(fromCurrency);
        double toRate = ratesCache.get(toCurrency);

        double amountInUsd = amount / fromRate;
        double finalResult = amountInUsd * toRate;

        return finalResult;
    }

    public void refreshRates() {
        loadRates();
    }
}