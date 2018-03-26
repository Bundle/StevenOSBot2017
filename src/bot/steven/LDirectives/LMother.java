package bot.steven.LDirectives;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LMother {
	private String password1,password2,burkemail;
	void getpasswords() {
		try{
		Scanner scan = new Scanner(new File("C:\\Users\\Yoloswag\\osbot\\data\\logininfo.btw"));
		password1 = scan.nextLine();
		password2 = scan.nextLine();
		burkemail = scan.nextLine();
		scan.close();
		}catch(Exception e){e.printStackTrace();}
	}
	private String getpassword() { return password1; }
	private String getpassword2() {return password2; }
	private String getburkemail(){ return burkemail; }
	
	static final String jarver = "2.5.3";
	public LMother() {
		
	}
	JFrame f ; 
	private static  void rsleep(long time)
	{
		try{Thread.sleep(time);
		}catch(Exception e){e.printStackTrace();}}
	private void startBurk(final String typename) {
		
			
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
				String burkcommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
						+ "OSBot " + jarver + ".jar\" "
						+ "-login gangsthurh:" + getpassword2() + " -bot "
						+ getburkemail() + ":" 
						+ getpassword() + ":1234"
						+ " -script " + getBurk(typename) + ":" + "1234"
						+ " -allow norandoms";
			Process pr = rt.exec(burkcommand);
			System.out.println(burkcommand);;
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();

	}			
	

	enum WatcherStates {waitForTut, startTut, runMotheredBot, scanForTutReq, scanForDone};
	class BotWatcher {
		
		WatcherStates watcherState;
		public int number;
		public String scriptType;
		public BotWatcher(int number, String scriptType) {
			this.scriptType = scriptType;this.number = number;watcherState = WatcherStates.runMotheredBot; }
		
		private void runMotheredBot() {
			final String LBotCommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
					+ "OSBot " + jarver + ".jar\" "
					+ "-login gangsthurh:" + getpassword2() + " -bot "
					+ "stevenfakeaccountemail" + number + "@gmail.com:"
					+ getpassword() + ":1234"
					+ " -script " + getStringNameForBotType(scriptType) + ":" + number
					+ " -allow norandoms";
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(LBotCommand);
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
		}
		
		void killTutorialScript() {
			if (pidHandlesOnMultipleTutorials.size()==0) {System.out.println("cant kill, process size is 0"); return;}
			
			for (int n = 0; n < pidHandlesOnMultipleTutorials.size(); n++) {
			final String killscriptstring = "taskkill /PID " + pidHandlesOnMultipleTutorials.get(n) + " /F";
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(killscriptstring);
			System.out.println("successfully ran: " + killscriptstring);
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
			}
			
			
		}
		
		ArrayList<Integer> pidHandlesOnMultipleTutorials;
		long tutBegin;
		public void tic(long delta) {
			
			
			
			switch (watcherState) {
			case waitForTut:
				//TODO: when it is done with tutorial, end the .jar process. this is a major flaw.
				
				//KHAL TUTORIAL ISLAND takes 8 minutes 40 seconds about
				final long TIME = 10;
				if (System.currentTimeMillis() - tutBegin > 
						TIME * 60 * 1000) {
					killTutorialScript();
					watcherState = WatcherStates.runMotheredBot;
				}
				
				break;
			case startTut:
				pidHandlesOnMultipleTutorials = new ArrayList<>();
				String nameParam = "591";//because SDN
				String optionsParam = "0;1;0;0;1";
				/*khal options:
				 * randomize char
				 * disable music
				 * drop items
				 * walk to G E 
				 * logout after completion
				 */
				final getrunnerboy g1 = new getrunnerboy("java.exe");
				g1.populatepidlist();
				System.out.println("g1.pidlistsize is " + g1.pidlist.size());
				final getrunnerboy g2 = new getrunnerboy("java.exe");
				
				
				final String command = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
						+ "OSBot " + jarver + ".jar\" "
						+ "-login gangsthurh:" + getpassword2() + " -bot "
						+ "stevenfakeaccountemail" + number + "@gmail.com:"
						+ getpassword() + ":1234"
						+ " -script " + nameParam + ":" + optionsParam;
				final Runtime rt = Runtime.getRuntime();
				new Thread() {
					public void run() {
				try{
				Process pr = rt.exec(command);
				
				if (g2.waitForExtraProcesses(g1, 15000, 2)){
					ArrayList<Integer> newpidlist = g2.extraProcesses(g1);
					pidHandlesOnMultipleTutorials = newpidlist;
					for (int i = 0; i < pidHandlesOnMultipleTutorials.size(); i++) {
						System.out.println("Detected process: " + pidHandlesOnMultipleTutorials.get(i));
					}
					/*if (newpidlist.size() > 1) {
						System.out.println("@@@: More than 1 new instance found? dumping list@@@");
						for (int i = 0; i < newpidlist.size(); i++) {
							System.out.println(newpidlist.get(i));
						}
						pidHandleOnTutorial = newpidlist.get(newpidlist.size()-1);
					}
					else if (newpidlist.size() == 1) {
						System.out.println("found the handle: " + newpidlist.get(0));
						pidHandleOnTutorial = newpidlist.get(0);
					}*/
					
					
					
					
				}
				
				}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
				
				}}.start();
				
				tutBegin = System.currentTimeMillis();
				
				watcherState = WatcherStates.waitForTut;
				break;
			case runMotheredBot:
				runMotheredBot();
				watcherState = WatcherStates.scanForTutReq;
				break;
			case scanForTutReq:
				rsleep(2000);
				File folder = new File("C:\\Users\\Yoloswag\\OSBot\\data");	
				File[] listOfFiles = folder.listFiles();
				
				
				
				for (File f : listOfFiles) {
					
					String name = f.getName();
					if (name.startsWith(""+number) && name.endsWith(".tutorialRequest")) {
						try{
					Scanner scan = new Scanner(f);
					long ms = System.currentTimeMillis() - Long.parseLong(scan.nextLine());
					scan.close();
					//file must be created within the past 10 seconds
					if (ms < 10 * 1000) {
						
						watcherState = WatcherStates.startTut;
						break;//only do 1 request per object.
						
					}
					else
					{
						//outdated file, delete the file request
						f.delete();
						break;//break cos iterator and concurrency and stuff
						
						
					}
						}catch(Exception e){e.printStackTrace();}
					
					}
				}
				
				break;
			case scanForDone:
				break;
			
			
			
			}
			
			
		}
	}
	
	ArrayList<BotWatcher>  botWatchers = new ArrayList<>();
	
	private final String getStringNameForBotType(String type) {
		switch(type) {
		case "LBot":
			return "LBot";
		
		case "KBot":
			return "KBot";
			
		}
		
		return null;
		
	}
	
	private void addBot(int number, String type) {
		
		try{
			File f = new File("C:\\Users\\Yoloswag\\OSBot\\data\\" + number + ".banned");
			if (f.exists())
			{
				System.out.println("Error: " + number + " is banned.");
				jta.append("Error: " + number + " is banned.\r\n");
				return;
			}
			
			
			
		}catch(Exception e){e.printStackTrace();}
		
		
		System.out.println("starting " + type + " on " + number);
		jta.append("starting " + type + " on " + number + "\r\n");
		////////////
		
		/////////////
		
		//TODO: end the process?
		for (int i =0 ; i < botWatchers.size(); i++)
		{
			if (botWatchers.get(i).number == number)
			{
				botWatchers.remove(i);
				break;
			}
		}
	
		
		botWatchers.add(new BotWatcher(number,type));
		
		
	}
	final JTextArea jta = new JTextArea();
	final String[] botChoices = {"KBot", "LBot"};
	static final String getBurk(String name) {
		switch (name) {
		case ("KBot"):
			return "KBurk";
		case ("LBot"):
			return "LBurk";
		}
		
		return null;
	}
	final JComboBox<String> botChoiceBox = new JComboBox<String>(botChoices);
	public void begin() {
		getpasswords();
		
		
		f = new JFrame("LMother.java");
		f.setSize(800,600);
		f.setLayout(new FlowLayout());
		
		
		botChoiceBox.setPreferredSize(new Dimension(100,100));
		final JTextField boynumber = new JTextField();
		final JTextField boynumber2 = new JTextField();
		boynumber.setPreferredSize(new Dimension(100,100));
		boynumber2.setPreferredSize(new Dimension(100,100));
		
		jta.setPreferredSize(new Dimension(400,400));
		
		final JButton burkbutton = new JButton("burk");
		burkbutton.setPreferredSize(new Dimension(60,40));
		burkbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBurk(botChoices[botChoiceBox.getSelectedIndex()]);
				jta.append("Starting burk ..." + "\r\n");
			}
			
		});
		
		
		final JButton b = new JButton("add boy");
		b.setPreferredSize(new Dimension(200,100));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (boynumber.getText().equals("")){
					return;
				}
				//start new bot
				int number = Integer.parseInt(boynumber.getText());
				
				if (boynumber2.getText().equals(""))
					addBot(number,botChoices[botChoiceBox.getSelectedIndex()]);
				else
				{
					for (int i = Integer.parseInt(boynumber.getText()); i <= 
								Integer.parseInt(boynumber2.getText()); 
							i++) {
						addBot(i,botChoices[botChoiceBox.getSelectedIndex()]);
					}
				}
				
				boynumber.setText("");
				boynumber2.setText("");
				
				
			}
		});
		
		
		f.add(burkbutton);
		f.add(botChoiceBox);
		
		f.add(b);
		f.add(boynumber);
		f.add(boynumber2);
		f.add(jta);
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		long CT=System.currentTimeMillis(),LT=System.currentTimeMillis();
		long LTprices = 0;
		while(true) {
			rsleep(2000);
			CT=System.currentTimeMillis();
			try{
			for (BotWatcher L : botWatchers) {
				L.tic(CT - LT);
			}
			}catch(ConcurrentModificationException cme) { };
			
			if (CT-LTprices > 15*60*1000)
			{
				LTprices = CT;
				writepricesfile();
			}
			
			//tic the lbotwatchers
			
			
			LT = CT;
		}
		
		
		
		
	}
	private static String lookupPriceString(String name) {
		
		return name + "=" + lookupPrice(name) + "\r\n";
		
	}
	private static int lookupPrice(String name) {
	    try {
	        Document doc = Jsoup.connect("http://2007.runescape.wikia.com/wiki/Exchange:" + name.replaceAll(" ", "_")).get();
	        
	        Element price = doc.getElementById("GEPrice");
	        return Integer.parseInt(price.text().replaceAll(",", ""));
	    } catch (Exception e) {}
	    return 0;
	}
	
	private void writepricesfile() {
		System.out.println("writing prices file");
		try{File f = new File("C:\\Users\\Yoloswag\\OSBot\\Data\\market.prices");
		PrintWriter p = new PrintWriter(f);
		p.write(lookupPriceString("Cowhide"));
		p.write(lookupPriceString("Leather"));
		p.write(lookupPriceString("Green_dragonhide"));
		p.write(lookupPriceString("Energy potion(4)"));
		p.write(lookupPriceString("Green dragon leather"));
		p.write(lookupPriceString("Amulet of glory(6)"));
		p.write(lookupPriceString("Ring of wealth (5)"));
		p.close();
		
		}catch(Exception e){e.printStackTrace();}
		
	}
	public static void main(String[]args) {
		LMother m = new LMother();
		m.begin();
		
		
	}
	
	
	
	
	
	
	
	
	
	
	static class getrunnerboy {
		//example processnamecriteria="java.exe"
		String processnamecriteria;
		public getrunnerboy(String processname) {
			this.processnamecriteria = processname;
		}
		ArrayList<Integer> pidlist;
		public ArrayList<Integer> getList() {
			return pidlist;
		}
		
		
		
		public void populatepidlist() {
			try{
				pidlist = new ArrayList<>();
			 String line;
			    Process p = Runtime.getRuntime().exec
			    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
			    BufferedReader input =
			            new BufferedReader(new InputStreamReader(p.getInputStream()));
			    int linecount = 0;
			    while ((line = input.readLine()) != null) {
			    	linecount++;
			    	if (linecount >= 4) {String[] split = line.split(" ");
			    	
			    	
			    	
			    	int pid = 0; String pname = "";
			    	boolean secondlinebtw = false;
			        for (int i = 0; i < split.length; i++){
			        	if (secondlinebtw && pname.equals(processnamecriteria) && split[i].equals("") == false){
				        	secondlinebtw=false;
				        	System.out.println(line);
				        	pid = Integer.parseInt(split[i]);
				        	
				        	}
			        	if (i == 0) {
			        		secondlinebtw=true;
			        		pname = split[i];
			        	}
			        	
			        	
			        	
			        }
			        
			        if (pname.equals(processnamecriteria)) {
			        	pidlist.add(pid);
			        }
			        
			      
			    }}
			    input.close();
			} catch (Exception err) {
			    err.printStackTrace();
			}
			
		}
		
		public boolean waitForExtraProcesses(getrunnerboy other, long waitinterval, int numtimes) {
			
			//attempt every waitinterval seconds for process check thing
			
			try{
				for (int i = 0; i < numtimes; i++) {
				Thread.sleep(waitinterval);
				populatepidlist();
				System.out.println("pidlist.size is " + pidlist.size());
				if (this.extraProcesses(other).size() != 0) {
					return true;
				}
				
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
			
		}
		
		//should be used as p2.extraProcesses(p1)
		public ArrayList<Integer> extraProcesses(getrunnerboy other) {
			ArrayList<Integer> out = new ArrayList<>();
			//returns: this object has X more objects than "other" object
			for (int i = 0; i < pidlist.size(); i++) {
				if (!other.pidlist.contains(pidlist.get(i)))
				{
					out.add(new Integer(pidlist.get(i)));
				}
				
				/*boolean found = false;
				for (int c = 0; c < other.pidlist.size(); c++) {
					System.out.println(pidlist.get(i) + " ?? " + other.pidlist.get(c));
					if (pidlist.get(i) == other.pidlist.get(c))
					{
						System.out.println("found it");
						found = true;
					}
				}
				if (!found)
					out.add(pidlist.get(i));*/
			}
			return out;
			
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
