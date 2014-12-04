import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.ListIterator;
import java.util.TreeMap;

import com.google.gson.Gson;

public class TriviaGameServer
{	
	//scores_map keeps track of scores
	public static TreeMap <String, Integer> scores_map = new TreeMap<String, Integer>();
	
	// Load Server configuration
	private static final TriviaGameServerConfig server_config = TriviaGameServerConfigLoader.loadServerConfig();
	
	private static ServerSocket server_socket = null;
	private static Socket client_socket = null;
	
	public static boolean flag_all_players_in = false;
	public static int player_answer_counter = 0;
	public static int player_count =0;
	public static boolean flag_next_question = false;
	
	public static int players_playing_this_game = 2; //this is how many players MUST BE running client side for the game to work
	
	public static final Stack<TriviaGameClientThread> threads = new Stack<TriviaGameClientThread>();
	public static final LinkedList<TriviaGameClientThread> running_threads = new LinkedList<TriviaGameClientThread>();
	
	public static void main(String[] args){
		TriviaGameClientThread helper;
		
		//Initialize server_socket to a new ServerSocket at the port from the configuration.
		try
		{
			server_socket = new ServerSocket(server_config.getPort()); // coming from JSON file
			System.out.println("Server is up on port " + server_config.getPort());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//Start threads stack
		for(int i = 0; i < server_config.getMaxClients(); i++){
			 
			helper = null;
			threads.push(helper);
		}
		
		GameObject game = new GameObject();
		
		//Enter infinite running loop for accepting new connections and running the game.
		while(true)
		{
			try
			{
				client_socket = server_socket.accept();
				System.out.println(client_socket.getInetAddress() +":" + client_socket.getPort() + " has connected.");

				if(threads.isEmpty())
				{
					PrintStream output_stream = new PrintStream(client_socket.getOutputStream());
					
					output_stream.println("Game is full. Try Later.");
					output_stream.close();
					client_socket.close();
				}	
				else
				{
					helper = threads.pop();
					helper = new TriviaGameClientThread(client_socket, game);
					player_count++;
					
					//TEST
					if(player_count % players_playing_this_game == 0) //this checks to see if enough clients are in the game to play
						flag_all_players_in = true; 
					
					helper.start();
					running_threads.add(helper);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static int getPlayerAnswerCounter()
	{
		return player_answer_counter;
	}
	
	public static void resetGame()
	{
		flag_all_players_in = false;
		player_answer_counter = 0;
		player_count =0;
		flag_next_question = false;
		scores_map.clear();
		sendToClients("scoreboard:"+scores_map.toString());
	}
	
	//when called sends message to every client
	public static void sendToClients(String send_str)
	{
		ListIterator <TriviaGameClientThread> iterator = running_threads.listIterator();
		
		while(iterator.hasNext())
		{
			TriviaGameClientThread client_thread = iterator.next();
			
			client_thread.output_stream.println(send_str);
		}
	}
	
	public static void updateScores(String ip_usr_score)
	{
		System.out.println(ip_usr_score);
		String[] raw_array = ip_usr_score.split("-");
		String key = raw_array[0];
		int score = Integer.parseInt(raw_array[1]);
		//String score = raw_array[1];
		
		//update score
		scores_map.put(key, score);
		
		//sort map - returns string of sorted map
		String sorted_scores = sortMap(scores_map);
		
		System.out.println(sorted_scores);
		
		//sendToClients("scoreboard:"+scores_map.toString());
		sendToClients("scoreboard:"+sorted_scores);
	}
	
	public static void updatePlayerCount(String player_count)
	{
		players_playing_this_game = Integer.parseInt(player_count.substring(15));
		
		System.out.println("players_playing_this_game:"+players_playing_this_game);
		
		//sendToClients(new_player_count);
	}
	
	public static String sortMap(TreeMap<String, Integer> tm)
	{
		String sorted_string = "";
		TreeMap<String, Integer > temp = new TreeMap<String, Integer>(tm);
		
		int max = 0; 
		String key="";
		
		while(temp.size() != 0)
		{
			//iterate through map, find max value, add to sorted_string and then remove from map
			for(Map.Entry<String, Integer> entry : temp.entrySet())
			{
				if(entry.getValue() >= max)
				{
					max = entry.getValue();
					key = entry.getKey();
				}
			}
			sorted_string = sorted_string + key +" "+ max+ "\n";
			temp.remove(key);
		}
		
		return sorted_string;
	}
}