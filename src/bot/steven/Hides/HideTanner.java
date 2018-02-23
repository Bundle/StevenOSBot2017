package bot.steven.Hides;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;




/*
 * DONETODO: open hide tanner guy door if closed
 * DONETODO: set player X,Y coordinate master control value
 * DONETODO: change hide value to normal hides
 * DONETODO: create path from lumbridge to desert
 * DONETODO: walk along the path state machine
 * DONETODO: walk to GE
 * DONETODO: walk back to desert from GE
 * DONETODO: buy items at GE
 * DONETODO: sell hides at GE
 * DONETODO: account for new states upon login: if starts in GE: 0 gold, enough gold , materials?
 * TODO: request LordBurk to log in when you hit the G E 
 * TODO: find LordBurk, trade
 * TODO: accept gold from LordBurk
 * TODO: buy 13k hides from GE using LordBurk's money
 * TODO: bank and run to the desert
 * TODO: tan the 13k hides
 * TODO: return to GE, give LordBurk his hides back
 */
/*
 * goal:
 * bot starts in lumby, runs to GE, requests LordBurk to log in, finds LordBurk at coordinates, 
 * trades him, gets the gold, buys 13k cowhide, banks it, runs to the desert, tans the 13k hides, returns to GE and 
 * gives LordBurk the hides back
 */

@ScriptManifest(author = "Steven Ventura", info = "Tan normal hides", logo = "", name = "HideTanner", version = 0)
public class HideTanner extends Script{

	final boolean LEFTCLICK = false, RIGHTCLICK = true;
	enum HIDESTATES {
		OpenBank,
		DepositHides,
		WithdrawHides,
		CloseBank,
		RunToTanner,
		TradeWithTanner,
		TanAllHides,
		ReturnToBank,
		Done
	}
	enum CONTROLLERBOY {
		HIDESTATES,
		WALKINGSPAWN,
		WALKINGTOGE,
		WALKINGTODESERT,
		BUYINGHIDES
	}
	enum WALKINGSPAWN {
		FindingLocation,
		WALKINGSPAWN,
		Done
	}
	WALKINGSPAWN walkingSpawnState;
	enum WALKINGTOGE {
		FindingLocation,
		WalkingtoGE,
		Done
		
	}
	WALKINGTOGE walkingToGE;
	
	enum WALKINGTODESERT {
		FindingLocation,
		WalkingToDesert,
		Done
	}
	WALKINGTODESERT walkingToDesert;
	
	enum BUYINGHIDES {
		CheckingBankForItems,
		BuyingFromShoppingList,
		SellHides,
		Done
	}
	BUYINGHIDES buyingHides;
	
	CONTROLLERBOY master;
	
	
	
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
	private void rightclick(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(RIGHTCLICK);
	}
	int totalJugsFilled = 0;
	HIDESTATES hideState = HIDESTATES.OpenBank;

	private int getExtraPotAmount()
	{
		return potAmount - (int)hideAmountLeft/26;
	}
	
	public void draw(Graphics2D g, StevenButton s)
	{
		if (s.pressed == true)
		g.setPaint(Color.BLUE);
		else
			g.setPaint(Color.GRAY);
		
		g.fillRect(s.x,s.y,s.width,s.height);
		
		g.setPaint(Color.WHITE);
		g.drawString(s.text,s.x,s.y+15);
	}
	enum StateBoys {
		GetGoldFromBurk,
		WalkToDesertToTan,
		DoTanning,
		returnToGE,
		GiveHidesToBurk,
		MarkAsBanned,
		UNKNOWN
	};
	class stateDataBoyFile {
		public stateDataBoyFile() {
			
		}
		public void write() {
			try {
			PrintWriter p = new PrintWriter(new File(getDirectoryData() + "\\" + myPlayer().getName() + ".hideStateData"));
			StateBoys s = StateBoys.UNKNOWN;
			if (master == CONTROLLERBOY.WALKINGSPAWN)
			
			
			
			p.close();}catch(Exception e){e.printStackTrace();}
			
		}
		public StateBoys getState() {
			try{
			Scanner scan = new Scanner(new File(getDirectoryData() + "\\" + myPlayer().getName() + ".hideStateData"));
			String line = scan.nextLine();
			switch (line) {
			case "GetGoldFromBurk":
				return StateBoys.GetGoldFromBurk;
			case "WalkToDesertToTan":
				return StateBoys.WalkToDesertToTan;
			case "DoTanning":
				return StateBoys.DoTanning;
			case "returnToGE":
				return StateBoys.returnToGE;
			case "GiveHidesToBurk":
				return StateBoys.GiveHidesToBurk;
			case "MarkAsBanned":
				return StateBoys.MarkAsBanned;
			case "UNKNOWN":
				return StateBoys.UNKNOWN;
			}
			}catch(Exception e){e.printStackTrace();}
			return StateBoys.UNKNOWN;
		}
		/*
		 * 
		 */
		
		
		
		
		
		
		
	}
	class HideSideBurkFile {
		
