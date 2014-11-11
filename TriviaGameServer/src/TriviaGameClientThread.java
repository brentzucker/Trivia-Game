import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class TriviaGameClientThread extends Thread{
	// Load Server configuration
		private static final TriviaGameServerConfig server_config = TriviaGameServerConfigLoader.loadServerConfig();
		
		private DataInputStream input_stream = null;
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
			System.out.println((input_stream = new DataInputStream(client_socket.getInputStream())) == null);
			System.out.println((output_stream = new PrintStream(client_socket.getOutputStream())) == null);
			
			System.out.println("Server: Welcome to our server. Future home of the Cool Kids Trivia Game!");
			output_stream.println("Server: Welcome to our server. Future home of the Cool Kids Trivia Game!");
			System.out.println("Server: Bye");
			output_stream.println("Server: Bye");
			
			for(TriviaGameClientThread thread: threads){
				if(thread == this){
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