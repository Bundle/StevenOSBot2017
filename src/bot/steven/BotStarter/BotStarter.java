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
		51,52,53,54,55,56,57,58,59,60,61,62,
		63,64,65,66,67,68,69,70,71,
		72,73,74,75,
		76,77,78,79,
		80,81,82,83,
		84,85,86,87,88,89,90,
		91,
		92,
		93,94,//93 and 94 are dueling each other
		95,//95 is nick's
		96,97,98,//these 4 will be tutorial island buddies
		99,100,101,102,//99-102 are also door closers LOL
		//going to bot tutorial island on 103-110.
		//customized by hand.
		103,104,105,106,
		107,
		108,//108 is ryan mcgregor (Member),dueling nick, advertising addy dagger(p)
		109,110,111,112,//buying wizard's mind bomb,
		113,//113 is Ruck Crab, used for training my main
		114,115,//these two are hand customized and botted tutorial island, hand walked to GE
		116,//116oscx: hand customized, botted tutorial island
		//giving both of them 500k and seeing how far they get
		116,117,118,119,120,122,123,
		124,125,
		126,127,
		128,129,130,131,132,133,
		134,135,
		136,137,138,139,140,141,
		142,
		145,143,144,145,146,//february 5 2018
		149,150,151,152,
		
		153,
		154,155,156,
		157,158,159,
		160
	};
	public int[] aboutToCreate = {
			
	};
	public int[] justCreatedNeedsTutorialIsland = {
			
			
			
	};
	public int[] doneTutorialIslandSittingAtLumby = {
			
			161,162,163,164
			
			
			
			
			
	};
	public int[] kebabBots = {
			118,119,120,
			
	};
	public int[] memberAccounts = {
			
			121,//121LordBurk: this one is tutorial island by hand so i can safely get 1m from burggy
			147,
			148,
	};
	public int[] availableAccounts = {
			
			
			
			
			
			//working on these . . .
			
			
			/*
			124,
			125,126,127,128,
			//working on these . . . 
			129,130,131,132,
			133,134,135,136,
			*/
			//done in vvv
			
			
			
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
	private void rsleep(long t)
	{
		try{
			Thread.sleep(t);
		}catch(Exception e){}
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
			int max = -1;
			if (!botNumberMax.getText().equals(""))
				max = Integer.parseInt(botNumberMax.getText());
			
			/*if (max - Integer.parseInt(botNumberMin.getText()) > 3) {
				jtf.append("dats more than 3 m8");
				return;
			}*/
			
			for (int i = Integer.parseInt(botNumberMin.getText());
					i <= max;
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
			if (name.equalsIgnoreCase("jug")) {
				name = "JugBoys";
			}
			if (name.equalsIgnoreCase("boss")) {
				name = "BossBoys";
			}
			if (name.equalsIgnoreCase("log"))
			{
				name = "JustLogin";
			}
			if (name.equalsIgnoreCase("bomb"))
			{
				name = "BeerBuyer";
			}
			if (name.equalsIgnoreCase("tutorial"))
			{
				
				//if (new File("C:\\Users\\Yoloswag\\osbot\\data\\" + i + ".tutDone").exists()) {
					name = "591";//because SDN
					options = "1;1;1;1;1";
						
				//}
				//else {
				//	name = "TutorialIsland";
				//}
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
				//name = "GreenDragonhides";
				name = "HideTanner";
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
					+ "OSBot 2.4.162.jar\" "
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
			//processes.put(pls,pr);
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
			}catch(Exception ee){ee.printStackTrace();} 
			rsleep(5000);
			}}
		
	});
	jtf.append("available: " );
	for (int i = 0; i < availableAccounts.length; i++)
	{
		jtf.append(availableAccounts[i] + ",");
	}
	
	jtf.append("\r\n");
	jtf.append("tut: ");
	for (int i = 0; i < justCreatedNeedsTutorialIsland.length; i++)
		jtf.append(justCreatedNeedsTutorialIsland[i] + ",");
	jtf.append("\r\n");
	f.setLocation(0,100);
	f.add(scriptname);
	f.add(botNumberMin);
	f.add(botNumberMax);
	f.add(start);
	f.add(jtf);
	
	jtf.append("scriptName,bot#min,bot#max GOButton\n");
	jtf.append("[jug][boss][tutorial][clan][kebab][desert][hide][bomb][log]\n");
	
	
	f.setFocusable(true);
	f.setVisible(true);
		
	}
	
	
	
	public static void main(String[]args)
	{
	BotStarter b = new BotStarter();
	b.begin();
		
		
	}
	
}