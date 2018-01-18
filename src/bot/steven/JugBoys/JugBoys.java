package bot.steven.JugBoys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;

import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/*
 * JugBoys:
 * living the dream. a fully automated bot with interface to take , receive, and command Jug interaction.
 */
@ScriptManifest(author = "Steven Ventura", info = "My Water Boys", logo = "", name = "JugBoys", version = 0)
public class JugBoys extends Script{
	/*
	 * 1) travel to falador
	 * 2) fill jugs
	 * 3) give full jugs
	 * 4) take empty jugs
	 */
	
	enum MASTERSTATES {
		Idle,
		TravelToFalador,
		FillJugs,
		GiveFullJugs,
		TakeEmptyJugs
	};
	
	MASTERSTATES master = MASTERSTATES.Idle;
	
	private void printPlayerDataToFile() {
		//TODO: location,numEmptyJugs,numFullJugs
		
		try{
			File f = new File(getDirectoryData() + getParameters() + ".jugData");
			
			PrintWriter p = new PrintWriter(f);
			
			p.println(""+myPlayer().getX());
			p.println(""+myPlayer().getY());
			p.println(""+numEmptyJugs);
			p.println(""+numFullJugs);
			p.close();
			
			
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	public void onMessage(Message message)
	{
		final int CLANCHAT = 9, WHISPER = 3;
		String text = message.getMessage();
		
		if (message.getTypeId() == CLANCHAT 
				|| message.getTypeId() == WHISPER) {
			
			String phrase = text.split(" ")[0];
			
			if (phrase == null)
				return;
			
			switch (phrase) {
			case "Falador":
				
				break;
				
			case "Fill":
				
				break;
				
			case "Give":
				
				break;
				
			case "Take":
				
				break;
				
			
			
			}
			
			
			
		}
		
		
		
		
	}
	@Override
	public void onExit() {
		
	}
	
	public void onPaint(Graphics2D g) {
		g.setPaint(Color.ORANGE);
		
		switch (master) {
		case Idle:
		g.drawString("JugBoy=" + master,10,60);
		break;
		case TravelToFalador:
			
			break;
		case FillJugs:
			
			break;
			
		case GiveFullJugs:
			
			break;
		case TakeEmptyJugs:
			
			break;
		}
		
		g.drawString(""+numEmptyJugs,10,80);
		g.drawString(""+numFullJugs,10,100);
		
	}
	int numEmptyJugs=-1,numFullJugs=-1;
	private long CT=System.currentTimeMillis(),LT=System.currentTimeMillis();
	@Override
	public int onLoop() throws InterruptedException {
		CT = System.currentTimeMillis();
		if (CT - LT > 30 * 1000) {
			LT = CT;
			printPlayerDataToFile();
		}
		
		switch (master) {
		case Idle:
			//do nothing
			break;
		
		
		
		}
		
		
		return 150;
	}
	
	
	
	
	
}
