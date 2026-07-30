package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

import model.ExchangeRateResponse;

public class ExchangeRateClient {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient httpClient;

    public ExchangeRateClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private String buildUrl(String baseCurrency) {
        String apiKey = util.ConfigReader.getApiKey();
        return BASE_URL + apiKey + "/latest/" + baseCurrency;
    }

    public ExchangeRateResponse getRates(String baseCurrency) {
        String url = buildUrl(baseCurrency);
        URI completeUrl = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder(completeUrl).GET().build();

        try {
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String json = response.body();
                Gson gson = new Gson();
                return gson.fromJson(json, ExchangeRateResponse.class);
            }
            throw new RuntimeException("API error. HTTP code: " + response.statusCode());
        } catch (Exception e) {
            throw new RuntimeException("Connection error: " + e.getMessage());
        }
    }
}
