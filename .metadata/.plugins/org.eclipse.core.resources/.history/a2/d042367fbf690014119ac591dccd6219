import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;



public class TriviaGameServer{
	
	// Load Server configuration
	private static final TriviaGameServerConfig server_config = TriviaGameServerConfigLoader.loadServerConfig();
	
	private static ServerSocket server_socket = null;
	private static Socket client_socket = null;
	private static final TriviaGameClientThread[] threads = new TriviaGameClientThread[server_config.getMaxClients()];
	
	public static void main(String[] args){
		
		
		//Initialize server_socket to a new ServerSocket at the port from the configuration.
		try{
			server_socket = new ServerSocket(server_config.getPort());
			System.out.println("Server is up on port " + server_config.getPort());
		}catch(IOException e){
			e.printStackTrace();
		}
		
		// Enter infinite running loop for accepting new connections and running the game.
		while(true){
			try{
				client_socket = server_socket.accept();
				System.out.println(client_socket.getInetAddress() +":" + client_socket.getPort() + " has connected.");
				
				int count = 0;
				
				for(count = 0; count < server_config.getMaxClients(); count++){
					if(threads[count] == null){
						(threads[count] = new TriviaGameClientThread(client_socket, threads)).start();
						break;
					}
				}
				if(count == server_config.getMaxClients()){
					PrintStream output_stream = new PrintStream(client_socket.getOutputStream());
					
					output_stream.println("Game is full. Try Later.");
					output_stream.close();
					client_socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}