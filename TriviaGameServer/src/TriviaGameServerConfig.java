/*
 * Class that will be created from a JSON string and be used for configuring the server.
 */
public class TriviaGameServerConfig {
	private int max_clients;
	private int port;
	
	public int getMaxClients(){
		return max_clients;
	}
	
	public int getPort(){
		return port;
	}
}
