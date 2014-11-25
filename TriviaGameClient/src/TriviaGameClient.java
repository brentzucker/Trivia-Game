import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;


public class TriviaGameClient extends JFrame implements Runnable
{
	public static int port = 9000;
	public static String host = "localhost";
	public static String screenname = "DefaultClient";
	
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
	
	public static JTextArea chatSubmit_TextArea;
	public static JTextArea chat_TextArea;
	
	public static JTextArea question_messageText;
	public static JLabel timer_messageLabel;
	public static JLabel notification_messageLabel;
	
	public static int score = 0;
	public static JLabel score_label = new JLabel(""+score);
	
	public static JLabel ip_label;
	public static JLabel name_label; 
	public static JLabel port_label;
	
	int timer_count = 10;  //the current frame number
	
	int pause = 0; // length of time between each tick counting down (1 second)
	int speed = 1000; //speed of every update
	
	Timer timer; //The timer counting down on each question
	
	public Random rn; // generates a random number to guess an answer if the Timer runs out

	public static int players = 0;//checks to see if player input has been changed
	
	public TriviaGameClient()
	{
		setTitle("Client GUI");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		setLayout(new BorderLayout());
		
		JPanel west_panel = new JPanel();
		JPanel east_panel = new JPanel();
		JPanel north_panel = new JPanel();
		JPanel center_panel = new JPanel();
		JPanel south_panel = new JPanel();
		
		String choice_A = client_choices[0];
		String choice_B = client_choices[1];
		String choice_C = client_choices[2];
		String choice_D = client_choices[3];
		
		question_messageText = new JTextArea(client_question);
		question_messageText.setEditable(false);
		question_messageText.setAlignmentX(CENTER_ALIGNMENT);
		question_messageText.setFont(new Font("Serif", Font.BOLD, 28));
		question_messageText.setLineWrap(true);
		question_messageText.setWrapStyleWord(true);
		question_messageText.setForeground(Color.BLACK);
		question_messageText.setBackground(getBackground());
		
		JLabel big_empty_label = new JLabel("    ");
		JLabel big_empty_label2 = new JLabel("    ");
		big_empty_label.setFont(new Font("Serif", Font.BOLD, 120));
		big_empty_label2.setFont(new Font("Serif", Font.BOLD, 120));
		timer_messageLabel = new JLabel("10");
		timer_messageLabel.setFont(new Font("Serif", Font.BOLD, 120));
		timer_messageLabel.setHorizontalAlignment(JLabel.CENTER);
		timer_messageLabel.setVerticalAlignment(JLabel.TOP);
		
		//timer title label
		JLabel timer_title_label = new JLabel("Time Left");
		timer_title_label.setFont(new Font("Serif", Font.ITALIC, 24));
		timer_title_label.setVerticalAlignment(JLabel.BOTTOM);
		timer_title_label.setHorizontalAlignment(JLabel.CENTER);
		
		//score title label
		JLabel score_title_label = new JLabel("Score");
		score_title_label.setFont(new Font("Serif", Font.ITALIC, 24));
		score_title_label.setVerticalAlignment(JLabel.BOTTOM);
		score_title_label.setHorizontalAlignment(JLabel.CENTER);
		score_label.setVerticalAlignment(JLabel.TOP);
		score_label.setFont(new Font("Serif", Font.BOLD, 120));
		score_label.setHorizontalAlignment(JLabel.CENTER);
		score_label.setForeground(Color.getHSBColor(52, 100, 67));
		
		notification_messageLabel = new JLabel(" ");
		notification_messageLabel.setVerticalAlignment(JLabel.BOTTOM);
		notification_messageLabel.setHorizontalAlignment(JLabel.CENTER);
		notification_messageLabel.setFont(new Font("Serif", Font.BOLD, 44));
		
		ip_label = new JLabel(host);
		name_label = new JLabel(screenname);
		port_label = new JLabel(""+port);
		
		name_label.setHorizontalAlignment(JLabel.CENTER);
		port_label.setHorizontalAlignment(JLabel.RIGHT);
		
		try //tries to find image for button
		{
			Image img = ImageIO.read(getClass().getResource("button.jpg"));
			Image scaled_img = img.getScaledInstance( 400, 50, Image.SCALE_SMOOTH);
			ImageIcon button_icon = new ImageIcon(scaled_img);
			
			button1 = new JButton(choice_A, button_icon);
			button2 = new JButton(choice_B, button_icon);
			button3 = new JButton(choice_C, button_icon);
			button4 = new JButton(choice_D, button_icon);
			
			button1.setHorizontalTextPosition(JButton.CENTER);
			button1.setVerticalTextPosition(JButton.CENTER);
			button2.setHorizontalTextPosition(JButton.CENTER);
			button2.setVerticalTextPosition(JButton.CENTER);
			button3.setHorizontalTextPosition(JButton.CENTER);
			button3.setVerticalTextPosition(JButton.CENTER);
			button4.setHorizontalTextPosition(JButton.CENTER);
			button4.setVerticalTextPosition(JButton.CENTER);
			
			button1.setFont(new Font("Serif", Font.PLAIN, 24));
			button2.setFont(new Font("Serif", Font.PLAIN, 24));
			button3.setFont(new Font("Serif", Font.PLAIN, 24));
			button4.setFont(new Font("Serif", Font.PLAIN, 24));
			
			button1.setForeground(Color.lightGray);
			button2.setForeground(Color.lightGray);
			button3.setForeground(Color.lightGray);
			button4.setForeground(Color.lightGray);
				
		}
		catch(IOException ex)
		{
			
		}
		
		button1.addActionListener(new ChoiceAListener());
		button2.addActionListener(new ChoiceBListener());
		button3.addActionListener(new ChoiceCListener());
		button4.addActionListener(new ChoiceDListener());
		
		chatSubmit_TextArea = new JTextArea("hello world");
		
		JButton chat_button = new JButton("Submit");
		chat_button.addActionListener(new ChatButtonListener());
		
		chat_TextArea = new JTextArea("");
		chat_TextArea.setEditable(false);
		
		JScrollPane chat_ScrollPane = new JScrollPane(chat_TextArea);
		chat_ScrollPane.getVerticalScrollBar();
		chat_ScrollPane.setSize(600, 300);

		south_panel.setLayout(new BorderLayout());
		west_panel.setLayout(new GridLayout(3,1));
		north_panel.setLayout(new GridLayout(1,3));
		center_panel.setLayout(new BorderLayout());
		east_panel.setLayout(new GridLayout(3,1));
		
		north_panel.add(ip_label);
		north_panel.add(name_label);
		north_panel.add(port_label);
		
		west_panel.add(timer_title_label);
		west_panel.add(timer_messageLabel);
		west_panel.add(big_empty_label);
		
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new GridLayout(2,2));
		
