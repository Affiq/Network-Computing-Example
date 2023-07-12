import java.util.ArrayList;

public class SharedActionState {

	private String ThreadName;
	private double BalanceA;
	private double BalanceB;
	private double BalanceC;
	private boolean accessing=false;
	
	// Constructor for our initial balance in each account
	SharedActionState(double InitialBalance) {
		this.BalanceA = InitialBalance;
		this.BalanceB = InitialBalance;
		this.BalanceC = InitialBalance;
	}
	
	
	// Lock Acquisition
	public synchronized void acquireLock() throws InterruptedException{
	        Thread currentThread = Thread.currentThread(); // Get reference for current thread
	        while (accessing) {
		      wait();
		    }
		    accessing = true;
		    System.out.println(currentThread.getName()+" acquired a lock"); 
	}
	
	
	// Lock Release
	public synchronized void releaseLock() {
        Thread currentThread = Thread.currentThread(); // Get reference for current thread
	    System.out.println(currentThread.getName()+" released a lock"); 
		accessing = false;
		notifyAll();
	}
	
	// Function to process inputs messages sent by the threads from the client
	public synchronized String processInput(String myThreadName, String theInput) {
		System.out.println("Lock acquired");
		System.out.println(myThreadName + " received message ["+ theInput+"]");
		String messageOutput = null;
		theInput = theInput.toUpperCase();
		
		// Check the input of the string and see which operation to perform
		try {
			
			String[] parameters = theInput.split(" ");
			String command = parameters[0];
			
			if (command.equals("ADD")) { // Perform add money function
				double value = Double.parseDouble(parameters[1]);
				Add_Money(myThreadName, value);				
				messageOutput = value + " added to " + myThreadName;
			}
			else if (command.equals("SUBTRACT")) { // Perform subtract money function
				double value = Double.parseDouble(parameters[1]);
				Subtract_Money(myThreadName, value);
				messageOutput = value + " subtracted from " + myThreadName;
			}
			else if (command.equals("TRANSFER")) { // Perform transfer money function
				String recipientAccount = parameters[1];
				double value = Double.parseDouble(parameters[2]);
				Transfer_Money(myThreadName, recipientAccount, value);
				messageOutput = value + " transferred from " + myThreadName + " to " + recipientAccount;
			}
			else 
				messageOutput = "Invalid command entered"; // Invalid first command
			
		} catch (Exception e) {
			messageOutput = "Exception occured parsing command"; // Usually occurs for invalid parameters of correct commands
		}
		
		Print_Balances();
		return messageOutput;
	}	
	
	
	
	// Add Money Function
	public void Add_Money(String account, double value) throws Exception {
		
		// Validity checks to ensure atomicity
		boolean validAccount = Valid_Account_Check(account);
		boolean positiveValue = Positive_Value_Check(value);
		
		// If checks fail, do not do operation
		if (!(validAccount && positiveValue)) {
			System.out.println("Invalid values added. Addition transaction did not go through.\n");
			throw new Exception("Invalid values");
		}
		else {
		// Check which account to add to and do accordingly...
			if (account.equals("A"))
				BalanceA = BalanceA + value;
			else if (account.equals("B"))
				BalanceB = BalanceB + value;
			else if (account.equals("C"))
				BalanceC = BalanceC + value;
			
			// Logging the add_money action
			System.out.println("Added "+value+" from Account "+account+"\n");
		}
	}
	
	
	// Subtract Money Function
	public void Subtract_Money(String account, double value) throws Exception {
		
		
		// Validity checks to ensure atomicity
		boolean validAccount = Valid_Account_Check(account);
		boolean positiveValue = Positive_Value_Check(value);
		
		// If checks fail, do not do operation
		if (!(validAccount && positiveValue)) {
			System.out.println("Invalid values in subtract command. Subtract transaction did not go through.\n");
			throw new Exception("Invalid values");
		}
		else {
			// Check which account to subtract from and do accordingly...
			if (account.equals("A"))
				BalanceA = BalanceA - value;
			else if (account.equals("B"))
				BalanceB = BalanceB - value;
			else if (account.equals("C"))
				BalanceC = BalanceC - value;
			else
				throw new Exception("");
				
			// Logging the subtract_money action
			System.out.println("Subtracted "+value+" from Account "+account+"\n" );
		}
	}
	
	
	public void Transfer_Money(String account1, String account2, double value) throws Exception {
	
		// Validity checks to ensure atomicity
		boolean validAccount1 = Valid_Account_Check(account1);
		boolean validAccount2 = Valid_Account_Check(account2);
		boolean transferAccount = Transfer_Account_Check(account1, account2);
		boolean positiveValue = Positive_Value_Check(value);
		
		
		// If checks fail, do not do operation
		if (!(validAccount1 && validAccount2 && transferAccount && positiveValue)) {
			System.out.println("Invalid values in transfer command. Transfer transaction did not go through.\n");
			throw new Exception("Invalid values");
		}
		else {
			// Perform a subtraction to account1...
			if (account1.equals("A"))
				BalanceA = BalanceA - value;
			else if (account1.equals("B"))
				BalanceB = BalanceB - value;
			else if (account1.equals("C"))
				BalanceC = BalanceC - value;
			
			// Perform an addition to account2...
			if (account2.equals("A"))
				BalanceA = BalanceA + value;
			else if (account2.equals("B"))
				BalanceB = BalanceB + value;
			else if (account2.equals("C"))
				BalanceC = BalanceC + value;
			
			// Logging the transfer money action
			System.out.println("Transferred "+value+" from Account "+account1 + " to Account "+account2+"\n");
		}
	}
	
	public void Print_Balances() {
		System.out.println("A Balance: " + BalanceA);
		System.out.println("B Balance: " + BalanceB);
		System.out.println("C Balance: " + BalanceC);		
	}
	
	public boolean Valid_Account_Check(String account) {
		// Validity check...
		ArrayList<String> AccountList = new ArrayList<String>();
		AccountList.add("A");
		AccountList.add("B");
		AccountList.add("C");
		
		if (AccountList.contains(account))
			return true;
		else
			return false;

	}
	
	public boolean Positive_Value_Check(double value) {
		if (value > 0) 
			return true;
		else
			return false;
	}
	
	public boolean Transfer_Account_Check(String account1, String account2) {
		if (account1.equals(account2))
			return false;
		else
			return true;
	}
}
