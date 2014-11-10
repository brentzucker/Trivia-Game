import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;


public class TriviaGameClientThread extends Thread {
	
	private DataInputStream input_stream = null;
	private PrintStream output_stream = null;
	private Socket client_socket = null;
	private final TriviaGameClientThread[] threads;
	private int max_clients;
	private Question[] question_list;
	private boolean ready = false;
	
	public TriviaGameClientThread(Socket client_socket, TriviaGameClientThread[] threads) throws IOException
	{
		this.client_socket = client_socket;
		this.threads= threads;
		
		question_list = buildQuestionList();
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
						output_stream.println(question_list[i].question);
						output_stream.println("A." + question_list[i].choices[0]);
						output_stream.println("B." + question_list[i].choices[1]);
						output_stream.println("C." + question_list[i].choices[2]);
						output_stream.println("D." + question_list[i].choices[3]);
						
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
	public Question[] buildQuestionList()throws IOException
	{
		File file = new File("question_set.txt");
		Scanner inputFile = new Scanner(file);

		//TODO
		//Question[] questions = new Question[number_of_questions];
		
		ArrayList<Question> question_array_list = new ArrayList<Question>();

		while(inputFile.hasNextLine())
		{
			String question = inputFile.nextLine();
			String[] choices = inputFile.nextLine().split(", ");
			char answer = inputFile.nextLine().charAt(0);

			question_array_list.add(new Question(question, choices, answer));
		}

		Question[] questions = question_array_list.toArray(new Question[question_array_list.size()]);
		
		return questions;
	}
	
	public boolean isReady(){
		return ready;
	}
}
