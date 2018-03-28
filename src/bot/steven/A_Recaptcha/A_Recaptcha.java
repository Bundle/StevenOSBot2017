package bot.steven.A_Recaptcha;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class A_Recaptcha {

	
	
	public enum ProxyType {
		HTTP, HTTPS, SOCKS4, SOCKS5;
	}
	
	
	public static String getStevensApiKey() {
		try{
		Scanner scan = new Scanner(new File("c:\\users\\yoloswag\\osbot\\data\\stevenapikey.txt"));
		final String s = scan.nextLine();
		scan.close();
		return s;
		
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	void getpasswords() {
		try{
		Scanner scan = new Scanner(new File("C:\\Users\\Yoloswag\\OSBot\\data\\logininfo.btw"));
		password1 = scan.nextLine();
		password2 = scan.nextLine();
		}catch(Exception e){e.printStackTrace();}
	}
	private String password1, password2;
	
public static void main(String[] args) {
		A_Recaptcha pwboy = new A_Recaptcha();
		pwboy.getpasswords();
	 String passwordToRunescapeAccount =  pwboy.password1;
		String apiKey = getStevensApiKey();
		String googleKey = "6LccFA0TAAAAAHEwUJx_c1TfTBWMTAOIphwTtd1b";
		String pageUrl = "https://secure.runescape.com/m=account-creation/g=oldscape/create_account";
		String proxyIp = "183.38.231.131";
		String proxyPort = "8888";
		String proxyUser = "username";
		String proxyPw = "password";
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		System.out.println("1 boy");
		/**
		 * With proxy and user authentication
		 */
		//TwoCaptchaService service = new TwoCaptchaService(apiKey, googleKey, pageUrl, proxyIp, proxyPort, proxyUser, proxyPw, ProxyType.HTTP);
		
		/**
		 * Without proxy and user authentication
		 * */
	try{	
		//TOOD: set up the page xd
		final WebClient wc = new WebClient();
		final HtmlPage p1 = wc.getPage(pageUrl);
		
		
		
		
		
		System.out.println("2 boy");
		System.out.println("its probably" + p1.getHtmlElementById("create-email-form").getClass().getName());
		final HtmlForm entireform =  (p1.getHtmlElementById("create-email-form"));//p1.getFormByName("create-email-form");
		System.out.println("clasname is " + entireform.getClass().getName());
		//TODO: fill in Email
		
		final HtmlTextInput emailformtextfield = entireform.getInputByName("email1");
		emailformtextfield.setValueAttribute("retardtestemail420@yamdex.ru");
		System.out.println("3 boy");
		//TODO: fill in Password
		final HtmlTextInput passwordfield = entireform.getInputByName("password1");
		passwordfield.setValueAttribute(passwordToRunescapeAccount);
		System.out.println("4 boy");
		//TODO: fill in Display Name
		final HtmlTextInput displaynamefield = entireform.getInputByName("displayname");
		displaynamefield.setValueAttribute("henlo wurld");
		System.out.println("5 boy");
		//TODO: fill in Age
		final HtmlTextInput agefield = entireform.getInputByName("age");
		agefield.setValueAttribute("23");
		System.out.println("6 boy");
		//TODO: answer captcha
		//https://2captcha.com/recaptchav2_eng_instruction
		//https://2captcha.com/newapi-recaptcha-en
		
	
		/*
		 * 5. User sends these forms. At the same time code is sent from the field "g-recaptcha-response"
6. Site receives data from the users and sends a request to google to check code's validity from the field "g-recaptcha-response"
7. Google checks the validity of the code. If the code is valid the site continues processing user's data
		 */
		  TwoCaptchaService service = new TwoCaptchaService(apiKey, googleKey, pageUrl);
			 
			
		  /*
		   * 
		   * 1. You fill all the needed fields
2. You send us Site_Key from the site + additional data ( optional: URL of the page, where you've encountered the captcha, PROXY that you use)
3. We upload captcha to our server and assign a worker to solve it
4. When the worker solves his captcha we receive g-recaptcha-response
5. We return g-recaptcha-response to you
6. You enter our answer into the field g-recaptcha-response and pass the form
		   */
		  
		  
		  /*
		   * 2. Find the field for text
<textarea id="g-recaptcha-response" name="g-recaptcha-response" class="g-recaptcha-response" style="width: 250px; height: 40px; border: 1px solid #c1c1c1; margin: 10px 25px; padding: 0px; resize: none; "></textarea>
You will need to enter our answer here
		   */
		  
		  
		
			String responseToken = service.solveCaptcha();
			System.out.println("The response token is: " + responseToken);
			
			final HtmlTextInput captchasolveform = entireform.getInputByName("g-recaptcha-response");
			captchasolveform.setValueAttribute(responseToken);
		
		
		//TODO: click Play Now button
		final HtmlSubmitInput button = entireform.getInputByName("submit");
		button.click();
		
		
		
	}catch(Exception e){e.printStackTrace();}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	static class TwoCaptchaService {
		
		/**
		 * This class is used to establish a connection to 2captcha.com 
		 * and receive the token for solving google recaptcha v2
		 * 
		 * @author Chillivanilli
		 * @version 1.0
		 * 
		 * If you have a custom software requests, please contact me 
		 * via forum: http://thebot.net/members/chillivanilli.174861/
		 * via eMail: chillivanilli@chillibots.com
		 * via skype: ktlotzek
		 */
		
		
		/**
		 * Your 2captcha.com captcha KEY
		 */
		private String apiKey;
		
		
		/**
		 * The google site key from the page you want to solve the recaptcha at
		 */
		private String googleKey;
		
		
		/**
		 * The URL where the recaptcha is placed.
		 * For example: https://www.google.com/recaptcha/api2/demo
		 */
		private String pageUrl;
		
		/**
		 * The proxy ip if you want a worker to solve the recaptcha through your proxy
		 */
		private String proxyIp;
		
		/**
		 * The proxy port
		 */
		private String proxyPort;
		
		/**
		 * Your proxy username, if your proxy uses user authentication
		 */
		private String proxyUser;
		
		/**
		 * Your proxy password, if your proxy uses user authentication
		 */
		private String proxyPw;
		
		/**
		 * Your proxy type, for example ProxyType.HTTP
		 */
		private ProxyType proxyType;
		
		/**
		 * The HttpWrapper which the requests are made with
		 */
		private HttpWrapper hw;
		
		
		/**
		 * Constructor if you don't use any proxy
		 * @param apiKey
		 * @param googleKey
		 * @param pageUrl
		 */
		public TwoCaptchaService(String apiKey, String googleKey, String pageUrl) {
			this.apiKey = apiKey;
			this.googleKey = googleKey;
			this.pageUrl = pageUrl;
			hw = new HttpWrapper();
		}
		
		/**
		 * Constructor if you are using a proxy without user authentication
		 * @param apiKey
		 * @param googleKey
		 * @param pageUrl
		 * @param proxyIp
		 * @param proxyPw
		 * @param proxyType
		 */
		public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, String proxyIp, String proxyPort, ProxyType proxyType) {
			this(apiKey, googleKey, pageUrl);
			this.proxyIp = proxyIp;
			this.proxyPort = proxyPort;
			this.proxyType = proxyType;
		}
		
		/**
		 * Constructor if you are using a proxy with user authentication
		 * @param apiKey
		 * @param googleKey
		 * @param pageUrl
		 * @param proxyIp
		 * @param proxyPort
		 * @param proxyUser
		 * @param proxyPw
		 * @param proxyType
		 */
		public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, String proxyIp, String proxyPort,
				String proxyUser, String proxyPw, ProxyType proxyType) {
			this(apiKey,googleKey,pageUrl);
			this.proxyIp = proxyIp;
			this.proxyPort = proxyPort;
			this.proxyUser = proxyUser;
			this.proxyPw = proxyPw;
			this.proxyType = proxyType;
		}
		
		/**
		 * Sends the recaptcha challenge to 2captcha.com and 
		 * checks every second if a worker has solved it
		 * 
		 * @return The response-token which is needed to solve and submit the recaptcha
		 * @throws InterruptedException, when thread.sleep is interrupted
		 * @throws IOException, when there is any server issue and the request cannot be completed
		 */
		public String solveCaptcha() throws InterruptedException, IOException {
			System.out.println("Sending recaptcha challenge to 2captcha.com");
			
			String parameters = "key=" + apiKey
					+ "&method=userrecaptcha"
					+ "&googlekey=" + googleKey
					+ "&pageurl=" + pageUrl;
					
			if (proxyIp != null) {
				if (proxyUser != null) {
					parameters += "&proxy=" 
							+ proxyUser + ":" + proxyPw 
							+ "@"
							+ proxyIp + ":" + proxyPort;
				} else {
					parameters += "&proxy=" 
							+ proxyIp + ":" + proxyPort;
				}
				
				parameters += "&proxytype=" + proxyType;
			}
			hw.get("http://2captcha.com/in.php?" + parameters);
			
			String captchaId = hw.getHtml().replaceAll("\\D", "");
			int timeCounter = 0;
			
			do {
				hw.get("http://2captcha.com/res.php?key=" + apiKey 
						+ "&action=get"
						+ "&id=" + captchaId);
				
				Thread.sleep(1000);
				
				timeCounter++;
				System.out.println("Waiting for captcha to be solved");
			} while(hw.getHtml().contains("NOT_READY"));
			
			System.out.println("It took "  + timeCounter + " seconds to solve the captcha");
			String gRecaptchaResponse = hw.getHtml().replaceAll("OK\\|", "").replaceAll("\\n", "");
			return gRecaptchaResponse;
		}

		/**
		 * 
		 * @return The 2captcha.com captcha key
		 */
		public String getApiKey() {
			return apiKey;
		}
		
		/**
		 * Sets the 2captcha.com captcha key
		 * @param apiKey
		 */
		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}
		
		/**
		 * 
		 * @return The google site key
		 */
		public String getGoogleKey() {
			return googleKey;
		}
		
		/**
		 * Sets the google site key
		 * @param googleKey
		 */
		public void setGoogleKey(String googleKey) {
			this.googleKey = googleKey;
		}
		
		/**
		 *
		 * @return The page url
		 */
		public String getPageUrl() {
			return pageUrl;
		}
		
		/**
		 * Sets the page url
		 * @param pageUrl
		 */
		public void setPageUrl(String pageUrl) {
			this.pageUrl = pageUrl;
		}
		
		/**
		 *
		 * @return The proxy ip
		 */
		public String getProxyIp() {
			return proxyIp;
		}
		
		/**
		 * Sets the proxy ip
		 * @param proxyIp
		 */
		public void setProxyIp(String proxyIp) {
			this.proxyIp = proxyIp;
		}
		
		/**
		 * 
		 * @return The proxy port
		 */
		public String getProxyPort() {
			return proxyPort;
		}
		
		/**
		 * Sets the proxy port
		 * @param proxyPort
		 */
		public void setProxyPort(String proxyPort) {
			this.proxyPort = proxyPort;
		}
		
		/**
		 * 
		 * @return The proxy authentication user
		 */
		public String getProxyUser() {
			return proxyUser;
		}
		
		/**
		 * Sets the proxy authentication user
		 * @param proxyUser
		 */
		public void setProxyUser(String proxyUser) {
			this.proxyUser = proxyUser;
		}
		
		/**
		 * 
		 * @return The proxy authentication password
		 */
		public String getProxyPw() {
			return proxyPw;
		}
		
		/**
		 * Sets the proxy authentication password
		 * @param proxyPw
		 */
		public void setProxyPw(String proxyPw) {
			this.proxyPw = proxyPw;
		}	
		
		/**
		 * 
		 * @return The proxy type
		 */
		public ProxyType getProxyType() {
			return proxyType;
		}
		
		/**
		 * Sets the proxy type
		 * @param proxyType
		 */
		public void setProxyType(ProxyType proxyType) {
			this.proxyType = proxyType;
		}
	}
	//https://github.com/2captcha/2captcha-api-examples/blob/master/ReCaptcha%20v2%20API%20Examples/Java%20Example/src/com/twocaptcha/http/HttpWrapper.java
	static class HttpWrapper {
		
	    private boolean printHeaders = false;
	    private String html;
	    private int responseCode = 0;

	    /**
	     * Default constructor
	     */
	    public HttpWrapper() {
	        html = "";
	    }
	 
	    /**
	     * A method to get the content and headers (if set) of a given page, with a given referer.
	     *
	     * @param url
	     *          The given URL.
	     * @param referer
	     *          The given referer.
	     * @throws IllegalStateException
	     *          Whenever an IO-related problem occurs.
	     * @post
	     *          new.getHtml() will return the headers and content of the given URL.
	     */
	    
	    public void get(String url) {
	        
	    	try {
	            URL url_ = new URL(url);
	            HttpURLConnection conn;
	                        
	            conn = (HttpURLConnection) url_.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setAllowUserInteraction(false);
	            conn.setDoOutput(false);
	            conn.setInstanceFollowRedirects(false);
	            
	            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
	            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
	            conn.setRequestProperty("Connection", "keep-alive");
	             
	            String headers = "";
	            
	            if (printHeaders) {
		            for(String key: conn.getHeaderFields().keySet())
		                headers += ((key != null)?key + ": ":"") + conn.getHeaderField(key) + "\n";
	            }
	            
	            responseCode = conn.getResponseCode();
	            
	            BufferedReader d = new BufferedReader(new InputStreamReader(new DataInputStream(conn.getInputStream())));
	            String result = "";
	            String line = null;
	            while ((line = d.readLine()) != null) {
	            	line = new String(line.getBytes(),"UTF-8");
	            	result += line + "\n";
	            }

	            d.close();
	            
	            if (printHeaders) {
	                setHtml(headers + "\n" + result);
	            } else {
	                setHtml(result);
	            }
	        } catch (IOException e) {
	            throw new IllegalStateException("An IOException occurred:" + "\n" + e.getMessage());
	        }
	    }
	 
	 
	    /**
	     * Return the html content that this Wrapper has last retrieved from a request.
	     */
	    public String getHtml() {
	        return this.html;
	    }
	 
	    /**
	     * Set the html content of this HttpWrapper.
	     *
	     * @param html
	     *          The new html content.
	     */
	    private void setHtml(String html) {
	        this.html = html;
	    }
	    
	    /**
	     * Set if headers should be print above the content or not
	     * @param trueOrFalse
	     */
	    public void setPrintHeaders(boolean trueOrFalse) {
	    	printHeaders = trueOrFalse;
	    }
	    
	    /**
	     * Returns the response code of the request
	     * @return
	     */
	    public int getResponseCode() {
	    	return responseCode;
	    }
	    
	}
}
