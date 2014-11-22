import java.awt.*;
import javax.swing.*;

public class ClientGUI extends JFrame
{
	public ClientGUI()
	{
		setTitle("Client GUI");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(800, 600);
		
		setLayout(new BorderLayout());
		
		JPanel center_panel = new JPanel();
		JPanel west_panel = new JPanel();
		JPanel east_panel = new JPanel();
		JPanel north_panel = new JPanel();
		JPanel south_panel = new JPanel();
		
		String choice_A = "A";
		String choice_B = "B";
		String choice_C = "C";
		String choice_D = "D";
		
		JButton button1 = new JButton(choice_A);
		JButton button2 = new JButton(choice_B);
		JButton button3 = new JButton(choice_C);
		JButton button4 = new JButton(choice_D);
		
		south_panel.add(button1);
		south_panel.add(button2);
		south_panel.add(button3);
		south_panel.add(button4);
		
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
		new ClientGUI();
	}
}