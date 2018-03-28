package JustLogin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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

	public static void main(String[]args) {
		final String command = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
				+ "OSBot 2.5.2.jar\" "
				+ "-login gangsthurh:s0134201342 -bot "
				+ "aaaaa@aaaaa.com:"
				+ "1234:1234"
				+ " -script " + "1" + ":" + "2";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		class getrunnerboy {
			//example processnamecriteria="java.exe"
			String processnamecriteria;
			public getrunnerboy(String processname) {
				this.processnamecriteria = processname;
			}
			ArrayList<Integer> pidlist;
			public ArrayList<Integer> getList() {
				return pidlist;
			}
			
			
			
			public void populatepidlist() {
				try{
					pidlist = new ArrayList<>();
				 String line;
				    Process p = Runtime.getRuntime().exec
				    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
				    BufferedReader input =
				            new BufferedReader(new InputStreamReader(p.getInputStream()));
				    int linecount = 0;
				    while ((line = input.readLine()) != null) {
				    	linecount++;
				    	if (linecount >= 4) {String[] split = line.split(" ");
				    	
				    	
				    	
				    	int pid = 0; String pname = "";
				    	boolean secondlinebtw = false;
				        for (int i = 0; i < split.length; i++){
				        	if (secondlinebtw && pname.equals(processnamecriteria) && split[i].equals("") == false){
					        	secondlinebtw=false;
					        	System.out.println(line);
					        	pid = Integer.parseInt(split[i]);
					        	
					        	}
				        	if (i == 0) {
				        		secondlinebtw=true;
				        		pname = split[i];
				        	}
				        	
				        	
				        	
				        }
				        
				        if (pname.equals(processnamecriteria)) {
				        	pidlist.add(pid);
				        }
				        
				      
				    }}
				    input.close();
				} catch (Exception err) {
				    err.printStackTrace();
				}
				
			}
			
			public boolean waitForExtraProcesses(getrunnerboy other, long waitinterval, int numtimes) {
				
				//attempt every waitinterval seconds for process check thing
				
				try{
					for (int i = 0; i < numtimes; i++) {
					Thread.sleep(waitinterval);
					if (this.extraProcesses(other).size() != 0) {
						return true;
					}
					
					}
				}catch(Exception e){e.printStackTrace();}
				return false;
				
			}
			
			//should be used as p2.extraProcesses(p1)
			public ArrayList<Integer> extraProcesses(getrunnerboy other) {
				ArrayList<Integer> out = new ArrayList<>();
				//returns: this object has X more objects than "other" object
				for (int i = 0; i < pidlist.size(); i++) {
					boolean notfound = true;
					for (int c = 0; c < other.pidlist.size(); c++) {
						if (pidlist.get(i) == other.pidlist.get(c))
							notfound = false;
					}
					if (notfound)
						out.add(pidlist.get(i));
				}
				return out;
				
				
				
			}
		}
		
		/*getrunnerboy g = new getrunnerboy("java.exe");
		g.populatepidlist();
		try{
		final Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		}catch(Exception e){};
		getrunnerboy g2 = new getrunnerboy("java.exe");
		g2.populatepidlist();
		
		if (g2.waitForExtraProcesses(g, 4000, 3))
		{
			ArrayList<Integer> extrapids = g2.extraProcesses(g);
			for (int i = 0; i < extrapids.size(); i++) {
				System.out.println("extrapids(i) is: " + extrapids.get(i));
			}
		}
		
		
		*/
		{
		try{
			
		
		Process p = Runtime.getRuntime().exec
	    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
	    BufferedReader input =
	            new BufferedReader(new InputStreamReader(p.getInputStream()));
	    int linecount = 0;
	    String line;
	    while ((line = input.readLine()) != null) {
	    	
	    	linecount++;
	    	if (linecount < 4) continue;
	    	int memoryusageinkb;
	    	
	    	String[] mempls = line.replace(",","").split(" ");
	    	memoryusageinkb = Integer.parseInt(mempls[mempls.length-2]);
	    	if (mempls[0].startsWith("java.")) {
	    		
	    	
	    	System.out.println("memory usage is " + memoryusageinkb);
	    	}
	    	/*
	    	Image Name                     PID Session Name        Session#    Mem Usage
	    	========================= ======== ================ =========== ============
	    	System Idle Process              0 Services                   0         24 K
	    	System                           4 Services                   0      1,048 K
	    	smss.exe                       444 Services                   0         60 K
	    	csrss.exe                      604 Services                   0      1,540 K
	    	csrss.exe                      712 Console                    1     21,020 K
	    	wininit.exe                    720 Services                   0         64 K
	    	winlogon.exe                   776 Console                    1      2,020 K
	    	services.exe                   824 Services                   0      6,860 K
	    	lsass.exe                      832 Services                   0      9,868 K
	    	lsm.exe                        844 Services                   0      1,980 K
	    	svchost.exe                    936 Services                   0      5,324 K
	    	svchost.exe                   1016 Services                   0      6,784 K
	    	MsMpEng.exe                    616 Services                   0    168,188 K
	    	svchost.exe                    492 Services                   0     14,900 K
	    	svchost.exe                    664 Services                   0     10,232 K
	    	svchost.exe                   1056 Services                   0     17,752 K
	    	*/
	    }
		}catch(Exception e){e.printStackTrace();}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
}
