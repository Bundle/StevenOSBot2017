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
			commandState = CommandStates.SendTradeRequest;
		}
		if (text.startsWith("Copy"))
		{
			String nameSubstring = text.split(" ")[1];
			script.log("copying");
			commandState = CommandStates.CopyPlayer;
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
		Done
	};
	
	private String nameToCopy = null;
	public void doInterruptStuff() {
		
		switch(commandState) {
		
		case StatusCheck:
			
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
