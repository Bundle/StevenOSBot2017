package bot.steven.BlueDragonhides;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


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
		ReturnToBank
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
		Done
	}
	BUYINGMATERIALS buyingState = BUYINGMATERIALS.CheckingBankForItems;
	CONTROLLERBOY master = CONTROLLERBOY.BUYINGMATERIALS;
	
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
	private int PRICE_BUYING_HIDE = 0, PRICE_BUYING_POT = 0, PRICE_SELLING_HIDE;
	@Override
	public void onStart() {
		PRICE_BUYING_HIDE = (int) (1.05 * lookupPrice("Green_dragonhide"));//1594;//use 1.05 times GE price
		PRICE_BUYING_POT = (int) (1.05 * lookupPrice("Energy potion(4)"));//474;//use 1.05 times GE price
		PRICE_SELLING_HIDE = (int) (0.95 * lookupPrice("Green dragon leather"));
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
	
	private TreeSet<String> shoppingList = new TreeSet<String>();
	
	int hideAmountLeft = 0;
	int hideAmountDone = 0;
	int potAmount = 0;
	
	
	@Override
	public int onLoop() throws InterruptedException {
		
		if (master == CONTROLLERBOY.BUYINGMATERIALS)
			switch(buyingState) {
			case CheckingBankForItems:
				/*
				 * 
				 */
				long coins=0,greenDragonhides=0,energyPots=0,finishedHides=0;
				String[] itemsToCheck = {"Coins","Green dragonhide","Energy potion(4)",
							"Green dragon leather"};
				
				while (!bank.isOpen())
				{
					bank.open();
					rsleep(500);
				}
				
				coins = bank.getAmount(itemsToCheck[0]) + inventory.getAmount(itemsToCheck[0]);
				greenDragonhides = bank.getAmount(itemsToCheck[1]) + inventory.getAmount(itemsToCheck[1]);
				energyPots = bank.getAmount(itemsToCheck[2]) + inventory.getAmount(itemsToCheck[2]);
				finishedHides = bank.getAmount(itemsToCheck[3]) + inventory.getAmount(itemsToCheck[3]);
				
				if (finishedHides > 0) {
					buyingState = BUYINGMATERIALS.SellHides;
				}
				//evaluate: do we go to the desert, or do we buy more items?
				if (coins < greenDragonhides*20 || energyPots < ((float)(greenDragonhides))/(26f+2f))
				{
					buyingState = BUYINGMATERIALS.CheckForTeleportCharges;
				}
				
				
				
				break;
				
			case SellHides:
				
				break;
				
			case CheckForTeleportCharges:
				
				boolean hasNeck = equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(1)") ||
				equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(2)") ||
				equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(3)") || 
				equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(4)") || 
				equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(5)") ||
				equipment.isWearingItem(EquipmentSlot.AMULET,"Amulet of glory(6)");
				
				if (!hasNeck)
				{
					buyingState = BUYINGMATERIALS.BuyingFromShoppingList;
				}
				
				boolean hasRing = equipment.isWearingItem(EquipmentSlot.RING, "Ring of wealth(5)") ||
						equipment.isWearingItem(EquipmentSlot.RING, "Ring of wealth(4)") || 
						equipment.isWearingItem(EquipmentSlot.RING, "Ring of wealth(3)") || 
						equipment.isWearingItem(EquipmentSlot.RING, "Ring of wealth(2)") || 
						equipment.isWearingItem(EquipmentSlot.RING, "Ring of wealth(1)");
				if (!hasRing) {
					buyingState = BUYINGMATERIALS.BuyingFromShoppingList;
				}
				if (hasNeck && hasRing) {
					buyingState = BUYINGMATERIALS.TeleportToDesert;
				}
				
				
					
				break;
			case BuyingFromShoppingList:
				
				String itemName = "";
				
				
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
					click(59,145);
					if (WaitForWidget(465,24,21)) {
						click(113,117);
						keyboard.typeString("Green dragonhide");
						if (WaitForWidget(162,39,2)) {
						click(34,388);
						rsleep(3000);
						click(386,205);
						if (WaitForWidget(162,34)) {
							keyboard.typeString("" + PRICE_BUYING_HIDE); 
						}
						}
					}
					
				}
				
				
				
				break;
			case Done:
				master = CONTROLLERBOY.HIDESTATES;
				break;
			}
		else if (master == CONTROLLERBOY.HIDESTATES)
		switch (currentState) {
		case OpenBank:
			walking.walk(new Position(3278,3179,0));
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
			hideAmountLeft = (int)bank.getAmount("Green dragonhide");
			potAmount = (int)bank.getAmount("Energy potion(4)");
			bank.withdrawAll("Green dragonhide");
			
			rsleep(500);
			currentState = HIDESTATES.CloseBank;
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
				
			break;
		case ReturnToBank:
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
