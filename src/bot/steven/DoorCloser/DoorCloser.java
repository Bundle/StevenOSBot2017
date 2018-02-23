package bot.steven.DoorCloser;
import java.util.ArrayList;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * TODO: make dank memes for them to type
 * TODO: upon starting program, determine if they are within range of the doors. change starting state from this.
 * TODO: make several bots, determine just how many i need to keep the door perma-shut
 * TODO: name the bots Gangsthurh1, Gangsthurh2, Gangsthurh3, Gangsthurh4, Gangsthurh5,
 */
@ScriptManifest(author = "Steven Ventura", info = "Take Turns closing doors", logo = "", name = "DoorCloser", version = 0)
public class DoorCloser extends Script{

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
	
	enum DOORCLOSERSTATES {
		FindWalkToDesertVertex,
		WalkingToDesert,
		ClosingDoors
	};
	DOORCLOSERSTATES doorCloseBoy;//assign this in OnStart()
	
	
	
	/*enum DoorStates  {
			ClosingDoor1,
			ClosingDoor2,
			ClosingDoor3,
			WaitingForMyTurn
	}
	
	
	public int getMyTurnNumber()
	{
		switch(myPlayer().getName()) {
		
		case ("Naruse"):
		return 0;
		case ("Utaite Reol"):
			return 1;
		case("iru beru"):
			return 2;
		case ("K r a d ness"):
			return 3;
		}
		return -1;
		
		
	}*/
	private void waitForMovements()
	{
		while(myPlayer().isAnimating() || myPlayer().isMoving())
		{
			rsleep(500);
		}
	}
	public final static int ORIENTATION_CLOSED = 0, ORIENTATION_OPEN = 1;
	//DoorStates state = DoorStates.ClosingDoor1;
	
	private ArrayList<String> autismMessages = new ArrayList<String>();
	
	@Override
	public void onStart() {
		///*
		//i bet your parents hate you lmao
		
		////////////////////
		autismMessages.add("report the trolls");
				autismMessages.add("stop...");
						autismMessages.add("stop with the door");
						autismMessages.add("report phanypants1");
								autismMessages.add("phancy stop");
										autismMessages.add("wtf i cant get in");
												autismMessages.add("wtf i cant get out");
														autismMessages.add("who is closing the door lel");
																autismMessages.add("this is funny lul");
   autismMessages.add("wtfffffff");
   autismMessages.add("gee dangit i cant click fast enough");
   autismMessages.add("lul the bots broke the furnace door");
   autismMessages.add("wheres the p mod?");
   autismMessages.add("Screw u man");
   autismMessages.add("the bots broke the door xd");
 
   autismMessages.add("................");
   autismMessages.add("really??");
   autismMessages.add("ok whos doin this?");
   autismMessages.add("who doing this ya'll?");
   
   //just always start walking to desert lol
  // if (myPlayer().getX() < 3267 || myPlayer().getY()  > 3196)
	   doorCloseBoy = DOORCLOSERSTATES.FindWalkToDesertVertex;
   //else
	//   doorCloseBoy = DOORCLOSERSTATES.ClosingDoors;
	}
	
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		
		//if (message )
		if (message.getTypeId() == CLANCHAT || message.getTypeId() == WHISPER) {
			
			
		}
		String text = message.getMessage();
	}
	/*
	 * to keep track of turns:
	 * 
	 */
	
	int slowdown = (int)(Math.random()*100);
	int currentLocationSpawn;
	@Override
	public int onLoop() throws InterruptedException {
	
		switch (doorCloseBoy) {
		case ClosingDoors:
		//ava.lang.ClassCastException: org.osbot.rs07.api.model.WallObject cannot be cast to org.osbot.rs07.api.model.InteractableObject
slowdown++;
if (slowdown > 100)
{
	slowdown = 0;
	//keyboard.typeString(autismMessages.get((int)(autismMessages.size()*Math.random())));
}
//3280,3185
if (myPlayer().getX() != 3280 || myPlayer().getY() != 3185) {
	walking.walk(new Position(3280,3185,0));
}
waitForMovements();
		
		WallObject door = (WallObject)objects.closest("Door");
//WallObject door = (WallObject)objects.closest("Large door");
		
		if (door != null)
		{
			if (door.getOrientation() == ORIENTATION_OPEN)
			{
				door.interact("Close");
			}
			
			
		}
		break;
		case FindWalkToDesertVertex:
			int closest = -1;
			double closestdistance = Double.MAX_VALUE;
			for (int i = 0; i < walkycoordsSpawn.length; i++) {
				double pls = Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsSpawn[i][0],2)+Math.pow(
						myPlayer().getY()-walkycoordsSpawn[i][1],2));
				if (pls < closestdistance)
				{
				closestdistance = pls;
				closest = i;
				}
			}
			currentLocationSpawn = closest;
			doorCloseBoy = DOORCLOSERSTATES.WalkingToDesert;
			break;
		case WalkingToDesert:
			walking.walk(new Position(walkycoordsSpawn[currentLocationSpawn][0],
					walkycoordsSpawn[currentLocationSpawn][1],
					0));
waitForMovements();

double pls = 
		Math.sqrt(Math.pow(myPlayer().getX()-walkycoordsSpawn[currentLocationSpawn][0],2) + 
				Math.pow(myPlayer().getY()-walkycoordsSpawn[currentLocationSpawn][1],2));
		
if (pls < 4)
	currentLocationSpawn++;
else
	log("uhh he aint movin xD");


if (currentLocationSpawn == walkycoordsSpawn.length) {
	doorCloseBoy = DOORCLOSERSTATES.ClosingDoors;
	//correct his position rq
	
}

			
		break;
		}
		
		return (int)(50*Math.random() + 50);
	}

	
	//walks from the tutorialboy to the desert bank
	int[][] walkycoordsSpawn = {{3232, 3230},
			{3237, 3226},
			{3245, 3226},
			{3248, 3225},
			{3254, 3226},
			{3259, 3232},
			{3259, 3238},
			{3259, 3243},
			{3256, 3249},
			{3252, 3253},
			{3250, 3255},
			{3250, 3263},
			{3247, 3271},
			{3246, 3273},
			{3241, 3279},
			{3239, 3283},
			{3239, 3289},
			{3239, 3290},
			{3239, 3298},
			{3243, 3308},
			{3247, 3316},
			{3255, 3322},
			{3265, 3325},
			{3273, 3330},
			{3279, 3324},
			{3277, 3318},
			{3278, 3311},
			{3278, 3305},
			{3278, 3297},
			{3275, 3293},
			{3275, 3287},
			{3275, 3279},
			{3275, 3275},
			{3275, 3267},
			{3275, 3261},
			{3275, 3255},
			{3275, 3243},//TODO: end at closing the door lol
			{3275, 3236},
			{3275, 3226},
			{3275, 3220},
			{3274, 3211},
			{3273, 3203},
			{3272, 3198},
			{3279, 3193},
			{3280, 3183},
			{3276, 3180},
			{3275, 3175}};

	
	
}
