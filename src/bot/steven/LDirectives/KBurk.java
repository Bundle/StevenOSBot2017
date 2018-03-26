package bot.steven.LDirectives;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "KBurk head", logo = "", name = "KBurk", version = 0)
public class KBurk extends Script{
	private String password1,password2;
	void getpasswords() {
		try{
		Scanner scan = new Scanner(new File(getDirectoryData() + "\\" + "logininfo.btw"));
		password1 = scan.nextLine();
		password2 = scan.nextLine();
		}catch(Exception e){e.printStackTrace();}
	}
	private String getpassword() { return password1; }
	enum MASTER {scanning, kebabtake};
	MASTER master;
	enum SCANNING {scanning, loggingOut};
	SCANNING scanning;
	
	enum KEBABTAKE {login,
		checkbags,sendtrade,gothroughwithtrade,returntoscanning
	};
	KEBABTAKE kebabtake;
	
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
	
	private Position meetupLocation() {
		int x,y,z;
		x=3270;
		y=3163;
		z = 0;
		
		
		
		return new Position(x,y,z);
	}
	public int getMarketPrice(String itemName) {
		return marketPrices.get(itemName);
		
		
	}
	public void onMessage(Message message)
	{
	String text = message.getMessage();
	if (text.equals("Other player declined trade.")) {
		
		
		
	}
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
		getpasswords();
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
		case kebabtake:
			kebabStateMachine();
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
				if (name.endsWith(".kebabTake")) {
				long milliseconds = Long.parseLong(name.substring(0,name.indexOf(".")));
				log("milliseconds is " + milliseconds);
				if (cthaha - milliseconds < 10*1000) {
					
					if (name.endsWith(".kebabTake")){
						master = MASTER.kebabtake;
						kebabtake = KEBABTAKE.login;
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
		numtimeslmao = 0;
			
				scanning = SCANNING.scanning;
		
			break;
			
			
		}
		
	}
	
	
	/*
	enum MASTER {scanning, coingive, kebabtake};
	MASTER master;
	enum SCANNING {scanning, loggingOut};
	SCANNING scanning;
	enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	enum KEBABTAKE {login,
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	};*/
	public boolean loginDoubleCheck() {
		if (getLobbyButton() != null)//very important thing
		{
			//this means he failed to log in , so log in again.
			kebabtake = kebabtake.login;
			return true;
		}
		return false;
	}
	public void onPaint(Graphics2D g)
	{
		
		
		g.setPaint(Color.CYAN);
		if (master == MASTER.scanning) 
			g.drawString("LBurk: interrupt: " + scanning,10,60);
		if (master == MASTER.kebabtake) 
			g.drawString("LBurk: interrupt: " + kebabtake,10,60);
		
			g.drawString("LBurk:master=" + master,10,40);
		
		
		g.setPaint(Color.BLACK);
		
		
	}
	private static final int getCoinGiveAmount() {
		//5.296296296296297 seconds per kebab
		//0.1888111888111888 kebabs per second
		//2 hours is 3600*2/5.296296296296297 = 1360
		return 1360*3;
		
	}
	/*enum COINGIVE {login,getcoinsfrombank,sendtrade,gothroughwithtrade,returntoscanning};
	COINGIVE coingive;
	
	};*/
	 private RS2Widget getLobbyButton() {
	    	try{
	        return widgets.get(378,76);//changed by hand on 3/15/18 after an update
	    	//return getWidgets().getWidgetContainingText("CLICK HERE TO PLAY");
	    	}catch(NullPointerException n){
	    		return null;
	    	}
	    
	    }
	 private boolean isOnWorldSelectorScreen() {
	        return getColorPicker().isColorAt(50, 50, Color.BLACK);
	    }
	private LoginEvent loginEvent;
	/*
	 * enum KEBABTAKE {login,
		emptybags,sendtrade,gothroughwithtrade,openge,sellleather,collectprofit,returntoscanning
	 */
	int numtimeslmao = 0;
	private void kebabStateMachine() {
		
		switch (kebabtake) {
		
			
		case login:
			
			 if (getClient().isLoggedIn() && getLobbyButton() == null
			  && widgets.get(548,69) != null) {
				
				 kebabtake = KEBABTAKE.checkbags;
		            break;
		        } else if (getLobbyButton() != null && getLobbyButton().isVisible()) {
		        
		        	getLobbyButton().interact();
		        	break;
		        } else if (isOnWorldSelectorScreen()) {
		       
		        	getMouse().click(new RectangleDestination(getBot(), 712, 8, 42, 8));
		        	break;
		        } else {
		        
		        	switch (getClient().getLoginUIState()) {
		            case 0:
		            	
		            	getMouse().click(new RectangleDestination(getBot(), 400, 280, 120, 20));
		                break;
		            case 1:
		            	getMouse().click(new RectangleDestination(getBot(), 240, 310, 120, 20));
		            //    clickLoginButton();
		                break;
		            case 2:
		            	if (client.isLoggedIn()) {
		            		
		            	}
		            	else
		            	{
		            		numtimeslmao++;
		            		log(numtimeslmao);
		            		if (numtimeslmao == 1) {
		            	getKeyboard().typeString("stevenfakeaccountemail121@gmail.com");
		               
		            	getKeyboard().typeString(getpassword());
		            	}
		            	}
		            	
		            //    enterUserDetails();
		                break;
		        }
		        }
	        
			
			
	        
			break;
	        
	       
			
			
		case checkbags:
			if (loginDoubleCheck())
				break;
			
			if (inventory.getItems()[0] != null && inventory.getItems()[0].getName().equals("Coins")
					&& inventory.getAmount("Coins") == getCoinGiveAmount()
					) {
				kebabtake = KEBABTAKE.sendtrade;
			}
			else {
			try{while (!bank.isOpen()) {
				bank.open();
				rsleep(1500);
			}}catch(Exception e){};
			bank.depositAll();
			rsleep(500);
			if (bank.contains("Coins")) {
				bank.withdraw("Coins",getCoinGiveAmount());
			}
			rsleep(1000);
			}
			break;
		case sendtrade:
			if ((inventory.getItems()[0] == null || inventory.getItems()[0].getName().equals("Kebab"))
			&& widgets.get(334,13) == null
			&& widgets.get(335,25) == null) {
			kebabtake = KEBABTAKE.returntoscanning;
			break;
		}
			if ((widgets.get(335,25) != null && widgets.get(335,25).isVisible())
					||
					(widgets.get(334,13) != null && widgets.get(334,13).isVisible()))
			{
				kebabtake = KEBABTAKE.gothroughwithtrade;
				break;
			}
			else
				walking.walk(meetupLocation());
			
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals(interactName)) {
					p.interact("Trade with");
					log("Sending trade to " + p.getName());
					if (WaitForWidget(335,25)) {
					kebabtake = KEBABTAKE.gothroughwithtrade;

					}
					rsleep(1500);
					break;
				}
			}
			
			break;
		case gothroughwithtrade:
			if (inventory.getItems()[0] == null
				&& widgets.get(334,13) == null
				&& widgets.get(335,25) == null) {
				kebabtake = KEBABTAKE.returntoscanning;
			}
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
			if (
					!(
					(widgets.get(335,25) != null && widgets.get(335,25).isVisible())
					||
					(widgets.get(334,13) != null && widgets.get(334,13).isVisible())
					)
					)
			{
				kebabtake = KEBABTAKE.sendtrade;
				break;
			}
		if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
				|| WaitForWidget(335,25)) {
			//TODO: wait for him to put the kebabs in the trade? but what if he has none?
			if (inventory.getItems()[0] != null)
				inventory.getItems()[0].interact("Offer-all");
			//sleep to wait for him to put items in if he has any
			rsleep(2000);
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			//press accept again
			click(215,303);
			//successful trade!
			if (widgets.get(334,13) == null || widgets.get(334,13).isHidden())
				kebabtake = KEBABTAKE.returntoscanning;
			}
		}
			break;
		
		case returntoscanning:
			scanning = SCANNING.loggingOut;
			master = MASTER.scanning;
			break;
		}
	}
	

}
