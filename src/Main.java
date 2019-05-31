import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends JFrame implements ActionListener{

	JTextField text = new JTextField();
	JTextArea textArea;
	JSpinner spin = new JSpinner();
	
	JButton send;
	JButton logoff;
	JButton[] listB = new JButton[100];
	
	String room;
	String computer;
	String localIp;
	String baseIp;
	
	JButton selected;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() != send) {			
			JButton click = (JButton) e.getSource();
			
			if (selected != null && click.getBackground() != Color.black) {
				if (Ping(baseIp + selected.getText())) {
					selected.setBackground(Color.white);
				} else {
					selected.setBackground(Color.black);
				}				
			}
			
			if (click.getBackground() == Color.white) {
				selected = (JButton) e.getSource();
				selected.setBackground(Color.ORANGE);
			} else {
				JOptionPane.showMessageDialog(this, "Computer is unaccessible");
			}
				
		} else {			
//			System.out.println(Ping(String.format("%s%s", baseIp, selected.getText())));
			sendMessage();
			text.setText("");
		}
 		
	}

	public Main() {
		
		init();
		initInfo();
		int amount = getAmount();
		
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel header = new JPanel(new GridLayout(3,1));
		header.add(new JLabel("Room : " + room));
		header.add(new JLabel("Computer : " + computer));
		header.add(new JSeparator());		
		
		add(header, BorderLayout.NORTH);
		
		JPanel body = new JPanel(new BorderLayout());
		JPanel top = new JPanel(new GridLayout(3,2));
		
		top.add(new JLabel("Amount: "));
		top.add(spin);
		
		top.add(new JLabel("Message: "));
		top.add(text);
		
		top.add(new JSeparator());
		top.add(new JSeparator());
		
		body.add(top, BorderLayout.NORTH);
		
		JPanel roomPanel = new JPanel(new GridLayout((amount/4)+1, 4));
		for (int i = 1; i <= amount; i++) {
			listB[i] = new JButton(String.format("%02d", i));			
			listB[i].addActionListener(this);
			roomPanel.add(listB[i]);
		}
		
		body.add(roomPanel, BorderLayout.CENTER);
		
		add(body, BorderLayout.CENTER);
		
		send = new JButton("Send");
		send.addActionListener(this);
		
		logoff = new JButton("Log Off");
		logoff.addActionListener(this);
		
		JPanel btmPanel = new JPanel(new FlowLayout());
		btmPanel.add(send);
		btmPanel.add(logoff);
		
		add(btmPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		
		
		for(int i = 1;  i <= amount ; i++) {
			if (Ping(String.format("%s%02d", baseIp, i))) {
				listB[i].setBackground(Color.white);
			} else {
				listB[i].setBackground(Color.black);
			}
		}
		
	}

	public static void main(String[] args) {
		Main main = new Main();
//		main.setVisible(true);

	}
	
	public void init() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			localIp = inetAddress.getHostAddress();
			baseIp = localIp.substring(0, localIp.length() - 2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void initInfo() {
		
		room = "";
		computer = "";
		
		if (localIp.charAt(localIp.lastIndexOf(".") - 3) == '1') {
			room += '6';
		} else {
			room += '7';
		}
		
		room += localIp.substring(localIp.lastIndexOf(".") - 2, localIp.lastIndexOf("."));
		computer = localIp.substring(localIp.length() - 2, localIp.length());
	}
	
	public int getAmount() {
		switch(room) {
		case "614":
			return 37;
		case "610":
		case "725":
			return 41;
		}
		return 0;
	}
	
	public boolean Ping(String ip) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ip);
			return inetAddress.isReachable(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	void sendMessage() {
		String cmd = "msg * /server:" + baseIp + selected.getText() + " " + text.getText();
//		System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