		public HideSideBurkFile() {
			
		}
		private void writeToFile() {
		
			try{File f = new File("C:\\Users\\Yoloswag\\OSBot\\Data\\" + myPlayer().getName() + ".burksummon");
		PrintWriter p = new PrintWriter(f);
		
		p.println(""+new Date());
		p.println(myPlayer().getName());
		p.println(""+myPlayer().getX());
		p.println(""+myPlayer().getY());
		p.close();
		
		p.close();}catch(Exception e){e.printStackTrace();}
		}
		
		
	}
	class StevenButton {
		
		public int x=-1000, y=-1000, width=80, height=20;
		public String text = "button";
		public boolean pressed = false;
		
		public StevenButton(String text, int x, int y) {
			this.text = text; this.x = x; this.y  = y;

		}
		//this gets overridden lol
		public void onStevenClick() {
			
		}
		
	}
	ArrayList<StevenButton> stevenbuttons = new ArrayList<>();
	private boolean restockThisTime = false;
	
	
	@Override
	public void onStart() {
		
		populatePrices();
		
		stevenbuttons.add(new StevenButton("Restock",10,440) {
			public void onStevenClick() {
				restockThisTime = !restockThisTime;
			}
		});
		
		bot.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {

				log("x= " + arg0.getX() + " y= " + arg0.getY());
				for (StevenButton b : stevenbuttons) {
					if (new Rectangle2D.Double(b.x,b.y,b.width,b.height).contains(arg0.getX(),arg0.getY())){
						b.onStevenClick();
						b.pressed = !b.pressed;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			
			
		});
		
		//TODO: add state for being at G E or inbetween G E and spawnboy
			
		
		if (myPlayer().getY() > 3337) {
			master = CONTROLLERBOY.WALKINGTOGE;
			walkingToGE = WALKINGTOGE.FindingLocation;
			//if he needs to get items from burk , that will be figured out at the G E from fileinfo 
			//if he needs to 
		}

		//set player X,Y coordinate master control value and sub control values initialize
		else if (myPlayer().getX() > 3267 && myPlayer().getY() < 3196) {
			master = CONTROLLERBOY.HIDESTATES;
			hideState = HIDESTATES.OpenBank;
		}
		else
		{
			master = CONTROLLERBOY.WALKINGSPAWN;	
			walkingSpawnState = WALKINGSPAWN.FindingLocation;
		}
		
		
	}
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
			if (message.getTypeId() == CLANCHAT) {
				if (text.equalsIgnoreCase("Hop")) {
					log("hoppin worlds btw");
					worlds.hopToF2PWorld();
				}
			}
		
		
		
		
	}
	public void onPaint(Graphics2D g)
	{
		for (StevenButton b : stevenbuttons)
		{
			this.draw(g,b);
		}
		
		g.setPaint(Color.CYAN);
		if (master == CONTROLLERBOY.WALKINGSPAWN) 
			g.drawString("HIDEBOT: interrupt: " + walkingSpawnState,10,60);
		else if (master == CONTROLLERBOY.HIDESTATES)
			g.drawString("HIDEBOT: hideState is " + hideState,10,60);
		else if (master == CONTROLLERBOY.WALKINGTOGE)
			g.drawString("HIDEBOT: interrupt: " + walkingToGE,10,60);
		else if (master == CONTROLLERBOY.WALKINGTODESERT)
			g.drawString("HIDEBOT: interrupt: " + walkingToDesert,10,60);
		else if (master == CONTROLLERBOY.BUYINGHIDES)
			g.drawString("HIDEBOT: interrupt: " + buyingHides,10,60);
		
		g.drawString("extra pots:" + getExtraPotAmount(),10,80);
		g.drawString("Left=" + hideAmountLeft + ",Done=" + hideAmountDone, 10,100);
		g.drawString("TimeLeft=" + reee((int)(hideAmountLeft/26*36)),10,120);
		
		
		g.setPaint(Color.BLACK);
		g.drawString("abortCx",0,0);
		
	}
	private String reee(int seconds)
	{
		
		int hours=0,minutes=0;
		hours = (int)Math.floor(seconds/3600);
		seconds -= hours*3600;
		minutes = (int)Math.floor(seconds/60);
		seconds -= minutes*60;
		return hours + ":" + minutes + ":" + seconds;
	}
	
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	
	private TreeMap<String,Integer> shoppingList = new TreeMap<String,Integer>();
	
