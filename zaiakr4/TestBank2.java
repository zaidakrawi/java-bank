package zaiakr4;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zaiakr4.BankLogic;
/** 
 * D0018D, Lab 2: Testar att klasserna Account, Customer and BankLogic  
 * fungerar som de ska. Notera att klassen kan uppdateras under kursens gång.
 * 
 * Denna klass placeras i default package OBS!
 * Ändra paketets namn <qwerty0> i importen ovan, så den matchar 
 * ditt pakets namn <användarid>
 * 
 * Testprogrammet kontrollerar att det som returneras från dina metoder är det 
 * som förväntas returneras. Detta betyder att du måste justera strängarna så 
 * den är exakt så som specificeras i uppgiften. 
 * 
 * Nu används den mer generella List<String> istället för ArrayList<String>
 * 
 * Last changes:  2024-10-07
 * @author Susanne Fahlman, susanne.fahlman@ltu.se        
 */
public class TestBank2
{
	
	private BankLogic bank = new BankLogic();
	private int testCounter = 1;

	//-----------------------------------------------------------------------------------
	// Runs the tests
	//-----------------------------------------------------------------------------------
	public void test() throws FileNotFoundException, InterruptedException
	{
		// Har du en äldre JDK kommer du får du kompileringsfel på följande rad
		// Kommentera då bort den och ta fram den bortkommenterade raden under
		Locale.setDefault(Locale.of("sv","SE"));		// För JDK 19 och senare
		//Locale.setDefault(new Locale("sv", "SE"));	// För JDK 18 och tidigare
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String 	customerName;
		String 	customerSurname;
		String 	personalNumber;
		int 	accountNumber;
		int 	amount;

		// #1 Gets the empty list
		testingGetAllCustomers("[]");

		// #2 Create customer
		customerName = "Olle";
		customerSurname = "Ohlsson";
		personalNumber = "0005220157";
		testingCreateCustomer(customerName, customerSurname, personalNumber, true);

		// #3 Create customer
		customerName = "Karl";
		customerSurname = "Carlsson";
		personalNumber = "8505221898";
		testingCreateCustomer(customerName, customerSurname, personalNumber, true);

		// #4 Create customer with a personal number that already exists
		customerName = "Donald";
		customerSurname = "Duck";
		personalNumber = "8505221898";
		testingCreateCustomer(customerName, customerSurname, personalNumber, false); 

		// #5 Create customer
		customerName = "Pelle";
		customerSurname = "Persson";
		personalNumber = "6911258876";
		testingCreateCustomer(customerName, customerSurname, personalNumber, true);

		// #6 Create customer
		customerName = "Lotta";
		customerSurname = "Larsson";
		personalNumber = "7505121231";
		testingCreateCustomer(customerName, customerSurname, personalNumber, true);

		// #7 Delete customer
		personalNumber = "0005220157";
		testingDeleteCustomer(personalNumber, "[0005220157 Olle Ohlsson]");

		// #8 Delete customer that don't exists
		personalNumber = "0005220157";
		testingDeleteCustomer(personalNumber, "null");

		// #9 Get all customers in the list
		testingGetAllCustomers("[8505221898 Karl Carlsson, 6911258876 Pelle Persson, 7505121231 Lotta Larsson]");

		// #10 Change a customers name
		customerName = "Kalle";
		customerSurname = ""; // Empty string means the last name should not change
		personalNumber = "8505221898";
		testingChangeName(customerName, customerSurname, personalNumber, true);

		// #11 Change a customers name
		customerName = ""; // Empty string means the last name should not change
		customerSurname = "Karlsson";
		personalNumber = "8505221898";
		testingChangeName(customerName, customerSurname, personalNumber, true);

		// #12 Change a  name for a customer that don't exists
		customerName = "Olle";
		customerSurname = "Karlsson";
		personalNumber = "9905225166";
		testingChangeName(customerName, customerSurname, personalNumber, false);

		// #13 Get information about the customer
		testingGetCustomer("8505221898", "[8505221898 Kalle Karlsson]");

		// #14 Gets customer that don't exists
		testingGetCustomer("9905225166", "null");

		// #15 Creates accounts
		personalNumber = "8505221898";
		testingCreateCreditAccount(personalNumber, 1001);

		// #16 Creates accounts for customer that don't exist
		personalNumber = "9905225166";
		testingCreateCreditAccount(personalNumber, -1);

		// #17 Creates accounts
		personalNumber = "6911258876";
		testingCreateCreditAccount(personalNumber, 1002);

		// #18 Creates accounts
		personalNumber = "8505221898";
		testingCreateSavingsAccount(personalNumber, 1003);

		// #19 Creates accounts
		personalNumber = "7505121231";
		testingCreateSavingsAccount(personalNumber, 1004);

		// #20 Get information about the customer including accounts
		testingGetCustomer("8505221898", "[8505221898 Kalle Karlsson, 1001 0,00 kr Kreditkonto 1,1 %, 1003 0,00 kr Sparkonto 2,4 %]");

		// #21 Get information about the customer including accounts
		testingGetCustomer("6911258876", "[6911258876 Pelle Persson, 1002 0,00 kr Kreditkonto 1,1 %]");

		// #22 Get information about the customer including accounts
		testingGetCustomer("7505121231", "[7505121231 Lotta Larsson, 1004 0,00 kr Sparkonto 2,4 %]");

		// #23 Transaction with customer that is not the owner of the account
		personalNumber 	= "8505221898";
		accountNumber  	= 1002;
		amount 			= 700;
		testingWithdraw(personalNumber, accountNumber, amount, false);	
		
		// #24 Transaction with customer that is not the owner of the account
		personalNumber 	= "8505221898";
		accountNumber  	= 1004;
		amount 			= 700;
		testingDeposit(personalNumber, accountNumber, amount, false);	

		// #25 Transaction
		personalNumber 	= "8505221898";
		accountNumber  	= 1001;
		amount 			= 500;		
		String strDate1001_1 = sdf.format(new Date()); // Save the time for transaction
		testingWithdraw(personalNumber, accountNumber, amount, true);	

		// Sleeps a second just to ensure the transaction time is different  
		Thread.sleep(1000);		

		// #26 Transaction
		personalNumber 	= "8505221898";
		accountNumber  	= 1001;
		amount 			= 4000;
		String strDate1001_2 = sdf.format(new Date()); // Save the time for transaction
		testingWithdraw(personalNumber, accountNumber, amount, true);	

		// #27 Transaction - not enough credit
		personalNumber 	= "8505221898";
		accountNumber  	= 1001;
		amount 			= 501;
		testingWithdraw(personalNumber, accountNumber, amount, false);

		// #28 Transaction - not enough money
		personalNumber 	= "8505221898";
		accountNumber  	= 1003;
		amount 			= 500;
		testingWithdraw(personalNumber, accountNumber, amount, false);

		// #29 Transaction
		personalNumber 	= "8505221898";
		accountNumber  	= 1003;
		amount 			= 500;
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #30 Get information about the customer including accounts
		testingGetCustomer("8505221898", "[8505221898 Kalle Karlsson, 1001 -4 500,00 kr Kreditkonto 5 %, 1003 500,00 kr Sparkonto 2,4 %]");

		// #31 Get transactions
		personalNumber  = "8505221898";
		accountNumber   = 1001;
		testingGetTransactions(personalNumber, accountNumber, "[" + strDate1001_1 + " -500,00 kr Saldo: -500,00 kr, " + strDate1001_2 + " -4 000,00 kr Saldo: -4 500,00 kr]");

		// #32 Get transactions - account exists but no transactions
		personalNumber  = "7505121231";
		accountNumber   = 1004;
		testingGetTransactions(personalNumber, accountNumber, "[]");

		// #33 Get transactions - customer not owner of account
		personalNumber  = "7505121231";
		accountNumber   = 1001;
		testingGetTransactions(personalNumber, accountNumber, "null");

		// #34 Get information about the account
		testingGetAccount("8505221898", 1001, "1001 -4 500,00 kr Kreditkonto 5 %");        

		// #35 Get information about the account
		testingGetAccount("8505221898", 1002, "null");	

		// #36 Closes the account
		testingCloseAccount("8505221898", 1001, "1001 -4 500,00 kr Kreditkonto -225,00 kr");	

		// #37 Get information about all customers
		testingGetAllCustomers("[8505221898 Kalle Karlsson, 6911258876 Pelle Persson, 7505121231 Lotta Larsson]");

		// #38 Transaction
		personalNumber 	= "8505221898";
		accountNumber  	= 1003;
		amount 			= 5000;
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #39 Transaction
		personalNumber 	= "8505221898";
		accountNumber  	= 1003;
		amount 			= 5000;
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #40 Creates account
		personalNumber = "7505121231";
		testingCreateSavingsAccount(personalNumber, 1005);

		// #41 Get information about the customer including accounts
		testingGetCustomer("8505221898", "[8505221898 Kalle Karlsson, 1003 10 500,00 kr Sparkonto 2,4 %]");

		// #42 Get information about the customer including accounts
		testingGetCustomer("6911258876", "[6911258876 Pelle Persson, 1002 0,00 kr Kreditkonto 1,1 %]");

		// #43 Get information about the customer including accounts
		testingGetCustomer("7505121231", "[7505121231 Lotta Larsson, 1004 0,00 kr Sparkonto 2,4 %, 1005 0,00 kr Sparkonto 2,4 %]");

		// #44 Transaction
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= 1000;
		String strDate1005_1 = sdf.format(new Date()); // Save the time for transaction
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #45 Transaction
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= 100;
		String strDate1005_2 = sdf.format(new Date()); // Save the time for transaction
		testingWithdraw(personalNumber, accountNumber, amount, true);

		// #46 Transaction - not enough money, don't forget the debt interest 890+(890*0.02)=907
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= 890;
		testingWithdraw(personalNumber, accountNumber, amount, false);

		// #47 Transaction
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= 100;
		String strDate1005_3 = sdf.format(new Date()); // Save the time for transaction
		testingWithdraw(personalNumber, accountNumber, amount, true);

		// #48 Transaction If a negative number is used for withdraw or deposit the transaction should not work
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= -100;
		testingWithdraw(personalNumber, accountNumber, amount, false);

		// #49 Transaction If a negative number is used for withdraw or deposit the transaction should not work
		personalNumber 	= "7505121231";
		accountNumber  	= 1005;
		amount 			= -1000;
		testingDeposit(personalNumber, accountNumber, amount, false);

		// #50 Transaction 
		personalNumber  = "7505121231";
		accountNumber   = 1004;
		amount          = 500;
		String strDate1003_1 = sdf.format(new Date()); // Save the time for transaction
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #51 Get transactions
		testingGetTransactions(personalNumber, 1004, "[" + strDate1003_1 + " 500,00 kr Saldo: 500,00 kr]");

		// #52 Get transactions
		testingGetTransactions(personalNumber, 1005, "[" + strDate1005_1 + " 1 000,00 kr Saldo: 1 000,00 kr, " + strDate1005_2 + " -100,00 kr Saldo: 900,00 kr, " + strDate1005_3 + " -102,00 kr Saldo: 798,00 kr]");        


		// #53 Get information about the customer including accounts
		testingGetCustomer("7505121231", "[7505121231 Lotta Larsson, 1004 500,00 kr Sparkonto 2,4 %, 1005 798,00 kr Sparkonto 2,4 %]");

		// #54 Creates accounts
		testingCreateSavingsAccount("7505121231", 1006);

		// #55 Closes the account
		testingCloseAccount("7505121231", 1006, "1006 0,00 kr Sparkonto 0,00 kr");	

		// #56 Deletes the customer including accounts
		testingDeleteCustomer("7505121231", "[7505121231 Lotta Larsson, 1004 500,00 kr Sparkonto 12,00 kr, 1005 798,00 kr Sparkonto 19,15 kr]");

		// #57 Creates accounts
		testingCreateSavingsAccount("6911258876", 1007);
		
		// #58 Transaction If a negative number is used for withdraw or deposit the transaction should not work
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= 1100;
		testingDeposit(personalNumber, accountNumber, amount, true);

		// #59 Transaction If a negative number is used for withdraw or deposit the transaction should not work
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= -1000;
		testingDeposit(personalNumber, accountNumber, amount, false);
		
		// #60 Transaction If a negative number is used for withdraw or deposit the transaction should not work
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= -100;
		testingWithdraw(personalNumber, accountNumber, amount, false);

		// #61 Transaction 
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= 2000;
		testingWithdraw(personalNumber, accountNumber, amount, true);
		
		// #62 Tries to delete a customer that don't exists
		testingDeleteCustomer("9905225166", "null");

		// #63 Tries to delete a customer that don't exists
		testingCloseAccount("7505121231", 1009, "null");

		// #64 Get information about all customers
		testingGetAllCustomers("[8505221898 Kalle Karlsson, 6911258876 Pelle Persson]");

		// #65 Get information about the account 
		testingGetAccount("6911258876", 1003, "null"); 

		// #66 Transaction
		personalNumber 	= "6911258876";
		accountNumber  	= 1003;
		amount 			= 900;
		testingDeposit(personalNumber, accountNumber, amount, false);

		// #67 Transaction
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= 900;
  		testingDeposit(personalNumber, accountNumber, amount, true);

		// #68 Get information about the customer including accounts
		testingGetCustomer("6911258876", "[6911258876 Pelle Persson, 1002 0,00 kr Kreditkonto 1,1 %, 1007 0,00 kr Sparkonto 2,4 %]");

		// #69 Transaction
		personalNumber 	= "6911258876";
		accountNumber  	= 1002;
		amount 			= 1000;
		testingWithdraw(personalNumber, accountNumber, amount, true);
		

		// #70 Transaction not owner of account
		personalNumber  = "6911258876";
		accountNumber   = 1003;
		amount          = 900;
		testingWithdraw(personalNumber, accountNumber, amount, false); 

		// #71 Change a customers name
		customerName = "";
		customerSurname = "";
		personalNumber = "8505221898";
		testingChangeName(customerName, customerSurname, personalNumber, false);


		// #72 Get information about the customer including accounts
		testingGetCustomer("6911258876", "[6911258876 Pelle Persson, 1002 -1 000,00 kr Kreditkonto 5 %, 1007 0,00 kr Sparkonto 2,4 %]");

		// #73 Deletes the customer
		testingDeleteCustomer("6911258876", "[6911258876 Pelle Persson, 1002 -1 000,00 kr Kreditkonto -50,00 kr, 1007 0,00 kr Sparkonto 0,00 kr]");

		// #74 Deletes the customer
		testingDeleteCustomer("8505221898", "[8505221898 Kalle Karlsson, 1003 10 500,00 kr Sparkonto 252,00 kr]");

		// #75 No customers left...
		testingGetAllCustomers("[]");
	}


