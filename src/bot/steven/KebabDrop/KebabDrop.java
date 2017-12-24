package bot.steven.KebabDrop;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "drop kebabs btw", logo = "", name = "KebabDrop", version = 0)
public class KebabDrop extends Script{

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
	
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
