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
		
		getrunnerboy g = new getrunnerboy("java.exe");
		g.populatepidlist();
		try{
		final Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		Thread.sleep(5);
		}catch(Exception e){};
		getrunnerboy g2 = new getrunnerboy("java.exe");
		g2.populatepidlist();
		
		ArrayList<Integer> newProcesses = g2.extraProcesses(g);
		
		
		/*
		class pls{
			public String name;
			public int pid;
			public pls(String name, int pid) {
				this.name=name;this.pid=pid;
			}
		}
		
		final ArrayList<pls> please = new ArrayList<>();
		
		final ArrayList<pls> please2 = new ArrayList<>();
		
		
		final Runtime rt = Runtime.getRuntime();
		new Thread() {
			public void run() {
		try{
		
		//	/
		//	  get a list of all javaw.exe processes beforehand, then compare it afterwards
			 //
			
			try {
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
			        	if (secondlinebtw && pname.equals("java.exe") && split[i].equals("") == false){
				        	secondlinebtw=false;
				        	System.out.println(line);
				        	pid = Integer.parseInt(split[i]);
				        	
				        	}
			        	if (i == 0) {
			        		secondlinebtw=true;
			        		pname = split[i];
			        	}
			        	
			        	
			        	
			        }
			        
			        if (pname.equals("java.exe")) {
			        	please.add(new pls(pname,pid));
			        }
			        
			      
			    }}
			    input.close();
			} catch (Exception err) {
			    err.printStackTrace();
			}
			
			
			Process pr = rt.exec(command);
		
		
		for (int i=9;i>=1;i--){
		Thread.sleep(1000);
			System.out.println(i);
		}
	//
	//	  pr is the cmd.exe file.
		 //
		
		
		
		try {
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
		        	if (secondlinebtw && pname.equals("java.exe") && split[i].equals("") == false){
			        	secondlinebtw=false;
			        	System.out.println(line);
			        	pid = Integer.parseInt(split[i]);
			        	
			        	}
		        	if (i == 0) {
		        		secondlinebtw=true;
		        		pname = split[i];
		        	}
		        	
		        	
		        	
		        }
		        
		        if (pname.equals("java.exe")) {
		        	please2.add(new pls(pname,pid));
		        }
		        
		      
		    }}
		    input.close();
		} catch (Exception err) {
		    err.printStackTrace();
		}
		
		
		//find the different value
				for (int i2 = 0; i2 < please2.size(); i2++){
					int pid = please2.get(i2).pid;
					boolean missing = true;
					for (int i1=0;i1<please.size();i1++) {
						if (please.get(i1).pid == pid){
							missing = false;
						}
						
						
						
					}
					if (missing == true)
						System.out.println("the new processid is " + pid);
				}
				
				System.out.println("1111111111");
				for (int i = 0; i < please.size(); i++){
					System.out.println(please.get(i).pid);
				}
				
				System.out.println("222222222");
				for (int c = 0; c < please2.size(); c++) {
					System.out.println(please2.get(c).pid);
				}
				
		
		
		}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
		
		}}.start();
		
		
		
		
		
		
		
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
}