	/**
	 * Tests the method getAllCustomers
	 * @param String facit - the facit that we compares with
	 */
	private void testingGetAllCustomers(String facit)
	{
		boolean pass = false;

		// Calls the function
		List<String> result = bank.getAllCustomers();

		// Convert the ArrayList to a String that we can compare with
		String str = result.toString();
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass) 
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - getAllCustomers()");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	/**
	 * Tests the method getCustomer
	 * @param String facit - the facit that we compares with
	 */
	private void testingGetCustomer(String pNo, String facit)
	{
		boolean pass = false;
		List<String> result = bank.getCustomer(pNo);

		// If the customer isn't in the bank result should be null
		// I want to use the string "null" to compare with
		String str = "null";

		// If the customer is found
		if(result != null)
		{
			// Convert the ArrayList to a string that we can compare with
			str = result.toString();

			// Because of the format of balance we have to
			// replace non-breaking-space with a normal space
			// and the negative sign from '−' to a '-'
			str = str.replaceAll("\\u00a0"," "); 
			str = str.replaceAll("−","-"); // 8722
			str = str.replaceAll("—","-"); // 8212
		}

		// Compare the strings
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - testingGetCustomer("+ pNo +")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	/**
	 * Tests the method getAccount
	 * @param pNo - Personal number of customer that owns the account
	 * @param accountId - Id of the account that will be printed
	 * @param String facit - the facit that we compares with
	 */
	private void testingGetAccount(String pNo, int accountId, String facit)
	{
		boolean pass = false;
		String result = bank.getAccount(pNo, accountId);

		// If we cant find that account for that customer result should be null
		// I want to use the string "null" to compare with
		String str = "null";

		//If account is found for that customer
		if(result != null)
		{
			// Convert the ArrayList to a string that we can compare with
			str = result.toString();

			// Because of the format of balance we have to
			// replace non-breaking-space with a normal space
			// and the negative sign from '−' to a '-'
			str = str.replaceAll("\\u00a0"," "); 
			str = str.replaceAll("−","-"); // 8722
			str = str.replaceAll("—","-"); // 8212
			str = str.replaceAll("\\.",",");
		}

		// Compare the strings
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - getAccount("+ pNo +", " + accountId + ")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	/**
	 * Tests the method getTransactions
	 * @param pNo - Personal number of customer that owns the account
	 * @param accountId - Id of the account that will be printed
	 * @param String facit - the facit that we compares with
	 */
	private void testingGetTransactions(String pNo, int accountId, String facit)
	{
		boolean pass = false;
		List<String> result = bank.getTransactions(pNo, accountId);

		// If the account is not found for the customer result should be null
		// I want to use the string "null" to compare with
		String str = "null";

		// If the customer is found
		if(result != null)
		{
			// Convert the ArrayList to a string that we can compare with
			str = result.toString();

			// Because of the format of balance we have to
			// replace non-breaking-space with a normal space
			// and the negative sign from '−' to a '-'
			str = str.replaceAll("\\u00a0"," "); 
			str = str.replaceAll("−","-"); // 8722
			str = str.replaceAll("—","-"); // 8212
			
		}

		// Compare the strings
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass)
			System.out.println("Test " + testCounter++ + ": PASS - " + str);
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - getTransactions("+ pNo +", " + accountId + ")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	/**
	 * Tests the method createCustomer
	 * @param name - Name of the customer
	 * @param pNo - Personal number of customer
	 * @param boolean facit - true if customer should be created otherwice false
	 */
	private void testingCreateCustomer(String name, String surname, String pNo, boolean facit)
	{
		// Just check if we get the right value back and give feedback to the user
		if(bank.createCustomer(name, surname, pNo) == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - createCustomer("+name+", "+ surname+", "+ pNo+")");
		}
	}

	/**
	 * Tests the method changeName
	 * @param name - The new name
	 * @param surname - The new surname
	 * @param pNo - Personal number of customer that is getting a new name
	 * @param facit - true if customer name should change otherwise false
	 */
	private void testingChangeName(String name, String surname, String pNo, boolean facit)
	{
		// Just check if we get the right value back and give feedback to the user
		if(bank.changeCustomerName(name, surname, pNo) == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - testingChangeName("+name+", "+ surname+", "+ pNo+")");
		}
	}

	/**
	 * Tests the method createSavingsAccount
	 * @param pNo - Personal number of customer that is getting a new account
	 * @param facit - "-1" if a account is not created otherwise a accountnumber
	 */
	private void testingCreateSavingsAccount(String pNo, int facit)
	{
		int accountNo = bank.createSavingsAccount(pNo);
		// Check if we get the right value back and give feedback to the user
		if(accountNo == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - createSavingsAccount("+pNo+")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + accountNo);
		}
	}

	/**
	 * Tests the method createCreditAccount
	 * @param pNo - Personal number of customer that is getting a new account
	 * @param facit - "-1" if a account is not created otherwise a accountnumber
	 */
	private void testingCreateCreditAccount(String pNo, int facit)
	{
		int accountNo = bank.createCreditAccount(pNo);
		// Check if we get the right value back and give feedback to the user
		if(accountNo == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - createCreditAccount("+pNo+")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + accountNo);
		}
	}

