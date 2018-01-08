package bot.steven.AddyDaggerPSellerLOL;

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


@ScriptManifest(author = "Steven Ventura", info = "Duel Arena Trainer", logo = "", name = "AddyDaggerPSellerLOL", version = 0)
public class AddyDaggerPSellerLOL extends Script{

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
		g.setPaint(new Color(0,188,0));
		g.drawString("buy addy P",10,80);
		
		
	}
	private String enemyName = null;
	private String foodName = null;
	private boolean attackBack = false;
	private boolean bankBack = false;
	@Override
	public void onStart() {
		
	
	}
	
	
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
