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
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LMother {

	static final String jarver = "2.5.2";
	public LMother() {
		
	}
	JFrame f ; 
	private static  void rsleep(long time)
	{
		try{Thread.sleep(time);
		}catch(Exception e){e.printStackTrace();}}
	private void startBurk() {
		
			
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(burkcommand);
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();

	}			
	private void addLBot(int number) {
		
		
		jta.append("starting LBot on " + number + "\r\n");
		////////////
		
		/////////////
		
		//TODO: end the process?
		for (int i =0 ; i < LBotWatchers.size(); i++)
		{
			if (LBotWatchers.get(i).number == number)
			{
				LBotWatchers.remove(number);
				break;
			}
		}
	
		
		LBotWatchers.add(new LBotWatcher(number));
		
		
		
		
		
		
	}
	static class LBotWatcher {
		enum WatcherStates {waitForTut, startTut, runLBot, scanForTutReq, scanForDone};
		WatcherStates watcherState;
		public int number;
		public LBotWatcher(int number) {this.number = number;watcherState = WatcherStates.runLBot; }
		
		private void runLBot() {
			final String LBotCommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
					+ "OSBot " + jarver + ".jar\" "
					+ "-login gangsthurh:s0134201342 -bot "
					+ "stevenfakeaccountemail" + number + "@gmail.com:"
					+ "0134201342:1234"
					+ " -script " + "LBot" + ":" + number;
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(LBotCommand);
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
		}
		
		void killTutorialScript() {
			if (pidHandleOnTutorial == -1) {System.out.println("cant kill process its -1"); return;}
			
			final String killscriptstring = "taskkill /PID " + pidHandleOnTutorial + " /F";
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(killscriptstring);
			System.out.println("successfully ran: " + killscriptstring);
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
			
			
		}
		
		int pidHandleOnTutorial = -1;
		
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
					watcherState = WatcherStates.runLBot;
				}
				
				break;
			case startTut:
				pidHandleOnTutorial = -1;
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
				final getrunnerboy g2 = new getrunnerboy("java.exe");
				
				
				final String command = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
						+ "OSBot 2.5.2.jar\" "
						+ "-login gangsthurh:s0134201342 -bot "
						+ "stevenfakeaccountemail" + number + "@gmail.com:"
						+ "0134201342:1234"
						+ " -script " + nameParam + ":" + optionsParam;
				final Runtime rt = Runtime.getRuntime();
				new Thread() {
					public void run() {
				try{
				Process pr = rt.exec(command);
				
				if (g2.waitForExtraProcesses(g1, 15000, 2)){
					ArrayList<Integer> newpidlist = g2.extraProcesses(g1);
					if (newpidlist.size() > 1) {
						System.out.println("@@@: More than 1 new instance found? dumping list@@@");
						for (int i = 0; i < newpidlist.size(); i++) {
							System.out.println(newpidlist.get(i));
						}
						pidHandleOnTutorial = newpidlist.get(newpidlist.size()-1);
					}
					else if (newpidlist.size() == 1) {
						System.out.println("found the handle: " + newpidlist.get(0));
						pidHandleOnTutorial = newpidlist.get(0);
					}
					
					
					
				}
				
				}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
				
				}}.start();
				
				tutBegin = System.currentTimeMillis();
				
				watcherState = WatcherStates.waitForTut;
				break;
			case runLBot:
				runLBot();
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
	
	ArrayList<LBotWatcher>  LBotWatchers = new ArrayList<>();
	
	
	final JTextArea jta = new JTextArea();
	public void begin() {
		
		startBurk();
		
		f = new JFrame("LMother.java");
		f.setSize(800,600);
		f.setLayout(new FlowLayout());
		final JTextField boynumber = new JTextField();
		boynumber.setPreferredSize(new Dimension(200,100));
		
		jta.setPreferredSize(new Dimension(400,400));
		jta.append("Starting Burk btw..." + "\r\n");
		
		final JButton b = new JButton("add boy");
		b.setPreferredSize(new Dimension(200,100));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (boynumber.getText().equals("")){
					return;
				}
				//start new bot
				int number = Integer.parseInt(boynumber.getText());
				
				
				addLBot(number);
				
				
				boynumber.setText("");
				
				
			}
		});
		
		
		
		f.add(b);
		f.add(boynumber);
		f.add(jta);
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		long CT=System.currentTimeMillis(),LT=System.currentTimeMillis();
		long LTprices = 0;
		while(true) {
			rsleep(2000);
			CT=System.currentTimeMillis();
			try{
			for (LBotWatcher L : LBotWatchers) {
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
				boolean notfound = true;
				for (int c = 0; c < other.pidlist.size(); c++) {
					if (pidlist.get(i) == other.pidlist.get(c))
						notfound = false;
				}
				if (notfound)
					out.add(pidlist.get(i));
			}
			return out;
			
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	final String burkcommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
			+ "OSBot " + jarver + ".jar\" "
			+ "-login gangsthurh:s0134201342 -bot "
			+ "stevenfakeaccountemail121@gmail.com:"
			+ "0134201342:1234"
			+ " -script " + "LBurk" + ":" + "1234"
			+ " -allow norandoms";
}