	int hideAmountLeft = 0;
	int hideAmountDone = 0;
	int potAmount = 0;
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
	
	@Override
	public int onLoop() throws InterruptedException {
		
		
			if (master == CONTROLLERBOY.HIDESTATES)
		switch (hideState) {
		case OpenBank:
			
			
			
			
			walking.walk(new Position(3278,3179,0));
			rsleep(500);
			walking.walk(new Position(3278,3179,0));
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(50);
			}
			int please = 0;
			while(!bank.isOpen()) {
				please++;
				if (please > 10) {
				click(583,140);//enable run
				please = 0;
				}
			bank.open();
			rsleep(100);
			}
			hideState = HIDESTATES.DepositHides;
			break;
		case DepositHides:
			//right click on hide and hit deposit all
			try{
			
			bank.depositAll();
			
			}catch(Exception e){log("error, nothing in slot 1 btw");};
			hideState = HIDESTATES.WithdrawHides;
			break;
		case WithdrawHides:
			//money
			bank.withdrawAll("Coins");
			rsleep(1000);//sometimes it doesnt get the pots
			//pots
			bank.withdraw("Energy potion(4)", 1);
			//right click on hides in bank and withdraw all
			rsleep(1000);//sometimes it doesnt get the fuckin hides
			if (bank.isOpen())
				hideAmountLeft = (int)bank.getAmount("Cowhide") + (int)inventory.getAmount("Cowhide");
			if (hideAmountLeft == 0) {
				if (restockThisTime) {
					master = CONTROLLERBOY.WALKINGTOGE;
					walkingToGE = WALKINGTOGE.FindingLocation;
				}else	System.exit(0);
				break;
			}
			bank.withdrawAll("Cowhide");
			
			rsleep(500);
			hideState = HIDESTATES.CloseBank;
			break;
		
		case Done:
			
			break;
		case CloseBank:
			bank.close();
			hideState = HIDESTATES.RunToTanner;
			break;
		case RunToTanner:
			//enable run
			if (colorPicker.isColorAt(583,140,new java.awt.Color(236,218,103)))
			{
				//then run is already enabled
				log("run is already enabled.");
			}
			else
			{
				log("enabling run");
				click(583,140);//enable run
			}
			
			//walk towards tanner
			walking.walk(new Position(3279,3191,0));
			
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(1500);
			}
			
			
			
