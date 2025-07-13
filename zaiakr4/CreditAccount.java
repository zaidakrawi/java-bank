package zaiakr4;


/**
 * Ärver från klassen Account
 * @author Zaid Akrawi, zaikr-4
 */

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreditAccount extends Account {
    private static final BigDecimal CREDIT_LIMIT = BigDecimal.valueOf(-5000); // Kreditgräns -5000 kr
    private static final BigDecimal INTEREST_RATE_POSITIVE = BigDecimal.valueOf(1.1); // 1.1 % på insatt kapital
    private static final BigDecimal INTEREST_RATE_NEGATIVE = BigDecimal.valueOf(5.0); // 5 % på skuld

    public CreditAccount() {
        super("Kreditkonto");
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false; // Kan inte ta ut negativa belopp
        }

        BigDecimal newBalance = balance.subtract(amount);

        // Kontrollera att saldot inte går under kreditgränsen
        if (newBalance.compareTo(CREDIT_LIMIT) >= 0) {
            balance = newBalance;
            transactions.add(new Transaction(amount.negate(), balance)); // Spara transaktion
            return true;
        }
        return false; // godkänns inte
    }

    @Override
    public BigDecimal calculateInterest() {
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            return balance.multiply(INTEREST_RATE_POSITIVE).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            return balance.multiply(INTEREST_RATE_NEGATIVE).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
    }
    
    
    @Override
    public BigDecimal getInterestRate() {
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            return BigDecimal.valueOf(1.1); // 1,1 % om saldot är positivt eller 0
        } else {
            return BigDecimal.valueOf(5.0); // 5 % om saldot är negativt
        }
    }
}