package bot.steven.GEToDesert;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "walks to desert Cx", logo = "", name = "GEToDesert", version = 0)

public class GEToDesert extends Script{

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
	class Coordinate {
		public Coordinate(int x, int y) {
			this.x = x; this.y = y;
		}
		public int x, y;
	}
	ArrayList<Coordinate> coordinates = new ArrayList<>();
	public void onStart() {
		coordinates.add(new Coordinate(3166,3480));
		coordinates.add(new Coordinate(3166,3464));
		coordinates.add(new Coordinate(3178,3454));
		coordinates.add(new Coordinate(3192,3446));
		coordinates.add(new Coordinate(3197,3431));
		coordinates.add(new Coordinate(3210,3421));
		coordinates.add(new Coordinate(3211,3403));
		coordinates.add(new Coordinate(3211,3386));
		coordinates.add(new Coordinate(3213,3371));
		coordinates.add(new Coordinate(3223,3359));
		coordinates.add(new Coordinate(3226,3342));
		coordinates.add(new Coordinate(3242,3338));
		coordinates.add(new Coordinate(3260,3335));
		coordinates.add(new Coordinate(3275,3328));
		coordinates.add(new Coordinate(3276,3309));
		coordinates.add(new Coordinate(3276,3291));
		coordinates.add(new Coordinate(3276,3274));
		coordinates.add(new Coordinate(3275,3256));
		coordinates.add(new Coordinate(3276,3239));
		coordinates.add(new Coordinate(3277,3221));
		coordinates.add(new Coordinate(3277,3203));
		coordinates.add(new Coordinate(3281,3186));
		coordinates.add(new Coordinate(3277,3169));
		
		
	}
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.PINK);
		g.drawString("walking to desert Cx " + currentCoordinateIndex + 
				coordinates.get(currentCoordinateIndex).x + "," + 
				coordinates.get(currentCoordinateIndex).y, 10, 100);
	}
	
	private int currentCoordinateIndex = -1;
	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		currentCoordinateIndex ++;
		Coordinate current = coordinates.get(currentCoordinateIndex);
		walking.walk(new Position(current.x,current.y,0));
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(50);
		}
		
		return 500;
	}
	
	
	
	
}
