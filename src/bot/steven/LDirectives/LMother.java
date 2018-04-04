package bot.steven.LDirectives;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import bot.steven.A_Recaptcha.A_Recaptcha;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

public class LMother {
	private String password1,password2,burkemail;
	void getpasswords() {
		try{
		Scanner scan = new Scanner(new File("C:\\Users\\Yoloswag\\osbot\\data\\logininfo.btw"));
		password1 = scan.nextLine();
		password2 = scan.nextLine();
		burkemail = scan.nextLine();
		scan.close();
		}catch(Exception e){e.printStackTrace();}
	}
	private String getpassword() { return password1; }
	private String getpassword2() {return password2; }
	private String getburkemail(){ return burkemail; }
	
	static final String jarver = "2.5.3";
	public LMother() {
		
	}
	JFrame f ; 
	private static  void rsleep(long time)
	{
		try{Thread.sleep(time);
		}catch(Exception e){e.printStackTrace();}}
	private void startBurk(final String typename) {
		
			
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
				String burkcommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
						+ "OSBot " + jarver + ".jar\" "
						+ "-login gangsthurh:" + getpassword2() + " -bot "
						+ getburkemail() + ":" 
						+ getpassword() + ":1234"
						+ " -script " + getBurk(typename) + ":" + "1234"
						+ " -allow norandoms";
			Process pr = rt.exec(burkcommand);
			System.out.println(burkcommand);;
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();

	}			
	

	enum WatcherStates {waitForTut, startTut, runMotheredBot, scanForTutReq, scanForDone};
	class BotWatcher {
		
		WatcherStates watcherState;
		public int number;
		public String scriptType;
		public BotWatcher(int number, String scriptType) {
			this.scriptType = scriptType;this.number = number;watcherState = WatcherStates.runMotheredBot; }
		private String getPortNumber() {
			if (number < 10)
				return "500" + number;
				else if (number < 100)
				return "50" + number;
			else if (number < 1000) 
				return "5" + number;
			else if (number < 10000)
				return ""+number;
			return null;
		}
		private void runMotheredBot() {
			
			/*
			 java -Xmx512m -jar "C:\Users\Yoloswag\Dropbox\RunescapeMoney\Bots\OSBot 2.5.3.jar" -login gangsthurh:Cubes01342117!1 -bot stevenfakeaccountemail280@gmail.com:0134201342:1234 -script KBot:280 -allow norandoms
			  
			  
			 */
			final String LBotCommand = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
					+ "OSBot " + jarver + ".jar\" "
					+ "-debug " + getPortNumber() + " "
					+ "-login gangsthurh:" + getpassword2() + " -bot "
					+ "stevenfakeaccountemail" + number + "@gmail.com:"
					+ getpassword() + ":1234"
					+ " -script " + getStringNameForBotType(scriptType) + ":" + number
					+ " -allow norandoms";
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(LBotCommand);
			System.out.println("pid is: " + getpid(pr));
			BufferedReader input =
		            new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			 while ((line = input.readLine()) != null) {
				 
				 //TODO: do the process reading thing here
				 System.out.println(line);
				 if (line.contains("GottaDoTutorial")) {
					 tutStartFlag = true;
					 closeprocess(pr);
				 }
				 
			 }
			System.out.println("[[[exitbtw]]]"); 
			
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
		}
		private long getpid(Process pr) {
			try {
			    Field f = pr.getClass().getDeclaredField("handle");
			    f.setAccessible(true);				
			    long handl = f.getLong(pr);
			    
			    Kernel32 kernel = Kernel32.INSTANCE;
			    W32API.HANDLE handle = new W32API.HANDLE();
			    handle.setPointer(Pointer.createConstant(handl));
			    long pid = kernel.GetProcessId(handle);
			    return pid;
			  } catch (Throwable e) {				
			  }
			return -1;
		}
		private void closeprocess(Process pr) {
			
			final String killscriptstring = "taskkill /PID " + getpid(pr) + " /T /F";
			final Runtime rt = Runtime.getRuntime();
			new Thread() {
				public void run() {
			try{
			Process pr = rt.exec(killscriptstring);
			
			System.out.println("successfully ran: " + killscriptstring);
			}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
			
			}}.start();
			
			
		}
		
