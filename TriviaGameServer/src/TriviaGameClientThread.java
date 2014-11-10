import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;


public class TriviaGameClientThread extends Thread {
	
	private DataInputStream input_stream = null;
	private PrintStream output_stream = null;
	private Socket client_socket = null;
	private final TriviaGameClientThread[] threads;
	private int max_clients;
	private Question[] question_list;
	private boolean ready = false;
	
	public TriviaGameClientThread(Socket client_socket, TriviaGameClientThread[] threads){
		this.client_socket = client_socket;
		this.threads= threads;
		
		question_list = buildQuestionList(1);
	}
	
	public void run(){
		int max_clients_count = this.max_clients;
		TriviaGameClientThread[] threads = this.threads;
		int score = 0;
		
		try{
			input_stream = new DataInputStream(client_socket.getInputStream());
			output_stream = new PrintStream(client_socket.getOutputStream());
			
			output_stream.println("Enter your username.");
			
			String username = input_stream.readUTF().trim();
			
			output_stream.println("Welcome " + username + " to our trivia game!");
			
			for(int i = 0; i < max_clients_count; i++){
				if(threads[i] != null && threads[i] != this){
					threads[i].output_stream.println(username + " are here to play.");
				}
			}
			String line;
			boolean ready_to_play;
			while(true){
				ready_to_play = true;
				line = input_stream.readUTF();
				if(line.startsWith("/ready")){
					ready = true;
				}
				if(line.startsWith("/exit")){
					break;
				}
				for(int i = 0; i < max_clients_count; i++){
					if(threads[i] != null && ! threads[i].isReady()){
						ready_to_play = false;
					}
				}
				
				if(ready_to_play){
					for(int i = 0; i < question_list.length; i++){
						output_stream.println(question_list[i].getQuestion());
						output_stream.println("A." + question_list[i].getAnswerA());
						output_stream.println("B." + question_list[i].getAnswerB());
						output_stream.println("C." + question_list[i].getAnswerC());
						output_stream.println("D." + question_list[i].getAnswerD());
						
						if(question_list[i].isCorrect(input_stream.readChar())){
							output_stream.println("Correct!");
							score++;
						}
						else{
							output_stream.println("Incorrect.");
						}
					}
					
					score = 0;
					
				}
				
				output_stream.println("Come and play again" + username + ".");
				
				
			}
			
			for(int i = 0; i < max_clients_count; i++){
				if(threads[i] != null && threads[i] != this){
					threads[i].output_stream.println(username + " has left the game.");
				}
			}
			
			input_stream.close();
			output_stream.close();
			client_socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/*
	 * This is where we will pull question form database and put them into an array to be showed to players.
	 */
	public Question[] buildQuestionList(int number_of_questions){
		//TODO
		Question[] questions = new Question[1];
		String [] question_string = {"What color is the sky?", "Magenta", "Green", "Blue", "Black"};
		questions[0] = new Question(question_string, 'C');
		
		return questions;
	}
	
	public boolean isReady(){
		return ready;
	}
			
}
