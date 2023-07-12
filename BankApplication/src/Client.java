import java.util.Random;

public class Client {

    // Function to get random time values - ensures that the objects arent always executed in the same sequence.
    // Discrete values allow certain threads to occur at the same time and then 'compete' for the lock
    protected static int Random_Time() {
    	int[] random_times = {7000,8000,9000,10000,11000}; 
    	
    	Random rand = new Random();
    	int randn = rand.nextInt(5);
    	return random_times[randn];
    	
    }
	
}
