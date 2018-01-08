package bot.steven.BeerBuyer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.InteractableObject;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import bot.steven.ChatCommands.ChatCommander;
import bot.steven.ChatCommands.ChatCommands;

/*
 * Wizard Mind Bombs takes 2 minutes and 15 seconds
 */

@ScriptManifest(author = "Steven Ventura", info = "wizard mind bomb", logo = "", name = "BeerBuyer", version = 0)
public class BeerBuyer extends Script implements ChatCommands{
	ChatCommander commando = new ChatCommander(this,getParameters());
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
	
	
	enum WizardBoy {
		FixingInventory,
		OpeningBank,
		DepositingWizards,
		ClosingBank,
		WalkingCloserToWizardGuy,
		InteractingWithWizardGuy
	}
	
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
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
	
	boolean inventoryIsFine() 
	{
		Item[] items = inventory.getItems();
		
		if (items[0] == null || !items[0].nameContains("Coins"))
			return false;
		
		for (int i = 1; i <= 27; i++)
		{
			if (items[i] != null && !items[i].getName().contains("bomb"))
			{
				log("Inventory broke 2ee: " + items[i].getName());
				return false;
			}
		}
		
		return true;
		
		
	}
	
	
	
	
	public void returnToNormalBehavior() {
		boy = WizardBoy.FixingInventory;
	}
	
	Entity Kaylee = null;
	WizardBoy boy = WizardBoy.FixingInventory;
	@Override
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
			commando.checkForInterruptText(message,this);
		
		
		
		
	}
	public void reportStatus()
	{
		keyboard.typeString("//Wizards= " + totalWizards + ",coins= " + currentCoins);
	}
	int WizardsBought = 0;
	int totalWizards = 0;
	int currentCoins = 0;
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.CYAN);
		g.drawString("#=" + getParameters(),10,40);
		g.drawString("name="+myPlayer().getName(),10,60);
		g.drawString("WizardsBought="+WizardsBought,10,80);
		g.drawString("TotalWizards="+totalWizards,10,100);
		
		boolean interruptNormalBehavior = commando.isInterrupting();
		if (interruptNormalBehavior == false)
			g.drawString("WizardBoy is " + boy,10,120);
		else
			g.drawString("interrupt: doing " + commando.commandState,10,120);
		
	}
	
	@Override
	public void onStart()
	{
		/*
		 * create the file Cx
		 */
		try{
			File f = new File(getDirectoryData() + "\\" + getParameters() + ".bot");
			log("creating file " + getDirectoryData() + "\\" + getParameters() + ".bot");
			f.deleteOnExit();
		PrintWriter out = new PrintWriter(f);
		out.println(myPlayer().getName());
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	public void onExit()
	{
		/*
		 * delete the file Cx
		 */
		/*try{
		File f = new File(getDirectoryData() + "\\" + getParameters() + ".bot");
		f.delete();
		
		}catch(Exception e){e.printStackTrace();}*/
	}
	private void fixIfUpstairs() {
		if (myPlayer().getZ() == 1) {
			log("god damn it hes upstairs");
			
			InteractableObject fuckingStairs = (InteractableObject) objects.closest("Staircase"); 
			fuckingStairs.interact("Climb-down");
			rsleep(1000);
		}
		
		
	}
	@Override
	public int onLoop() throws InterruptedException {
		 
		fixIfUpstairs();
		
		boolean interruptNormalBehavior = commando.isInterrupting();
		
		
		
		
		if (interruptNormalBehavior == false)
			log("WizardBoy is " + boy);
		else
			log("interrupt: doing " + commando.commandState);
		
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
			rsleep(50);
			bank.withdrawAll("Coins");
			boy = WizardBoy.ClosingBank;
			break;
		case OpeningBank:
			walking.walk(new Position(3269,3167,0));
			waitForMovements();
			bank.open();
			waitForMovements();
			boy = WizardBoy.DepositingWizards;
			break;
		case DepositingWizards:
			rsleep(500);
			//log(inventory.getItems()[1].getName());
			totalWizards = (int)bank.getAmount("Wizard's mind bomb");
			currentCoins = (int)inventory.getAmount("Coins");
			if (inventory.getItems()[1] != null)// && inventory.getItems()[1].nameContains("Wizard"))
				inventory.getItems()[1].interact("Deposit-All");
			
			//double check hes got the coins
			if (inventory.getItems()[0].nameContains("Coins"))
				boy = WizardBoy.ClosingBank;	
			else
				boy = WizardBoy.OpeningBank;
			
			break;
		case ClosingBank:
			bank.close();
			boy = WizardBoy.WalkingCloserToWizardGuy;
			break;
		case WalkingCloserToWizardGuy:
			walking.walk(new Position(2952,3378,0));
			rsleep(1500);
			
			boy = WizardBoy.InteractingWithWizardGuy;
			break;
		case InteractingWithWizardGuy:
			Kaylee = npcs.closest("Kaylee");
			
			
			if (Kaylee == null)
			{
				walking.walk(new Position(2952,3378,0));
				break;
			}
			else
				Kaylee.interact("Talk-To");
			rsleep(50);//for reliability
			
			boolean itsFine = inventoryIsFine();
			
			if (itsFine == false)
			{
				boy = WizardBoy.FixingInventory;
				break;
			}
			
			
			if (WaitForWidget(231,2)) {
			click(300,454);
			rsleep(10);
			if (WaitForWidget(217,2)) {
			click(300,454);
			rsleep(10);
			if (WaitForWidget(231,2)) {
			rsleep(10);
			click(300,454);
			if (WaitForWidget(219,0,2)) {
			click(258,409);
			if (WaitForWidget(217,2)) {
				click(300,454);
			WizardsBought++;
			}
			}
			}
			}
			}
			rsleep(250);
			
			
			
			
			//check if thing is full
			if (inventory.getItems()[26] != null)// && inventory.getItems()[2].getName().contains("bomb"))
			{
				boy = WizardBoy.OpeningBank;
			}
			else
			{
				boy = WizardBoy.InteractingWithWizardGuy;	
			}
			
			
			break;
		}
		
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
