package bot.steven.CopyCatCx;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.input.keyboard.TypeStringEvent;

@ScriptManifest(author = "Steven Ventura", info = "copy text btw", logo = "", name = "COPYCATCX", version = 0)
public class CopyCatCx extends Script{

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
	
	@Override
	public int onLoop() throws InterruptedException {
	
		if (first) {
		TypeStringEvent c = new TypeStringEvent("glow2:shake:heyoøguys,øamordeusøhere");
		c.execute();
		
		rsleep(500);first=false;}
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
