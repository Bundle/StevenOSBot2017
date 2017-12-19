package bot.steven.dropparty;

import org.osbot.rs07.api.GroundItems;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Steven Ventura", info = "Tan Blue Dragonhides", logo = "", name = "BlueDragonhides", version = 0)
public class dropparty extends Script{

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
	int totalJugsFilled = 0;
	
	
	@Override
	public int onLoop() throws InterruptedException {
		
		for (GroundItem g : groundItems.getAll())
		{
			g.interact("Pick Up");
		}
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
