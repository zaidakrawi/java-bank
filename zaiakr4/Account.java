package zaiakr4;

/**
 * Hanterar saldo, räntesats, kontonummer och kontotyp
 * @author Zaid Akrawi, zaikr-4
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Account {
	private static int nextAccountNumber = 1001;
	private int accountNumber;
	private BigDecimal balance;
	private static final BigDecimal INTEREST_RATE = new BigDecimal("2.4");
	private String accountType;
	
	
	public Account() {
		this.accountNumber = nextAccountNumber++;
		this.balance = BigDecimal.ZERO;
		this.accountType = "Sparkonto";
	}
	
	
	// Metod för att sätta in pengar
	public boolean deposit(int amount) {
		if (amount > 0) {
			this.balance = this.balance.add(new BigDecimal(amount));
			return true;
		}
		return false;
	}
	
	
	// Metod för att ta ut pengar
	public boolean withdraw(int amount) {
		BigDecimal withdrawAmount = new BigDecimal(amount);
		if (amount > 0 && balance.compareTo(withdrawAmount) >= 0) {
			this.balance = this.balance.subtract(withdrawAmount);
			return true;
		}
		return false;
	}
	
	
	// Metod för att hämta kontonummer
	public int getAccountNumber() {
		return accountNumber;
	}
	
	
	public BigDecimal getBalance() {
	    return balance;
	}
	
	
	// Metod för att beräkna ränta
	public BigDecimal calculateInterest() {
		return balance.multiply(INTEREST_RATE).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
	
	
	// Metod för att hämta information om kontot
	//public String getAccountInfo() {
	//	return accountNumber + " " + balance.setScale(2, RoundingMode.HALF_UP) + " kr " + accountType + " " + INTEREST_RATE + " %";
	//}
	public String getAccountInfo() {
	    // Skapa svensk valutaformat (SEK)
	    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
	    String balanceStr = currencyFormat.format(balance);

	    // Skapa procentformat för räntan
	    NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
	    percentFormat.setMaximumFractionDigits(1); // Vi vill ha max 1 decimal
	    percentFormat.setMinimumFractionDigits(1); // Se till att vi alltid har en decimal

	    // Dividera räntan med exakt 100 för att få korrekt procentformat
	    BigDecimal formattedInterest = INTEREST_RATE.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
	    String interestStr = percentFormat.format(formattedInterest);

	    return accountNumber + " " + balanceStr + " " + accountType + " " + interestStr;
	}
	
	public String getAccountType() {
	    return accountType;
	}

}
