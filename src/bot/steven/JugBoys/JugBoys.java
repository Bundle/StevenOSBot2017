package bot.steven.JugBoys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import bot.steven.ChatCommands.ChatCommander.CommandStates;

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
	
	enum TRAVELTOFALADOR {
		
	};
	TRAVELTOFALADOR travelToFaladorState;
	enum FILLJUGS {
		
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
	private void rightclick(int x, int y)
	{
		mouse.move(x,y);
		mouse.click(RIGHTCLICK);
	}
	
	private void printPlayerDataToFile() {
		//TODO: location,numEmptyJugs,numFullJugs
		
		try{
			File f = new File(getDirectoryData() + getParameters() + ".jugData");
			
			PrintWriter p = new PrintWriter(f);
			
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
			
			switch (phrase) {
			case "Falador":
				master = MASTERSTATES.TravelToFalador;
				break;
				
			case "Fill":
				master = MASTERSTATES.FillJugs;
				break;
				
			case "Give":
				master = MASTERSTATES.GiveFullJugs;
				break;
				
			case "Take":
				stateData1 = message.getUsername();//name
				//stateData2 is unused
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
			g.drawString("JugBoy=" + travelToFaladorState,10,60);
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


	TravellingToFalador traveller = null;
	
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
			
			break;
		case FillJugs:
			
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
				rsleep(400);
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
			//wait for widget
			//take the items from him
			if  (WaitForWidget(335,25)) {
			//offer nothing
			//press accept
			click(264,180);
			//wait for him to accept
			if (WaitForWidget(334,13)) {
			click(215,303);
			//WARNING: possible for bot to stall here ?
			WaitForWidgetToDisappear(334,13);
			//success so go back to farming
			takeEmptyJugsState = TAKEEMPTYJUGS.Done;
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
			String text = widgets.get(7,17).getMessage();
			log("text is " + text);
			if (text.equals("Join Chat"))
			{
			
			
			click(590,445);
			joiner = JOINSTATES.enterName;
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
			if (widgets.get(162,17).getMessage().equals("<col=ffff00>Friends")) {
				widgets.get(162,17).interact("<col=ffff00>Private:</col> Show all");
			}
				
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
	
	
}
