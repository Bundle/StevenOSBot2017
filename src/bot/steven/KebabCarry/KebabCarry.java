package bot.steven.KebabCarry;
import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.Chatbox;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import bot.steven.ChatCommands.ChatCommander;
import bot.steven.ChatCommands.ChatCommands;

@ScriptManifest(author = "Steven Ventura", info = "drop kebabs btw", logo = "", name = "KebabCarry", version = 0)
public class KebabCarry extends Script implements ChatCommands{
	ChatCommander commando = new ChatCommander(this);
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
	private void rightclick(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(RIGHTCLICK);
	}
	boolean first = true;
	
	
	enum KebabBoy {
		FixingInventory,
		OpeningBank,
		DepositingKebabs,
		ClosingBank,
		WalkingCloserToKebabGuy,
		InteractingWithKebabGuy
	}
	
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
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
	
	boolean inventoryIsFine() 
	{
		Item[] items = inventory.getItems();
		
		if (items[0] == null || !items[0].nameContains("Coins"))
			return false;
		
		for (int i = 1; i <= 27; i++)
		{
			if (items[i] != null && !items[i].nameContains("Kebab"))
			{
				return false;
			}
		}
		
		return true;
		
		
	}
	
	
	
	
	public void returnToNormalBehavior() {
		boy = KebabBoy.FixingInventory;
	}
	
	Entity Karim = null;
	KebabBoy boy = KebabBoy.FixingInventory;
	@Override
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
			commando.checkForInterruptText(message);
		
		
		
		
	}
	int kebabsBought = 0;
	int totalKebabs = 0;
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.GREEN);
		g.drawString("KebabsBought="+kebabsBought,10,80);
		g.drawString("TotalKebabs="+totalKebabs,10,100);
	}
	@Override
	public int onLoop() throws InterruptedException {
		
		
		
		boolean interruptNormalBehavior = commando.isInterrupting();
		
		
		log("KebabBoy is " + boy);
		
		if (commando.returnToNormalBehaviorFlag == true)
		{
			returnToNormalBehavior();
			commando.returnToNormalBehaviorFlag = false;
		}
		
		if (interruptNormalBehavior == true)
		{
			
			commando.doInterruptStuff();
		}
		else
		switch (boy) {		
		case FixingInventory:
			bank.open();
			waitForMovements();
			bank.depositAll();
			bank.withdrawAll("Coins");
			boy = KebabBoy.ClosingBank;
			break;
		case OpeningBank:
			walking.walk(new Position(3269,3167,0));
			waitForMovements();
			bank.open();
			waitForMovements();
			boy = KebabBoy.DepositingKebabs;
			break;
		case DepositingKebabs:
			rsleep(500);
			//log(inventory.getItems()[1].getName());
			totalKebabs = (int)bank.getAmount("Kebab");
			if (inventory.getItems()[1] != null && inventory.getItems()[1].nameContains("Kebab"))
				inventory.getItems()[1].interact("Deposit-All");
			
			//double check hes got the coins
			if (inventory.getItems()[0].nameContains("Coins"))
				boy = KebabBoy.ClosingBank;	
			else
				boy = KebabBoy.OpeningBank;
			
			break;
		case ClosingBank:
			bank.close();
			boy = KebabBoy.WalkingCloserToKebabGuy;
			break;
		case WalkingCloserToKebabGuy:
			walking.walk(new Position(3276,3172,0));
			rsleep(1500);
			
			boy = KebabBoy.InteractingWithKebabGuy;
			break;
		case InteractingWithKebabGuy:
			Karim = npcs.closest("Karim");
			
			
			if (Karim == null)
			{
				walking.walk(new Position(3269,3167,0));
				break;
			}
			else
				Karim.interact("Talk-To");
			
			boolean itsFine = inventoryIsFine();
			
			if (itsFine == false)
			{
				boy = KebabBoy.FixingInventory;
				break;
			}
			
			
			WaitForWidget(231,2);
			click(300,454);
			WaitForWidget(219,0,2);
			click(257,432);
			rsleep(10);
			WaitForWidget(217,2);
			click(202,449);
			kebabsBought++;
			
			
			
			//check if thing is full
			if (inventory.getItems()[26] != null && inventory.getItems()[26].nameContains("Kebab"))
			{
				boy = KebabBoy.OpeningBank;
			}
			else
			{
				boy = KebabBoy.InteractingWithKebabGuy;	
			}
			
			
			break;
		}
		
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
