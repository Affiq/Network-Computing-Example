import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientA extends Client{
	

	// Constructor from superclass
    public ClientA() {
    }

    // Socket details
    static int ActionSocketNumber = 4545;
    static String ActionServerName = "localhost";
    static String ActionClientID = "Alpha";
    PrintWriter out;
    BufferedReader in; 
  
	public static void main(String[] args) throws IOException, InterruptedException {
		// Client initiation - run the object
		try {
			// Initialise the BufferedReader and PrintWriter
	        Socket ActionClientSocket = new Socket(ActionServerName, ActionSocketNumber);
			PrintWriter out = new PrintWriter(ActionClientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(ActionClientSocket.getInputStream()));
			
		    System.out.println("Initialised Client " + ActionClientID + " and IO connections");
		    System.out.println("Welcome to Client "+ ActionClientID + " console...");
		    
			String fromUser = "", fromServer = "";

		    // We can pre-programme some input here for our clients...
		    // Add 100 Transaction    	    
		    fromUser = "add 100";
		    Thread.sleep(Random_Time());
	        System.out.println(ActionClientID + " sending message [" + fromUser + "] to Bank Server");
	        out.println(fromUser);
	        fromServer = in.readLine();
	        System.out.println(ActionClientID + " received [" + fromServer + "] from Bank Server");
	        
		    // Subtract 100 Transaction
		    fromUser = "subtract 500";
		    Thread.sleep(Random_Time());
	        System.out.println(ActionClientID + " sending message [" + fromUser + "] to Bank Server");
	        out.println(fromUser);
	        fromServer = in.readLine();
	        System.out.println(ActionClientID + " received [" + fromServer + "] from Bank Server");
	        
	        ActionClientSocket.close();
	        in.close();
	        out.close();
	        
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ ActionSocketNumber);
            System.exit(1);
        }
	
    }
	
}
