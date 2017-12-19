package net.steven.GELooter;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
@ScriptManifest(author = "Steven Ventura", info = "loot GE", logo = "", name = "GELooter", version = 0)
public class GELooter extends Script {

	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
		return 50;
	}

	public void onStart() {
		
		log("ayy GELooter started");
		
	}
	
	public void onExit() {
		
		log("script exited btw");
	}
	
}
