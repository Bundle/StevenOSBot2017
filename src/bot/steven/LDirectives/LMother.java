package bot.steven.LDirectives;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		
		
		jta.append("starting LBot on " + number);
		////////////
		
		/////////////
		
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
		
		long tutBegin;
		public void tic(long delta) {
			
			switch (watcherState) {
			case waitForTut:
				//TODO: when it is done with tutorial, end the .jar process. this is a major flaw.
				
				//KHAL TUTORIAL ISLAND takes 8 minutes 40 seconds about
				final long TIME = 10;
				if (System.currentTimeMillis() - tutBegin > 
						TIME * 60 * 1000) {
					watcherState = WatcherStates.runLBot;
				}
				break;
			case startTut:
				String nameParam = "591";//because SDN
				String optionsParam = "0;1;0;0;1";
				/*khal options:
				 * randomize char
				 * disable music
				 * drop items
				 * walk to G E 
				 * logout after completion
				 */
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
					long ms = System.currentTimeMillis() - Integer.parseInt(scan.nextLine());
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
			for (LBotWatcher L : LBotWatchers) {
				L.tic(CT - LT);
			}
			
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	final String burkcommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
			+ "OSBot " + jarver + ".jar\" "
			+ "-login gangsthurh:s0134201342 -bot "
			+ "stevenfakeaccountemail121@gmail.com:"
			+ "0134201342:1234"
			+ " -script " + "LBurk" + ":" + "1234"
			+ " -allow norandoms";
}
