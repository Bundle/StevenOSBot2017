package bot.steven.BlueDragonhides;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * TODO: DONE bot gets stuck in the desert waiting room and his steady state says "state: opening bank"
 * TODO: bot gets stuck in G E saying "energy potion" out loud and error message "please choose an item"
 * TODO: DONE if tanner door gets closed, we need to re-open it. on state TanningHides
 * TODO: make a "finish up" command that tells the bots they are currently doing their last run.
 * TODO: DONE returntoGE state doesnt empty inventory
 * TODO: if the banrate is less than 2 days , then fuckin make it hop worlds on detect
 */

@ScriptManifest(author = "Steven Ventura", info = "Tan Green Dragonhides", logo = "", name = "GreenDragonhides", version = 0)
public class BlueDragonhides extends Script{

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
		ReturnToGE,
		Done
	}
	enum CONTROLLERBOY {
		HIDESTATES,
		BUYINGMATERIALS
	}
	enum BUYINGMATERIALS {
		CheckingBankForItems,
		CheckForTeleportCharges,
		TeleportToDesert,
		BuyingFromShoppingList,
		SellHides,
		WalkToDesertBank,
		Done
	}
	BUYINGMATERIALS buyingState = BUYINGMATERIALS.CheckingBankForItems;
	CONTROLLERBOY master = CONTROLLERBOY.BUYINGMATERIALS;
	private int getPrice(String name)
	{
		switch(name) {
		case "Green dragonhide":
			return PRICE_BUYING_HIDE;
		case "Energy potion(4)":
			return PRICE_BUYING_POT;
		case "Green dragon leather":
			return PRICE_SELLING_HIDE;
		case "Ring of wealth (5)":
			return PRICE_BUYING_RING;
		case "Amulet of glory(6)":
			return PRICE_BUYING_AMULET;
		}
		return -1;
	}
	//TODO: they got stuck when there was a collection to be made. on poitons. so put hte thing before the thing
	
	private void updatePrices() {
		
		try{
		Scanner scan = new Scanner(new File(getDirectoryData() + "\\" + "leather.data"));
		PRICE_BUYING_HIDE = (int)(1.05*Integer.parseInt(scan.nextLine()));
		PRICE_BUYING_POT = (int)(2.00*Integer.parseInt(scan.nextLine()));
		PRICE_SELLING_HIDE = (int)(0.95*Integer.parseInt(scan.nextLine()));
		PRICE_BUYING_AMULET = (int)(1.10*Integer.parseInt(scan.nextLine()));
		PRICE_BUYING_RING = (int)(1.10*Integer.parseInt(scan.nextLine()));
		}catch(Exception e){}
	}
	
	
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
	HIDESTATES currentState = HIDESTATES.OpenBank;

	private int getExtraPotAmount()
	{
		return potAmount - (int)hideAmountLeft/26;
	}
	private int PRICE_BUYING_HIDE = 0, PRICE_BUYING_POT = 0, PRICE_SELLING_HIDE,
			PRICE_BUYING_AMULET,PRICE_BUYING_RING;
	@Override
	public void onStart() {
		updatePrices();
		
		if (myPlayer().getY() < 3200) {
			//he's in the desert m8
			 master = CONTROLLERBOY.HIDESTATES;
			currentState = HIDESTATES.OpenBank;
		}
		else
		{
			//he's at the GE m8
			master = CONTROLLERBOY.BUYINGMATERIALS;
			buyingState = BUYINGMATERIALS.CheckingBankForItems;
		}
		
	}
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.CYAN);
		if (master == CONTROLLERBOY.BUYINGMATERIALS) 
			g.drawString("HIDEBOT: interrupt: " + buyingState,10,60);
		else if (master == CONTROLLERBOY.HIDESTATES)
			g.drawString("HIDEBOT: currentState is " + currentState,10,60);
		g.drawString("extra pots:" + getExtraPotAmount(),10,80);
		g.drawString("Left=" + hideAmountLeft + ",Done=" + hideAmountDone, 10,100);
		g.drawString("TimeLeft=" + reee((int)(hideAmountLeft/26*36)),10,120);
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
			WallObject doorplease = (WallObject)o;
			//door.x is 3277 door.y is 3191
			if (doorplease.getX() == 3277 && doorplease.getY() == 3191) {
				doorplease.interact("Open");
			}
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		//TODO: determine player location upon logging in, because
		//he could still be in the desert when i start the bot
		String[] itemsToCheck = {"Coins","Green dragonhide","Energy potion(4)",
		"Green dragon leather"};
		if (master == CONTROLLERBOY.BUYINGMATERIALS)
			switch(buyingState) {
			case CheckingBankForItems:
				/*
				 * 
				 */
				long coins=0,greenDragonhides=0,energyPots=0,finishedHides=0;
				
				
				while (!bank.isOpen())
				{
					bank.open();
					rsleep(500);
				}
				
				coins = bank.getAmount(itemsToCheck[0]) + inventory.getAmount(itemsToCheck[0])
						- 50000;//save 50k coins for buying amulets lol
				greenDragonhides = bank.getAmount(itemsToCheck[1]) + inventory.getAmount(itemsToCheck[1]);
				energyPots = bank.getAmount(itemsToCheck[2]) + inventory.getAmount(itemsToCheck[2]);
				finishedHides = bank.getAmount(itemsToCheck[3]) + inventory.getAmount(itemsToCheck[3]);
				
				
				
				if (coins > 400000) {
					int numHidespls = 
							(int)coins/(getPrice("Green dragonhide") + getPrice("Energy potion(4)")/26);
							//(int)coins/(getPrice("Green dragonhide"))
							//+ getPrice("Energy potion(4)")/26;
					shoppingList.put("Green dragonhide", numHidespls);
					if (energyPots < (numHidespls + greenDragonhides)/26)
					{
						shoppingList.put("Energy potion(4)",numHidespls/26);
					}//dont buy them twice
						
					buyingState = BUYINGMATERIALS.BuyingFromShoppingList;
				}
				else if (finishedHides > 0) {
					buyingState = BUYINGMATERIALS.SellHides;
				}
				//evaluate: do we go to the desert, or do we buy more items?
				else
				{
					buyingState = BUYINGMATERIALS.CheckForTeleportCharges;
				}
				
				
				
				
				break;
				
			case SellHides:
				
				while (!bank.isOpen()) {
					bank.open();
					rsleep(500);
				}
				long finishedHides2 = bank.getAmount(itemsToCheck[3]) + inventory.getAmount(itemsToCheck[3]);
				if (finishedHides2 == 0) {
					buyingState = BUYINGMATERIALS.CheckingBankForItems;
					break;
				}
				bank.depositAll();
				rsleep(1500);
				//click the note thing
				click(295,322);
				rsleep(500);
				bank.withdrawAll("Green dragon leather");
				bank.close();
				
				Entity clerk2 = npcs.closest("Grand Exchange Clerk");
				if (clerk2 != null)
					clerk2.interact("Exchange");
				else
					break;
				
				if (WaitForWidget(465,7,3)) {//create buy offer widget 
					//click on item to "offer" it
					inventory.getItem("Green dragon leather").interact("Offer");
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
				
			case CheckForTeleportCharges:
				
				while(!bank.isOpen()) {
					bank.open();
					rsleep(500);
				}
				
				boolean hasNeck = bank.getAmount("Amulet of glory(6)") + 
						bank.getAmount("Amulet of glory(5)") + 
						bank.getAmount("Amulet of glory(4)") + 
						bank.getAmount("Amulet of glory(3)") +
						bank.getAmount("Amulet of glory(2)") +
						bank.getAmount("Amulet of glory(1)") + 
						inventory.getAmount("Amulet of glory(6)") +
						inventory.getAmount("Amulet of glory(5)") +
						inventory.getAmount("Amulet of glory(4)") + 
						inventory.getAmount("Amulet of glory(3)") + 
						inventory.getAmount("Amulet of glory(2)") + 
						inventory.getAmount("Amulet of glory(1)") > 0;
				
						
						
				
				
				if (!hasNeck)
				{
					shoppingList.put("Amulet of glory(6)",1);//getPrice("Amulet of glory(6)"));
					buyingState = BUYINGMATERIALS.BuyingFromShoppingList;
				}
				
				boolean hasRing = bank.getAmount("Ring of wealth (6)") + 
						bank.getAmount("Ring of wealth (5)") + 
						bank.getAmount("Ring of wealth (4)") + 
						bank.getAmount("Ring of wealth (3)") +
						bank.getAmount("Ring of wealth (2)") +
						bank.getAmount("Ring of wealth (1)") + 
						inventory.getAmount("Ring of wealth (6)") +
						inventory.getAmount("Ring of wealth (5)") +
						inventory.getAmount("Ring of wealth (4)") + 
						inventory.getAmount("Ring of wealth (3)") + 
						inventory.getAmount("Ring of wealth (2)") + 
						inventory.getAmount("Ring of wealth (1)") > 0;
				
				if (!hasRing) {
					shoppingList.put("Ring of wealth (5)",1);//getPrice("Ring of wealth (5)"));
					buyingState = BUYINGMATERIALS.BuyingFromShoppingList;
				}
				if (hasNeck && hasRing) {
					buyingState = BUYINGMATERIALS.TeleportToDesert;
				}
				
				
				
				break;
			case TeleportToDesert:
				/*
				 * teleport to the desert using amulet of glory,
				 * and then get through the room and run to the bank.
				 * then pass control back to the thing.
				 */
				
				while(!bank.isOpen()) {
				bank.open();
				rsleep(500);
				}
				bank.depositAll();
				
				for (int i = 1; i <= 6; i++)
					bank.withdrawAll("Amulet of glory(" + i + ")");
				
				bank.close();
				
				for (int i = 1; i <= 6; i++)
				{
					if (inventory.contains("Amulet of glory(" + i + ")"))
					{
						inventory.getItem("Amulet of glory(" + i + ")").interact("Rub");
						if (WaitForWidget(219,0,4)) {
							click(261,435);//teleport to Al Kharid
						buyingState = BUYINGMATERIALS.WalkToDesertBank;
						}
						break;
						
						
					}
				}
				
				
				
				
				
				
				
				
				break;
			case WalkToDesertBank:
				
//open the door if it is closed
				WallObject door = (WallObject) objects.closest("Large door");
				
				if (door != null  && door.getOrientation() == 3)//closed is 3 on both parts of big door
					door.interact("Open");
				//TODO: don't let it leave this state until its down there
				rsleep(2000);
				if (door != null  && door.getOrientation() == 3)
					break;//reeeeeeeeeeee. probably relog idk this could mean someones shutting the door?
					//usually it just means it lagged and hasnt closed the door yet
				
				walking.walk(new Position(3292,3170,0));
				rsleep(1000);
				waitForMovements();
				walking.walk(new Position(3292,3177,0));
				rsleep(1000);
				waitForMovements();
				walking.walk(new Position(3279,3181,0));
				rsleep(1000);
				waitForMovements();
				
				
				
				
				
				//walk to a few waypoints
				
				//arrive at the bank
				
				//pass control to hide tanner thing
				
				buyingState = BUYINGMATERIALS.Done;
				
				break;
			case BuyingFromShoppingList:
				
				System.out.println(shoppingList);
				String itemName = "";
				
				//get 1 item name
				for (String s : shoppingList.keySet()) {
					itemName = s;
					break;
				}
				
				
				
				while (!bank.isOpen()) {
					bank.open();
					rsleep(1000);
				}
				bank.withdrawAll("Coins");
				rsleep(1000);
				
				Entity clerk = npcs.closest("Grand Exchange Clerk");
				if (clerk != null)
					clerk.interact("Exchange");
				
				
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
							buyingState = BUYINGMATERIALS.CheckingBankForItems;
							}
						}
						}
						
					}
					
				}
				
				
				
				break;
			case Done:
				master = CONTROLLERBOY.HIDESTATES;
				currentState = HIDESTATES.OpenBank;
				break;
			}
		else if (master == CONTROLLERBOY.HIDESTATES)
		switch (currentState) {
		case OpenBank:
			
			WallObject door = (WallObject) objects.closest("Large door");
			if (door != null && Math.sqrt(Math.pow(door.getX() - myPlayer().getX(),2) + 
						Math.pow(door.getY() - myPlayer().getY(),2) ) < 8)
			{
				master = CONTROLLERBOY.BUYINGMATERIALS;
				buyingState = BUYINGMATERIALS.WalkToDesertBank;
				if (door.getOrientation() == 3)//closed is 3 on both parts of big door
					door.interact("Open");
				break;
			}
			
			
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
			currentState = HIDESTATES.DepositHides;
			break;
		case DepositHides:
			//right click on hide and hit deposit all
			try{
			
			bank.depositAll();
			
			}catch(Exception e){log("error, nothing in slot 1 btw");};
			currentState = HIDESTATES.WithdrawHides;
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
				hideAmountLeft = (int)bank.getAmount("Green dragonhide") + (int)inventory.getAmount("Green dragonhide");
			potAmount = (int)bank.getAmount("Energy potion(4)");
			if (hideAmountLeft == 0) {
				currentState = HIDESTATES.ReturnToGE;
				break;
			}
			bank.withdrawAll("Green dragonhide");
			
			rsleep(500);
			currentState = HIDESTATES.CloseBank;
			break;
		case ReturnToGE:
			while(!bank.isOpen()) {
				bank.open();
				rsleep(500);
			}
			bank.depositAll();
			for (int i = 1; i <= 5; i++)
				bank.withdrawAll("Ring of wealth (" + i + ")");
			bank.close();
			
			for (int i = 1; i <= 5; i++)
			{
				if (inventory.contains("Ring of wealth (" + i + ")"))
				{
					inventory.getItem("Ring of wealth (" + i + ")").interact("Rub");
					if (WaitForWidget(219,0,2)) {
						rsleep(800);
						click(250,400);
						currentState = HIDESTATES.Done;
						break;
					}
				}
			}
			
			
			
			break;
		case Done:
			master = CONTROLLERBOY.BUYINGMATERIALS;
			buyingState = BUYINGMATERIALS.CheckingBankForItems;
			break;
		case CloseBank:
			bank.close();
			currentState = HIDESTATES.RunToTanner;
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
			int travelClickCount = 0;
			while (inventory.getItems()[1].hasAction("Drink")) {
			inventory.getItems()[1].interact("Drink");
			//and travel
			travelClickCount++;
			if (travelClickCount <= 2)
				click(658,133);
			rsleep(600);
			}
			//walk towards tanner
			walking.walk(new Position(3279,3191,0));
			
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(1500);
			}
			
			
			
			currentState = HIDESTATES.TradeWithTanner;
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
			
			
			currentState = HIDESTATES.TanAllHides;
			break;
		case TanAllHides:
			WaitForWidget(324,153);
			
			int RED = 154, BLUE = 153, GREEN = 152, BLACK = 107;
			if (widgets.get(324,GREEN) != null && widgets.get(324,GREEN).isVisible())
				{
				widgets.get(324,GREEN).interact("Tan All");
				hideAmountDone += 26;
				currentState = HIDESTATES.ReturnToBank;
				}
			else
			{
				currentState = HIDESTATES.TradeWithTanner;
			}
			openTannerDoor();
				
			break;
		case ReturnToBank:
			openTannerDoor();
			walking.walk(new Position(3278,3179,0));
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(50);
			}
			currentState = HIDESTATES.OpenBank;
			break;
			
		
		
		
		}
		
		
		return (int)(50*Math.random() + 50);
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
		p.write(lookupPrice("Green_dragonhide") + "\r\n");
		p.write(lookupPrice("Energy potion(4)") + "\r\n");
		p.write(lookupPrice("Green dragon leather") + "\r\n");
		p.write(lookupPrice("Amulet of glory(6)") + "\r\n");
		p.write(lookupPrice("Ring of wealth (5)") + "\r\n");
		p.close();
		
		}catch(Exception e){e.printStackTrace();}
		
double howmuchgold,costofhides,numberofbots;
Scanner scan = new Scanner(System.in);
System.out.println("how much gold u got (in millions pls)");


final double COST_1_HIDE = 1.05 * lookupPrice("Green_dragonhide");//1594;//use 1.05 times GE price
final double COST_1_POT = 1.05 * lookupPrice("Energy potion(4)");//474;//use 1.05 times GE price

howmuchgold = scan.nextDouble() * 1000000;

final double COST_TO_TAN_1_HIDE = 20;

double hides=0,pots=0;

hides = howmuchgold/(COST_1_HIDE + COST_1_POT/26);
pots = hides/26;

double remaindermoney = hides*20;





System.out.println(" buy HIDES:" + hides + ", POTS:" + pots);


		
		
		
	}

	
	
	
	
	
}
