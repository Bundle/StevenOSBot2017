package bot.steven.TutorialIsland;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.PrintWriter;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.InteractableObject;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "Steven's Tutorial Island", logo = "", name = "TutorialIsland", version = 0)
public class TutorialIsland extends Script{
enum States {
	RedGuy,
	Survival,
	WalkToCooking,
	Cooking,
	
};

public boolean inRectangle(int BLx, int BLy, int TRx, int TRy) {
	return (new Rectangle2D.Double(BLx,BLy,TRx-BLx,TRy-BLy).contains(myPlayer().getX(),myPlayer().getY()));
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
	rsleep(50);
}
private void rightclick(int x, int y)
{
	mouse.move(x,y);
	mouse.click(RIGHTCLICK);
	rsleep(50);
}
final int COMPLETED_BLUE = 8355839;
States currentState = States.RedGuy;
public void onStart() {
if (inRectangle(3091,3101,3097,3110))
	currentState = States.RedGuy;
else if (widgets.get(371,2).getTextColor() == COMPLETED_BLUE) {
	currentState = States.Survival;
}
else if (widgets.get(371,4).getTextColor() == COMPLETED_BLUE) {
	currentState = States.Cooking;
}


	
	
}
public void onPaint(Graphics2D g)
{
	g.setPaint(Color.GREEN);
	g.drawString("BotNumber="+getParameters(),10,60);
	g.drawString("name="+myPlayer().getName(),10,80);
	
	
	
		g.drawString("currentState=" + currentState,10,120);
	
	
}

@Override
public int onLoop() throws InterruptedException {

	switch (currentState) {
	case RedGuy:
		
		if (WaitForWidget(269,96)) {
			for (int y = 0; y < 7; y++){
				int randy = (int)(Math.random()*12) + 1;
				for (int c = 0; c < randy; c++)
					if (Math.random() > 0.25)
					click(164,90+(124-90)*y);
					else
						click(55,90+(124-90)*y);
				
			}
			for (int y = 0; y <= 5; y++) {
				int randy = (int)(Math.random()*12) + 1;
				for (int c = 0; c < randy; c++)
					if (Math.random() > 0.25)
						click(462,90+(124-90)*y);
					else
						click(352,90+(124-90)*y);
			}
			
			click (260,283);
		}
		
		Entity redGuy = npcs.closest("RuneScape Guide");
		if (redGuy == null)
			break;
		
		redGuy.interact("Talk-to");
		if (WaitForWidget(231,2)) {
			
				while(widgets.get(231,2) != null && widgets.get(231, 2).isVisible()
						||
						(widgets.get(229,1) != null && widgets.get(229,1).isVisible())
						||
						(widgets.get(219,0,3) != null && widgets.get(219,0,3).isVisible())
						||
						widgets.get(217,2) != null && widgets.get(217,2).isVisible())
				{
					click(329,451);
					click(225,450);
					rsleep(400);
				}
			
			
		}
		
		//if (WaitForWidget(548,41))//the options widget 
				{
					
					click(668,485);
					//shut off the fucking sound lol
					if (WaitForWidget(261,1,3))
					{
						click(627,224);
						if (WaitForWidget(261,24)) {
							click(608,286);
							rsleep(500);
							click(611,339);
							rsleep(500);
							click(611,384);
						}
					}
				}
				
		//try the door
		WallObject door = (WallObject) objects.closest("Door");
		if (door != null){
			door.interact("Open");
			rsleep(1200);
			waitForMovements();
			rsleep(400);
		}
		rsleep(1200);
		if (!inRectangle(3091,3104,3097,3110))
		{
			currentState = States.Survival;
		}
		
		
		
		
		
		
		break;
	case Survival:
		if (inRectangle(3091,3101,3097,3110))
			{currentState = States.RedGuy;break;}
		if (inventory.getAmount("Shrimps") >= 1) {
			 if (widgets.get(371,4).getTextColor() == COMPLETED_BLUE) {
					currentState = States.Cooking;
					break;
				}
			walking.walk(new Position(3091,3092,0));
			rsleep(1200);
			waitForMovements();
			WallObject gate = (WallObject)objects.closest("Gate");
			if (gate.interact("Open")) {
				currentState = States.WalkToCooking;
				rsleep(3000);
				break;
			}
		}
		Entity survivalExpert = npcs.closest("Survival Expert");
		if (survivalExpert == null)
			break;
		if (widgets.get(162, 34) != null)//close error message
		{
			click(274,428);
		}
		survivalExpert.interact("Talk-to");
		//get the tinderbox or the net
		if (WaitForWidget(231,2)) {
			while(widgets.get(231,2) != null && widgets.get(231, 2).isVisible())
			{
				click(329,451);
			}
		}
		if (widgets.get(162, 34) != null)//close error message
		{
			click(274,428);
		}
		if (inventory.getAmount("Raw shrimps") < 2 && 
				skills.getExperience(Skill.FISHING) < 20 && inventory.contains("Small fishing net")) {
			
			NPC fishingSpot = npcs.closest("Fishing spot");
			fishingSpot.interact("Net");
			
			
			
		}
		else if (skills.getExperience(Skill.FISHING) >= 20){
			//cook the fish then because we have at least 2
			log("COOK ON FIRE PLEASE");
			InteractableObject fire = (InteractableObject)objects.closest("Fire");
			if (fire != null)
			{
				click(645,183);
				rsleep(300);
				inventory.getItem("Raw shrimps").interact("Use");
				fire.interact("Use");
				rsleep(1200);
				waitForMovements();
			}
			
			
			
		}if (widgets.get(162, 34) != null)//close error message
		{
			click(274,428);
		}
		
		
		if (widgets.get(162, 34) != null)//close error message
		{
			click(274,428);
		}
		if (skills.getExperience(Skill.FIREMAKING) == 0) {
		
		if (inventory.contains("Logs"))
		{
			inventory.getItem("Tinderbox").interact("Use");
			inventory.getItem("Logs").interact("Use");
		}
		else if (inventory.contains("Tinderbox"))
		{
			//inventory icon
			click(642,181);
			Entity tree = objects.closest("Tree");
			if (tree != null)
				tree.interact("Chop down");
			
			WaitForWidget(193,3);//received a log
		}
		}
		else if (inventory.contains("Logs")) {
				inventory.getItem("Tinderbox").interact("Use");
				inventory.getItem("Logs").interact("Use");
		}
		
		if (widgets.get(162, 34) != null)//close error message
		{
			click(274,428);
		}
		
		//levels widget thing
		click(574,187);
		
		//loop back and talk to the expert again
		
		
		 if (widgets.get(371,4).getTextColor() == COMPLETED_BLUE) {
				currentState = States.Cooking;
			}
		
		break;
	case WalkToCooking:
		walking.walk(new Position(3079,3084,0));
		rsleep(1200);
		waitForMovements();
		WallObject door2 = (WallObject)objects.closest("Door");
		if (door2 != null)
		{
			if (door2.interact("Open"))
			{
				currentState = States.Cooking;
			}
		}
		break;
	
	case Cooking:
		createDoneFileAndExit();
		break;
	
		
		
	}
	
return 100;
}

private void createDoneFileAndExit() {
	try{
	File f = new File(getDirectoryData() + "\\" + getParameters() + ".tutDone");
	
	PrintWriter p = new PrintWriter(f);
	p.print(myPlayer().getName());
	p.print("\r\n");
	p.close();
	System.exit(0);
	
	}catch(Exception e){e.printStackTrace();}
	
}




}
