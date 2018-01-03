package bot.steven.bodies;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Spells.NormalSpells;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Steven Ventura", info = "bodies", logo = "", name = "bodies", version = 0)
public class bodies extends Script{

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
		g.setPaint(Color.ORANGE);
		
	
		
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		
		Entity boy = npcs.closest("Rat");
		if (boy != null) {
			magic.castSpellOnEntity(NormalSpells.CURSE,boy);
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
