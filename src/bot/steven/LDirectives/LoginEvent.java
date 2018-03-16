//https://osbot.org/forum/topic/109560-a-simple-login-handler/
//https://osbot.org/forum/topic/109560-a-simple-login-handler/
//edited for patch on 3/15/18 by Gangsthurh (osbot)

package bot.steven.LDirectives;

import java.awt.Color;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.constants.ResponseCode;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.listener.LoginResponseCodeListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

public final class LoginEvent extends Event implements LoginResponseCodeListener {

    private String username, password;

    public LoginEvent(Script script) {
    	this.script = script;
    }
    Script script;
    
    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    
    public final int execute() throws InterruptedException {
        if (getClient().isLoggedIn() && getLobbyButton() == null) {
            setFinished();
        } else if (getLobbyButton() != null) {
            clickLobbyButton();
        } else if (isOnWorldSelectorScreen()) {
            cancelWorldSelection();
        } else {
            login();
        }
        return random(100, 150);
    }

    private boolean isOnWorldSelectorScreen() {
        return getColorPicker().isColorAt(50, 50, Color.BLACK);
    }

    private void cancelWorldSelection() {
        if (getMouse().click(new RectangleDestination(getBot(), 712, 8, 42, 8))) {
            new ConditionalSleep(3000) {
         
                public boolean condition() throws InterruptedException {
                    return !isOnWorldSelectorScreen();
                }
            }.sleep();
        }
    }

    private void login() {
        switch (getClient().getLoginUIState()) {
            case 0:
                clickExistingUsersButton();
                break;
            case 1:
                clickLoginButton();
                break;
            case 2:
                enterUserDetails();
                break;
        }
    }

    private void clickExistingUsersButton() {
        getMouse().click(new RectangleDestination(getBot(), 400, 280, 120, 20));
    }

    private void clickLoginButton() {
        getMouse().click(new RectangleDestination(getBot(), 240, 310, 120, 20));
    }

    private void enterUserDetails() {
        if (!getKeyboard().typeString(username)) {
            setFailed();
            return;
        }
        if (!getKeyboard().typeString(password)) {
            setFailed();
            return;
        }
        new ConditionalSleep(10_000) {
            
            public boolean condition() throws InterruptedException {
                return getLobbyButton() != null;
            }
        }.sleep();
    }

    private void clickLobbyButton() {
        if (getLobbyButton().interact()) {
            new ConditionalSleep(10_000) {
            
                public boolean condition() throws InterruptedException {
                    return getLobbyButton() == null;
                }
            }.sleep();
        }
    }

    private RS2Widget getLobbyButton() {
    	try{
        return script.widgets.get(378,76);//changed by hand on 3/15/18 after an update
    	//return getWidgets().getWidgetContainingText("CLICK HERE TO PLAY");
    	}catch(NullPointerException n){
    		return null;
    	}
    
    }


    public final void onResponseCode(final int responseCode) throws InterruptedException {
        if(ResponseCode.isDisabledError(responseCode)) {
            log("Login failed, account is disabled");
            setFailed();
            return;
        }

        if(ResponseCode.isConnectionError(responseCode)) {
            log("Connection error, attempts exceeded");
            setFailed();
            return;
        }
    }
}