import java.io.IOException;
import java.net.ServerSocket;

public class BankServer {
	
	public static void main(String[] args) throws IOException {
		  // Some server details...
		  ServerSocket BankServerSocket= null;
		  boolean listening = true;
		  String BankServerName = "Bank Server";
		  int BankServerNumber = 4545;
		    
		  // This will be the balance of the individual accounts A B and C
		  double InitialClientBalance = 1000;
		  // Create our shared object...
		  SharedActionState sharedActionStateObject = new SharedActionState(InitialClientBalance);

		  // Initialise Server Socket...
		  try {
			  BankServerSocket= new ServerSocket(BankServerNumber);
		  } catch (IOException e) {
		      System.err.println("Could not start " + BankServerName + " on specified port: "+BankServerNumber);
		      System.exit(-1);
		  }
		      System.out.println(BankServerName + " started on port: "+ BankServerNumber);
		      System.out.println("Welcome to the Bank Server console...");
		    
			  // Let us create a thread for each of our respective clients...
		      // I believe the threads have to be in the server...
		      // I want the main of the clients to execute a critical section, but that involves the static problem
		      // The threads should not execute within there anyway...
		      
		      new BankServerThread(BankServerSocket.accept(), "A", sharedActionStateObject).start();
		      new BankServerThread(BankServerSocket.accept(), "B", sharedActionStateObject).start();
		      new BankServerThread(BankServerSocket.accept(), "C", sharedActionStateObject).start();

		      
		      try {
		    	  while (listening){

				  }
		      } catch (Exception e) {
		    	  System.out.println("Done listening mate");
		      }
		  
		  // End the server...
		  // BankServerSocket.close();
		  
	  }
	
}
