package bot.steven.BotStarter;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BotStarter {
	
	public BotStarter() {
		
	}
	
	JFrame f;
	public void begin()
	{
	f = new JFrame("OSBot Starter Steven Ventura");
	f.setSize(800,800);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setLayout(new FlowLayout());
	final JTextArea jtf = new JTextArea();
	jtf.setPreferredSize(new Dimension(600,600));
	final JButton start = new JButton();
	start.setPreferredSize(new Dimension(80,80));
	final JTextField scriptname = new JTextField();
	scriptname.setPreferredSize(new Dimension(200,80));
	scriptname.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			
		}
	});
	final JTextField botNumberMin = new JTextField();
	botNumberMin.setPreferredSize(new Dimension(200,80));
	botNumberMin.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			
		}
	});
	final JTextField botNumberMax = new JTextField();
	botNumberMax.setPreferredSize(new Dimension(200,80));
	botNumberMax.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			
		}
	}); 
	start.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String name = scriptname.getText();
			for (int i = Integer.parseInt(botNumberMin.getText());
					i <= Integer.parseInt(botNumberMax.getText());
					i++) {
			String number = ""+ i;
			String options = "1234";
			if (name.equalsIgnoreCase("tutorial"))
			{
				name = "591";//because SDN
				options = "1;1;1;1;1";
			}
			if (name.equalsIgnoreCase("clan"))
			{
				name = "JoinCC";
			}
			if (name.equalsIgnoreCase("kebab"))
			{
				name = "KebabCarry";
			}
			if (name.equalsIgnoreCase("hide"))
			{
				name = "BlueDragonhides";
			}
			if (name.equalsIgnoreCase("kill"))
			{
				String command = "taskkill //IM java.exe //F";
				Runtime rt = Runtime.getRuntime();
				try{
				Process pr = rt.exec(command);
				}catch(Exception ee){ee.printStackTrace(); }
				
				
				return;
			}
			
			String command = "java -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\OSBot 2.4.147.jar\" "
					+ "-login gangsthurh:s0134201342 -bot "
					+ "stevenfakeaccountemail" + number + "@gmail.com:"
					+ "0134201342:1234"
					+ " -script " + name + ":" + options;
			
			//jtf.append(command + "\r\n");
			
			jtf.append("starting " + name + " on #" + number + "\r\n");
			Runtime rt = Runtime.getRuntime();
			try{
			Process pr = rt.exec(command);
			}catch(Exception ee){ee.printStackTrace(); }
			}try{
			Thread.sleep(1000);}catch(Exception e2){}
		}
	});
	
	f.setLocation(0,100);
	f.add(scriptname);
	f.add(botNumberMin);
	f.add(botNumberMax);
	f.add(start);
	f.add(jtf);
	
	jtf.append("scriptName,bot#min,bot#max GOButton\n");
	jtf.append("[tutorial][clan][kebab][hide]\n");
	
	
	f.setFocusable(true);
	f.setVisible(true);
		
	}
	
	
	
	public static void main(String[]args)
	{
	BotStarter b = new BotStarter();
	b.begin();
		
		
	}
	
}