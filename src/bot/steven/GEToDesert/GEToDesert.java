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
		coordinates.add(new Coordinate(3253,3334));
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
		
		
		
		//find the coorinate he is closest to , and start there
		
		setToClosestBTW();
		
	}
	private void setToClosestBTW()
	{
		double closest = Double.MAX_VALUE;
		int closestIndex = -1;
		for (int i = 0; i < coordinates.size(); i++)
		{
			Coordinate current = coordinates.get(i);
			if ((Math.sqrt(Math.pow(myPlayer().getX()-current.x,2)
					+Math.pow(myPlayer().getY()-current.y,2)) < closest))
			{
				closest = (Math.sqrt(Math.pow(myPlayer().getX()-current.x,2)
						+Math.pow(myPlayer().getY()-current.y,2)));
				closestIndex = i;
			}
		}
		
		currentCoordinateIndex = closestIndex;
		
	}
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.PINK);
		g.drawString("walking to desert Cx " + currentCoordinateIndex + "::" + 
				coordinates.get(currentCoordinateIndex).x + "," + 
				coordinates.get(currentCoordinateIndex).y, 10, 100);
	}
	
	private int currentCoordinateIndex = 0;
	private long timeLastMove = System.currentTimeMillis();
	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		long CT = System.currentTimeMillis();
		if (CT - timeLastMove > 6*1000) {
			setToClosestBTW();
			timeLastMove = CT;
		}
		Coordinate current = coordinates.get(currentCoordinateIndex);
		walking.walk(new Position(current.x,current.y,0));
		if (myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(50);
		}
		else{
			if (Math.sqrt(Math.pow(myPlayer().getX()-current.x,2)
					+Math.pow(myPlayer().getY()-current.y,2)) < 6)
				{timeLastMove = CT;currentCoordinateIndex ++;}
		}
		
		if (currentCoordinateIndex == coordinates.size())
		{
			log("all done walking to desert Cx");
			System.exit(0);
		}
		
		
		
		return 500;
	}
	
	
	
	
}
