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
		
	
	public TriviaGameClientThread(Socket client_socket){
		this.client_socket = client_socket;
	}
	
	public void run(){
		TriviaGameClientThread helper;
		String temp;
		try{
			System.out.println("New thread started");
			input_stream = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			output_stream = new PrintStream(client_socket.getOutputStream());
			
			output_stream.println("Hello, send me somthing.");
			
			while((temp = input_stream.readLine()) != null)
			{
				System.out.println(temp);
				if(temp.equals("/exit"))
					break;
			}
			
			
			output_stream.println("***END***");
			
			//Remove thread and push null TriviaGameClientThread to thread pool
			for(TriviaGameClientThread thread: TriviaGameServer.running_threads){
				if(thread == this){
					TriviaGameServer.running_threads.remove(thread);
					helper = null;
					TriviaGameServer.threads.push(helper);
					System.out.println("Thread removed.");
					
					
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