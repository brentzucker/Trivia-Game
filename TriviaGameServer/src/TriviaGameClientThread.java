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
		public GameObject g1;
		
	
	public TriviaGameClientThread(Socket client_socket, GameObject g){
		this.client_socket = client_socket;
		g1 = new GameObject(g);
	}
	
	public void run(){
		TriviaGameClientThread helper;
		String temp;
		try{
			System.out.println("New thread started");
			input_stream = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			output_stream = new PrintStream(client_socket.getOutputStream());
			
			output_stream.println("Waiting for game to fill up...");
			
			while(!TriviaGameServer.flag)
			{
				
			}
			
			while(!g1.question_stack.empty())
			{
				g1.nextQuestion();
				output_stream.println(g1.current_question.question+ "\n"+g1.current_question.choicesToString());
				
				if((temp = input_stream.readLine()) != null)
				{
					System.out.println("You answered: "+temp);
					if(g1.current_question.isCorrect(temp.charAt(0)))
						output_stream.println("Correct!");
					else
						output_stream.println("Wrong, the correct answer was "+g1.current_question.choices[g1.current_question.answer]);
					
					if(temp.equals("/exit"))
						break;
				}
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