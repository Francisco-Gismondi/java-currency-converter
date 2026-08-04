package model;

import java.util.Map;

public class ExchangeRateResponse {

	private Map<String, Double> conversion_rates;

	private String time_last_update_utc;

	public Map<String, Double> getConversionRates() {
		return conversion_rates;
	}

	public String getTimeLastUpdateUtc() {
		return time_last_update_utc;
	}
}
