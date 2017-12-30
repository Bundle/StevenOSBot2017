package bot.steven.JoinCC;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "joins pinball boy clanchat", logo = "", name = "JoinCC", version = 0)
public class JoinCC  extends Script {

	enum JoinStates {
		clickCC,
		clickJoin,
		enterName,
		Done
	}
	
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
		g.setPaint(Color.cyan);
		g.drawString("name="+myPlayer().getName(),10,60);
		g.drawString("Joining Clan Chat...",10,80);
		g.drawString("state=" + joiner,10,100);
	}
	
	boolean first = true;
	
	JoinStates joiner = JoinStates.clickCC;
	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		
		switch (joiner) {
		case clickCC:
			click(541,482);
			joiner = JoinStates.clickJoin;
			break;
		case clickJoin:
			String text = widgets.get(7,17).getMessage();
			log("text is " + text);
			if (text.equals("Join Chat"))
			{
			
			
			click(590,445);
			joiner = JoinStates.enterName;
			}
			else
				joiner = JoinStates.Done;
			break;
		case enterName:
			keyboard.typeString("Pinball Boy");
			joiner = JoinStates.Done;
			break;
		case Done:
			//do nothing. should be in CC
			break;
		
		}
		
		
		return 1000;
	}

	
	
}