		button_panel.add(button1);
		button_panel.add(button2);
		button_panel.add(button3);
		button_panel.add(button4);
		
		south_panel.add(button_panel, BorderLayout.SOUTH);
		
		east_panel.add(score_title_label);
		east_panel.add(score_label);
		east_panel.add(big_empty_label2);
		
		JPanel inner_south_panel = new JPanel();
		inner_south_panel.setLayout(new GridLayout(2, 1));
		inner_south_panel.add(chatSubmit_TextArea);
		inner_south_panel.add(chat_button);
		
		south_panel.add(notification_messageLabel, BorderLayout.NORTH);
		
		center_panel.add(question_messageText, BorderLayout.NORTH);
		center_panel.add(chat_ScrollPane, BorderLayout.CENTER);
		center_panel.add(inner_south_panel, BorderLayout.SOUTH);
		
		add(south_panel, BorderLayout.SOUTH);
		add(north_panel, BorderLayout.NORTH);
		add(center_panel, BorderLayout.CENTER);
		add(east_panel, BorderLayout.EAST);
		add(west_panel, BorderLayout.WEST);
		
		pack();
		
		setSize(800, 600);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		//get port and host from client
		
		JTextField client_name = new JTextField();
		JTextField client_host = new JTextField();
		JTextField client_port = new JTextField();
		JTextField players_to_play = new JTextField();
		
		Object[] fields = {"Screenname:", client_name, "Host IP:", client_host, "Port:", client_port, "Players:", players_to_play };
		
		JOptionPane.showConfirmDialog(null, fields, "Trivia Game Config - leave blank for default", JOptionPane.OK_OPTION);
		
		if(client_name.getText().length() > 0)
		{
			screenname = client_name.getText();
		}
			
		
		if(client_host.getText().length() > 5)
		{
			host = client_host.getText();
		}
			
		
		if(client_port.getText().length() > 3)
		{
			port = Integer.parseInt(client_port.getText());
		}
		
		
		if(players_to_play.getText().length() >= 1 && Integer.parseInt(players_to_play.getText()) > 0)
		{
			players = Integer.parseInt(players_to_play.getText());
		}
		
