package bot.steven.LordBurk;

import java.io.File;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "LordBurk", logo = "", name = "LordBurk", version = 0)
public class LordBurk extends Script{
 /*
  *  * TODO: request LordBurk to log in
 * TODO: find LordBurk, trade
 * TODO: accept gold from LordBurk
 * TODO: buy 13k hides from GE using LordBurk's money
 * TODO: bank and run to the desert
 * TODO: tan the 13k hides
 * TODO: return to GE, give LordBurk his hides back
 * TODO: file write
 * TODO: file read
 * TODO: log in and act on file
  */
	enum THEWAITINGBURK {
		waitingForFileWrite,
		iHaveBeenSummonedLogin,
		iHaveBeenSummonedWorldHop,
	};
	enum MASTER {
		THEWAITINGBURK,
		THETRADINGBURK
	}
	MASTER master = MASTER.THEWAITINGBURK;
	enum THETRADINGBURK {
		sendingTradeRequest,
		managingTradeWaterfall
		//wait for either: 1) sending the item to them or 2) giving them the item. this is determined from file data
	}
	THEWAITINGBURK theWaitingBurk = THEWAITINGBURK.waitingForFileWrite;
	THETRADINGBURK theTradingBurk = THETRADINGBURK.sendingTradeRequest;
	private int tradeMeetingSpotX, tradeMeetingSpotY;
	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		switch(master) {
		case THEWAITINGBURK:
			doTheWaitingBurkStateMachine();
			break;
		case THETRADINGBURK:
			doTheTradingBurkStateMachine();
				break;
		}

		return 150 + (int)(50*Math.random());
	}
	class BurksideBurkFile {
		int world;
		String tradeboyname;
		String filename;
		
		private void writeToBurkFile() {
			
		}
		private void defineFromBurkFile(File f) {
			
		}
		
		
	}
	private void doTheWaitingBurkStateMachine() {
	switch (theWaitingBurk) {
	
	case waitingForFileWrite:
		//Scanner scan = new Scanner(
			//	new File("C:\\Users\\Yoloswag\\OSBot\\Data\\" + myPlayer().getName() + ".burksummon");
		//scanner list all files ending with .burksummon
		
		//if one of them has a time stamp within 8 seconds, then begin sequence.
		
		break;
		
	case iHaveBeenSummonedLogin:
		//log into the game
		
		theWaitingBurk = THEWAITINGBURK.iHaveBeenSummonedWorldHop;

		
		//move to the specified location
		
		
		break;
	case iHaveBeenSummonedWorldHop:
		//hop to the world specified in the file
		
		/*if (worlds.getCurrentWorld() == )
			theWaitingBurk = THEWAITINGBURK.moveAndSendTradeRequest;
		*/
		break;
	
	}
	}
	private void doTheTradingBurkStateMachine() {
		switch (theTradingBurk) {
		
		}
		
		
	}

	
	
}
