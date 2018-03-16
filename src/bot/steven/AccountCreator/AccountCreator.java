package bot.steven.AccountCreator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AccountCreator {

	private void runShellCommand(final String command) {
try{
			
			final Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
			}catch(Exception e){e.printStackTrace();}
	}
	
	public AccountCreator() {
		
	}
	private int currentNumber = -1;
	private int startingNumber = -1;
	public void begin() {
		
		JFrame f = new JFrame("AccountCreator by Steven Ventura");
		f.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				System.out.println("ending btw");
				runShellCommand("Taskkill /IM AutoHotkey.exe /F");
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		/*
		 * The accountCreator has:
		 * 1) list of usernames
		 * 2) list of numbers we are going through so we can do email stuff
		 * 3) it will get the usernames from a file of 1500 minecraft usernames or some shit
		 */
		
		JButton go = new JButton("Go");
		go.setPreferredSize(new Dimension(80,80));
		final JTextArea jta = new JTextArea();
		jta.setPreferredSize(new Dimension(600,600));
		
		final JTextField number = new JTextField();
		number.setPreferredSize(new Dimension(200,80));
		
		final JLabel controls = new JLabel();
		controls.setPreferredSize(new Dimension(200,80));
		controls.setText("+r=email,+f=pass,+v=charName");
		
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startFlag = true;
				
			}
			
		});
		
		f.add(controls);f.add(number);f.add(go);f.add(jta);
		
		
		f.setSize(800,800);
		f.setVisible(true);
		
		while(true){
			try{
			if (startFlag == true) {
				startFlag = false;
				currentNumber = Integer.parseInt(number.getText());
				startingNumber = currentNumber;
				
				jta.append("starting number is " + startingNumber + "\r\n");
				
				runShellCommand("Taskkill /IM AutoHotKey.exe /F");
				Thread.sleep(1500);
				
				createAHKFile();
				
				runShellCommand("C:\\Program Files\\AutoHotkey\\AutoHotKey.exe AccountCreator.ahk");
				
				jta.append("okie go\r\n");
			}
			else
				Thread.sleep(500);
			}catch(Exception e){e.printStackTrace();}
		}
		
		
	}
	private volatile boolean startFlag = false;
	private void createAHKFile() {
		try{
			
			 File ahkFile;
			
			ahkFile = new File("AccountCreator.ahk");
			String c = "";
			String n = "\r\n";
			c = "+v::addAccount()" + n +
				"+r::addEmail() " + n +
				"+f::typePassword()" + n +
				"+n::typeURL()" + n + 
				"typeURL() { " + n + 
				"Send, {ctrl down}t{ctrl up}" + n + 
				"SendRaw, https://secure.runescape.com/m=account-creation/g=oldscape/create_account" + n +
				"Send, {enter}" + n + 
				"}" + n +
				"typePassword() {" + n +
				"SendInput," + getpassword() + n +
					"}" + n + 
			"addEmail() {" + n + 
			"	static starting := " + startingNumber + " - 1" + n +
			"	starting := starting + 1" + n +
			"	SendInput,stevenfakeaccountemail%starting%@gmail.com" + n +

			"	}" + n +
			"addAccount() {" + n +
			"	static names := [" + generateNameList() + "]" + n +
			"	static nameIndex := 0" + n +
			"	nameIndex := nameIndex + 1" + n +

			"	SendInput, % names[nameIndex]" + n +


			"	}" + n;
			
			
		
			PrintWriter out = new PrintWriter(ahkFile);
			out.println(c);
			out.close();
		
		}catch(Exception e){e.printStackTrace();}
	}
	/*
	 * get 8 names
	 */
	private String generateNameList() {
		String out = "";
		try{
		Scanner scan = new Scanner(new File("C:\\Users\\Yoloswag\\OSBot\\Data\\coolbotnames.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			
			String name = line.substring(0, line.indexOf(":"));
			lines.add(name);
		};
		for (int i = 0; i < 8-1; i++)
		{
			int index = (int)(Math.random()*lines.size());
			String please = lines.get(index);
			/*if (lines.get(index).length() < 12)
			{
				for (int x = 0; x < 12-lines.get(index).length(); x++)
				{
					int random = (int)(Math.random()*52);
					please += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".substring(random,random+1);
				}
			}*/
			out += "\"" + please + "\", ";
			lines.remove(index);
		}
		int index = (int)(Math.random()*lines.size());
		out += "\"" + lines.get(index) + "\"";
		
		return out;
		}catch(Exception e){e.printStackTrace();}
		return null;
		
	}
	
	public static void main(String[]args) {
		AccountCreator c = new AccountCreator();
		c.begin();
		
	}
	
	
}
