package util;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CurrencyLoader {
	public static String[] obtenerMonedasFormateadas() {
		String rutaArchivo = "currencies.json";
		List<String> listaMonedas = new ArrayList<>();

		try (FileReader reader = new FileReader(rutaArchivo)) {
			Gson gson = new Gson();

			Type tipoMapa = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> monedas = gson.fromJson(reader, tipoMapa);

			for (Map.Entry<String, String> entry : monedas.entrySet()) {
				String codigo = entry.getKey();
				String nombre = entry.getValue();
				String opcion = codigo + " - " + nombre;
				listaMonedas.add(opcion);
			}

		} catch (Exception e) {
			System.out.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return listaMonedas.toArray(new String[0]);
	}
}
