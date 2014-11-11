import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class TriviaGameClient implements Runnable{
	
	private static Socket client_socket = null;
	private static PrintStream output_stream = null;
	private static DataInputStream input_stream = null;
	
	private static boolean is_closed = false;
	
	public static void main(String[] args){
		int port = 9000;
		String host = "168.16.201.163";
		
		try{
			client_socket = new Socket(host, port);
			
			output_stream = new PrintStream(client_socket.getOutputStream());
			input_stream = new DataInputStream(client_socket.getInputStream());
			
			 if (client_socket != null && output_stream != null && input_stream != null){
				 new Thread(new TriviaGameClient()).start();
			 }
			 
			 
			while(! is_closed){
				
			}
			 
			 output_stream.close();
			 input_stream.close();
			 client_socket.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		String response;
		
		try{
			while((response = input_stream.readLine()) != null){
				System.out.println(response);
				
				if(response.equals("Server: Bye"))
					break;
			}
			is_closed = true;
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
