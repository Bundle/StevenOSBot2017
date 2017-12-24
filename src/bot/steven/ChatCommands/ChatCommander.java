package bot.steven.ChatCommands;

public class ChatCommander {
	private String mainName = "3DSpaceCadet";
	public boolean returnToNormalBehaviorFlag = false;
	public ChatCommander(String mainName)
	{
		this.mainName = mainName;
	}
	boolean isInterrupting = false;
	public void checkForInterruptText() {
		String textFound = "";
		if (textFound.equals("done"))
		{
			returnToNormalBehaviorFlag = true;
		}
	}
	public boolean isInterrupting()
	{
		return isInterrupting();
	}
}
