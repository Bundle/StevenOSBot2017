package net.steven.JugFiller2017;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Steven Ventura", info = "Fill jugs", logo = "", name = "JugFiller2017", version = 0)
public class JugFiller2017 extends Script{

	
	enum JUGSTATES {
		OpenBank,
		Deposit,
		WithdrawJugs,
		CloseBank,
		WalkToWater,
		FillJugs,
		ReturnToBank
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
		mouse.click(false);
	}
	int totalJugsFilled = 0;
	JUGSTATES currentState = JUGSTATES.OpenBank;
	Entity spicket;
	@Override
	public int onLoop() throws InterruptedException {
		log("currentState is " + currentState);
		switch (currentState) {
		case OpenBank:
			int please = 0;
			while(!bank.isOpen()) {
				please++;
				if (please > 10) {
				click(583,140);//enable run
				please = 0;
				}
			bank.open();
			rsleep(100);
			}
			currentState = JUGSTATES.Deposit;
			break;
		case Deposit:
			bank.depositAll();
			rsleep(500);
			currentState = JUGSTATES.WithdrawJugs;
			break;
		case WithdrawJugs:
			bank.withdrawAll("Jug");
			rsleep(500);
			currentState = JUGSTATES.CloseBank;
			break;
		case CloseBank:
			bank.close();
			currentState = JUGSTATES.WalkToWater;
			break;
		case WalkToWater:
			spicket = objects.closest("Waterpump");
			camera.toEntity(spicket);
			try{//sometimes the inventory is empty. try banking again.
				inventory.getItems()[0].interact("Use");
				currentState = JUGSTATES.FillJugs;
				}catch(Exception e){log("had to use tryCatch block."); currentState = JUGSTATES.OpenBank; break;}
			break;
		case FillJugs:
			spicket.interact("Use");
			rsleep(2000);
			while(myPlayer().isAnimating() || myPlayer().isMoving())
			{
				rsleep(50);
			}
			
			int jugsLeft = 28;
			while(true)
			{
				//wait for all the jugs to be filled.
				long j = inventory.getAmount("jug");
				if (j == 0)
					break;
				
				sleep(2000);
				if (j == jugsLeft)//if it's not filling
				{
					log("777 didnt start filling jugs. had to run special code.");
					inventory.getItems()[0].interact("Use");
					spicket.interact("Use");
					return random(1300,1500);
				}
				jugsLeft = (int)j;
				
			}
			totalJugsFilled += 28;
			log("jugsFilled = " + totalJugsFilled);
			currentState = JUGSTATES.ReturnToBank;
			break;
		case ReturnToBank:
			currentState = JUGSTATES.OpenBank;
			break;
		
		}
		
		
		
		
		
		return (int)(50*Math.random() + 50);
	}

	
	
	
	
	
}