	private boolean tutDoneFlag = false;
	private boolean tutStartFlag = false;
		public void tic(long delta) {
			
			
			
			switch (watcherState) {
			case waitForTut:
				
				if (tutDoneFlag) {
					watcherState = WatcherStates.runMotheredBot;
					tutDoneFlag = false;
				}
				
				break;
			case startTut:
				String nameParam = "591";//because SDN
				String optionsParam = "0;1;0;0;1";
				/*khal options:
				 * randomize char
				 * disable music
				 * drop items
				 * walk to G E 
				 * logout after completion
				 */
				
				
				//start process
				final String command = "java -Xmx512m -jar \"C:\\Users\\Yoloswag\\Dropbox\\RunescapeMoney\\Bots\\"
						+ "OSBot " + jarver + ".jar\" "
						+ "-login gangsthurh:" + getpassword2() + " -bot "
						+ "stevenfakeaccountemail" + number + "@gmail.com:"
						+ getpassword() + ":1234"
						+ " -script " + nameParam + ":" + optionsParam;
				final Runtime rt = Runtime.getRuntime();
				new Thread() {
					public void run() {
				try{
				Process pr = rt.exec(command);
				
				BufferedReader input =
			            new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				 while ((line = input.readLine()) != null) {
					 
					 //TODO: do the process reading thing here
					 System.out.println(line);
					 if (line.contains("exited with")) {
						 tutDoneFlag = true;
						 closeprocess(pr);
					 }
				 }
				System.out.println("2[[[exitbtw]]]"); 
				//wait for process to begin
				}catch(Exception forfucksakes){forfucksakes.printStackTrace();}
				
				}}.start();
				
				
				
				watcherState = WatcherStates.waitForTut;
				break;
			case runMotheredBot:
				runMotheredBot();
				watcherState = WatcherStates.scanForTutReq;
				break;
			case scanForTutReq:
				if (tutStartFlag) {
				watcherState = WatcherStates.startTut;
				tutStartFlag = false;
				}
				break;
			case scanForDone:
				break;
			
			
			
			}
			
			
		}
	}
	
	ArrayList<BotWatcher>  botWatchers = new ArrayList<>();
	
	private final String getStringNameForBotType(String type) {
		switch(type) {
		case "LBot":
			return "LBot";
		
		case "KBot":
			return "KBot";
			
		}
		
		return null;
		
	}
	
	private void addBot(int number, String type) {
		
		
		try{
			File f = new File("C:\\Users\\Yoloswag\\OSBot\\data\\" + number + ".banned");
			if (f.exists())
			{
				System.out.println("Error: " + number + " is banned.");
				jta.append("Error: " + number + " is banned.\r\n");
				return;
			}
			
			
			
		}catch(Exception e){System.out.println("couldnt find banfile info for " + number);}
		
		try{
			File f = new File("C:\\Users\\Yoloswag\\OSBot\\data\\pulse\\" + number + ".pulse");
			Scanner scan = new Scanner(f);
			long timeboy = Long.parseLong(scan.nextLine());
			scan.close();
			if (System.currentTimeMillis() - timeboy < 2 * 1_000 * 60) {
				System.out.println("@@::" + (System.currentTimeMillis() - timeboy));
				System.out.println("2Error: bot " + number + " is currently running.");
				return;
			}
			else
			{
				f.delete();
			}
			
			
		}catch(Exception e){System.out.println("couldnt find pulse for " + number);}
		
		
		System.out.println("starting " + type + " on " + number);
		jta.append("starting " + type + " on " + number + "\r\n");
		////////////
		
		/////////////
		
		//TODO: end the process?
		for (int i =0 ; i < botWatchers.size(); i++)
		{
			if (botWatchers.get(i).number == number)
			{
				botWatchers.remove(i);
				break;
			}
		}
	
		
		botWatchers.add(new BotWatcher(number,type));
		
		
	}
	final JTextArea jta = new JTextArea();
	
	JScrollPane jscrollpane;
	