			hideState = HIDESTATES.TradeWithTanner;
			break;
		case TradeWithTanner:
			openTannerDoor();
			
			
			Entity ellis = npcs.closest("Ellis");
			ellis.interact("Trade");
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				//slowed it down for reliability
				rsleep(50);
			}
			
			
			hideState = HIDESTATES.TanAllHides;
			break;
		case TanAllHides:
			WaitForWidget(324,153);
			
			int SOFT = 148, HARD = 149,
			RED = 154, BLUE = 153, GREEN = 152, BLACK = 107;
			if (widgets.get(324,SOFT) != null && widgets.get(324,SOFT).isVisible())
				{
				widgets.get(324,SOFT).interact("Tan All");
				hideAmountDone += 26;
				hideState = HIDESTATES.ReturnToBank;
				}
			else
			{
				hideState = HIDESTATES.TradeWithTanner;
			}
			openTannerDoor();
				
			break;
		case ReturnToBank:
			//if stuck in tanning room, then return to "ReturnToBank" state so we can open door.
			openTannerDoor();
			walking.walk(new Position(3278,3179,0));
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(50);
			}
			
			if (myPlayer().getY() < 3190-1)//otherwise he is trapped in the room and needs to try the door again. 
				hideState = HIDESTATES.OpenBank;
			break;
			
		
		
		
		}
			else if (master == CONTROLLERBOY.WALKINGSPAWN) 
				switch(walkingSpawnState) {
				case FindingLocation:
					int closest = -1;
					double closestdistance = Double.MAX_VALUE;
					for (int i = 0; i < walkycoordsSpawn.length; i++) {
						double pls = Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsSpawn[i][0],2)+Math.pow(
								myPlayer().getY()-walkycoordsSpawn[i][1],2));
						if (pls < closestdistance)
						{
						closestdistance = pls;
						closest = i;
						}
					}
					currentLocationSpawn = closest;
					walkingSpawnState = WALKINGSPAWN.WALKINGSPAWN;
					break;
				case WALKINGSPAWN:
					
					walking.walk(new Position(walkycoordsSpawn[currentLocationSpawn][0],
										walkycoordsSpawn[currentLocationSpawn][1],
										0));
					waitForMovements();
					
					double pls = 
							Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsSpawn[currentLocationSpawn][0],2) + 
									Math.pow(myPlayer().getY()-walkycoordsSpawn[currentLocationSpawn][1],2));
							
					if (pls < 4)
						currentLocationSpawn++;
					else
						log("uhh he aint movin xD");
					
					
					if (currentLocationSpawn == walkycoordsSpawn.length) {
						walkingSpawnState = WALKINGSPAWN.Done;
					}
					
					break;
				case Done:
					//begin tanning i guess
					master = CONTROLLERBOY.HIDESTATES;
					hideState = HIDESTATES.OpenBank;
					break;
				
				
				
				}
			else if (master == CONTROLLERBOY.WALKINGTOGE) {
				stateMachineWalkingtoGE();
			}
			else if (master == CONTROLLERBOY.WALKINGTODESERT) {
				stateMachineWalkingBackToDesert();
			}
			else if (master == CONTROLLERBOY.BUYINGHIDES) {
				stateMachineBuyingHides();
			}
				
		
		
		return (int)(50*Math.random() + 50);
	}
	private int PRICE_BUYING_HIDE, PRICE_SELLING_HIDE;
