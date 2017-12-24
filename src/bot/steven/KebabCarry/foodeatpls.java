package bot.steven.KebabCarry;

import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class foodeatpls {
	public foodeatpls()
	{
		
	}
	public void begin()
	{
		
		Robot r = null;
		try{
			r = new Robot();
			while(true){
			Color c = r.getPixelColor(619,91);
			if (c.equals(new Color(19,19,19)))
			{
				r.keyPress(KeyEvent.VK_E);
				Thread.sleep(30);
				r.keyRelease(KeyEvent.VK_E);
			}
			Thread.sleep(1600);
			}
			
		}
		catch(Exception e) {};
	}
public static void main(String[]args)
{
	foodeatpls f = new foodeatpls();
	f.begin();
}
}
