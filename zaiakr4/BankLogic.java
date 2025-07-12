package zaiakr4;

/**
 * Hanterar kunder och konton och innehåller en samling med alla inlagda kunder.
 * @author Zaid Akrawi, zaikr-4
 */

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;



// Klass som innehåller en lista (samling) med alla inlagda kunder. 
// Innehåller ett antal publika metoder som hanterar kunder och konton
public class BankLogic {
	private List<Customer> customers;
	
	
	// Konstruktor
	public BankLogic() {
		this.customers = new ArrayList<>();
	}
	
	
	// returnera en lista med kunders info
	public List<String> getAllCustomers() {
	    List<String> customerList = new ArrayList<>();
	    
	    for (Customer customer : customers) {
	        customerList.add(customer.getCustomerInfo().get(0));
	    }
	    
	    return customerList;
	}
	
	
	// Skapar en ny kund
	public boolean createCustomer(String firstName, String lastName, String personalNumber) {
		// kontrollera ifall kunden finns redan
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				return false; // Kunden finns redan i systemet
			}
		}
		// Skapa ny kund
		Customer newCustomer = new Customer(firstName, lastName, personalNumber);
		customers.add(newCustomer);
		return true;
	}
	
	
	// Hämtar info om kunden och dess konton
	public List<String> getCustomer(String personalNumber) {
		for (Customer customer : customers) {
	        if (customer.getPersonalNumber().equals(personalNumber)) {
	            return customer.getCustomerInfo();
	        }
	    }
	    return null; // Returnerar null om kunden inte finns
	}
	
	
	// ändrar namn men behåller pnummer på kunden
	public boolean changeCustomerName(String firstName, String lastName, String personalNumber) {
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				// special fall
				if (firstName.isEmpty() && lastName.isEmpty()) {
					return false;
				}
				
				if (!firstName.isEmpty()) {
					customer.changeName(firstName, customer.getFirstName());
				}
				if (!lastName.isEmpty()) {
					customer.changeName(customer.getFirstName(), lastName);
				}
				return true;
			}
		}
		return false; // om ingen kund hittades
	}
	
	
	// lägga till sparkonto
	public int createSavingsAccount(String personalNumber) {
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				return customer.addAccount();// skapar och returnerar kontonummer
			}
		}
		return -1;
	}
	
	
	// returnerar info om ett specifikt konto
	public String getAccount(String personalNumber, int accountNumber) {
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				for (Account account : customer.getAccounts()) {
					if (account.getAccountNumber() == accountNumber) {
						return account.getAccountInfo();
					}
				}
			}
		}
		return null;
	}
	
	
	// sätt in pengar i kontot
	public boolean deposit(String personalNumber, int accountNumber, int amount) {
		if (amount <= 0) {
			return false;
		}
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				for (Account account : customer.getAccounts()) {
					if (account.getAccountNumber() == accountNumber) {
						return account.deposit(amount);
					}
				}
			}
		}
		return false;
	}
	
	
	// ta ut pengar från kontot
	public boolean withdraw(String personalNumber, int accountNumber, int amount) {
		if (amount <= 0) {
			return false;
		}
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				for (Account account : customer.getAccounts()) {
					if (account.getAccountNumber() == accountNumber) {
						return account.withdraw(amount);
					}
				}
			}
		}
		return false;
	}
	
	
	// avslutar konto med kontonummer och beräknar ränta
	public String closeAccount(String pNo, int accountId) {
	    for (Customer customer : customers) {
	        if (customer.getPersonalNumber().equals(pNo)) {
	            List<Account> accounts = customer.getAccounts();

	            for (Account account : accounts) {
	                if (account.getAccountNumber() == accountId) {
	                    // Beräkna räntan
	                    BigDecimal interestEarned = account.calculateInterest();
	                    //BigDecimal finalBalance = account.getBalance().add(interestEarned);
	                    BigDecimal finalBalance = account.getBalance();

	                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
	                    
	                    String balanceStr = currencyFormat.format(finalBalance);
	                    String interestStr = currencyFormat.format(interestEarned);


	                    String closedAccountInfo = account.getAccountNumber() + " " +
	                            balanceStr + " " +
	                            account.getAccountType() + " " +
	                            interestStr;

	                    // Ta bort kontot
	                    accounts.remove(account);
	                    return closedAccountInfo;
	                }
	            }
	        }
	    }
	    return null; // Konto hittades ej
	}
	
	
	
	public List<String> deleteCustomer(String personalNumber) {
	    for (Customer customer : customers) {
	        if (customer.getPersonalNumber().equals(personalNumber)) {
	            List<String> deletedInfo = new ArrayList<>();
	            deletedInfo.add(customer.getCustomerInfo().get(0));
	            
	            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));

	            // Stäng alla kundens konton
	            List<Account> accounts = new ArrayList<>(customer.getAccounts());
	            for (Account account : accounts) {
	                BigDecimal interestEarned = account.calculateInterest();
	                BigDecimal finalBalance = account.getBalance();
	                
	                String balanceStr = currencyFormat.format(finalBalance);
	                String interestStr = currencyFormat.format(interestEarned);

	                String accountInfo = account.getAccountNumber() + " " +
	                        balanceStr + " " +
	                        account.getAccountType() + " " +
	                        interestStr;

	                deletedInfo.add(accountInfo);
	                customer.getAccounts().remove(account);
	            }

	            // Ta bort kunden från bankens lista
	            customers.remove(customer);
	            return deletedInfo;
	        }
	    }
	    return null; // Kunden hittades inte
	}
}
