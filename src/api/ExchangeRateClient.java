package api;

import java.net.URI;
import java.net.http.*;
import com.google.gson.Gson;
import model.ExchangeRateResponse;

public class ExchangeRateClient {

	private static final String baseUrl = "https://v6.exchangerate-api.com/v6/";
	private HttpClient client;

	public ExchangeRateClient() {
		client = HttpClient.newHttpClient();
	}

	private String buildUrl(String baseCurrency) {
		String key;
		key = util.ConfigReader.getApiKey();
		return baseUrl + key + "/latest/" + baseCurrency;
	}

	public ExchangeRateResponse getRates(String baseCurrency) {

		String url = buildUrl(baseCurrency);
		URI completeUrl = URI.create(url);
		HttpRequest request = HttpRequest.newBuilder(completeUrl).GET().build();

		try {
			HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				String json = response.body();
				Gson gson = new Gson();
				ExchangeRateResponse rateResponse = gson.fromJson(json, ExchangeRateResponse.class);
	            return rateResponse;
				
			} else {
				throw new RuntimeException("Error en la API. Código HTTP: " + response.statusCode());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error de conexión: " + e.getMessage());
		}
	}
}
