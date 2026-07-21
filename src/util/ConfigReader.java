package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private ConfigReader() {
	}

	public static String getApiKey() {
		Properties properties = new Properties();

		try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {

			if (inputStream == null) {
				throw new RuntimeException(
						"No se pudo encontrar el archivo config.properties en la raíz del proyecto.");
			}

			properties.load(inputStream);

			return properties.getProperty("api.key");

		} catch (IOException e) {
			throw new RuntimeException("Error al leer el archivo de configuración: " + e.getMessage(), e);
		}
	}
}