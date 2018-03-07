package bot.steven.DoorCloser;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.WallObject;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
//i could do it. "pay me 2mil and i shut off the bots for 1 hour"
/*
 * DONETODO: make it save the words people say
 * DONETODO: dolphin emulator btw online play melee
 * DONETODO: make dank memes for them to type
 * DONETODO: upon starting program, determine if they are within range of the doors. change starting state from this.
 * DONETODO: make several bots, determine just how many i need to keep the door perma-shut
 * NOPETODO: name the bots Gangsthurh1, Gangsthurh2, Gangsthurh3, Gangsthurh4, Gangsthurh5,
 * TODO: 
 */
@ScriptManifest(author = "Steven Ventura", info = "Take Turns closing doors", logo = "", name = "DoorCloser", version = 0)
public class DoorCloser extends Script{

	final boolean LEFTCLICK = false, RIGHTCLICK = true;
	class WordWithCooldown {
	//WordWithCooldown is basically a particle effect
		long creationTime;
		long deathTime;
		private final long lifeDuration = 30 * 1000; 
		String sentence;
		public WordWithCooldown(String sentence, long currentTime) {
			this.sentence = sentence;
			this.creationTime = currentTime;
			this.deathTime = currentTime + lifeDuration;
		}
		public boolean isExpired() {
			return (System.currentTimeMillis() > this.deathTime);
		}
		
		
	}
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
		autismMessages.add("i bet your parents hate you lmao");
				autismMessages.add("people doing this dont deserve to play the game");
						autismMessages.add("i cant get in :stuck_out_tongue:");
								autismMessages.add("i cant get out :frowning:");
										autismMessages.add("i dont even feel bad bc i know this low life is gonna get banned");
												autismMessages.add("eat a dik troll");
														autismMessages.add("this troll is rated A for aspergers");

		autismMessages.add("look what youve done lol");
				autismMessages.add("im just gonna wait it out LOL");
						autismMessages.add("what a funny troll lel");
								autismMessages.add("i cant wait for him to get timed out");
										autismMessages.add("please I need to get in");
												autismMessages.add("f this im gonna go play roblox");
														autismMessages.add("just report him");
																autismMessages.add("this isnt impactful for the game");
																		autismMessages.add("dont break the game lul");
																				autismMessages.add("fuck i have work soon and i have to deal with this");
																						autismMessages.add("i beat his mum dont even luve 'em");
																								autismMessages.add("before you go on and troll, expect to get reported");
																										autismMessages.add("these trolls should stay underground");
																												autismMessages.add("this guy is probably fat irl ha ha");
																														autismMessages.add("whata joke he he");
																																autismMessages.add("im just gonna go watch anime ffs");
																																		autismMessages.add("they need to nerf this");
		autismMessages.add("is this a bug?*/");
		///////////////////
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
   
   autismSuper = "What the fuck did you just fucking say about me, you little bitch? I’ll have you know I graduated top of my class in the Navy Seals, and I’ve been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I’m the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You’re fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that’s just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little “clever” comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn’t, you didn’t, and now you’re paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You’re fucking dead, kiddo.";
   
   //just always start walking to desert lol
  // if (myPlayer().getX() < 3267 || myPlayer().getY()  > 3196)
	   doorCloseBoy = DOORCLOSERSTATES.FindWalkToDesertVertex;
   //else
	//   doorCloseBoy = DOORCLOSERSTATES.ClosingDoors;
	}
	private String autismSuper;
	
	private boolean copyTextHaha = false;
	TreeMap<String, WordWithCooldown> wordsWithCooldowns = new TreeMap<>();
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3, SAY = 2;
		if (message.getMessage().startsWith("Stevenhop")) {
			worlds.hop(Integer.parseInt(message.getMessage().split(" ")[1]));
		}
		else if (Math.random() < 0.20 && copyTextHaha == true && message.getTypeId() == SAY) {
			String text = message.getMessage();
			if (!wordsWithCooldowns.containsKey(text)) {
				wordsWithCooldowns.put(text,new WordWithCooldown(text,System.currentTimeMillis()));
				keyboard.typeString(text);
			}
			ArrayList<String> deadwords = new ArrayList<>();
			for (String s : wordsWithCooldowns.keySet()) {
				if (wordsWithCooldowns.get(s).isExpired())
					deadwords.add(s);
			}
			for (String s : deadwords) {
				wordsWithCooldowns.remove(s);
			}
			
			
		}
		
		
		
		if (message.getMessage().contains("Me?")) {
			keyboard.typeString("Me?");
		}
		if (message.getMessage().contains("Oh dear, you are dead!")) {
			doorCloseBoy = DOORCLOSERSTATES.FindWalkToDesertVertex;
		}
		
		
		if (message.getTypeId() == SAY || message.getTypeId() == WHISPER){
		String line = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
				+
		"[" + message.getUsername() + "]:" + message.getMessage();
		
		try{
			
			
		File f = new File(getDirectoryData() + "\\" + myPlayer().getName() + ".doorTears");
		ArrayList<String> lines = new ArrayList<>();
		if (f.exists()) {
		Scanner scan = new Scanner(f);
		
		while(scan.hasNextLine())
			lines.add(scan.nextLine());
		}
		log("adding line: " + line);
		
		//scan.close();
		PrintWriter p = new PrintWriter(f);
		for (String s : lines)
			p.println(s);
		p.println(line);
		p.close();
		
		}catch(Exception e){
			log("ree error");
			e.printStackTrace();}
		}
		
		
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
			if (myPlayer().getX() < 3228) {
				
					doorCloseBoy = DOORCLOSERSTATES.FindWalkToDesertVertex;
					break;
			}
		//ava.lang.ClassCastException: org.osbot.rs07.api.model.WallObject cannot be cast to org.osbot.rs07.api.model.InteractableObject
slowdown++;
if (slowdown > 100)
{
	slowdown = 0;
	int index = (int)((autismSuper.length()-80)*Math.random());
	String sub =  autismSuper.substring(index, index+80);
	//keyboard.typeString(sub);
	
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