	/**
	 * Tests the method deposit
	 * @param pNo - The personal number of the customer that owns the account
	 * @param accountId -  The id of the account
	 * @param amount - The amount
	 * @param facit - true if the amount is deposit false otherwise
	 */
	private void testingDeposit(String pNo, int accountId, int amount, boolean facit)
	{
		// Check if we get the right value back and give feedback to the user
		if(bank.deposit(pNo, accountId, amount) == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - deposit("+ pNo +", "+ accountId+", " + amount +")");
		}			
	}

	/**
	 * Tests the method withdraw
	 * @param pNr - The personal number of the customer that owns the account
	 * @param accountId -  The id of the account
	 * @param amount - The amount
	 * @param facit - true if the amount is withdraw false otherwise
	 */
	private void testingWithdraw(String pNo, int accountId, int amount, boolean facit)
	{
		// Check if we get the right value back and give feedback to the user
		if(bank.withdraw(pNo, accountId, amount) == facit)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - withdraw("+ pNo +", "+ accountId+", " + amount +")");
		} 
	}

	/**
	 * Tests the method withdraw closeAccount
	 * @param pNo - The personal number of the customer that owns the account
	 * @param accountId - The id of the account that is to be closed
	 * @param String facit - the facit that we compares with
	 */
	private void testingCloseAccount(String pNo, int accountId, String facit)
	{
		boolean pass = false;
		String result = bank.closeAccount(pNo, accountId);

		// If the account wasn't closed result should be null
		// I want to use the string "null" to compare with
		String str = "null";

		// If the account is closed
		if(result != null)
		{
			// Convert the ArrayList to a string that we can compare with
			str = result.toString();

			// Because of the format of balance we have to
			// replace non-breaking-space with a normal space
			// and the negative sign from '−' to a '-'
			str = str.replaceAll("\\u00a0"," "); 
			str = str.replaceAll("−","-"); // 8722
			str = str.replaceAll("—","-"); // 8212
		}

		// Compare the two strings
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - closeAccount("+pNo+", " + accountId + ")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	/**
	 * Tests the method withdraw deleteCustomer
	 * @param pNo - The personal number of the customer that is to be deleted
	 * @param String facit - the facit that we compares with
	 */
	private void testingDeleteCustomer(String pNo, String facit)
	{	
		boolean pass = false;
		List<String> result = bank.deleteCustomer(pNo);

		// If the account wasn't closed result should be null
		// I want to use the string "null" to compare with
		String str = "null";

		// If the customer is deleted
		if(result != null)
		{
			// Convert the ArrayList to a string that we can compare with
			str = result.toString();

			// Because of the format of balance we have to
			// replace non-breaking-space with a normal space
			// and the negative sign from '−' to a '-'
			str = str.replaceAll("\\u00a0"," "); 
			str = str.replaceAll("−","-"); // 8722
			str = str.replaceAll("—","-"); // 8212
		}

		// Compare the two strings
		pass = facit.equals(str);

		// Give feedback to the user
		if(pass)
			System.out.println("Test " + testCounter++ + ": PASS");
		else
		{
			System.out.println("Test " + testCounter++ + ": FAIL - testingDeleteCustomer("+pNo+")");
			System.out.println("Expected : " + facit);
			System.out.println("Factual  : " + str);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, InterruptedException
	{		
		TestBank2 bankMenu = new TestBank2();
		bankMenu.test();	
	}
}
