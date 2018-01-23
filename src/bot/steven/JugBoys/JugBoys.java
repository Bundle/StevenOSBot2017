package bot.steven.JugBoys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * JugBoys:
 * living the dream. a fully automated bot with interface to take , receive, and command Jug interaction.
 */
@ScriptManifest(author = "Steven Ventura", info = "My Water Boys", logo = "", name = "JugBoys", version = 0)
public class JugBoys extends Script{
	/*
	 * 1) travel to falador
	 * 2) fill jugs
	 * 3) give full jugs
	 * 4) take empty jugs
	 */
	
	enum MASTERSTATES {
		Idle,
		JoinCC,
		TravelToFalador,
		FillJugs,
		GiveFullJugs,
		TakeEmptyJugs
	};
	
	MASTERSTATES master;
	
	enum TRAVELLINGS {
		FindStart,
		Travel,
		Done
	};
	TRAVELLINGS travellingState;
	
	enum FILLJUGS {
		Bank,
		FillJugs,
		Done
	};
	FILLJUGS fillJugState;
	enum GIVEFULLJUGS {
		NotingItems,
		SendTradeRequest,
		WaitForTradeToOpen,
		Done,
	};
	GIVEFULLJUGS giveFullJugsState;
	enum TAKEEMPTYJUGS {
		WaitForTradeToOpen,
		Done,
		SendTradeRequest,
		EmptyBags
	};
	TAKEEMPTYJUGS takeEmptyJugsState;
	
	
	//this is taken from my JoinCC 1/18/2018
	enum JOINSTATES {
		clickCC,
		clickJoin,
		enterName,
		Done
	};
	JOINSTATES joiner;
	
final boolean LEFTCLICK = false, RIGHTCLICK = true;
	
