package zaiakr4;

/**
 * Hanterar kundens för- och efternamn samt personnummer.
 * @author Zaid Akrawi, zaikr-4
 */

import java.util.ArrayList;
import java.util.List;


public class Customer {
	private String firstName;
	private String lastName;
	private String personalNumber;
	private List<Account> accounts; // Lista med konton
	
	
	public Customer(String firstName, String lastName, String personalNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.personalNumber = personalNumber;
		this.accounts = new ArrayList<>(); // tom lista som kommer innehålla konton
	}
	
	
	// Metod för att ändra kundens namn
	public boolean changeName(String newFirstName, String newLastName) {
		if (!newFirstName.isEmpty()) {
			this.firstName = newFirstName;
		}
		if (!newLastName.isEmpty()) {
			this.lastName = newLastName;
		}
		return true;
	}
	
	
	// Skapar sparkonto
    public int addSavingsAccount() {
        SavingsAccount newAccount = new SavingsAccount();
        accounts.add(newAccount);
        return newAccount.getAccountNumber();
    }
	
	
    // Skapar kreditkonto
    public int addCreditAccount() {
        CreditAccount newAccount = new CreditAccount();
        accounts.add(newAccount);
        return newAccount.getAccountNumber();
    }
    
    
	// Metoder för att hämta förnamn, efternamn samt personnummer
	public String getFirstName() {
		return firstName;
	}
	
	
	public String getLastName() {
		return lastName;
	}
	
	
	public String getPersonalNumber() {
		return personalNumber;
	}
	
	
	public List<String> getCustomerInfo() {
	    List<String> info = new ArrayList<>();
	    info.add(personalNumber + " " + firstName + " " + lastName);
	    info.addAll(getCustomerAccounts());
	    return info;
	}
	
	
	// returnerar lista över Account objekt
	public List<Account> getAccounts() {
	    return accounts;
	}
	
	
	public List<String> getCustomerAccounts() {
	    List<String> accountDetails = new ArrayList<>();
	    for (Account account : accounts) {
	        accountDetails.add(account.getAccountInfo());
	    }
	    return accountDetails;
	}
	
}

