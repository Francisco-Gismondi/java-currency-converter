package service;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import com.google.gson.Gson;

import api.ExchangeRateClient;
import model.ExchangeRateResponse;

public class ConversionService {

	private final ExchangeRateClient apiClient;
	private Map<String, Double> ratesCache;
	private String lastUpdateTime;
	private boolean isOfflineMode = false; 

	private final String BASE_CURRENCY = "USD";
	private final String CACHE_FILE = "rates_cache.json";
	private final Gson gson;

	public ConversionService() {
		this.apiClient = new ExchangeRateClient();
		this.gson = new Gson();
		loadRates();
	}

	private void loadRates() {
		try {
			System.out.println("Attempting to download rates (Base: " + BASE_CURRENCY + ")...");
			ExchangeRateResponse response = apiClient.getRates(BASE_CURRENCY);

			this.ratesCache = response.getConversionRates();
			this.lastUpdateTime = response.getTimeLastUpdateUtc();
			this.isOfflineMode = false;

			saveToLocalCache(response);
			System.out.println("Rates updated and saved locally.");

		} catch (Exception e) {
			System.out.println("No connection. Loading local fallback rates...");
			loadFromLocalCache();
		}
	}


	private void saveToLocalCache(ExchangeRateResponse response) {
		try (FileWriter writer = new FileWriter(CACHE_FILE)) {
			gson.toJson(response, writer);
		} catch (Exception e) {
			System.out.println("Error saving the local cache: " + e.getMessage());
		}
	}

	private void loadFromLocalCache() {
		try (FileReader reader = new FileReader(CACHE_FILE)) {
			ExchangeRateResponse localData = gson.fromJson(reader, ExchangeRateResponse.class);

			this.ratesCache = localData.getConversionRates();
			this.lastUpdateTime = localData.getTimeLastUpdateUtc();
			this.isOfflineMode = true;

		} catch (Exception e) {
			throw new RuntimeException("There is no internet connection and there is no local backup data.");
		}
	}


	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public boolean isOfflineMode() {
		return isOfflineMode;
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