	private void rsleep(long millis)
	{
		try{
			Thread.sleep(millis);
		}catch(Exception e){};
	}
	private void click(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(LEFTCLICK);
	}
	private void rclick(int x, int y)
	{
		int r1 = (int)(Math.random()*9);
		int r2 = (int)(Math.random()*9);
		int[] values = {-4,-3,-2,-1,0,1,2,3,4};
		mouse.move(x+values[r1],y+values[r2]);
		mouse.click(LEFTCLICK);
	}
	private void rightclick(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(RIGHTCLICK);
	}
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	
	private void printPlayerDataToFile() {
		//TODO: location,numEmptyJugs,numFullJugs
		
		try{
			File f = new File(getDirectoryData() + getParameters() + ".jugData");
			
			PrintWriter p = new PrintWriter(f);
			p.println(""+new Date());
			p.println(myPlayer().getName());
			p.println(""+myPlayer().getX());
			p.println(""+myPlayer().getY());
			p.println(""+numEmptyJugs);
			p.println(""+numFullJugs);
			p.close();
			
			
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	String stateData1, stateData2;
	
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
		if (message.getTypeId() == CLANCHAT 
				|| message.getTypeId() == WHISPER) {
			
			
			String[] split = text.split(" ");
			String phrase = split[0];
			
			if (phrase == null)
				return;
			
			log("Phrase is " + phrase);
			
			switch (phrase) {
			case "Falador":
				master = MASTERSTATES.TravelToFalador;
				travellingState = TRAVELLINGS.FindStart;
				break;
				
			case "Fill":
				master = MASTERSTATES.FillJugs;
				fillJugState = FILLJUGS.Bank;
				break;
				
			case "Give":
				stateData1 = message.getUsername();
				//stateData2 is unused
				giveFullJugsState = GIVEFULLJUGS.NotingItems;
				master = MASTERSTATES.GiveFullJugs;
				break;
				
			case "Take":
				stateData1 = message.getUsername();//name
				//stateData2 is unused
				takeEmptyJugsState = TAKEEMPTYJUGS.EmptyBags;
				master = MASTERSTATES.TakeEmptyJugs;
				break;
				
			
			
			}
			
			
			
		}
		
		
		
		
	}
	@Override
	public void onExit() {
		
	}
	
	public void onPaint(Graphics2D g) {
		g.setPaint(Color.ORANGE);
		
		switch (master) {
		
		case TravelToFalador:
			g.drawString("JugBoy=" + travellingState,10,60);
			break;
		case FillJugs:
			g.drawString("JugBoy=" + fillJugState,10,60);
			break;
			
		case GiveFullJugs:
			g.drawString("JugBoy=" + giveFullJugsState,10,60);
			break;
		case TakeEmptyJugs:
			g.drawString("JugBoy=" + takeEmptyJugsState,10,60);
		break;
		
		default:
			g.drawString("JugBoy master=" + master,10,60); 
		break;
		}
		
		g.drawString(""+numEmptyJugs,10,80);
		g.drawString(""+numFullJugs,10,100);
		
	}
	
class TravellingToFalador {
		
		TravelNode currentDestination;
		
		class TravelNode {
			TravelNode nextTowardsFalador = null;
			TravelNode previous = null;
			private int x, y;
			
			public TravelNode(int x, int y) {
				this.x=x;this.y=y;
				
			}
		}
		
		//TODO: put things in path thing manually. maybe use arraylist idk whatever works for easiest inputing w helper function
		public TravellingToFalador() {
			
			defineTree();
		}
		private void defineTree() {
			//TODO: populate the tree so its usable
			
		}
		
		
	}
	@Override
public void onStart() {
		master = MASTERSTATES.JoinCC;
		joiner = JOINSTATES.clickCC;
	}


	
	
	int numEmptyJugs=-1,numFullJugs=-1;
	private long CT=System.currentTimeMillis(),LT=System.currentTimeMillis();
	@Override
	public int onLoop() throws InterruptedException {
		CT = System.currentTimeMillis();
		if (CT - LT > 30 * 1000) {
			LT = CT;
			printPlayerDataToFile();
		}
		
		switch (master) {
		case Idle:
			//do nothing
			break;
		case TravelToFalador:
			if (traveller == null)
				traveller = new Travelling(true);
			stateMachineTravelling();
			break;
		case FillJugs:
			stateMachineFillJugs();
			break;
		case GiveFullJugs:
			stateMachineGiveFullJugs();
			break;
		case TakeEmptyJugs:
			stateMachineTakeEmptyJugs();
			break;
		case JoinCC:
			stateMachineJoinCCAndFriendsON();
			break;
			
		
		
		
		}
		
		
		return 150;
	}
	private void stateMachineFillJugs() {
		switch (fillJugState) {
		case Bank:
			while (!bank.isOpen()) {
				try{
				bank.open();
				rsleep(1000);
				}catch(Exception e){e.printStackTrace();}
			}
			
			numEmptyJugs = (int)(inventory.getAmount("Jug") + bank.getAmount("Jug"));
			numFullJugs = (int)(inventory.getAmount("Jug of water") + bank.getAmount("Jug of water"));
			
			if (numEmptyJugs == 0)
			{
				fillJugState = FILLJUGS.Done;
				break;
			}
			//deposit all
			rclick(443,313);
			rsleep(300);
			
			bank.withdrawAll("Jug");
			
			bank.close();
			
			//check inventory rq
			boolean good = true;
			for (int i = 0; i < 28; i++) {
				if (inventory.getItem(i) != null && !inventory.getItem(i).nameContains("Jug")) {
					good = false;
				}
			}
			if (!good) {
				//no state change: bank again
			}
			else
			{
				fillJugState = FILLJUGS.FillJugs;
			}
			
			break;
			
		case FillJugs:
			if (!inventory.contains("Jug")) {
				log("we didnt withdraw any jugs? maybe we are done?");
				fillJugState = FILLJUGS.Bank;
				break;
			}
			
			
			
			//walk to spicket
			walking.walk(new Position(2949,3382,0));
			rsleep(800);
			waitForMovements();
			
			Entity spicket = objects.closest("Waterpump");
			if (spicket == null) {
				log("ERROR: spicket is null? are you in fally?");
				break;
			}
			inventory.getItem("Jug").interact("Use");
			spicket.interact("Use");
			rsleep(3000);//3000 lag-sleep to ensure the animation begins
			//now wait until the jugs are full
			
			//can probably stall on this but oh well.
			boolean doneFilling = false;
			while (doneFilling == false) {
				
				doneFilling = true;
				if (myPlayer().getAnimation() == 832)//filling jug animation
					doneFilling = false;
				rsleep(150);
				if (myPlayer().getAnimation() == 832)//filling jug animation
					doneFilling = false;
				rsleep(150);
				if (myPlayer().getAnimation() == 832)//filling jug animation
					doneFilling = false;
				rsleep(150);
				if (myPlayer().getAnimation() == 832)//filling jug animation
					doneFilling = false;
				rsleep(150);
				
				//if these many cycles pass without the animation happening then stop
				
				
				
			}
			
			fillJugState = FILLJUGS.Bank;
			
			
			
			
			break;	
		case Done:
			//TODO: he is done filling jugs, so register this.
			master = MASTERSTATES.Idle;
			break;
			
		
		}
		
		
	}
	private void stateMachineTravelling() {
		switch (travellingState) {
		
		case FindStart:
			double closestDistance = Double.MAX_VALUE;
			int closestIndex = -1;
			double x2 = myPlayer().getX();
			double y2 = myPlayer().getY();
			for (int i = 0; i < traveller.temp.size(); i++) {
				double x1 = traveller.temp.get(i).x;
				double y1 = traveller.temp.get(i).y;
				
				
				
				double distance = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
				if (distance < closestDistance) {
					closestDistance = distance;
					closestIndex = i;
				}
				
			}
			
			traveller.currentDestination = traveller.temp.get(closestIndex);
			
			travellingState = TRAVELLINGS.Travel;
			break;
		case Travel:
			
			waitForMovements();
			if (traveller.directionTowardsFalador) {
				if (traveller.currentDestination.towardsFalador == null) {
					travellingState = TRAVELLINGS.Done;
					break;
				}
				else
				{
					traveller.currentDestination = traveller.currentDestination.towardsFalador;
				}
				
			}
			else
			{
				if (traveller.currentDestination.towardsGE == null) {
					travellingState = TRAVELLINGS.Done;
					break;
				}
				else
				{
					traveller.currentDestination = traveller.currentDestination.towardsGE;
				}
				
			}
			
			//now walk toward currentDestination
			walking.walk(new Position(
					traveller.currentDestination.x,
					traveller.currentDestination.y,
					0));
			rsleep(800);
			waitForMovements();
			
			
			break;
			
		case Done:
			master = MASTERSTATES.Idle;
			registerJustArrivedAtDestination();
			
			break;
		
		
		}
		
		
		
	}
	private void registerJustArrivedAtDestination() {
		//TODO
		
	}
	
	private void stateMachineGiveFullJugs() {
	
	switch (giveFullJugsState) {
	case Done:
		//go to idle
		master = MASTERSTATES.Idle;
		break;
	case SendTradeRequest:
		Entity tradeboy = players.closest(stateData1);
		if (tradeboy != null)
			{
			tradeboy.interact("Trade with");
			rsleep(500);
			//TODO: check for message "interact with" to verify trade was sent
			giveFullJugsState = GIVEFULLJUGS.WaitForTradeToOpen;
			}
		//else keep trying
		
		break;
	case WaitForTradeToOpen:
		//wait for widget
		//give him the item
		if  (WaitForWidget(335,25)) {
		inventory.interact("Offer-All", "Jug of water");
		//press accept
		click(264,180);
		//wait for him to accept
		if (WaitForWidget(334,13)) {
		click(215,303);
		//WARNING: possible for bot to stall here ?
		WaitForWidgetToDisappear(334,13);
		//success so go back to farming
		giveFullJugsState = GIVEFULLJUGS.Done;
		}
		}
		//try again. this if statement is needed because "concurrency" and sheeit
		if (giveFullJugsState != GIVEFULLJUGS.Done){ 
		giveFullJugsState = GIVEFULLJUGS.WaitForTradeToOpen;
		}
		break;
	
	
	
	case NotingItems:
		//open bank
		while(!bank.isOpen()) {
			try{
				bank.open();
				}catch(InterruptedException e) {
					e.printStackTrace();
					break;
				}
			rsleep(400);
		}
		
		rsleep(100);
		//deposit all items
		
		//hit it twice cos god damn it
		bank.depositAll();
		bank.depositAll();

		rsleep(100);
		//hit withdraw-as-note
		
		click(288,318);
		rsleep(100);
		//interact, withdraw all
		bank.withdrawAll("Jug of water");
		rsleep(1500);
		if (inventory.getItems()[0].nameContains("Jug of water"))
		{
			giveFullJugsState = GIVEFULLJUGS.SendTradeRequest;
		}
		else
			//try again
			giveFullJugsState = GIVEFULLJUGS.NotingItems;
		break;
		}
		
		
	
	
	}
	
	
	private void stateMachineTakeEmptyJugs() {
		
		
		switch(takeEmptyJugsState) {
		case Done:
			//go to idle
			master = MASTERSTATES.Idle;
			break;
		//empty his bags first so he has room for trading
		case EmptyBags:
			//TODO: does this actually reach the bank always?
			while (!bank.isOpen()) {
				
				try{
				bank.open();
				rsleep(3000);
				}catch(Exception e){e.printStackTrace();}
				
			}
			bank.depositAll();
			rsleep(800);
			bank.close();
			takeEmptyJugsState = TAKEEMPTYJUGS.SendTradeRequest; 
			break;
		case SendTradeRequest:
			Entity tradeboy = players.closest(stateData1);
			if (tradeboy != null)
				{
				tradeboy.interact("Trade with");
				rsleep(500);
				//TODO: check for message "interact with" to verify trade was sent
				takeEmptyJugsState = TAKEEMPTYJUGS.WaitForTradeToOpen;
				}
			//else keep trying
			
			break;
		case WaitForTradeToOpen:
			//TODO: this currently assumes the trade was successfully sent.
			//success so go back to farming
			if (inventory.getItems()[0] != null)
				takeEmptyJugsState = TAKEEMPTYJUGS.Done;
			
			//wait for widget
			//take the items from him
			if  (
					(widgets.get(334,13) != null && widgets.get(334, 13).isVisible()) 
					||
					(WaitForWidget(335,25))
					) {
			//offer nothing
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			click(215,303);
			//now go back to top and wait for item in inventory
			}
			}
			//try again. this if statement is needed because concurrency and sheeit
			if (takeEmptyJugsState != TAKEEMPTYJUGS.Done){ 
			takeEmptyJugsState = TAKEEMPTYJUGS.WaitForTradeToOpen;
			}
			break;
		
		
		}
		
		
	}
	
