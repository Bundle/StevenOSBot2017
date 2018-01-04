package bot.steven.circleboy;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "Circle Experiment", logo = "", name = "Circle Experiment", version = 0)
public class circleboy extends Script{

	
	
	
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
	private void click(double x, double y)
	{
		click((int)x,(int)y);
	}
	
	
	

	private int centerX = 644;
	private int centerY = 79;
	private int radius = 60;
	
	private void clickRandomBoy() {
		
		double angle = Math.PI*2*Math.random();
		anglepls = (int) angle;
		//at least half 
		double halfcx = Math.random()*0.5 + 0.5;
		
		click(centerX + radius * halfcx * Math.cos(angle), centerY + radius * halfcx * Math.sin(angle));
		
	}
	private int anglepls = 0;
	@Override
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.MAGENTA);
		
		g.drawString("circleboy: Moving at angle:" + anglepls,10,100);
		
		
	}
	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		
		
		clickRandomBoy();
		//Cx
		rsleep(4000);
		
		return 50 + 50*(int)(Math.random());
	}

}
