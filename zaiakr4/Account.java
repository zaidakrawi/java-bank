package zaiakr4;

/**
 * Abstrakt klass
 * Hanterar saldo, räntesats, kontonummer och kontotyp
 * @author Zaid Akrawi, zaikr-4
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;


public abstract class Account {
	private static int nextAccountNumber = 1001;
	protected int accountNumber;
	protected BigDecimal balance;
	protected String accountType;
	protected List<Transaction> transactions; // Lista för transaktioner
	
	
	public Account(String accountType) {
		this.accountNumber = nextAccountNumber++;
		this.balance = BigDecimal.ZERO;
		this.accountType = accountType;
		this.transactions = new ArrayList<>();
	}
	
	
	// Abstrakt metod som barnklasserna måste implementera
    public abstract BigDecimal calculateInterest();
    
    
    public abstract BigDecimal getInterestRate();
	
	
	// Metod för att sätta in pengar
    public boolean deposit(BigDecimal amount) { // Tar emot BigDecimal
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
            transactions.add(new Transaction(amount, this.balance)); // Spara transaktion
            return true;
        }
        return false;
    }
	
	
	// hanteras av barnklasserna
	public abstract boolean withdraw(BigDecimal amount);
	
	
	// Metod för att hämta kontonummer
	public int getAccountNumber() {
		return accountNumber;
	}
	
	
	public BigDecimal getBalance() {
	    return balance;
	}
	
	
	
	// Metod för att hämta information om kontot
	//public String getAccountInfo() {
	//	return accountNumber + " " + balance.setScale(2, RoundingMode.HALF_UP) + " kr " + accountType + " " + INTEREST_RATE + " %";
	//}
	/*
	 * public String getAccountInfo() { // Skapa svensk valutaformat (SEK)
	 * NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new
	 * Locale("sv", "SE")); String balanceStr = currencyFormat.format(balance);
	 * 
	 * // Skapa procentformat för räntan NumberFormat percentFormat =
	 * NumberFormat.getPercentInstance(new Locale("sv", "SE"));
	 * percentFormat.setMaximumFractionDigits(1); // Vi vill ha max 1 decimal
	 * percentFormat.setMinimumFractionDigits(1); // Se till att vi alltid har en
	 * decimal
	 * 
	 * // Dividera räntan med exakt 100 för att få korrekt procentformat BigDecimal
	 * formattedInterest = INTEREST_RATE.divide(BigDecimal.valueOf(100), 4,
	 * RoundingMode.HALF_UP); String interestStr =
	 * percentFormat.format(formattedInterest);
	 * 
	 * return accountNumber + " " + balanceStr + " " + accountType + " " +
	 * interestStr; }
	 */
	
	public String getAccountType() {
	    return accountType;
	}
	
	
	// Hämta transaktionshistorik
    public List<String> getTransactions() {
        List<String> transactionList = new ArrayList<>();
        for (Transaction t : transactions) {
            transactionList.add(t.getTransactionInfo());
        }
        return transactionList;
    }
    
    //hämta konto info
    public String getAccountInfo() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        String balanceStr = currencyFormat.format(balance);
        
        
        BigDecimal interestRate = getInterestRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        percentFormat.setMaximumFractionDigits(1);
        percentFormat.setMinimumFractionDigits(1);
        String interestStr = percentFormat.format(interestRate); 

        return accountNumber + " " + balanceStr + " " + accountType + " " + interestStr;
    }

}