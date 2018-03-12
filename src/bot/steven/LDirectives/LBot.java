package bot.steven.LDirectives;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
@ScriptManifest(author = "Steven Ventura", info = "LBurk head", logo = "", name = "LBurk", version = 0)
public class LBot extends Script{
	
	final static double K = 1000;
	enum STATEMACHINE {
		locationcheck,
		traveltogefromspawn1,traveltogefromspawn2,
		requestburkgivecoins,waitfortradecoins,
		returntoge1,gotodesert1,returntoge2,gotodesert2,tanhides1,tanhides2,tanhides3,completetradecoins,
		requestburktakehides,waitfortradehides,completetradehides,
		auditbank,buycowhides,logoutretire,
		notefinishedhidesfrombank,
		requesttutorial
	};
	STATEMACHINE state;
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
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
		fetchMarketPrices();
		state = STATEMACHINE.locationcheck;
	}
	final boolean LEFTCLICK=false,RIGHTCLICK=true;
	private void click(int x, int y)
	{
		mouse.move(x+(int)(3*Math.random()),y+(int)(3*Math.random()));
		mouse.click(LEFTCLICK);
	}
	private void openTannerDoor() {
		for (RS2Object o : objects.getAll()) {
			try{
			WallObject doorplease = (WallObject)o;
			//door.x is 3277 door.y is 3191
			if (doorplease.getX() == 3277 && doorplease.getY() == 3191) {
				doorplease.interact("Open");
				break;
			}
			}catch(Exception e){}
		}
	}
	long lastfiletime = 0;
	@Override
	public int onLoop() throws InterruptedException {
		switch(state) {
		
		case locationcheck:
			int x,y;
			x = myPlayer().getX(); y = myPlayer().getY();
			if (x < 3220 && y < 3208) {
				state = STATEMACHINE.requesttutorial;
			}
			else if (x > 3260 && y < 3330)
			{
				state = STATEMACHINE.gotodesert1;
			}
			else {
				state = STATEMACHINE.traveltogefromspawn1;
			}
			break;
		
		
		
		case requesttutorial:
			try{
				File f = new File(getDirectoryData() + "\\" + getParameters() + ".tutorialRequest");
				log("creating file " + getDirectoryData() + "\\" + getParameters() + ".tutorialRequest");
			PrintWriter out = new PrintWriter(f);
			out.println(System.currentTimeMillis());
			out.close();
			System.exit(0);
			}catch(Exception e){log("file error :3c");System.exit(-1);}
			System.exit(-1);
			
			break;
		case gotodesert1:
		{
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < GETODESERT.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-GETODESERT[i][0],2)+Math.pow(
						myPlayer().getY()-GETODESERT[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			iGOTODESERT = closest;
			state = STATEMACHINE.gotodesert2;
		}
			break;
		case gotodesert2:
		{
			walking.walk(new Position(GETODESERT[iGOTODESERT][0],
					GETODESERT[iGOTODESERT][1],
					0));
			waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-GETODESERT[iGOTODESERT][0],2) + 
				Math.pow(myPlayer().getY()-GETODESERT[iGOTODESERT][1],2));
		
if (pls < 4)
	iGOTODESERT++;
else
	log("uhh he aint movin xD");


if (iGOTODESERT == GETODESERT.length) {
	state = STATEMACHINE.tanhides1;
	
}
		}
			break;
			
		case traveltogefromspawn1:
		{
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < LUMBRIDGETOGE.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-LUMBRIDGETOGE[i][0],2)+Math.pow(
						myPlayer().getY()-LUMBRIDGETOGE[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			iLUMBRIDGETOGE = closest;
			state = STATEMACHINE.traveltogefromspawn2;
		}
			break;
		case traveltogefromspawn2:
		{
			walking.walk(new Position(LUMBRIDGETOGE[iLUMBRIDGETOGE][0],
					LUMBRIDGETOGE[iLUMBRIDGETOGE][1],
					0));
			waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-LUMBRIDGETOGE[iLUMBRIDGETOGE][0],2) + 
				Math.pow(myPlayer().getY()-LUMBRIDGETOGE[iLUMBRIDGETOGE][1],2));
		
if (pls < 4)
	iLUMBRIDGETOGE++;
else
	log("uhh he aint movin xD");


if (iLUMBRIDGETOGE == LUMBRIDGETOGE.length) {
	state = STATEMACHINE.auditbank;

	
}
		}
			break;
		case requestburkgivecoins:
			
			lastfiletime = System.currentTimeMillis();
			try{
				File f = new File(getDirectoryData() + "\\" + System.currentTimeMillis() + ".coinRequest");
				log("creating file " + getDirectoryData() + "\\" + System.currentTimeMillis() + ".coinRequest");
			PrintWriter out = new PrintWriter(f);
			out.println(myPlayer().getName());
			out.close();
			}catch(Exception e){log("file error :3c");}
			
			state = STATEMACHINE.waitfortradecoins;
			
			break;
		case waitfortradecoins:
			if (tradeFlag) {
				//wait for trade variable to be flipped
				state = STATEMACHINE.completetradecoins;
			}
			else if (System.currentTimeMillis() - lastfiletime > 15*K) {
				//its been 15 seconds, try again
				state = STATEMACHINE.requestburkgivecoins;
			}
			
			break;
		case returntoge1:
		{
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < DESERTTOGE.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-DESERTTOGE[i][0],2)+Math.pow(
						myPlayer().getY()-DESERTTOGE[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			iDESERTTOGE = closest;
			state = STATEMACHINE.returntoge2;
		}
			break;
		case returntoge2:
		{
			walking.walk(new Position(DESERTTOGE[iDESERTTOGE][0],
					DESERTTOGE[iDESERTTOGE][1],
					0));
			waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-DESERTTOGE[iDESERTTOGE][0],2) + 
				Math.pow(myPlayer().getY()-DESERTTOGE[iDESERTTOGE][1],2));
		
