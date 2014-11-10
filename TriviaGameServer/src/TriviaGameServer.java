import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;


public class TriviaGameServer {
	
	private static Gson gson = new Gson();
	private static BufferedReader reader = null;
	private static TriviaGameServerConfig server_config;
	private static ServerSocket server_socket = null;
	private static Socket client_socket = null;
	private static TriviaGameClientThread[] threads;
	private static PrintStream output_stream = null;
	
	
	
	public static void main(String[] args){
		server_config = loadServerConfig();
		threads = new TriviaGameClientThread[server_config.getMaxClients()];
		
		
		try{
			server_socket = new ServerSocket(server_config.getPort());
		}catch (IOException e){
			e.printStackTrace();
		}
		
		System.out.println("Server is running on port " + server_config.getPort() + ".");
		
		while(true){
			try{
				client_socket = server_socket.accept();
				
				System.out.println(client_socket.getInetAddress() +":" + client_socket.getPort() + " has connected.");
				
				for(int i = 0; i < server_config.getMaxClients(); i++){
					if(threads[i] == null){
						(threads[i] = new TriviaGameClientThread(client_socket, threads)).start();
						break;
					}
				}
				
				output_stream = new PrintStream(client_socket.getOutputStream());
				
				output_stream.println("Server has reached max number of players. try again in a bit.");
				
				output_stream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			try{
				client_socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	/* 
	 * Reads file TriviaGameServer.config for a JSON string and the uses that to return a TriviaGameServerConfig object
	 */
	public static TriviaGameServerConfig loadServerConfig(){
		String json_string = "";
		try{
			reader = new BufferedReader(new FileReader("config/TriviaGameServer.json"));
			
			for(String temp = reader.readLine(); temp !=null; temp = reader.readLine()){
				json_string = json_string + temp;
			}
			
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return gson.fromJson(json_string, TriviaGameServerConfig.class);
	}
	
}
