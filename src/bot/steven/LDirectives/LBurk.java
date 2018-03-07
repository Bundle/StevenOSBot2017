package bot.steven.LDirectives;

import java.io.File;
import java.util.Scanner;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "LBurk head", logo = "", name = "LBurk", version = 0)
public class LBurk extends Script{

	enum MASTER {scanning, coingive, leathertake};
	MASTER master;
	enum SCANNING {scanning, loggingOut};
	SCANNING scanning;
	enum COINGIVE {getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	enum LEATHERTAKE {
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	};
	LEATHERTAKE leathertake;
	
	
	
	public void onStart() {
		master = MASTER.scanning;
		
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		switch (master) {
		case scanning:
			scanStateMachine();
			break;
		case coingive:
			coinStateMachine();
			break;
		case leathertake:
			leatherStateMachine();
			break;
			
		}
		return 150 + (int)(50*Math.random());
	}
	private String interactName = null;
	private void scanStateMachine() {
		try{
		/*
		 * 3/7/2018
		 * file looks like:
		 * fname = milliseconds.coinRequest | milliseconds.giveRequest
		 * {
		 * playername (ingame name)
		 * }
		 * file only has 1 line
		 * 
		 */
		switch (scanning) {
		case scanning:
			Thread.sleep(3500);//slow down dat scanning boi XDD
			File folder = new File(getDirectoryData());	
			File[] listOfFiles = folder.listFiles();
			
			long cthaha = System.currentTimeMillis();
			
			for (File f : listOfFiles) {
				
				String name = f.getName();
				if (name.endsWith(".coinRequest") || name.endsWith(".leatherTake")) {
				long milliseconds = Long.parseLong(name.substring(0,name.indexOf(".")));
				if (cthaha - milliseconds < 10*1000) {
					
					if (name.endsWith(".coinRequest"))
						master = MASTER.coingive;
					if (name.endsWith(".leatherTake"))
						master = MASTER.leathertake;
					
					Scanner scan = new Scanner(f);
					
					interactName = scan.nextLine();
					scan.close();
					
					break;//break out of for
													}
				
				
					}
			
		break;
		}
		case loggingOut: 
			//TODO
			
			
			
			break;
			
			
		}
		}catch(Exception e){e.printStackTrace();}
	}
	private void coinStateMachine() {
		//TODO
		switch (coingive) {
		
		}
	}
	private void leatherStateMachine() {
		switch (leathertake) {
		
		}
	}
	

}
