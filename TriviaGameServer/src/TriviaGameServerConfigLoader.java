import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;


public class TriviaGameServerConfigLoader {
	private static BufferedReader reader = null;
	private static Gson gson = new Gson();
	
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