	final String[] botChoices = {"KBot", "LBot"};
	static final String getBurk(String name) {
		switch (name) {
		case ("KBot"):
			return "KBurk";
		case ("LBot"):
			return "LBurk";
		}
		
		return null;
	}
	final JComboBox<String> botChoiceBox = new JComboBox<String>(botChoices);
	public void begin() {
		getpasswords();
		
		final A_Recaptcha stevenAccountCreator = new A_Recaptcha();
		f = new JFrame("LMother.java");
		f.setSize(800,600);
		f.setLayout(new FlowLayout());
		
		
		botChoiceBox.setPreferredSize(new Dimension(100,100));
		final JTextField boynumber = new JTextField();
		final JTextField boynumber2 = new JTextField();
		final JTextField recaptchaBotField = new JTextField();
		boynumber.setPreferredSize(new Dimension(100,100));
		boynumber2.setPreferredSize(new Dimension(100,100));
		recaptchaBotField.setPreferredSize(new Dimension(100,100));
		recaptchaBotField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String t = recaptchaBotField.getText();
				recaptchaBotField.setText("");
				jta.append("Captcha creating bots " + t + " through " + (Integer.parseInt(t)+3) + "\r\n");
				
				new Thread() {
					public void run() {
						int pint = Integer.parseInt(t);
				for (int x = pint ; x < pint + 4; x++){
					jta.append("captcha-creating bot " + t + "\r\n");
					stevenAccountCreator.stevenCreateAccount(x);
				}
					}
				}.start();
				
			}
			
		});
		
		jscrollpane = new JScrollPane(jta);
		jscrollpane.setPreferredSize(new Dimension(400,400));
		final JButton burkbutton = new JButton("burk");
		burkbutton.setPreferredSize(new Dimension(60,40));
		burkbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBurk(botChoices[botChoiceBox.getSelectedIndex()]);
				jta.append("Starting burk ..." + "\r\n");
			}
			
		});
		
		
		final JButton b = new JButton("add boy");
		b.setPreferredSize(new Dimension(200,100));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (boynumber.getText().equals("")){
					return;
				}
				//start new bot
				int number = Integer.parseInt(boynumber.getText());
				
				if (boynumber2.getText().equals(""))
					addBot(number,botChoices[botChoiceBox.getSelectedIndex()]);
				else
				{
					for (int i = Integer.parseInt(boynumber.getText()); i <= 
								Integer.parseInt(boynumber2.getText()); 
							i++) {
						addBot(i,botChoices[botChoiceBox.getSelectedIndex()]);
					}
				}
				
				boynumber.setText("");
				boynumber2.setText("");
				
				
			}
		});
		
		
		f.add(burkbutton);
		f.add(botChoiceBox);
		
		f.add(b);
		f.add(boynumber);
		f.add(boynumber2);
		f.add(jscrollpane);//TODO: scrollpane
		f.add(recaptchaBotField);
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		long CT=System.currentTimeMillis(),LT=System.currentTimeMillis();
		long LTprices = 0;
		while(true) {
			rsleep(2000);
			CT=System.currentTimeMillis();
			try{
			for (BotWatcher L : botWatchers) {
				L.tic(CT - LT);
			}
			}catch(ConcurrentModificationException cme) { };
			
			if (CT-LTprices > 15*60*1000)
			{
				LTprices = CT;
				writepricesfile();
			}
			
			//tic the lbotwatchers
			
			
			LT = CT;
		}
		
		
		
		
	}
	private static String lookupPriceString(String name) {
		
		return name + "=" + lookupPrice(name) + "\r\n";
		
	}
	private static int lookupPrice(String name) {
	    try {
	        Document doc = Jsoup.connect("http://2007.runescape.wikia.com/wiki/Exchange:" + name.replaceAll(" ", "_")).get();
	        
	        Element price = doc.getElementById("GEPrice");
	        return Integer.parseInt(price.text().replaceAll(",", ""));
	    } catch (Exception e) {}
	    return 0;
	}
	
	private void writepricesfile() {
		System.out.println("writing prices file");
		try{File f = new File("C:\\Users\\Yoloswag\\OSBot\\Data\\market.prices");
		PrintWriter p = new PrintWriter(f);
		p.write(lookupPriceString("Cowhide"));
		p.write(lookupPriceString("Leather"));
		p.write(lookupPriceString("Green_dragonhide"));
		p.write(lookupPriceString("Energy potion(4)"));
		p.write(lookupPriceString("Green dragon leather"));
		p.write(lookupPriceString("Amulet of glory(6)"));
		p.write(lookupPriceString("Ring of wealth (5)"));
		p.close();
		
		}catch(Exception e){e.printStackTrace();}
		
	}
	public static void main(String[]args) {
		
		   
		  
		LMother m = new LMother();
		m.begin();
		
		
	}
	
	
	
	
	
	
	
	
	
	
	static class getrunnerboy {
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
			    	
			    	
			    	
			    	String[] mempls = line.replace(",","").split(" ");
			    	int memoryusageinkb = Integer.parseInt(mempls[mempls.length-2]);
			    	
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
				populatepidlist();
				System.out.println("pidlist.size is " + pidlist.size());
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
				if (!other.pidlist.contains(pidlist.get(i)))
				{
					out.add(new Integer(pidlist.get(i)));
				}
				
				/*boolean found = false;
				for (int c = 0; c < other.pidlist.size(); c++) {
					System.out.println(pidlist.get(i) + " ?? " + other.pidlist.get(c));
					if (pidlist.get(i) == other.pidlist.get(c))
					{
						System.out.println("found it");
						found = true;
					}
				}
				if (!found)
					out.add(pidlist.get(i));*/
			}
			return out;
			
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	/* Copyright (c) 2007 Timothy Wall, All Rights Reserved
	 * 
	 * This library is free software; you can redistribute it and/or
	 * modify it under the terms of the GNU Lesser General Public
	 * License as published by the Free Software Foundation; either
	 * version 2.1 of the License, or (at your option) any later version.
	 * 
	 * This library is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	 * Lesser General Public License for more details.  
	 */


	/** Base type for most W32 API libraries.  Provides standard options
	 * for unicode/ASCII mappings.  Set the system property w32.ascii
	 * to true to default to the ASCII mappings.
	 */
	interface W32API extends StdCallLibrary, W32Errors {
	    
	    /** Standard options to use the unicode version of a w32 API. */
	    Map UNICODE_OPTIONS = new HashMap() {
	        {
	            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
	            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
	        }
	    };
	    
	    /** Standard options to use the ASCII/MBCS version of a w32 API. */
	    Map ASCII_OPTIONS = new HashMap() {
	        {
	            put(OPTION_TYPE_MAPPER, W32APITypeMapper.ASCII);
	            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.ASCII);
	        }
	    };

	    Map DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? ASCII_OPTIONS : UNICODE_OPTIONS;
	    
	    public class HANDLE extends PointerType {
	    	@Override
	        public Object fromNative(Object nativeValue, FromNativeContext context) {
	            Object o = super.fromNative(nativeValue, context);
	            if (INVALID_HANDLE_VALUE.equals(o))
	                return INVALID_HANDLE_VALUE;
	            return o;
	        }
	    }

	    /** Constant value representing an invalid HANDLE. */
	    HANDLE INVALID_HANDLE_VALUE = new HANDLE() { 
	        { super.setPointer(Pointer.createConstant(-1)); }
	        @Override
	        public void setPointer(Pointer p) { 
	            throw new UnsupportedOperationException("Immutable reference");
	        }
	    };
	}
	/* Copyright (c) 2007 Timothy Wall, All Rights Reserved
	 *
	 * This library is free software; you can redistribute it and/or
	 * modify it under the terms of the GNU Lesser General Public
	 * License as published by the Free Software Foundation; either
	 * version 2.1 of the License, or (at your option) any later version.
	 * 

	 * This library is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	 * Lesser General Public License for more details.  
	 */

	interface W32Errors {
		
	    int NO_ERROR               = 0;
	    int ERROR_INVALID_FUNCTION = 1;
	    int ERROR_FILE_NOT_FOUND   = 2;
	    int ERROR_PATH_NOT_FOUND   = 3;

	}

	/* https://jna.dev.java.net/ */
	public interface Kernel32 extends W32API {
	    Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class, DEFAULT_OPTIONS);
	    /* http://msdn.microsoft.com/en-us/library/ms683179(VS.85).aspx */
	    HANDLE GetCurrentProcess();
	    /* http://msdn.microsoft.com/en-us/library/ms683215.aspx */
	    int GetProcessId(HANDLE Process);
	}
	
	
	
}
