import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class BankServerThread extends Thread{

	protected Socket ActionClientSocket;
	  private SharedActionState sharedActionStateObject;
	  protected String threadName;
	  protected PrintWriter out;
	  protected BufferedReader in;
	  String ActionServerName = "localhost";
	  int ActionSocketNumber = 4545;
	
	  // Thread initialisation - constructor
	  	public BankServerThread(Socket ActionClientSocket, String BankServerThreadName, SharedActionState SharedObject) throws IOException {
		  this.ActionClientSocket = ActionClientSocket;
	  	this.sharedActionStateObject = SharedObject;
		  this.threadName = BankServerThreadName;
		}
	  	
	    public void run() {
	        try {
	        // Attempt initialisation of printwriter and bufferedreader 
	          System.out.println("Thread " + threadName + " initialising...");
	          String inputLine, outputLine = "";

	          // Initialise BufferedReader and PrintWriter for server...
	  		PrintWriter out = new PrintWriter(ActionClientSocket.getOutputStream(), true);
	  		BufferedReader in = new BufferedReader(new InputStreamReader(ActionClientSocket.getInputStream()));

	  		System.out.println("Listening for client input...");
	          // Listen for input
	          while ((inputLine = in.readLine()) != null) {
	        	  try {
	        	  // Acquire a lock, and then execute the processInput function, then release the lock...
	        	  Crit_Section(inputLine, outputLine, out);
	        	  } catch (Exception e) {
	        		  System.err.println("Failed to get lock when reading:"+e);
	        	  }
	          }

	          out.close();
	          in.close();
	          ActionClientSocket.close();
	          
	        } catch (IOException e) {
	          System.out.println("Thread "+ threadName +" over - exception: "+e );
	        }
	    }
	    
	    // Includes lock acquisition, execution and release
	   void Crit_Section(String inputLine, String outputLine, PrintWriter out) {
	    	try { 
      		  sharedActionStateObject.acquireLock();  
      		  outputLine = sharedActionStateObject.processInput(threadName, inputLine);
      		  out.println(outputLine);
      		  sharedActionStateObject.releaseLock();  
      	  } 
      	  catch(InterruptedException e) {
      		  System.err.println("Failed to get lock when reading:"+e);
      		  out.println("Failed to get lock when reading");
      	  }
	    }
	    
	 
}