package bot.steven.BlueDragonhides;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
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
	//bi = new BufferedImage(getSize().width,getSize().height, 5);
	private int getExtraPotAmount()
	{
		return potAmount - (int)hideAmountLeft/26;
	}
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.CYAN);
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
	int hideAmountLeft = 0;
	int hideAmountDone = 0;
	int potAmount = 0;
	@Override
	public int onLoop() throws InterruptedException {
		log("currentState is " + currentState);
		
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
	private void WaitForWidget (int arg1, int arg2)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2) == null || !widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return;
			rsleep(100);
		}
	}
	private void WaitForWidget (int arg1, int arg2, int arg3)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2,arg3) == null || !widgets.get(arg1,arg2,arg3).isVisible()){
			loops++;
			if (loops > 80)
				return;
			rsleep(100);
		}
	}

	
	
	
	
	
}
