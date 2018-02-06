package bot.steven.Hides;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeMap;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * DONETODO: open hide tanner guy door if closed
 * DONETODO: set player X,Y coordinate master control value
 * DONETODO: change hide value to normal hides
 * DONETODO: create path from lumbridge to desert
 * DONETODO: walk along the path state machine
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
		WALKINGTODESERT
	}
	enum WALKINGTODESERT {
		FindingLocation,
		WalkingToDesert,
		Done
	}
	WALKINGTODESERT walkingState;
	
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
	HIDESTATES currentState = HIDESTATES.OpenBank;

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
	private boolean finishAfterThis = false;
	
	@Override
	public void onStart() {
		
		stevenbuttons.add(new StevenButton("Finish Up",10,440) {
			public void onStevenClick() {
				finishAfterThis = !finishAfterThis;
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
		
		
		//set player X,Y coordinate master control value and sub control values initialize
		if (myPlayer().getX() > 3267 && myPlayer().getY() < 3196) {
			master = CONTROLLERBOY.HIDESTATES;
			currentState = HIDESTATES.OpenBank;
		}
		else
		{
			master = CONTROLLERBOY.WALKINGTODESERT;	
			walkingState = WALKINGTODESERT.FindingLocation;
		}
		
		
	}
	public void onPaint(Graphics2D g)
	{
		for (StevenButton b : stevenbuttons)
		{
			this.draw(g,b);
		}
		
		g.setPaint(Color.CYAN);
		if (master == CONTROLLERBOY.WALKINGTODESERT) 
			g.drawString("HIDEBOT: interrupt: " + walkingState,10,60);
		else if (master == CONTROLLERBOY.HIDESTATES)
			g.drawString("HIDEBOT: currentState is " + currentState,10,60);
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
		switch (currentState) {
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
				hideAmountLeft = (int)bank.getAmount("Cowhide") + (int)inventory.getAmount("Cowhide");
			if (hideAmountLeft == 0) {
				//TODO: heyo guys, amordeus here
				break;
			}
			bank.withdrawAll("Cowhide");
			
			rsleep(500);
			currentState = HIDESTATES.CloseBank;
			break;
		
		case Done:
			
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
			
			int SOFT = 148, HARD = 149,
			RED = 154, BLUE = 153, GREEN = 152, BLACK = 107;
			if (widgets.get(324,SOFT) != null && widgets.get(324,SOFT).isVisible())
				{
				widgets.get(324,SOFT).interact("Tan All");
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
			//if stuck in tanning room, then return to "ReturnToBank" state so we can open door.
			openTannerDoor();
			walking.walk(new Position(3278,3179,0));
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(50);
			}
			
			if (myPlayer().getY() < 3190-1)//otherwise he is trapped in the room and needs to try the door again. 
				currentState = HIDESTATES.OpenBank;
			break;
			
		
		
		
		}
			else if (master == CONTROLLERBOY.WALKINGTODESERT) 
				switch(walkingState) {
				case FindingLocation:
					int closest = -1;
					double closestdistance = Double.MAX_VALUE;
					for (int i = 0; i < walkycoords.length; i++) {
						double pls = Math.sqrt(Math.pow(myPlayer().getX()-walkycoords[i][0],2)+Math.pow(
								myPlayer().getY()-walkycoords[i][1],2));
						if (pls < closestdistance)
						{
						closestdistance = pls;
						closest = i;
						}
					}
					currentLocation = closest;
					walkingState = WALKINGTODESERT.WalkingToDesert;
					break;
				case WalkingToDesert:
					
					walking.walk(new Position(walkycoords[currentLocation][0],
										walkycoords[currentLocation][1],
										0));
					rsleep(1000);
					waitForMovements();
					
					double pls = 
							Math.sqrt(Math.pow(myPlayer().getX()-walkycoords[currentLocation][0],2) + 
									Math.pow(myPlayer().getY()-walkycoords[currentLocation][1],2));
							
					if (pls < 4)
						currentLocation++;
					else
						log("uhh he aint movin xD");
					
					
					if (currentLocation == walkycoords.length) {
						walkingState = WALKINGTODESERT.Done;
					}
					
					break;
				case Done:
					//begin tanning i guess
					master = CONTROLLERBOY.HIDESTATES;
					currentState = HIDESTATES.OpenBank;
					break;
				
				
				
				}
				
		
		
		return (int)(50*Math.random() + 50);
	}
	int currentLocation = -1;
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
	
	int[][] walkycoords = {{3232, 3230},
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

	
	
	
	
}
