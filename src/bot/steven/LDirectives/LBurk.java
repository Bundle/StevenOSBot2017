package bot.steven.LDirectives;

import java.io.File;
import java.util.Scanner;

import org.osbot.rs07.api.Client;
import org.osbot.rs07.event.RandomExecutor;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "LBurk head", logo = "", name = "LBurk", version = 0)
public class LBurk extends Script{

	enum MASTER {scanning, coingive, leathertake};
	MASTER master;
	enum SCANNING {scanning, loggingOut};
	SCANNING scanning;
	enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	enum LEATHERTAKE {login,
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	};
	LEATHERTAKE leathertake;
	
	
	
	public void onStart() {
		master = MASTER.scanning;
		scanning = SCANNING.loggingOut;
		
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
	private static  void rsleep(long time)
	{
		try{Thread.sleep(time);
		}catch(Exception e){e.printStackTrace();}}
	private String interactName = null;
	private void scanStateMachine() {
	
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
			rsleep(3500);//slow down dat scanning boi XDD
			
			
			File folder = new File(getDirectoryData());	
			File[] listOfFiles = folder.listFiles();
			log("filelistsize is " + listOfFiles.length);
			long cthaha = System.currentTimeMillis();
			
			for (File f : listOfFiles) {
				log("reee");
				String name = f.getName();
				log("name is " + name);
				if (name.endsWith(".coinRequest") || name.endsWith(".leatherTake")) {
				long milliseconds = Long.parseLong(name.substring(0,name.indexOf(".")));
				log("milliseconds is " + milliseconds);
				if (cthaha - milliseconds < 10*1000) {
					
					if (name.endsWith(".coinRequest")){
						master = MASTER.coingive;
						coingive = COINGIVE.login;
					}
					if (name.endsWith(".leatherTake")){
						master = MASTER.leathertake;
						leathertake = LEATHERTAKE.login;
					}
					try{
					Scanner scan = new Scanner(f);
					
					interactName = scan.nextLine();
					/*try {
					    this.bot.getRandomExecutor().registerHook(new RandomBehaviourHook(RandomEvent.AUTO_LOGIN) {
					        @Override
					        public boolean shouldActivate() {
					            return super.shouldActivate() && canStartBreak();
					        }
					    });
					} catch (Exception ex) {
					    //Break manager is not enabled
					    log("Failed to modify break handler");
					}*/
					
					
					
					scan.close();
					}catch(Exception e){log(e.getMessage());}
					break;//break out of for
													}
				
				
					}
			
		
		}
			break;
		case loggingOut: 
			
			log(System.currentTimeMillis());
			log("???????");
			//TODO
		if (client.isLoggedIn()) {
			
			logoutTab.open();
			logoutTab.logOut();
		}
			
			
				scanning = SCANNING.scanning;
		
			break;
			
			
		}
		
	}
	
	/*enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	
	};*/
	
	private LoginEvent loginEvent;
	private void coinStateMachine() {
		//TODO
		log("log in pls");
		switch (coingive) {
		case login:
			
			loginEvent = new LoginEvent();
	        getBot().addLoginListener(loginEvent);
	        
	        loginEvent.setUsername("stevenfakeaccountemail121@gmail.com");
	        loginEvent.setPassword("0134201342");
	        execute(loginEvent);
	        
			
			
	        
			break;
		case getcoinsfrombank:
			break;
		case sendtrade:
			break;
		case gothroughwithtrade:
			break;
		case returntoscanning:
			break;
		}
	}
	/*
	 * enum LEATHERTAKE {login,
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	 */
	private void leatherStateMachine() {
		log("log in pls");
		switch (leathertake) {
		case login:
			
			//new RandomExecutor(bot).run();
			
			break;
		case emptybags:
			break;
		case sendtrade:
			break;
		case gothroughwithtrade:
			break;
		case openge:
			break;
		case sellleather:
			break;
		case collectprofit:
			break;
		case returntoscanning:
			break;
		}
	}
	

}
