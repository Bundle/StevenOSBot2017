package bot.steven.DuelTrainer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Steven Ventura", info = "Duel Arena Trainer", logo = "", name = "DuelTrainer", version = 0)
public class DuelTrainer extends Script{

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
	
	
	public void onPaint(Graphics2D g)
	{
		g.setPaint(Color.CYAN);
		g.drawString("DuelTrainer: " + currentState,10,60);
		g.drawString("Enemy:" + enemyName,10,80);
		g.drawString("Food:" + foodName,10,100);
		
	}
	private String enemyName = null;
	private String foodName = null;
	private boolean attackBack = false;
	private boolean bankBack = false;
	@Override
	public void onStart() {
		
	final JFrame f = new JFrame("DuelTrainer GUI for " + myPlayer().getName());
	final JCheckBox attackEntry = new JCheckBox("Attack Back");
	final JCheckBox bankEntry = new JCheckBox("move/bank");
	f.setSize(800,200);
	f.setLayout(new FlowLayout());
	attackEntry.setPreferredSize(new Dimension(80,80));
	bankEntry.setPreferredSize(new Dimension(80,80));
	final JButton go = new JButton("ok");
	final JTextField nameEntry = new JTextField();
	nameEntry.setPreferredSize(new Dimension(200,80));
	nameEntry.setToolTipText("name of dueller");
	String[] foodStrings = {"None","Shark", "Jug of wine"};
	final JComboBox<String> foodEntry = new JComboBox<>(foodStrings);
	foodEntry.setSelectedIndex(0);
	foodEntry.setPreferredSize(new Dimension(200,80));
	
	//try to load
	try{
		File saved = new File(getDirectoryData() + "\\" + myPlayer().getName() + ".duel");
		Scanner scan = new Scanner(saved);
		
		nameEntry.setText(scan.nextLine());
		foodEntry.setSelectedItem(scan.nextLine());
		attackEntry.setSelected(Boolean.parseBoolean(scan.nextLine()));
		bankEntry.setSelected(Boolean.parseBoolean(scan.nextLine()));
	
	}catch(Exception e) {
		e.printStackTrace();
	}
	f.add(nameEntry);
	f.add(foodEntry);
	f.add(attackEntry);
	f.add(bankEntry);
	f.add(go);
	f.setVisible(true);
	
	go.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			enemyName = nameEntry.getText();
			foodName = (String)(foodEntry.getSelectedItem());
			attackBack = attackEntry.isSelected();
			bankBack = bankEntry.isSelected();
			try{
				File f = new File(getDirectoryData() + "\\" + myPlayer().getName() + ".duel");
				PrintWriter out = new PrintWriter(f);
				out.println(enemyName);
				out.println(foodName);
				out.println(attackBack);
				out.println(bankBack);
				out.close();
				
			}catch(Exception ee){ee.printStackTrace();}
			f.setVisible(false);
		}
	});
	currentState = DuelState.GettingFood;
	}
	DuelState currentState;
	enum DuelState {
		GettingFood,
		ChallengingEnemy,
		DuelingAndEating
	};
	
	private boolean WaitForWidget (int arg1, int arg2)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2) == null || !widgets.get(arg1,arg2).isVisible()) {
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	private boolean WaitForWidget (int arg1, int arg2, int arg3)
	{
		int loops = 0;
		while (widgets.get(arg1,arg2,arg3) == null || !widgets.get(arg1,arg2,arg3).isVisible()){
			loops++;
			if (loops > 80)
				return false;
			rsleep(100);
		}
		return true;
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		
		
		if (enemyName != null)//this means the UI is done
		switch(currentState) {
		case GettingFood:
			if (imDefinitelyInArena()) {
				currentState = DuelState.DuelingAndEating;
				break;
			}
			if (bankBack == false)
			{
				currentState = DuelState.ChallengingEnemy;
				break;
			}
			
			bank.open();
			if (bank.isOpen()){
				bank.depositAll();
				rsleep(500);
				if (!foodName.equals("None"))
					bank.withdrawAll(foodName);
				rsleep(500);
				if (bank.isOpen())
					bank.close();
				rsleep(500);
				walking.walk(new Position(3377,3266,0));
				currentState = DuelState.ChallengingEnemy;
			}
			else
				//do nothing;
			break;
		case ChallengingEnemy:
			if (bankBack == true)
				walking.walk(new Position(3377,3266,0));
			Entity duelBuddy = players.closest(enemyName);
			if (duelBuddy != null)
				{duelBuddy.interact("Challenge");
			if (WaitForWidget(482,103)) {				
				widgets.get(482, 112).interact("Load Preset Settings");
				rsleep(4000);//for the fucking thing
				widgets.get(482,103).interact("Accept");
				if (WaitForWidget(481,70))
				{
					widgets.get(481,70).interact("Accept");
					if (WaitForWidget(476,78))
					{
						widgets.get(476,78).interact("Accept");
						currentState = DuelState.DuelingAndEating;
						rsleep(13000);//because it has to load and then theres the 3 2 1 . . . 
					}
				}
			}
				}
			
			break;
		
		case DuelingAndEating:
			//attack guy again
			if (attackBack)
			{
				Entity duelBuddy2 = players.closest(enemyName);
				if (duelBuddy2 != null)
				{
					duelBuddy2.interact("Fight");
					rsleep(1800);//for combat check thing
				}
			}
			if (imInArena())//0 means in arena
			{
				if (!foodName.equals("None"))
				if (myPlayer().getHealthPercent() < 50)
				{
					Item[] inv = inventory.getItems();
					for (int i = 0; i < inv.length; i++)
					{
						if (inv[i] != null && inv[i].nameContains(foodName))
						{
							if (inv[i].hasAction("Eat"))
							{
								inv[i].interact("Eat");
								break;
							}
							if (inv[i].hasAction("Drink"))
							{
								inv[i].interact("Drink");
								break;
							}
						}
					}
					
					
				}
				
			}
			else
			{
				currentState = DuelState.GettingFood;
			}
			break;
		
		
		}
		
		
		return (int)(50*Math.random() + 50);
	}
	//returns true if unsure
	private boolean imInArena() {
		
		return myPlayer().isHitBarVisible();//god damn it
		//return System.currentTimeMillis() - myPlayer().getHitBarLoopCycle() < 5000;
		
	}
	//returns false if unsure
	private boolean imDefinitelyInArena() {
		return false;
		
	}
	
	
	
	
	
	
}