if (pls < 4)
	iDESERTTOGE++;
else
	log("uhh he aint movin xD");


if (iDESERTTOGE == DESERTTOGE.length) {
	state = STATEMACHINE.auditbank;

	
}
		}
			break;
		case tanhides1:
		{
			openTannerDoor();
			walking.walk(new Position(3278,3179,0));
			rsleep(500);
			walking.walk(new Position(3278,3179,0));
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(150);
			}
			
			
			
			while(!bank.isOpen()) {
				bank.open();
				rsleep(500);
				}
			
			if (bank.getAmount("Cowhide") == 0) {
				state = STATEMACHINE.returntoge1;
				break;
			}
			
			rsleep(100);
			
			if (inventory.getItems()[0] == null || !inventory.getItems()[0].getName().equals("Coins")) {
				bank.depositAll();
				rsleep(1300);
				bank.withdrawAll("Coins");
			}
			else if (inventory.getItems()[1] != null){
				inventory.getItems()[1].interact("Deposit All");
			}
			
			if (bank.isBankModeEnabled(Bank.BankMode.WITHDRAW_NOTE)) {
			bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
			}
			rsleep(600);
			bank.withdrawAll("Cowhide");
			
			while(bank.isOpen()) {
				rsleep(400);
				bank.close();
			}
			
			state = STATEMACHINE.tanhides2;
		}
			break;
		case tanhides2:
			//run to the tanner
			//enable run
			
			//walk towards tanner
			walking.walk(new Position(3279,3191,0));
			
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(1500);
			}
			
			//if door is closed, then open it.
			openTannerDoor();
			
			state = STATEMACHINE.tanhides3;
			
			break;
		case tanhides3:
			openTannerDoor();
			if (inventory.getItems()[1] != null &&
					inventory.getItems()[1].getName().equals("Cowhide"))
			{
			Entity ellis = npcs.closest("Ellis");
			ellis.interact("Trade");
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				//slowed it down for reliability
				rsleep(150);
			}
			
			//TODO: waitforwidget tanner
			WaitForWidget(324,153);
			if (widgets.get(324,1) != null && widgets.get(324,1).isVisible())
			{
			widgets.get(324,1).interact("Tan All");
			rsleep(1000);
			}
			}
			
			openTannerDoor();
			state = STATEMACHINE.tanhides1;
			break;
		case completetradecoins:
			//get the coins from him
			
			//make sure i am not already trading.
			if ((widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)
					&&
					(widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false))
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals("LordBurk")) {
					p.interact("Trade");
					rsleep(1500);
					break;
				}
			}
			
			
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
			if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
					|| WaitForWidget(335,25)) {
				//wait for him to put the coins in
				rsleep(2000);
				//press accept
				click(264,180);
				//wait for him to accept
				if (WaitForWidget(334,13)) {
				//press accept again
				click(215,303);
				//successful trade!
				state = STATEMACHINE.buycowhides;
				
				}
			}
			
			
			
			break;
		case notefinishedhidesfrombank:
			while (!bank.isOpen()) {
				bank.open();
			}
			
			bank.depositAll();
			rsleep(1500);
			if (bank.isBankModeEnabled(Bank.BankMode.WITHDRAW_ITEM)) {
				bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
				rsleep(500);
				}
			
			bank.withdrawAll("Leather");
			
			if (inventory.getItems()[0] != null &&
					inventory.getItems()[0].getName().equals("Leather"))
			{
				if (bank.isOpen())
					bank.close();
				
				state = STATEMACHINE.requestburktakehides;
				
			}
			
			
			
			break;
		case requestburktakehides:
			
			lastfiletime = System.currentTimeMillis();
			try{
				File f = new File(getDirectoryData() + "\\" + System.currentTimeMillis() + ".leatherTake");
				log("creating file " + getDirectoryData() + "\\" + System.currentTimeMillis() + ".leatherTake");
			PrintWriter out = new PrintWriter(f);
			out.println(myPlayer().getName());
			out.close();
			}catch(Exception e){log("file error :3c");}
			
			state = STATEMACHINE.waitfortradehides;
			
			break;
		case waitfortradehides:
			if (tradeFlag) {
				//wait for trade variable to be flipped
				state = STATEMACHINE.completetradehides;
			}
			else if (System.currentTimeMillis() - lastfiletime > 15*K) {
				//its been 15 seconds, try again
				state = STATEMACHINE.requestburktakehides;
			}
			break;
		case completetradehides:


			//make sure i am not already trading.
			if ((widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)
					&&
					(widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false))
			for (Player p : getPlayers().getAll()) {
				if (p.getName().equals("LordBurk")) {
					p.interact("Trade");
					rsleep(1500);
					break;
				}
			}
			
			//trade screen 1 widget is 335,25
			//trade screen 2 widget is 334,13
		if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
				|| WaitForWidget(335,25)) {
			//put the hides in
			inventory.getItems()[0].interact("Offer-All");
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			//press accept again
			click(215,303);
			//successful trade!
			state = STATEMACHINE.logoutretire;
			
			}
		}
			break;
		case auditbank:
			while(!bank.isOpen())
			{
				bank.open();
			}
			
			if (bank.getAmount("Coins") + inventory.getAmount("Coins") > 30*K)
			{
				state = STATEMACHINE.buycowhides;
				
				
			}
			else if (bank.getAmount("Leather") + inventory.getAmount("Leather") > 0)
			{
				state = STATEMACHINE.notefinishedhidesfrombank;
				tradeFlag = false;
				
			}
			else if (bank.getAmount("Cowhide") + inventory.getAmount("Cowhide") > 0)
			{
				state = STATEMACHINE.gotodesert1;
				
				
			}
			else
			{
				state = STATEMACHINE.requestburkgivecoins;
				tradeFlag = false;
				
			}
			
			if (bank.isOpen()) {
				bank.close();
			}
			
			
			
			break;
		case buycowhides:
			
			
			String itemName = "Leather";
			
			
			
			
			while (!bank.isOpen()) {
				bank.open();
				rsleep(1000);
			}
			if (inventory.getItems()[0] != null && inventory.getItems()[0].getName().equals("Coins")) {
				
			}
			else
			{
				bank.depositAll();
				rsleep(1000);
				bank.withdrawAll("Coins");
				
			}
			rsleep(1000);
			
			Entity clerk = npcs.closest("Grand Exchange Clerk");
			if (clerk != null)
				clerk.interact("Exchange");
			
			
			//click collect button btw
			rsleep(1500);
			click(456,64);
			
			
			if (WaitForWidget(465,7,3)) {//create buy offer widget 
				
				click(456,64);
				rsleep(800);
				click(59,145);
				if (WaitForWidget(465,24,21)) {
					click(113,117);
					rsleep(1500);
					keyboard.typeString(itemName);
					if (WaitForWidget(162,39,2)) {
				if (widgets.get(162,39,1).getMessage().contains(itemName)) {
					click(34,388);
					rsleep(3000);
					click(386,205);
					if (WaitForWidget(162,34)) {
						rsleep(1500);
						keyboard.typeString(""+(int)(getMarketPrice(itemName)*1.11)); 
						rsleep(3000);
						//click on amount to buy ...
						click(233,209);
						rsleep(3000);
						//type in amount to buy
						keyboard.typeString(""+getLeatherAmountToBuy());
						rsleep(1500);
						
						click(260,287);//click confirm
						//wait for collect button to appear
						if (WaitForWidget(465,6,1))
						{
							click(456,64);
						}
						
						state = STATEMACHINE.auditbank;
						}
					}
					}
					
				}
				
			}
			
			
			
			break;
		case logoutretire:
			try{
			File f = new File(getDirectoryData() + "\\" + getParameters() + ".LRetire");
			log("creating file " + getDirectoryData() + "\\" + getParameters() + ".LRetire");
		PrintWriter out = new PrintWriter(f);
		out.println(myPlayer().getName());
		out.close();
			}catch(Exception e){e.printStackTrace();}
			break;
		
				
		
		}
		
		
		
		
		return 50 + (int)(Math.random()*50);
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
		
		return spending / (int)((getMarketPrice("Leather")*1.11));
		
		
		
		
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
