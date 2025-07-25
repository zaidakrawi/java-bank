package zaiakr4;

/**
 * Ärver från klassen Account
 * skapar sparkonto specifikt
 * @author Zaid Akrawi, zaikr-4
 */


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class SavingsAccount extends Account {
	private static final BigDecimal INTEREST_RATE = new BigDecimal("2.4"); // 2.4 % årlig ränta
    private static final BigDecimal WITHDRAW_FEE = new BigDecimal("0.02"); // 2 % uttagsavgift efter första uttaget
    private boolean freeWithdrawalUsed; // Spårar om första fria uttaget har använts

	
	
	public SavingsAccount() {
		super("Sparkonto");
		this.freeWithdrawalUsed = false;
	}
	
	
	@Override
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false; // Kan inte ta ut negativa belopp
        }

        BigDecimal totalAmountToWithdraw = amount;

        // om första uttaget redan har gjorts lägg till 2% avgift
        if (freeWithdrawalUsed) {
            BigDecimal fee = amount.multiply(WITHDRAW_FEE).setScale(2, RoundingMode.HALF_UP);
            totalAmountToWithdraw = totalAmountToWithdraw.add(fee);
        }

        // Kontrollera att saldot räcker
        if (balance.compareTo(totalAmountToWithdraw) >= 0) {
            balance = balance.subtract(totalAmountToWithdraw);
            transactions.add(new Transaction(totalAmountToWithdraw.negate(), balance));
            freeWithdrawalUsed = true;
            return true;
        }
        return false; // godkänns inte
    }

    @Override
    public BigDecimal calculateInterest() {
        return balance.multiply(INTEREST_RATE).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
    
    
    @Override
    public BigDecimal getInterestRate() {
        return BigDecimal.valueOf(2.4); // 2,4 % ränta
    }
    
    
    @Override
    public List<String> getTransactions() {
        List<String> transactionList = new ArrayList<>();
        for (Transaction t : transactions) {
            transactionList.add(t.getTransactionInfo());
        }
        return transactionList;
    }
    
}
