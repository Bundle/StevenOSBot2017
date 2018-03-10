package JustLogin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Steven Ventura", info = "JustLogin", logo = "", name = "JustLogin", version = 0)
public class JustLogin extends Script{
	private ArrayList<Integer> loggedXs = new ArrayList<>(), loggedYs = new ArrayList<>();
	
	public void onStart() {
		StevenButton b = 
		new StevenButton("coords",10,440) {
			public void onStevenClick() {
				if (this.pressed == true)//reverse btw
				{	log("" + myPlayer().getX() + ", " + myPlayer().getY() + "\r\n");
					loggedXs.add(myPlayer().getX());
					loggedYs.add(myPlayer().getY());
				}
				this.pressed = false;
				new Thread() {
					public void run() {
						try{
							Thread.sleep(800);
							pressed=true;
						}catch(Exception e){}
					}
				}.start();
			}
		};
		StevenButton b2 = 
				new StevenButton("print",120,440) {
					public void onStevenClick() {
						String logme = "{";
						for (int i = 0; i < loggedXs.size(); i++) {
							logme += "{" + loggedXs.get(i) + ", " + loggedYs.get(i) + "},";
						}
						logme += "\r\n};";
						log("\r\n"+logme);
					}
				};
				StevenButton b3 = 
						new StevenButton("Clear",120,300	) {
							public void onStevenClick() {
								loggedXs = new ArrayList<Integer>();
								loggedYs = new ArrayList<Integer>();
							}
						};
		stevenbuttons.add(b);
		stevenbuttons.add(b2);
		stevenbuttons.add(b3);
		bot.addMouseListener(new BotMouseListener() {
			//TODO: ADD IN THE BUTTONS Cx
			public void checkMouseEvent(MouseEvent e) {
				
				if (e.getID() == MouseEvent.MOUSE_PRESSED) {
					for (StevenButton b : stevenbuttons) {
						if (new Rectangle2D.Double(b.x,b.y,b.width,b.height).contains(e.getX(),e.getY())){
							b.onStevenClick();
							b.pressed = !b.pressed;
						}
					}
				}
				
			}
		

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			

		
			
			
		});
		//mouse.getListener().mouseClicked(arg0);
	}
	public void onPaint(Graphics2D g) {
	for (StevenButton b : stevenbuttons)
	{
		this.draw(g,b);
	}
	}
	public void draw(Graphics2D g, StevenButton s)
	{
		if (s.pressed == true)
		g.setPaint(Color.BLUE);
		else
			g.setPaint(Color.GRAY);
		
		g.fillRect(s.x,s.y,s.width,s.height);
		
		g.setPaint(Color.WHITE);
		g.drawString(s.text,s.x,s.y+15);
	}
	class StevenButton {
		
		public int x=-1000, y=-1000, width=80, height=20;
		public String text = "button";
		public boolean pressed = false;
		
		public StevenButton(String text, int x, int y) {
			this.text = text; this.x = x; this.y  = y;

		}
		//this gets overridden lol
		public void onStevenClick() {
			
		}
		
	}
	ArrayList<StevenButton> stevenbuttons = new ArrayList<>();

	@Override
	public int onLoop() throws InterruptedException {
		// TODO Auto-generated method stub
	
		return 1000;
	}

	
	
	
}
