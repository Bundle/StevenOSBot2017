package bot.steven.LDirectives;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
@ScriptManifest(author = "Steven Ventura", info = "LBurk head", logo = "", name = "LBurk", version = 0)
public class LBot extends Script{
	
	enum STATEMACHINE {
		locationcheck,
		traveltogefromspawn,
		requestburkgivecoins,waitfortradecoins,
		returntoge,gotodesert,tanhides,completetradecoins,
		requestburktakehides,waitfortradehides,completetradehides,
		auditbank,buycowhides,logoutretire,
		requesttutorial
	};
	STATEMACHINE state;
	
	public void onStart() {
		state = STATEMACHINE.locationcheck;
	}
	@Override
	public int onLoop() throws InterruptedException {
		switch(state) {
		
		case locationcheck:
			int x,y;
			x = myPlayer().getX(); y = myPlayer().getY();
			if (x < 3220 && y < 3208) {
				state = STATEMACHINE.requesttutorial;
			}
			else if (x > 3260 && y < 3330)
			{
				state = STATEMACHINE.gotodesert;
			}
			else {
				state = STATEMACHINE.traveltogefromspawn;
			}
			break;
		
		
		
		case requesttutorial:
			
			
			
			break;
		case gotodesert:
			
			break;
			
		case traveltoge:
			
			break;
			
		}
		
		
		
		
		return 50 + (int)(Math.random()*50);
	}
	
	
	
	
	
	




}