	private void stateMachineJoinCCAndFriendsON() {
		switch (joiner) {
		case clickCC:
			click(541,482);
			joiner = JOINSTATES.clickJoin;
			break;
		case clickJoin:
			if (widgets.get(7,17) != null) {
			String text = widgets.get(7,17).getMessage();
			log("text is " + text);
			if (text != null && text.equals("Join Chat"))
			{
			
			
			click(590,445);
			joiner = JOINSTATES.enterName;
			}
			else
				joiner = JOINSTATES.Done;
			}
			else
				joiner = JOINSTATES.Done;
			break;
		case enterName:
			keyboard.typeString("Pinball Boy");
			joiner = JOINSTATES.Done;
			break;
		case Done:
			//do nothing. should be in CC
			
			//actually , if friends is on private , then set it to all.
			try{
			if (!widgets.get(162,17).getMessage().equals("On")) {
				widgets.get(162,17).interact("<col=ffff00>Private:</col> Show all");
			}
			
			master = MASTERSTATES.Idle;
				
			}catch(Exception e){e.printStackTrace();}
			
			break;
		
		}
		
	}
	
	private boolean WaitForWidgetToDisappear (int arg1, int arg2)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2) != null || widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	private boolean WaitForWidget (int arg1, int arg2)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2) == null || !widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	private boolean WaitForWidget (int arg1, int arg2, int arg3)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2,arg3) == null || !widgets.get(arg1,arg2,arg3).isVisible()) {
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	
	
	
class Travelling {
		
		TravelNode currentDestination;
		TravelNode headGE;
		TravelNode tailFalador;
		
		boolean directionTowardsFalador;
		
		
		
		class TravelNode {
			TravelNode towardsFalador=null, towardsGE=null;
			private int x, y;
			boolean finalDestination;
			public TravelNode(int x, int y) {
				this.x=x;this.y=y;
				finalDestination=false;
			}
		}
		
		//TODO: put things in path thing manually. maybe use arraylist idk whatever works for easiest inputing w helper function
		public Travelling(boolean directionTowardsFalador) {
			this.directionTowardsFalador = directionTowardsFalador;
			defineTree();
		}
		public ArrayList<TravelNode> temp;
		private void defineTree() {
			//TODO: populate the tree so its usable
			temp = new ArrayList<>();
			//0 is grand exchange. end is falador west bank.
			for (int i = 0; i < xymashup.length; i+=2) {
				temp.add(new TravelNode(xymashup[i],xymashup[i+1]));
			}
			
			temp.get(0).towardsFalador = temp.get(1);
			for (int i = 1; i < temp.size()-1; i++) {
				temp.get(i).towardsFalador = temp.get(i+1);
				temp.get(i).towardsGE = temp.get(i-1);
			}
			temp.get(temp.size()-1).towardsGE = temp.get(temp.size()-2);
			
			
			
			
		}
		
		
	}
	
	Travelling traveller = null;
	
	int[] xymashup = {
			//top is lumbridge (tutorial island spawn)
			3232, 3231,
			3228, 3235,
			3224, 3239,
			3221, 3243,
			3215, 3242,
			3211, 3241,
			3207, 3242,
			3195, 3240,
			3191, 3243,
			3187, 3245,
			3179, 3249,
			3169, 3253,
			3161, 3254,
			3154, 3254,
			3148, 3257,
			3143, 3259,
			3133, 3261,
			3129, 3262,
			3126, 3263,
			3120, 3263,
			3116, 3263,
			3110, 3263,
			3108, 3266,
			3102, 3271,
			3094, 3272,
			3095, 3280,
			3091, 3281,
			3085, 3275,
			3083, 3271,
			3077, 3272,
			3069, 3276,
			3063, 3276,
			3060, 3275,
			3056, 3275,
			3050, 3275,
			3046, 3275,
			3039, 3275,
			3035, 3276,
			3029, 3279,
			3025, 3279,
			3017, 3279,
			3013, 3277,
			3011, 3281,
			3007, 3287,
			3008, 3294,
			3007, 3300,
			3007, 3310,
			3007, 3312,
			3007, 3318,
			3002, 3329,
			2997, 3324,
			2992, 3320,
			2987, 3319,
			2976, 3318,
			2970, 3316,
			2962, 3316,
			2960, 3316,
			2953, 3319,
			2948, 3324,
			2946, 3329,
			2947, 3333,
			2947, 3339,
			2946, 3348,
			2949, 3354,
			2949, 3360,
			2951, 3368,
			2949, 3373,
			2946, 3374,
			};//BOTTOM ,IS FALADOR
	}
	
	

