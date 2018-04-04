package bot.steven.LDirectives;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.constants.ResponseCode;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
@ScriptManifest(author = "Steven Ventura", info = "KBot", logo = "", name = "KBot", version = 0)
public class KBot extends Script{
	private String password1,password2;
	void getpasswords() {
		try{
		Scanner scan = new Scanner(new File(getDirectoryData() + "\\" + "logininfo.btw"));
		password1 = scan.nextLine();
		password2 = scan.nextLine();
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String getpassword() { return password1; }
	final static double K = 1000;
	enum STATEMACHINE {
		locationcheck,
		traveltodesertfromspawn1,traveltodesertfromspawn2,
		requestburkgivecoins,waitfortradecoins,
		completetradecoins,
		kebab1,kebab2,kebab3,
		notekebabsfrombank,
		requesttutorial,
		waitingforlogin,
		loginstate1,loginstate2,loginstate3,loginstate4,
	
	};
	STATEMACHINE state;
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	private Position meetupLocation() {
		int x,y,z;
		x=3270;
		y=3163;
		z = 0;
		
		if (Math.random()<0.50)
			x=x+1;
		else
			x=x-1;
		if (Math.random()<0.50)
			y=y+1;
		else
			y=y-1;
		
		
		return new Position(x,y,z);
	}
	public static String errorString(Exception errorboyyyy) {
		StringWriter errors = new StringWriter();
		errorboyyyy.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	public void onPaint(Graphics2D g) {
		
		
		
		g.setPaint(Color.CYAN);
		
		g.drawString("KBot # " + getParameters(),10,60);
			g.drawString("KBot:state=" + state,10,40);
		
		g.setPaint(Color.BLACK);
	}
	
	private void rsleep(long millis)
	{
		try{
			Thread.sleep(millis);
		}catch(Exception e){};
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
	
	
	public void onStart() {
		getpasswords();
		fetchMarketPrices();
		state = STATEMACHINE.loginstate1;
		
		
	}
	
	String myguysname = null;
	@Override
	 public final void onResponseCode(final int responseCode) throws InterruptedException { 
		if (myguysname == null) {
			//try to get his name from our files
			try{
			Scanner scan = new Scanner(new File(getDirectoryData() + "\\numbertoname\\"
					+ getParameters() + ".toName"));
			myguysname = scan.nextLine();
			scan.close();
			}catch(Exception e){e.printStackTrace();}
		}
		if(ResponseCode.isDisabledError(responseCode)) {
	            log("Login failed, account is disabled");
	          //create banned file
				try{
				File f2 = new File(getDirectoryData() + "\\" + getParameters() + ".banned");
				log("creating file " + getDirectoryData() + "\\" + getParameters() + ".banned");
			PrintWriter out2 = new PrintWriter(f2);
			out2.println(System.currentTimeMillis());
			out2.println("rip " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					new Date(System.currentTimeMillis())));
			
			
			
			if (myguysname != null)
				out2.println("here lies " + myguysname);
			else
			out2.close();
				
				
				}catch(Exception e){};
				//close client
				System.exit(0);
	            return;
	        }

	        if(ResponseCode.isConnectionError(responseCode)) {
	            log("Connection error, attempts exceeded");
	            
	            return;
	        }
	    }
	final boolean LEFTCLICK=false,RIGHTCLICK=true;
	private void click(int x, int y)
	{
		mouse.move(x+(int)(3*Math.random()),y+(int)(3*Math.random()));
		mouse.click(LEFTCLICK);
	}
	private void openKebabDoor() {
		for (RS2Object o : objects.getAll()) {
			try{
			WallObject doorplease = (WallObject)o;
			//door coordinate for closed door: 3275,3180
			if (doorplease.getX() == 3275 && doorplease.getY() == 3180) {
				doorplease.interact("Open");
				break;
			}
			}catch(Exception e){}
		}
	}
	private int kebabsBought = 0;
	 private RS2Widget getLobbyButton() {
	    	try{
	        return widgets.get(378,76);//changed by hand on 3/15/18 after an update
	    	//return getWidgets().getWidgetContainingText("CLICK HERE TO PLAY");
	    	}catch(NullPointerException n){
	    		return null;
	    	}
	    
	    }
	 private void printSocketMessage(String message) {
		 System.out.println(message);
	 }
	 public boolean loginDoubleCheck() {
			if (getLobbyButton() != null
					|| !getClient().isLoggedIn())//very important thing
			{
				//this means he failed to log in , so log in again.
				state = STATEMACHINE.loginstate1;
				return true;
			}
			return false;
		}
	 private boolean isOnWorldSelectorScreen() {
	        return getColorPicker().isColorAt(50, 50, Color.BLACK);
	    }
	 long timeLastPulse = 0;
	 
	 private void printPulse() {
		 if (System.currentTimeMillis()  - timeLastPulse < 60 * 1_000 * 2)
			 return;
		 else
			 timeLastPulse = System.currentTimeMillis();
		 
		 try{
		 File f = new File(getDirectoryData() + "\\pulse\\" + getParameters() + ".pulse");
		 PrintWriter out = new PrintWriter(f);
			out.println(System.currentTimeMillis());
			out.println("KBot");
			out.close();
			
		 }catch(Exception e){e.printStackTrace();}
	 }
	 Entity Karim = null;
	long lastfiletime = 0;
	int numtimeslmao = 0;
	@Override
	public int onLoop() throws InterruptedException {
		printPulse();
		
		switch(state) {
		case loginstate1:
			 if (getClient().isLoggedIn() && getLobbyButton() == null
			  && widgets.get(548,69) != null) {
				 numtimeslmao = 0;
		            state = STATEMACHINE.locationcheck;
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
		            	keyboard.typeString("stevenfakeaccountemail" + getParameters() + "@gmail.com");
		               rsleep(800);
		            	keyboard.typeString(getpassword());
		            	
		            		}
		            	}
		            	
		            //    enterUserDetails();
		                break;
		        }
		        }
        
    break;
			
			
		case locationcheck:
			if (myguysname == null) {
				myguysname = myPlayer().getName();
				try{
					//check if numbertoname file exists
					File f = new File(getDirectoryData() + "\\numbertoname\\"
							+ getParameters() + ".toName");
					
					if (!f.exists()) {
						PrintWriter p = new PrintWriter(f);
						p.println(myPlayer().getName());
						p.close();
					}
					//if not then make it 
					
				}catch(Exception e){
				log(errorString(e));};
			}
			log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			int x,y;
			x = myPlayer().getX(); y = myPlayer().getY();
			if (y > 9000 || (x < 3220 && y < 3208)) {
				state = STATEMACHINE.requesttutorial;
			}
			else
				state = STATEMACHINE.traveltodesertfromspawn1;
			/*else if (x > 3260 && y < 3330)
			{
				state = STATEMACHINE.gotodesert1;
			}
			else {
				state = STATEMACHINE.traveltogefromspawn1;
			}*/
			break;
		
		
		
		case requesttutorial:
			
				printSocketMessage("GottaDoTutorial");
				
			break;
		case traveltodesertfromspawn1:
		{
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < LUMBRIDGETODESERT.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-LUMBRIDGETODESERT[i][0],2)+Math.pow(
						myPlayer().getY()-LUMBRIDGETODESERT[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			iLUMBRIDGETODESERT = closest;
			state = STATEMACHINE.traveltodesertfromspawn2;
		}
			break;
		case traveltodesertfromspawn2:
		{
			walking.walk(new Position(LUMBRIDGETODESERT[iLUMBRIDGETODESERT][0],
					LUMBRIDGETODESERT[iLUMBRIDGETODESERT][1],
					0));
			waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-LUMBRIDGETODESERT[iLUMBRIDGETODESERT][0],2) + 
				Math.pow(myPlayer().getY()-LUMBRIDGETODESERT[iLUMBRIDGETODESERT][1],2));
		
if (pls < 4)
	iLUMBRIDGETODESERT++;
else
	log("uhh he aint movin xD");


if (iLUMBRIDGETODESERT == LUMBRIDGETODESERT.length) {
	state = STATEMACHINE.kebab1;
	
}
		}
			break;
			
		
		case requestburkgivecoins:
			tradeFlag = false;
			//rendevooooooooooooooooo
			walking.walk(meetupLocation());
			lastfiletime = System.currentTimeMillis();
			try{
				File f = new File(getDirectoryData() + "\\" + System.currentTimeMillis() + ".kebabTake");
				log("creating file " + getDirectoryData() + "\\" + System.currentTimeMillis() + ".kebabTake");
			PrintWriter out = new PrintWriter(f);
			out.println(myPlayer().getName());
			out.close();
			}catch(Exception e){log("file error :3c");}
			
			state = STATEMACHINE.waitfortradecoins;
			
			break;
		case waitfortradecoins:
			/*
			 * TODO: sometimes the bot gets logged out in this state.
			 */
			
			if (loginDoubleCheck())
				break;
			
			if (tradeFlag) {
				//wait for trade variable to be flipped
				state = STATEMACHINE.completetradecoins;
			}
			else if (System.currentTimeMillis() - lastfiletime > 15*K) {
				//its been 15 seconds, try again
				state = STATEMACHINE.requestburkgivecoins;
			}
			
			break;
		case kebab1:
			openKebabDoor();
			walking.walk(new Position(3269,3167,0));
			//check bank for kebabs.
			try{
			while(!bank.isOpen()) {
				bank.open();
				rsleep(500);
			}
			}catch(Exception e){};
			if (!bank.isOpen()) {
				state = STATEMACHINE.kebab1;
				break;
			}
			//if there are no kebabs or there is no gold, then do notekebabsfrombank.
			if (inventory.getAmount("Coins") + bank.getAmount("Coins") < 30) {
				state = STATEMACHINE.notekebabsfrombank;
				break;
			}
			
			
			if (inventory.getItems()[1] != null)
			{
				if (inventory.getItems()[1].getName().equals("Kebab"))
				{
					inventory.getItems()[1].interact("Deposit-all");
				}
				else
				{
					bank.depositAll();
				}
			}
			
			if (inventory.getItems()[0] == null || !inventory.getItems()[0].getName().equals("Coins"))
			{
				bank.depositAll();
			}
			
			
			if (bank.getAmount("Coins") > 0) {
				bank.withdrawAll("Coins");
			}
			
			//make sure theres just coins in his bag
			boolean justcoins = true;
			for (int i = 1; i <= 27; i++) {
				if (inventory.getItems()[i] != null)
					justcoins = false;
			}
			
			if (!justcoins) {
				bank.depositAll();
				state = STATEMACHINE.kebab1;
				break;
			}
			
			
			
			if (inventory.getItems()[0] != null && inventory.getItems()[0].getName().equals("Coins"))
				state = STATEMACHINE.kebab2;
			
			
			
			
			
			
			break;
		case kebab2:
			while(bank.isOpen()) {
				bank.close();
				rsleep(500);
			}
			
			
			walking.walk(new Position(3276,3172,0));
			rsleep(500);
			waitForMovements();
			
			
			state = STATEMACHINE.kebab3;
			
			
			
			
			
			break;
		case kebab3:
			
Karim = npcs.closest("Karim");
			
			
			if (Karim == null)
			{
				walking.walk(new Position(3269,3167,0));
				break;
			}
			else
				Karim.interact("Talk-To");
			rsleep(50);//for reliability
			
			
		
			
			
			if (WaitForWidget(231,2)) {
			click(300,454);
			if (WaitForWidget(219,0,2)) {
			click(257,432);
			rsleep(10);
			if (WaitForWidget(217,2)) {
			click(202,449);
			kebabsBought++;
			}
			}
			}
			rsleep(250);
			
			
			
			
			//check if thing is full
			if (inventory.getAmount("Coins") == 0 || 
					(inventory.getItems()[26] != null && inventory.getItems()[26].nameContains("Kebab")))
			{
				state = STATEMACHINE.kebab1;
			}
			else
			{
				state = STATEMACHINE.kebab3;	
			}
			
			break;
		
		
		case completetradecoins:
			//get the coins from him
			
			//make sure i am not already trading.
			if ((widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)
					&&
					(widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false))
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals("LordBurk")) {
					p.interact("Trade with");
					rsleep(1500);
					break;
				}
			}
			
			
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
			if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
					|| WaitForWidget(335,25)) {
				//TODO: manage the new items here
				if (inventory.getItems()[0] != null)
					inventory.getItems()[0].interact("Offer-all");
				//wait for him to put the coins in
				rsleep(2000);
				//press accept
				click(264,180);
				//wait for him to accept
				if (WaitForWidget(334,13)) {
					while (widgets.get(334,13) != null) {
						//press accept again
						click(215,303);
						rsleep(800);
					}
					
				
				
				//successful trade!
				state = STATEMACHINE.kebab1;
				
				}
			}
			
			
			
			break;
		case notekebabsfrombank:
			boolean hasnokebabs = false;
			while (!bank.isOpen()) {
				bank.open();
				rsleep(500);
			}
			
			if (!bank.isOpen())
			{
				state = STATEMACHINE.notekebabsfrombank;
				break;
			}
			
			
			bank.depositAll();
			
			if (bank.getAmount("Kebab") + inventory.getAmount("Kebab") == 0) {
				hasnokebabs = true;
			}
			
			
			if (bank.isBankModeEnabled(Bank.BankMode.WITHDRAW_ITEM)) {
				bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
				rsleep(500);
				}
			
			bank.withdrawAll("Kebab");
			rsleep(1500);
			
			if (hasnokebabs && inventory.isEmpty()
					|| (inventory.getItems()[0] != null && inventory.getItems()[0].getName().equals("Kebab")))
			{
				if (bank.isOpen())
					bank.close();
				
				state = STATEMACHINE.requestburkgivecoins;
				
			}
			
			
			
			break;
		
		
		}
		
		
		
		
		return 150 + (int)(Math.random()*50);
	}
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
	private long getLeatherAmountToBuy() {
		long coins = inventory.getAmount("Coins");
		long spending = coins - (int)(13*K);
		
		return spending / (int)((getMarketPrice("Cowhide")*1.11));
		
		
		
		
	}
	
	volatile boolean tradeFlag = false;
	public void onMessage(Message message)
	{
	String text = message.getMessage();
	if (text.contains("Lord") && text.contains("wishes to trade with you.")) {
		log("tradeFlag has been raised");
		tradeFlag = true;
	}
	
	}
	
	int[][] LUMBRIDGETODESERT = {{3232, 3231},{3233, 3239},{3232, 3249},{3231, 3259},{3232, 3261},{3241, 3263},{3240, 3269},{3240, 3276},{3240, 3282},{3239, 3290},{3239, 3298},{3242, 3308},{3249, 3318},{3255, 3322},{3257, 3323},{3263, 3323},{3271, 3330},{3277, 3331},{3279, 3320},{3273, 3313},{3273, 3301},{3273, 3285},{3273, 3276},{3272, 3268},{3272, 3260},{3272, 3246},{3272, 3238},{3272, 3230},{3272, 3219},{3272, 3209},{3272, 3200},{3269, 3188},{3275, 3175},{3272, 3167},{3270, 3167},
	};//TODO
	int iLUMBRIDGETODESERT = -1;
	
	/*
	 * mesh for LUMBRIDGESPAWN TO G E:
	 */
	int[][] LUMBRIDGETOGE = {{3233, 3237},{3233, 3242},{3232, 3247},{3232, 3249},{3231, 3257},{3232, 3261},{3241, 3263},{3240, 3269},{3240, 3275},{3240, 3281},{3238, 3287},{3238, 3293},{3238, 3301},{3234, 3305},{3228, 3310},{3225, 3316},{3222, 3319},{3217, 3324},{3215, 3330},{3211, 3334},{3206, 3342},{3206, 3348},{3204, 3351},{3202, 3357},{3199, 3363},{3196, 3366},{3193, 3371},{3187, 3377},{3183, 3381},{3181, 3385},{3181, 3393},{3176, 3398},{3172, 3400},{3172, 3408},{3172, 3416},{3172, 3417},{3172, 3425},{3172, 3431},{3173, 3435},{3173, 3441},{3175, 3449},{3174, 3454},{3168, 3460},{3166, 3466},{3165, 3470},{3165, 3476},{3165, 3480},{3165, 3484},
	};
	int iLUMBRIDGETOGE = -1;
	
	/*
	 * mesh for DESERT TO G E 
	 */
	
	int[][] DESERTTOGE = {{3273, 3168},{3275, 3175},{3279, 3182},{3280, 3186},{3280, 3189},{3280, 3193},{3279, 3203},{3279, 3207},{3279, 3215},{3279, 3221},{3275, 3225},{3275, 3232},{3275, 3237},{3274, 3243},{3274, 3248},{3274, 3254},{3274, 3260},{3273, 3267},{3273, 3272},{3273, 3277},{3272, 3279},{3272, 3285},{3272, 3291},{3272, 3297},{3272, 3301},{3272, 3307},{3272, 3312},{3273, 3314},{3273, 3318},{3276, 3326},{3277, 3330},{3270, 3330},{3267, 3331},{3258, 3331},{3250, 3335},{3241, 3336},{3233, 3336},{3231, 3338},{3228, 3340},{3227, 3346},{3224, 3352},{3222, 3354},{3217, 3357},{3212, 3363},{3209, 3367},{3211, 3371},{3208, 3375},{3204, 3376},{3199, 3376},{3195, 3377},{3189, 3377},{3182, 3380},{3180, 3387},{3176, 3395},{3172, 3400},{3172, 3403},{3172, 3408},{3172, 3415},{3171, 3421},{3171, 3429},{3173, 3436},{3175, 3442},{3175, 3448},{3175, 3453},{3169, 3459},{3166, 3464},{3165, 3469},{3165, 3473},{3165, 3479},{3165, 3484},{3165, 3487},
	};
	int iDESERTTOGE = -1;
	/*
	 * mesh for G E TO DESERT
	 */
	int[][] GETODESERT = {{3164, 3483},{3164, 3475},{3164, 3469},{3164, 3464},{3168, 3460},{3172, 3454},{3175, 3447},{3175, 3439},{3175, 3434},{3175, 3432},{3172, 3424},{3172, 3418},{3172, 3411},{3172, 3405},{3173, 3397},{3173, 3391},{3176, 3385},{3183, 3379},{3189, 3372},{3191, 3370},{3191, 3368},{3193, 3364},{3198, 3361},{3204, 3356},{3207, 3353},{3210, 3354},{3218, 3354},{3224, 3352},{3224, 3343},{3227, 3339},{3232, 3337},{3237, 3337},{3241, 3337},{3245, 3337},{3249, 3336},{3252, 3334},{3259, 3334},{3262, 3332},{3265, 3332},{3271, 3331},{3276, 3331},{3279, 3329},{3275, 3322},{3272, 3319},{3272, 3314},{3272, 3307},{3272, 3302},{3271, 3297},{3271, 3292},{3271, 3286},{3270, 3282},{3270, 3276},{3270, 3270},{3270, 3264},{3270, 3258},{3270, 3250},{3270, 3246},{3270, 3241},{3270, 3230},{3270, 3222},{3271, 3217},{3271, 3211},{3271, 3205},{3272, 3199},{3278, 3195},{3280, 3185},{3277, 3182},{3277, 3176},{3276, 3170},{3273, 3167},
	};
	int iGOTODESERT = -1;
	


}