		try
		{
			client_socket = new Socket(host, port);
			
			output_stream = new PrintStream(client_socket.getOutputStream());
			input_stream = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
			output_line = new BufferedReader(new InputStreamReader(System.in));
			
			if(client_socket != null && output_stream != null && input_stream != null)
			{
				 new Thread(new TriviaGameClient()).start();
			}
			 

			
			//ready to play game
			output_stream.println(":readytoplay:");
			 
			while(! is_closed)
			{
				String input = output_line.readLine().trim();
				output_stream.println(input);
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
	
	public void run()
	{
		setResizable(false);
		String response;
		int count = 0;
		
		//ip_label.setText(host);
		//name_label.setText(screenname);
		//port_label.setText(""+port);
		
		if(players > 0)
		{
			output_stream.println("players:"+players);
			System.out.println("players:"+players);
			output_stream.println(":readytoplay:");
		}
			
		
		try{
			while((response = input_stream.readLine()) != null)
			{
				
				
				
				//The system.out.println can now be commented out
				System.out.println(response);
				
				if(response.contains("?"))
				{
					question_messageText.setText(response);
					client_question = response;
					count++;
				}
					
				if(response.startsWith("A:"))
				{
					button1.setText(response.substring(3));
					client_choices[0] = response;
					count++;
				}
					
				if(response.startsWith("B:"))
				{
					button2.setText(response.substring(3));
					client_choices[1] = response;
					count++;
				}
					
				if(response.startsWith("C:"))
				{
					button3.setText(response.substring(3));
					client_choices[2] = response;
					count++;
				}	
					
				if(response.startsWith("D:"))
				{
					button4.setText(response.substring(3));
					client_choices[3] = response;
					count++;
				}
				
				if(response.startsWith("Correct!"))
				{
					//call action Listener for correct
					notification_messageLabel.setText("EXCELLENT!");
					notification_messageLabel.setForeground(Color.GREEN);
					score += 10;
					score_label.setText(""+score);
				}
				
				if(response.startsWith("Wrong"))
				{
					//call action Listener for Wrong
					notification_messageLabel.setText("Nice Try");
					notification_messageLabel.setForeground(Color.RED);
				}
				
				if(response.equals("Waiting for game to fill up..."))
				{
					notification_messageLabel.setText("Waiting for opponenets...");
				}
				
				if(response.equals("***END***"))
				{
					JOptionPane.showMessageDialog(null, screenname+", you scored "+score);
					break;
				}
				
				//handle chat messages -- display in chat_TextArea
				if(response.startsWith("chat:"))
				{
					chat_TextArea.append(response.substring(5));
					chat_TextArea.append("\n");
				}
				
				if(count-5 == 0)
				{
					//do this every second 10..0
					//timer_messageLabel.setText(""+timer_count--);
					
					//call when message text is being update
					//Set up timer to drive animation events.
			        timer = new Timer(speed, new timerListener());
			        timer.setInitialDelay(pause);
					timer.start(); 

					count = 0;
				}
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
			timer.stop();
			timer_count = 10;
			output_stream.println("A");
		}
	}
	
	private class ChoiceBListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			timer.stop();
			timer_count = 10;
			output_stream.println("B");
		}
	}
	
	private class ChoiceCListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			timer.stop();
			timer_count = 10;
			output_stream.println("C");
		}
	}
	
	private class ChoiceDListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			timer.stop();
			timer_count = 10;
			output_stream.println("D");
		}
	}
	
	//When you press chat button it sends to server
	private class ChatButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			output_stream.println("chat:"+screenname+": "+chatSubmit_TextArea.getText());
			chatSubmit_TextArea.setText("hello world");
		}
	}
	
	//Handle timer event. Update the timer_count (time left on question)
	//if time runs out generates a random answer and "guesses"
	private class timerListener implements ActionListener
	{
		String abcd = "ABCD";
		public void actionPerformed(ActionEvent e) 
	    {
			timer_messageLabel.setText(""+timer_count);

	        if(timer_count <= 3)
	        {
	        	timer_messageLabel.setForeground(Color.RED);
	        }
	        else
	        	timer_messageLabel.setForeground(Color.BLACK);
	        
	        if (timer_count == 0) 
	        {
	            timer_messageLabel.setForeground(Color.RED);
	            timer.stop();
	            timer_count = 10;
	            rn = new Random();
	            output_stream.println(abcd.charAt(rn.nextInt(4))); //outputs an A, b, c, or d
	        }
	        else
	        	timer_count--;
	    }
	}
    
	
}