private void populatePrices() {
	try{
	Scanner scan = new Scanner(new File(getDirectoryData() + "\\" + "cowhides.data"));
	PRICE_BUYING_HIDE = (int)(1.10f*Integer.parseInt(scan.nextLine()));
	PRICE_SELLING_HIDE = (int)(0.90f*Integer.parseInt(scan.nextLine()));
	scan.close();
	}catch(Exception e){e.printStackTrace();}
	}

	private int getPrice(String name)
	{
		switch(name) {
		case "Cowhide":
			return PRICE_BUYING_HIDE;
		case "Leather":
			return PRICE_SELLING_HIDE;
		
		}
		return -1;
	}
	private long coins = -1;
	private long hidesCount = -1;
	private long finishedHides = -1;
	private void stateMachineBuyingHides() {
		String[] itemsToCheck = {"Coins","Cowhide","Leather"};
		switch (buyingHides) {
		case CheckingBankForItems:
			int threshold = 50000;
			
			coins = bank.getAmount(itemsToCheck[0]) + inventory.getAmount(itemsToCheck[0])
			- threshold;//save 13k coins for buying hides
			
			hidesCount = bank.getAmount(itemsToCheck[1]) + inventory.getAmount(itemsToCheck[1]);
			
			finishedHides = bank.getAmount(itemsToCheck[2]) + inventory.getAmount(itemsToCheck[2]);
			
			if (coins > threshold+1000) {
				int numHidespls = (int)coins/(getPrice("Cowhide"));
						
				shoppingList.put("Cowhide", numHidespls);
				
					
				buyingHides = BUYINGHIDES.BuyingFromShoppingList;
			}
			else if (finishedHides > 0) {
				buyingHides = BUYINGHIDES.SellHides;
			}
			//evaluate: do we go to the desert, or do we buy more items?
			
			break;
		case BuyingFromShoppingList:
			
			

			String itemName = "";
			
			//get 1 item name
			for (String s : shoppingList.keySet()) {
				itemName = s;
				break;
			}
			
			
			try{
			while (!bank.isOpen()) {
				bank.open();
				rsleep(1000);
			}
			}catch(Exception e){e.printStackTrace();}
			bank.withdrawAll("Coins");
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
						keyboard.typeString(""+getPrice(itemName)); 
						rsleep(3000);
						//click on amount to buy ...
						click(233,209);
						rsleep(3000);
						//type in amount to buy
						keyboard.typeString(""+shoppingList.get(itemName));
						rsleep(1500);
						
						click(260,287);//click confirm
						//wait for collect button to appear
						if (WaitForWidget(465,6,1))
						{
							click(456,64);
						}
						shoppingList.remove(itemName);
						buyingHides = BUYINGHIDES.CheckingBankForItems;
						}
					}
					}
					
				}
				
			}
			
			
			
			break;
		case SellHides:
			try{
			while (!bank.isOpen()) {
				bank.open();
				rsleep(500);
			}
			}catch(Exception e){e.printStackTrace();}
			long finishedHides2 = bank.getAmount(itemsToCheck[2]) + inventory.getAmount(itemsToCheck[2]);
			if (finishedHides2 == 0) {
				buyingHides = BUYINGHIDES.CheckingBankForItems;
				break;
			}
			bank.depositAll();
			rsleep(1500);
			//click the note thing
			click(295,322);
			rsleep(500);
			bank.withdrawAll("Leather");
			bank.close();
			
			Entity clerk2 = npcs.closest("Grand Exchange Clerk");
			if (clerk2 != null)
				clerk2.interact("Exchange");
			else
				break;
			
			//click collect
			rsleep(2000);
			click(456,64);
			
			if (WaitForWidget(465,7,3)) {//create buy offer widget 
				//click on item to "offer" it
				inventory.getItem("Leather").interact("Offer");
				if (WaitForWidget(465,24,21)) {
					
					
					click(386,205);
					if (WaitForWidget(162,34)) {
						keyboard.typeString("" + PRICE_SELLING_HIDE); 
						rsleep(1500);
						click(260,287);//click confirm
						//wait for collect button to appear
						if (WaitForWidget(465,6,1))
						{
							click(456,64);
							//fall back and let base case handle the state change
						}
						//click on it
						
					}
					
					}
				
			}
			
			
			
			
			break;
		
		}
		
		
		
	}
	private void stateMachineWalkingBackToDesert() {
		switch(walkingToDesert) {
		case FindingLocation:
			
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < walkycoordsGEDESERT.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsGEDESERT[i][0],2)+Math.pow(
						myPlayer().getY()-walkycoordsGEDESERT[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			currentLocationTowardsDesert = closest;
			walkingToDesert = WALKINGTODESERT.WalkingToDesert;
			
			
			break;
		case WalkingToDesert:
			walking.walk(new Position(walkycoordsGEDESERT[currentLocationTowardsDesert][0],
					walkycoordsGEDESERT[currentLocationTowardsDesert][1],
					0));
waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsGEDESERT[currentLocationTowardsDesert][0],2) + 
				Math.pow(myPlayer().getY()-walkycoordsGEDESERT[currentLocationTowardsDesert][1],2));
		
if (pls < 4)
	currentLocationTowardsDesert--;
else
	log("uhh he aint movin xD");


if (currentLocationTowardsDesert == 0) {
	walkingToDesert = WALKINGTODESERT.Done;
}

			break;
		case Done:
			if (restockThisTime) {
				master = CONTROLLERBOY.HIDESTATES;
				hideState = HIDESTATES.OpenBank;
			}
			else
				System.exit(0);
			break;
		
		
		
	}
		
	}
	private void stateMachineWalkingtoGE() {
		switch(walkingToGE) {
			case FindingLocation:
				
				int closest = -1;
				double closestdistance = Double.MAX_VALUE;
				for (int i = 0; i < walkycoordsGEDESERT.length; i++) {
					double pls = Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsGEDESERT[i][0],2)+Math.pow(
							myPlayer().getY()-walkycoordsGEDESERT[i][1],2));
					if (pls < closestdistance)
					{
					closestdistance = pls;
					closest = i;
					}
				}
				currentLocationTowardsGE = closest;
				walkingToGE = WALKINGTOGE.WalkingtoGE;
				
				
				break;
			case WalkingtoGE:
				walking.walk(new Position(walkycoordsGEDESERT[currentLocationTowardsGE][0],
						walkycoordsGEDESERT[currentLocationTowardsGE][1],
						0));
	waitForMovements();
	
	double pls = 
			Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsGEDESERT[currentLocationTowardsGE][0],2) + 
					Math.pow(myPlayer().getY()-walkycoordsGEDESERT[currentLocationTowardsGE][1],2));
			
	if (pls < 4)
		currentLocationTowardsGE++;
	else
		log("uhh he aint movin xD");
	
	
	if (currentLocationTowardsGE == walkycoordsGEDESERT.length) {
		walkingToGE = WALKINGTOGE.CheckBankBeforeAskingBurk;
	}
	
				break;
	
			case CheckBankBeforeAskingBurk: 
				//just read the statedata
				
				//write the outline of the program
				
				//check for item1
				
				//check for item2
				
				//if we already have enough of item1 and item2, then dont ask burk, just continue another way
				
				//else if we have a certain amount of both, then trade burk accordingly
				
				break;
			case AskBurkToLogIn:
				//ask burk to log in
				
				
				
				break;
			case doTradeWaterfallWithBurk:
				
				break;
			case Done:
				
				break;
			
			
			
		}
	}
	int currentLocationSpawn = -1;
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
	
	int[][] walkycoordsSpawn = {{3232, 3230},
	{3237, 3226},
	{3245, 3226},
	{3248, 3225},
	{3254, 3226},
	{3259, 3232},
	{3259, 3238},
	{3259, 3243},
	{3256, 3249},
	{3252, 3253},
	{3250, 3255},
	{3250, 3263},
	{3247, 3271},
	{3246, 3273},
	{3241, 3279},
	{3239, 3283},
	{3239, 3289},
	{3239, 3290},
	{3239, 3298},
	{3243, 3308},
	{3247, 3316},
	{3255, 3322},
	{3265, 3325},
	{3273, 3330},
	{3279, 3324},
	{3277, 3318},
	{3278, 3311},
	{3278, 3305},
	{3278, 3297},
	{3275, 3293},
	{3275, 3287},
	{3275, 3279},
	{3275, 3275},
	{3275, 3267},
	{3275, 3261},
	{3275, 3255},
	{3275, 3243},
	{3275, 3236},
	{3275, 3226},
	{3275, 3220},
	{3274, 3211},
	{3273, 3203},
	{3272, 3198},
	{3279, 3193},
	{3280, 3183},
	{3276, 3180},
	{3275, 3175}};

	int currentLocationTowardsGE = -1;
	int currentLocationTowardsDesert = -1;
	int[][] walkycoordsGEDESERT = {{3273, 3167},{3276, 3175},{3278, 3177},{3278, 3181},{3280, 3187},{3281, 3188},{3281, 3192},{3281, 3196},{3280, 3202},{3278, 3206},{3279, 3213},{3277, 3221},{3275, 3223},{3275, 3229},{3275, 3235},{3275, 3239},{3273, 3241},{3273, 3247},{3273, 3255},{3272, 3259},{3272, 3265},{3272, 3271},{3273, 3277},{3273, 3281},{3273, 3287},{3273, 3293},{3273, 3295},{3273, 3301},{3273, 3305},{3273, 3311},{3274, 3313},{3274, 3317},{3274, 3323},{3278, 3330},{3272, 3330},{3269, 3329},{3265, 3330},{3257, 3330},{3252, 3333},{3246, 3335},{3238, 3335},{3234, 3336},{3230, 3336},{3228, 3342},{3227, 3348},{3227, 3352},{3223, 3353},{3219, 3354},{3213, 3360},{3212, 3361},{3212, 3367},{3210, 3373},{3204, 3376},{3199, 3374},{3191, 3374},{3185, 3378},{3182, 3381},{3181, 3386},{3176, 3392},{3172, 3396},{3172, 3402},{3172, 3408},{3172, 3414},{3172, 3420},{3172, 3424},{3174, 3426},{3174, 3430},{3174, 3439},{3174, 3442},{3175, 3447},{3170, 3452},{3168, 3458},{3167, 3459},{3166, 3463},{3166, 3469},{3166, 3475},{3164, 3477},{3164, 3481},{3164, 3484},{3165, 3486},
	};
	
	private static int lookupPrice(String name) {
	    try {
	        Document doc = Jsoup.connect("http://2007.runescape.wikia.com/wiki/Exchange:" + name.replaceAll(" ", "_")).get();
	        
	        Element price = doc.getElementById("GEPrice");
	        return Integer.parseInt(price.text().replaceAll(",", ""));
	    } catch (Exception e) {}
	    return 0;
	}
	public static void main(String[]args) {
		try{File f = new File("C:\\Users\\Yoloswag\\OSBot\\Data\\leather.data");
		PrintWriter p = new PrintWriter(f);
		p.write(lookupPrice("Cowhide") + "\r\n");
		p.write(lookupPrice("Leather") + "\r\n");
		p.close();
		
		}catch(Exception e){e.printStackTrace();}
	}
}
