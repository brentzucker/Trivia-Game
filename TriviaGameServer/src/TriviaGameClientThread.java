import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class TriviaGameClientThread extends Thread{
	// Load Server configuration
		private static final TriviaGameServerConfig server_config = TriviaGameServerConfigLoader.loadServerConfig();
		
		private BufferedReader input_stream = null;
		private PrintStream output_stream = null;
		private Socket client_socket = null;
		private final TriviaGameClientThread[] threads;
		
	
	public TriviaGameClientThread(Socket client_socket, TriviaGameClientThread[] threads){
		this.client_socket = client_socket;
		this.threads = threads;
	}
	
	public void run(){
		try{
			System.out.println("New thread started");
			input_stream = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			output_stream = new PrintStream(client_socket.getOutputStream());
			
			System.out.println("Server: Welcome to our server. Future home of the Cool Kids Trivia Game!");
			output_stream.println("Server: Welcome to our server. Future home of the Cool Kids Trivia Game!");
			System.out.println("Server: Bye");
			output_stream.println("Server: Bye");
			
			for(TriviaGameClientThread thread: threads){
				if(thread == this){
					System.out.println("Thread removed.");
					thread = null;
				}
			}
			
			input_stream.close();
			output_stream.close();
			client_socket.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}