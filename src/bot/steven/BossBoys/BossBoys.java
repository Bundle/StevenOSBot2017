package bot.steven.BossBoys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * BossBoys:
 * living the dream. a fully automated bot with interface to take , receive, and command Jug interaction.
 */
@ScriptManifest(author = "Steven Ventura", info = "My Water Boys", logo = "", name = "BossBoys", version = 0)
public class BossBoys extends Script{
	
	enum MASTERSTATES {
		Idle,
		Travelling,
		Distributing,
		Collecting,
		
	};
	
	//TODO: in the UI, prevent him from distributing and collecting if he is not in falador.
	enum DISTRIBUTING {
		PopulatingCustomerArray,
		 SendingTradeRequestToEach,
		 Done
	}
	DISTRIBUTING distributingState;
	
	enum COLLECTING {
		PopulatingCustomerArray,
		 SendingTradeRequestToEach,
		 Done
	}
	
	COLLECTING collectingState;
	
	MASTERSTATES master = MASTERSTATES.Idle;
	
	class Travelling {
		
		TravelNode currentDestination;
		TravelNode headGE;
		TravelNode tailFalador;
		
		boolean directionTowardsFalador;
		
		int[] xymashup = {3164, 3485,
				3164, 3474,
				3165, 3465,
				3162, 3458,
				3159, 3452,
				3147, 3442,
				3139, 3437,
				3134, 3426,
				3128, 3420,
				3121, 3417,
				3115, 3419,
				3108, 3420,
				3104, 3420,
				3096, 3420,
				3089, 3420,
				3083, 3419,
				3074, 3420,
				3066, 3418,
				3058, 3414,
				3054, 3412,
				3046, 3412,
				3042, 3414,
				3037, 3420,
				3031, 3424,
				3029, 3425,
				3023, 3425,
				3019, 3425,
				3014, 3427,
				3004, 3429,
				3000, 3429,
				2992, 3429,
				2988, 3424,
				2985, 3412,
				2977, 3403,
				2971, 3397,
				2967, 3395,
				2967, 3389,
				2963, 3383,
				2962, 3382,
				2958, 3382,
				2952, 3377,
				2949, 3374 };
		
		class TravelNode {
			TravelNode towardsFalador=null, towardsGE=null;
			private int x, y;
			boolean finalDestination;
			public TravelNode(int x, int y) {
				this.x=x;this.y=y;
				finalDestination=false;
			}
		}
		CITY destination;
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
	
	TreeMap<String,JugPlayer> playerboys;
	
	
	Stack<String> tradeboys;
	
	private void travelToCity(CITY city) {
		traveller = new Travelling(true);
		master = MASTERSTATES.Travelling;
		
		
	}
	enum CITY {
		Falador,
		GrandExchange
	};
	
	//the city of this bot
	enum CURRENTCITY {
		Unknown,
		Falador,
		GrandExchange,
		Transit
	}
	CURRENTCITY currentCity = CURRENTCITY.Unknown;
	
	private void printPlayerDataToFile() {
		//TODO: location,numEmptyJugs,numFullJugs
		
		try{
			File f = new File(getDirectoryData() + getParameters() + ".bossData");
			
			PrintWriter p = new PrintWriter(f);
			p.println(""+new Date());
			p.println(myPlayer().getName());
			//DateFormat.parse( );
			p.println(""+myPlayer().getX());
			p.println(""+myPlayer().getY());
			p.println(""+numEmptyJugs);
			p.println(""+numFullJugs);
			p.close();
			
			
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
		//the game messages
		if (text.contains("wishes to trade")) {
			String name = text.split(" ")[0];
			//dont allow duplicates
			if (!tradeboys.contains(name)) {
				if (playerboys.containsKey(name))
					tradeboys.push(name);
				
				
			}
			
		}
		
		//the whisper commands
		if (message.getTypeId() == CLANCHAT 
				|| message.getTypeId() == WHISPER) {
			
			String phrase = text.split(" ")[0];
			
			if (phrase == null)
				return;
			
			switch (phrase) {
			case "Travelfalador":
				break;
			case "Travelge":
				
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
		case Collecting:
			g.drawString("BossBoy=" + collectingState,10,60);
			break;
		case Distributing:
			g.drawString("BossBoy=" + distributingState,10,60);
			break;
		default:
			g.drawString("BossBoy=" + master,10,60);
			break;
		
		}
		
		g.drawString(""+numEmptyJugs,10,80);
		g.drawString(""+numFullJugs,10,100);
		
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
		case Travelling:
			//TODO: travel to next node until done
			stateMachineTravelling();
			//TODO: output the done signal
			break;
		case Distributing:
			stateMachineDistributing();
			break;
		case Collecting:
			stateMachineCollecting();
			break;
		
		
		}
		
		
		return 150;
	}
	
	enum TRAVELLINGS {
		FindStart,
		Travel,
		Done
	};
	TRAVELLINGS travellingState;
	
	
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
			//TODO pull up next waypoint
			
			
			break;
			
		case Done:
			master = MASTERSTATES.Idle;
			
			
			break;
		
		
		}
		
		
		
	}
	
	class JugPlayer {
		
