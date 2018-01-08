package bot.steven.DoorCloser;
import java.util.ArrayList;

import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

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
	
	enum DoorStates  {
			ClosingDoor1,
			ClosingDoor2,
			ClosingDoor3,
			WaitingForMyTurn
	}
	/*
	 * i am 1
	 * i am 2
	 * i am 3
	 * i am 4
	 * 
	 */
	
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
		
		
	}
	public final static int ORIENTATION_CLOSED = 0, ORIENTATION_OPEN = 1;
	DoorStates state = DoorStates.ClosingDoor1;
	
	private ArrayList<String> autismMessages = new ArrayList<String>();
	
	@Override
	public void onStart() {
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
	
	@Override
	public int onLoop() throws InterruptedException {
	
		//ava.lang.ClassCastException: org.osbot.rs07.api.model.WallObject cannot be cast to org.osbot.rs07.api.model.InteractableObject
slowdown++;
if (slowdown > 100)
{
	slowdown = 0;
	keyboard.typeString(autismMessages.get((int)(autismMessages.size()*Math.random())));
}
		
		WallObject door = (WallObject)objects.closest("Door");
//WallObject door = (WallObject)objects.closest("Large door");
		
		if (door != null)
		{
			if (door.getOrientation() == ORIENTATION_OPEN)
			{
				door.interact("Close");
			}
			
			
		}
		
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
