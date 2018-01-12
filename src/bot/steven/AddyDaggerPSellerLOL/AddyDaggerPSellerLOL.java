package bot.steven.AddyDaggerPSellerLOL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Steven Ventura", info = "Duel Arena Trainer", logo = "", name = "AddyDaggerPSellerLOL", version = 0)
public class AddyDaggerPSellerLOL extends Script{

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
	
	
	public void onPaint(Graphics2D g)
	{
		g.setPaint(new Color(0,188,0));
		g.drawString("buy addy P",10,80);
		g.drawString("state = " + currentState, 10, 100);
		g.drawString("trades received = " + tradesReceived, 10, 120);
		g.drawString("world = " + memberWorlds[currentWorldIndex],10,140);
	}
	
	ArrayList<Coordinate> locations = new ArrayList<>();
	class Coordinate {
		public Coordinate(int x, int y) {
			this.x = x; this.y = y;
		}
		public int x, y;
	}
	
	@Override
	public void onStart() {
		locations.add(new Coordinate(3165,3497));
				locations.add(new Coordinate(3169,3494));
						locations.add(new Coordinate(3171,3490));
								locations.add(new Coordinate(3170,3484));
										locations.add(new Coordinate(3165,3483));
												locations.add(new Coordinate(3160,3485));
														locations.add(new Coordinate(3157,3487));
																locations.add(new Coordinate(3158,3491));
																		locations.add(new Coordinate(3160,3493));
																				locations.add(new Coordinate(3161,3496));
	
final JFrame f = new JFrame("ADDY DAGGER SCAM LOL");
f.setSize(400,200);
JButton go = new JButton("GO");
f.setLayout(new FlowLayout());
go.setPreferredSize(new Dimension(80,80));
final JTextField world = new JTextField();
world.setPreferredSize(new Dimension(200,80));

go.addActionListener(new ActionListener() {
	
	public void actionPerformed(ActionEvent e) {
		
		//find the index in the array
		for (int i = 0; i < memberWorlds.length; i++)
		{
			if (memberWorlds[i] == Integer.parseInt(world.getText()))
			{
				currentWorldIndex = i;
				f.setVisible(false);
			}
		}
		
	}
	
});

f.add(world);
f.add(go);

f.setVisible(true);

																				
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
		while (widgets.get(arg1,arg2,arg3) == null || !widgets.get(arg1,arg2,arg3).isVisible()){
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	
	enum ScammerStates {
		AdvertisingDagger,
		HoppingWorlds
	}
	/*
	 * 
	*/
	
	private void waitForMovements()
	{
		rsleep(800);//in case they like click and then it has to wait before movement actually starts
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	
	int tradesReceived = 0;
	int tradeDump = 0;
	
	
	@Override
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		int tradesEverReceived = 0;
		if (message.getTypeId() == WHISPER) {
			//collect tears for meme
			ArrayList<String> tears = new ArrayList<>();
			
			try{
//				Scanner scan = new Scanner(new File(getDirectoryData() + "\\"  + "AddyDaggerTears.txt"));
				Scanner scan = new Scanner(new File(getDirectoryData() + "\\"  + "MindBombTears.txt"));
				tradesEverReceived = Integer.parseInt(scan.nextLine());
				while(scan.hasNextLine()) {
					tears.add(scan.nextLine());
				}
				
			}catch(Exception e){e.printStackTrace();}
			
			//now write to file
			try{
//				File tearBucket = new File(getDirectoryData() + "\\"  + "AddyDaggerTears.txt");
				File tearBucket = new File(getDirectoryData() + "\\"  + "MindBombTears.txt");
				PrintWriter p = new PrintWriter(tearBucket);
				p.write(tradesEverReceived + tradeDump + "\r\n");
				tradeDump = 0;//dump the trade counter into the file
				
				for (String cry : tears)
					p.write(cry + "\r\n");
				
				//now add the new tear
				p.write("[" + message.getUsername() + "]:" + text + "\r\n");
				p.close();
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		if (text.contains("wishes to trade with you.")) {
			//LOL
			tradesReceived++;
			tradeDump++;
			currentState = ScammerStates.HoppingWorlds;
			
			
			
			
			
		}
		
		
		
		
	}
	
	ScammerStates currentState = ScammerStates.AdvertisingDagger;
	
	int lapCount = 0;
	int currentLocationIndex = 0;
	int currentWorldIndex = -1;//wait for the jframe to set it
	
	private static final int[] memberWorlds = {
		2,3,4,5,6,7,
		9,10,11,12,13,14,15,17,18,19,
		20,21,22,23,24,25,27,28,29,
		30,31,32,33,34,36,37,38,39,
		40,41,42,43,44,46,47,48,/*49,*/50,
		51,52,/*53,*/54,55,56,
		57,
		58,59,60,/*61,*/62,65,/*66,*/67,68,69,
		70,/*73,*/74,75,76,77,78,
		86,87,88,89,90,/*91,*/92
	};
	
	public static final int WORLD_WIDGET_1 = 69, WORLD_WIDGET_2 = 14;
	
	@Override
	public int onLoop() throws InterruptedException {
		
		
		if (currentWorldIndex != -1)//until the JFrame has set the value
		switch(currentState) {
		
		case AdvertisingDagger:
			
			walking.walk(new Position(locations.get(currentLocationIndex).x,locations.get(
						currentLocationIndex).y,0));
			
			waitForMovements();
			
			currentLocationIndex++;
			if (currentLocationIndex == locations.size())
			{
				lapCount++;
				currentLocationIndex = 0;
				if (lapCount > 0)
				{
					currentState = ScammerStates.HoppingWorlds;
				}
				//hop worlds after 2 laps
			}
			
			//keyboard.typeString("green:buying all adamant dagger(p) 20k each [daily limit was 100]");
			//keyboard.typeString("cyan:Buying all Wizard's Mind Bomb , 200each (hit daily limit)");
			keyboard.typeString("flash3:buying all Dragonstone Bolts 1650 each");
			
			break;
		case HoppingWorlds:
			lapCount = 0;
			//time to hop worlds boys LOL
			currentWorldIndex++;
			if (currentWorldIndex == memberWorlds.length)
				currentWorldIndex = 0;
			
			//{ now hop worlds
			
			click(644,480);//click ("World Switcher")
			rsleep(50);
			click(724,216);//click on the X
			rsleep(1500);//wait
			
			if (WaitForWidget(182,1))
			{
				
				click(639,358);
				rsleep(200);
				if (WaitForWidget(WORLD_WIDGET_1,WORLD_WIDGET_2,301))
				{
					rsleep(1000);
					String stateData = "" + memberWorlds[currentWorldIndex];
					
					click(725,255+(int) (150D*((double)(Integer.parseInt(stateData)))/100D) );//moves the mouse bar thing to our world
					click(725,255+(int) (150D*((double)(Integer.parseInt(stateData)))/100D) );//moves the mouse bar thing to our world
					click(725,255+(int) (150D*((double)(Integer.parseInt(stateData)))/100D) );//moves the mouse bar thing to our world
					rsleep(1000);
					//now try to log into our world cx
					int world = Integer.parseInt(stateData);
					if (world < 300)
					{
						world = world + 300;
					}
					widgets.get(WORLD_WIDGET_1, WORLD_WIDGET_2,world)
						.interact("Switch");
					if (WaitForWidget(219,0,2))//the "switch to world u sure"? dialogue
					{
						
						widgets.get(219,0,2).interact("Continue");
					}
					
					currentState = ScammerStates.AdvertisingDagger;
				}
			}
			
			
			
			
			break;
		
		
		}
		
		
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
	
}