		public JugPlayer(File f) {
			try{
			Scanner scan = new Scanner(f);
			this.postTime = DateFormat.getDateInstance().parse(scan.nextLine());
			this.name = scan.nextLine();
			this.x = Integer.parseInt(scan.nextLine());
			this.y = Integer.parseInt(scan.nextLine());
			this.numEmptyJugs = Integer.parseInt(scan.nextLine());
			this.numFullJugs = Integer.parseInt(scan.nextLine());
			scan.close();
			donetrading = false;
			}catch(Exception e) {e.printStackTrace();}
		}
		public void setTradeWith(boolean trademe) {
			
		}
		boolean donetrading = false;
		String name;
		Date postTime;
		int x,y;
		int numEmptyJugs;
		int numFullJugs;
		
	}
	
	private void populateboys() {
		//TODO: add criteria, like they only trade if they have above the threshold of items.
		try{
			File dir = new File(".");
			File [] files = dir.listFiles(new FilenameFilter() {
			    @Override
			    public boolean accept(File dir, String name) {
			        return name.endsWith(".jugData");
			    }
			});

			for (File f : files) {
			    JugPlayer p = new JugPlayer(f);
			    if (Math.abs(new Date().getTime() - p.postTime.getTime() )
			    		< 50*1000) {
			    	log("detected file," + f.getName() + ",created within the last 50 seconds.");
			    	playerboys.put(p.name, p);
			    }
			}
		}catch(Exception e){e.printStackTrace();}

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
	private void rsleep(long millis)
	{
		try{
			Thread.sleep(millis);
		}catch(Exception e){};
	}
	final boolean LEFTCLICK = false, RIGHTCLICK = true;
	
	
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
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	
	String currentTradeBoy = null;
	private void stateMachineCollecting() {
		
		switch(collectingState) {
		case PopulatingCustomerArray:
			//TODO: read from file to see who has stuff ready for me right now
			playerboys = new TreeMap<>();
			tradeboys = new Stack<>();//this stack is populated in the onMessage() thing
			currentTradeBoy = null;
			populateboys();
			collectingState = COLLECTING.SendingTradeRequestToEach;
			break;
		case SendingTradeRequestToEach:
			//TODO: cycle through each person and go through with trade request
			if (!tradeboys.isEmpty()) {
				if (currentTradeBoy == null)
					currentTradeBoy = tradeboys.pop();
			}
			else
			{
				collectingState = COLLECTING.Done;
				break;
			}
			
			//TODO: put this new version of widget handling in jugboys
			Entity tradeboy = players.closest(currentTradeBoy);
			if (tradeboy != null)
				{
					if ( (widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false)
							&& 
						 (widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)) {
				tradeboy.interact("Trade with");
				rsleep(1000);
					}
				
				//trade screen 1 widget is 335,25
					//trade screen 2 widget is 334,13
				if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
						|| WaitForWidget(335,25)) {
					//wait a second for him to put the goods up
					rsleep(2000);
					//press accept
					click(264,180);
					//wait for him to accept
					if (WaitForWidget(334,13)) {
					//press accept again
					click(215,303);
					//successful trade!
					currentTradeBoy = null;
					}
				}
			}
			
			
			
			
			
			break;
			
		
		case Done:
				master = MASTERSTATES.Idle;
			break;
		}
		
		
	}
	private void stateMachineDistributing() {
		//TODO: add the state where i put the jugs in my inventory and then bring them to falador.
		switch(distributingState) {
		case PopulatingCustomerArray:
			playerboys = new TreeMap<>();
			tradeboys = new Stack<>();//this stack is populated in the onMessage() thing
			currentTradeBoy = null;
			populateboys();
			
			//TODO: read from file to see who is available , and who needs jugs, and how many to give
			distributingState = DISTRIBUTING.SendingTradeRequestToEach;
			break;
		case SendingTradeRequestToEach:
			//cycle through each person and go through with trade request
			if (!tradeboys.isEmpty()) {
				if (currentTradeBoy == null)
					currentTradeBoy = tradeboys.pop();
			}
			else
			{
				distributingState = DISTRIBUTING.Done;
				break;
			}
			
			//TODO: put this new version of widget handling in jugboys
			Entity tradeboy = players.closest(currentTradeBoy);
			if (tradeboy != null)
				{
					//this if statement is here to skip ahead if he is already in another window
					if ( (widgets.get(335,25) == null || widgets.get(335,25).isVisible() == false)
							&& 
						 (widgets.get(334,13) == null || widgets.get(334,13).isVisible() == false)) {
				tradeboy.interact("Trade with");
				rsleep(1000);
					}
				
				//trade screen 1 widget is 335,25
					//trade screen 2 widget is 334,13
				if  ((widgets.get(334,13) != null && widgets.get(334,13).isVisible())
						|| WaitForWidget(335,25)) {
					//offer my item
					inventory.interact("Offer-All", "Jug");
					//press accept
					click(264,180);
					//wait for him to accept
					if (WaitForWidget(334,13)) {
					//press accept again
					click(215,303);
					//successful trade!
					currentTradeBoy = null;
					}
				}
			}
			
			break;
		case Done:
				master = MASTERSTATES.Idle;
			break;
		}
		
		
	}
	
	
	
	
	
}
