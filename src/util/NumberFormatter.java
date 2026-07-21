package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatter {

	private NumberFormatter() {

	}

	public static double round(double value) {

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String formatForDisplay(double value) {
		DecimalFormat format = new DecimalFormat("#,##0.00");
        return format.format(value);
	}

}
