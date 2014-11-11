import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class TriviaGameClient implements Runnable {
	
	private static Socket client_socket = null;
	private static PrintStream output_stream = null;
	private static DataInputStream input_stream = null;
	private static BufferedReader input_line = null;
	private static boolean is_closed = false;
	
	public static void main(String[] args){
		int port = 9000;
		String host = "localhost";
		
		try{
			client_socket = new Socket(host, port);
			input_line = new BufferedReader(new InputStreamReader(System.in));
			output_stream = new PrintStream(client_socket.getOutputStream());
			input_stream = new DataInputStream(client_socket.getInputStream());
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(client_socket != null && output_stream != null && input_stream != null){
			try{
				new Thread(new TriviaGameClient()).start();
				while(!is_closed){
					output_stream.println(input_line.readLine().trim());
				}
				
				output_stream.close();
				input_stream.close();
				client_socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		String response_line;
		
		try{
			while((response_line = input_stream.readUTF()) != null){
				System.out.println(response_line);
				
				if(response_line.indexOf("Come and play again") != -1){
					break;
				}
			}
			is_closed = true;
			input_line.close();
		}catch(IOException e){
			e.printStackTrace();
		}

	}

}
