package zaiakr4;

/* Hanterar och lagrar transaktioner.
 * sparar transkationstid, belopp och
 * saldo efter transkaktion
* @author Zaid Akrawi, zaikr-4
*/

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;


public class Transaction {
	private String timestamp;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;

    public Transaction(BigDecimal amount, BigDecimal balanceAfterTransaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getTransactionInfo() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        return timestamp + " " + currencyFormat.format(amount) + " Saldo: " + currencyFormat.format(balanceAfterTransaction);
    }

}
