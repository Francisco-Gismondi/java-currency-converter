package util;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CurrencyLoader {
    public static String[] getFormattedCurrencies() {
        String filePath = "currencies.json";
        List<String> currencyList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();

            Type currencyType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> currencies = gson.fromJson(reader, currencyType);

            for (Map.Entry<String, String> entry : currencies.entrySet()) {
                String code = entry.getKey();
                String name = entry.getValue();
                String option = code + " - " + name;
                currencyList.add(option);
            }
        } catch (Exception e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
            e.printStackTrace();
        }

        return currencyList.toArray(new String[0]);
    }
}
