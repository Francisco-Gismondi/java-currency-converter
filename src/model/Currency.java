package model;

public class Currency {

	private String currencyCode;
	private String currencyName;
	private String symbol;

	public Currency() {
		
	}
	
	public Currency(String currencyCode, String currencyName, String symbol) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.symbol = symbol;
    }

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	
}
