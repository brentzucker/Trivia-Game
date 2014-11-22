import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class TriviaGameClient extends JFrame implements Runnable{
	
	private static Socket client_socket = null;
	private static PrintStream output_stream = null;
	private static BufferedReader input_stream = null;
	private static BufferedReader output_line = null;
	
	public static String client_question;
	public static String[] client_choices = new String[4];
	
	private static boolean is_closed = false;
   
	public static JButton button1;
   	public static JButton button2;
	public static JButton button3;
	public static JButton button4;
	
	public static JLabel messageLabel;
   
   public static JPanel south_panel;

	public TriviaGameClient()
	{
		setTitle("Client GUI");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(800, 600);
		
		setLayout(new BorderLayout());
		
		JPanel center_panel = new JPanel();
		JPanel west_panel = new JPanel();
		JPanel east_panel = new JPanel();
		JPanel north_panel = new JPanel();
		south_panel = new JPanel();
		
		String choice_A = client_choices[0];
		String choice_B = client_choices[1];
		String choice_C = client_choices[2];
		String choice_D = client_choices[3];
		
		messageLabel = new JLabel(client_question);
      
      
		
		button1 = new JButton(choice_A);
		button2 = new JButton(choice_B);
		button3 = new JButton(choice_C);
		button4 = new JButton(choice_D);
      
		button1.addActionListener(new ChoiceAListener());
		button2.addActionListener(new ChoiceBListener());
		button3.addActionListener(new ChoiceCListener());
		button4.addActionListener(new ChoiceDListener());
		
		south_panel.add(button1);
		south_panel.add(button2);
		south_panel.add(button3);
		south_panel.add(button4);
		
		center_panel.add(messageLabel);
		
		
		
		add(south_panel, BorderLayout.SOUTH);
		add(north_panel, BorderLayout.NORTH);
		add(east_panel, BorderLayout.EAST);
		add(west_panel, BorderLayout.WEST);
		add(center_panel, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		int port = 9000;
		String host = "localhost";
		
		try{
			client_socket = new Socket(host, port);
			
			output_stream = new PrintStream(client_socket.getOutputStream());
			input_stream = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
			output_line = new BufferedReader(new InputStreamReader(System.in));
			
			 if (client_socket != null && output_stream != null && input_stream != null){
				 new Thread(new TriviaGameClient()).start();
			 }
			
			while(! is_closed){
				//output_stream.println(output_line.readLine().trim());
				String input = output_line.readLine().trim();
				output_stream.println(input);
			}
			
			//new TriviaGameClient();
				 
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
	
	public void run(){
		String response;
		int count = 0;
		try{
			while((response = input_stream.readLine()) != null){
				System.out.println(response);
				
				if(response.contains("?"))
				{
					messageLabel.setText(response);
					client_question = response;
					count++;
				}
					
				
				if(response.startsWith("A:"))
				{
					button1.setText(response);
               //south_panel.revalidate();
               //south_panel.repaint();
					client_choices[0] = response;
					count++;
				}
					
					
				if(response.startsWith("B:"))
				{
					button2.setText(response);
					client_choices[1] = response;
					count++;
				}
					
					
				if(response.startsWith("C:"))
				{
					button3.setText(response);
					client_choices[2] = response;
					count++;
				}
					
					
				if(response.startsWith("D:"))
				{
					button4.setText(response);
					client_choices[3] = response;
					count++;
				}
				
				if(count == 5)
				{
					//new TriviaGameClient();
               south_panel.revalidate();
               south_panel.repaint();
					count = 0;
				}
            
				if(response.equals("***END***"))
					break;
				
			}
			is_closed = true;
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private class ChoiceAListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			output_stream.println("A");
		}
	}
	
	private class ChoiceBListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			output_stream.println("B");
		}
	}
	
	private class ChoiceCListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			output_stream.println("C");
		}
	}
	
	private class ChoiceDListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			output_stream.println("D");
		}
	}

}
