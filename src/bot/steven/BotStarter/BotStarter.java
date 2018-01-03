package bot.steven.BotStarter;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BotStarter {
	
	public int[] bannedAccounts = {
		1,2,3,4,5,6,7,8,9,10,
		11,12,13,14,15,16,17,18,19,20,
		21,22,23,24,25,26,27,28,29,30,
		31,32,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,
		54,
	};
	public int[] availableAccounts = {
			47,44,51,49,52,53,58,57,55,56,
			59,60,
	};
	private boolean banned(int i)
	{
		for (int x = 0; x < bannedAccounts.length; x++)
		{
			if (bannedAccounts[x] == i)
				return true;
		}
		return false;
	}
	private boolean file(int i)
	{
		try{File f = new File("C:\\Users\\Yoloswag\\OSBot\\Data\\" + i + ".bot");
		if (f.exists())
			return true;
		else
			return false;
		}catch(Exception e){e.printStackTrace();}
		
		return false;
	}
	public BotStarter() {
		
	}
	private TreeMap<Integer, Process> processes = new TreeMap<>();
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
			name = name.trim();
			for (int i = Integer.parseInt(botNumberMin.getText());
					i <= Integer.parseInt(botNumberMax.getText());
					i++) {
			if (banned(i))
			{
				jtf.append("error: " + i + " is BANNED.\r\n");
				continue;
			}
			if (file(i))
			{
				jtf.append("error: " + i + " is Currently Running\r\n");
				continue;
			}
			String number = ""+ i;
			//pass these parameters to KebabCarry so it knows which bots are actively running
			String options = "" + i;
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
				processes.get(i).destroyForcibly();
				jtf.append("killing process #" + i);
				continue;
			}
			if (name.equalsIgnoreCase("desert"))
			{
				name = "GEToDesert";
			}
			
			final String command = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
					+ "OSBot 2.4.149.jar\" "
					+ "-login gangsthurh:s0134201342 -bot "
					+ "stevenfakeaccountemail" + number + "@gmail.com:"
					+ "0134201342:1234"
					+ " -script " + name + ":" + options;
			
			//jtf.append(command + "\r\n");
			jtf.append("starting " + name + " on #" + number + "\r\n");
			final int pls = i;
			try{
			
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(command);
			processes.put(pls,pr);
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
			}catch(Exception ee){ee.printStackTrace();} 
			}}
		
	});
	jtf.append("available: " );
	for (int i = 0; i < availableAccounts.length; i++)
	{
		jtf.append(availableAccounts[i] + " ");
	}
	jtf.append("\r\n");
	f.setLocation(0,100);
	f.add(scriptname);
	f.add(botNumberMin);
	f.add(botNumberMax);
	f.add(start);
	f.add(jtf);
	
	jtf.append("scriptName,bot#min,bot#max GOButton\n");
	jtf.append("[tutorial][clan][kebab][desert][hide]\n");
	
	
	f.setFocusable(true);
	f.setVisible(true);
		
	}
	
	
	
	public static void main(String[]args)
	{
	BotStarter b = new BotStarter();
	b.begin();
		
		
	}
	
}