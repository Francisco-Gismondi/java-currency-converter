package service;

import api.ExchangeRateClient;
import model.ExchangeRateResponse;

public class ConversionService {

	private ExchangeRateClient apiClient;

	public ConversionService() {
		this.apiClient = new ExchangeRateClient();
	}

	public double convert(String baseCurrency, String targetCurrency, double amount) {
		
		ExchangeRateResponse response = apiClient.getRates(baseCurrency.toUpperCase());

		Double rate = response.getConversionRates().get(targetCurrency.toUpperCase());

		if (rate == null) {
			throw new IllegalArgumentException(
					"La moneda destino '" + targetCurrency + "' no existe o no es soportada.");
		}
		return amount * rate;
	}
}
