package bot.steven.LDirectives;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
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
	
	public TreeMap<String,Integer> marketPrices;
	private void fetchMarketPrices() {
		marketPrices = new TreeMap<>();
		try{
			Scanner scan = new Scanner(new File(getDirectoryData() + "\\market.prices"));
			while(scan.hasNextLine()) {
				//format literal:
				//name=price
				String line = scan.nextLine();
				String name = line.substring(0, line.indexOf("="));
				int price = Integer.parseInt(line.substring(line.indexOf("=")+1));
				marketPrices.put(name,price);
			}
			scan.close();
		}catch(Exception e){log("unable to find market.prices file");};
	}
	public int getMarketPrice(String itemName) {
		return marketPrices.get(itemName);
		
		
	}
	private boolean WaitForWidget (int arg1, int arg2)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2) == null || !widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 40)
				return false;
			rsleep(100);
		}
		return true;
	}
	private boolean WaitForWidget (int arg1, int arg2, int arg3)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2,arg3) == null || !widgets.get(arg1,arg2,arg3).isVisible()){
			loops++;
			if (loops > 40)
				return false;
			rsleep(100);
		}
		return true;
	}
	
	private void click(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(LEFTCLICK);
	}
	private void rightclick(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(RIGHTCLICK);
	}final boolean LEFTCLICK = false, RIGHTCLICK = true;
	public void onStart() {
		fetchMarketPrices();
		
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
	
	
	/*
	enum MASTER {scanning, coingive, leathertake};
	MASTER master;
	enum SCANNING {scanning, loggingOut};
	SCANNING scanning;
	enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	enum LEATHERTAKE {login,
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	};*/
	
	public void onPaint(Graphics2D g)
	{
		
		
		g.setPaint(Color.CYAN);
		if (master == MASTER.scanning) 
			g.drawString("LBurk: interrupt: " + scanning,10,60);
		if (master == MASTER.coingive) 
			g.drawString("LBurk: interrupt: " + coingive,10,60);
		if (master == MASTER.leathertake) 
			g.drawString("LBurk: interrupt: " + leathertake,10,60);
		
			g.drawString("LBurk:master=" + master,10,40);
		/*g.drawString("extra pots:" + getExtraPotAmount(),10,80);
		g.drawString("Left=" + hideAmountLeft + ",Done=" + hideAmountDone, 10,100);
		g.drawString("TimeLeft=" + reee((int)(hideAmountLeft/26*36)),10,120);
		*/
		
		g.setPaint(Color.BLACK);
		
		
	}
	/*enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	
	};*/
	
	private LoginEvent loginEvent;
	private void coinStateMachine() {
		//TODO
		
		switch (coingive) {
		case login:
			
			loginEvent = new LoginEvent();
	        getBot().addLoginListener(loginEvent);
	        
	        loginEvent.setUsername("stevenfakeaccountemail121@gmail.com");
	        loginEvent.setPassword("0134201342");
	        execute(loginEvent);
	        
			coingive = COINGIVE.getcoinsfrombank;
			
	        
			break;
		case getcoinsfrombank:
			walking.walk(new Position(3167,3485,0));
			try{
			while (!bank.isOpen()) {
				bank.open();
			}}catch(Exception e){};
			bank.depositAll();
			rsleep(1000);
			final int cashamount =666666 / 6; 
			//money
			bank.withdraw("Coins",cashamount);
			rsleep(1000);
			if (bank.isOpen()) {
				bank.close();
			}
			if (inventory.getItems()[0] != null &&
					inventory.getItems()[0].nameContains("Coins")
					&& inventory.getItems()[0].getAmount() == cashamount) {
				coingive = COINGIVE.sendtrade;
				break;
			}
			break;
		case sendtrade:
			
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals(interactName)) {
					p.interact("Trade with");
					log("Sending trade to " + p.getName());
					coingive = COINGIVE.gothroughwithtrade;
					rsleep(1500);
					break;
				}
			}
			
			break;
		case gothroughwithtrade:
			
			/*//TODO: put this new version of widget handling in jugboys
			Entity tradeboy = players.closest(currentTradeBoy);
			if (tradeboy != null)
				{
					if ( (widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false)
							&& 
						 (widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)) {
				tradeboy.interact("Trade with");
				rsleep(1000);
					}
				
				//trade screen 1 widget is 335,25
					//trade screen 2 widget is 334,13
				if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
						|| WaitForWidget(335,25)) {
					//wait a second for him to put the goods up
					rsleep(2000);
					//press accept
					click(264,180);
					//wait for him to accept
					if (WaitForWidget(334,13)) {
					//press accept again
					click(215,303);
					//successful trade!
					currentTradeBoy = null;
					}
				}
			}
			
			*/
			
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
		if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
				|| WaitForWidget(335,25)) {
			//put the coins in
			inventory.getItems()[0].interact("Offer-All");
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			//press accept again
			click(215,303);
			//successful trade!
			coingive = COINGIVE.returntoscanning;
			
			}
		}
			
			
			break;
		case returntoscanning:
			scanning = SCANNING.loggingOut;
			master = MASTER.scanning;
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
			
			loginEvent = new LoginEvent();
	        getBot().addLoginListener(loginEvent);
	        
	        loginEvent.setUsername("stevenfakeaccountemail121@gmail.com");
	        loginEvent.setPassword("0134201342");
	        execute(loginEvent);
	        
	        leathertake = LEATHERTAKE.emptybags;
			
			break;
		case emptybags:
			
			try{if (!bank.isOpen()) {
				bank.open();
			}}catch(Exception e){};
			bank.depositAll();
			rsleep(1000);
			if (inventory.isEmpty()) {
				leathertake = LEATHERTAKE.sendtrade;
			}
			break;
		case sendtrade:
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals(interactName)) {
					p.interact("Trade with");
					log("Sending trade to " + p.getName());
					if (WaitForWidget(335,25)) {
					leathertake = LEATHERTAKE.gothroughwithtrade;
					
					}
					rsleep(1500);
					break;
				}
			}
			
			break;
		case gothroughwithtrade:
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
		if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
				|| WaitForWidget(335,25)) {
			//wait a second for him to put the goods up
			rsleep(2000);
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			//press accept again
			click(215,303);
			//successful trade!
			leathertake = LEATHERTAKE.openge;
			}
		}
			break;
		case openge:
			Entity clerk2 = npcs.closest("Grand Exchange Clerk");
			if (clerk2 != null)
				clerk2.interact("Exchange");
			else
				break;
			rsleep(2000);
			//grand exchange opening screen
			if (WaitForWidget(465,2,1)) {
			//click collect
			
			click(456,64);
			
			if (WaitForWidget(465,7,3)) {//create buy offer widget 
				//click on item to "offer" it
				inventory.getItem("Leather").interact("Offer");
				if (WaitForWidget(465,24,21)) {
					
					
					click(386,205);
					if (WaitForWidget(162,36)) {
						keyboard.typeString("" + (int)(getMarketPrice("Leather")*0.89)); 
						rsleep(1500);
						click(260,287);//click confirm
						//wait for collect button to appear
						if (WaitForWidget(465,6,1))
						{
							click(456,64);
							leathertake = LEATHERTAKE.returntoscanning;
							//fall back and let base case handle the state change
						}
						//click on it
						
					}
					
					}
				
			}
			}
			
			break;
		case sellleather:
			break;
		case collectprofit:
			break;
		case returntoscanning:
			scanning = SCANNING.loggingOut;
			master = MASTER.scanning;
			break;
		}
	}
	

}
