package bot.steven.ChatCommands;

import org.osbot.rs07.api.Keyboard;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;


public class ChatCommander {
	final int CLANCHAT = 9, WHISPER = 3;
	private String mainName = "3DSpaceCadet";
	public boolean returnToNormalBehaviorFlag = false;
	public Script script;
	public ChatCommander(Script script)
	{
		this.script = script;
	}
	CommandStates commandState = CommandStates.Done;
	String stateData = "";
	String stateData2 = "";
	boolean isInterrupting = false;
	
	public void checkForInterruptText(Message message) {
		String text = message.getMessage();
		script.log("raw text is:" + text);
		script.log("sender is:" + message.getUsername());
		if (commandState == CommandStates.CopyPlayer)
		{
			if (message.getUsername().equals(nameToCopy))
			{
				script.keyboard.typeString(text);
			}
		}
		
		if (message.getTypeId() == CLANCHAT || message.getTypeId() == WHISPER) {
		if (text.startsWith("Done"))
		{
			returnToNormalBehaviorFlag = true;
			commandState = CommandStates.Done;
			return;
		}
		if (text.startsWith("Trade"))
		{
			stateData = message.getUsername();
			stateData2 = text.split(" ")[1];
			commandState = CommandStates.SendTradeRequest;
		}
		if (text.startsWith("Copy"))
		{
			String nameSubstring = text.split(" ")[1];
			script.log("copying");
			commandState = CommandStates.CopyPlayer;
		}
		if (text.startsWith("Note"))
		{
			stateData = text.split(" ")[1];
			script.log("noting");
			commandState = CommandStates.NotingItems;
		}
		if (text.startsWith("Roll"))
		{
			//just do it rq lol
			script.keyboard.typeString("//" + script.getName() + " reporting for duty captain Cx");
		}
		
		
		
		
		
		
		
		if (text.startsWith("Status"))
		{
			commandState = CommandStates.StatusCheck;
		}
		if (text.startsWith("Follow"))
		{
			commandState = CommandStates.FollowUser;
			stateData = message.getUsername();
		}
		
		
		}
		
	}
	
	public enum CommandStates {
		StatusCheck,
		FollowUser,
		CopyPlayer,
		SharedWaitingState,
		SendTradeRequest,
		WaitForTradeToOpen,
		NotingItems,
		Done
	};
	
	private String nameToCopy = null;
	public void doInterruptStuff() {
		
		switch(commandState) {
		
		case StatusCheck:
			//do nothing yet Cx
			break;
		case FollowUser:
			Entity followguy = script.players.closest(stateData);
			if (followguy == null)
			{
				commandState = CommandStates.Done;
			}
			else
			{followguy.interact("Follow");
			commandState = CommandStates.SharedWaitingState;
			}
			break;
		case CopyPlayer:
			Entity copyguy = script.players.closestThatContains(stateData);
			
			if (copyguy == null)
			{
				commandState = CommandStates.Done;
			}
			else
			{
				nameToCopy = copyguy.getName();
				script.log("name is " + nameToCopy);
			}
			rsleep(100);
			break;
		case SharedWaitingState:
			rsleep(500);
			break;
		case SendTradeRequest:
			Entity tradeboy = script.players.closest(stateData);
			if (tradeboy != null)
				{
				tradeboy.interact("Trade with");
				commandState = CommandStates.WaitForTradeToOpen;
				}
			else
				commandState = CommandStates.Done;
			break;
		case WaitForTradeToOpen:
			//wait for widget
			WaitForWidget(335,25);
			//give him the item
			script.inventory.interact("Offer-All", stateData2);
			//press accept
			click(264,180);
			WaitForWidget(334,13);
			click(215,303);
			WaitForWidgetToDisappear(334,13);
			commandState = CommandStates.Done;
			break;
		case NotingItems:
			//open bank
			try{
			script.bank.open();
			}catch(Exception e) {
				commandState = CommandStates.Done;
			}
			//deposit all items
			script.bank.depositAll();
			//hit withdraw-as-note
			click(288,318);
			rsleep(100);
			//interact, withdraw all
			script.bank.withdrawAll(stateData);
			commandState = CommandStates.SharedWaitingState;
			break;
		
		}
		
		
	}
	final boolean LEFTCLICK = false, RIGHTCLICK = true;
	private void click(int x, int y)
	{
		script.mouse.move(x,y);
		script.mouse.click(LEFTCLICK);
	}
	private void rightclick(int x, int y)
	{
		script.mouse.move(x,y);
		script.mouse.click(RIGHTCLICK);
	}
	private void WaitForWidgetToDisappear (int arg1, int arg2)
	{
		int loops = 0;
		while (script.widgets.get(arg1,arg2) != null || script.widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return;
			rsleep(100);
		}
	}
	private void WaitForWidget (int arg1, int arg2)
	{
		int loops = 0;
		while (script.widgets.get(arg1,arg2) == null || !script.widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return;
			rsleep(100);
		}
	}
	private void rsleep(long millis)
	{
		try{
			Thread.sleep(millis);
		}catch(Exception e){};
	}
	public boolean isInterrupting()
	{
		if (commandState == CommandStates.Done)
			return false;
		else
			return true;
	}
}
