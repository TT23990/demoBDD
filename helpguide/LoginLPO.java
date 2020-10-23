package com.moodys.pageobjects.lightning;


import com.moodys.interfaces.Home;
import com.moodys.interfaces.Login;
import com.moodys.selenium.base.configuration.ConfigUtil;
import com.moodys.pageobjects.sharedservices.Utils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.awt.*;

public class LoginLPO extends BaseObjectsLPO implements Login {

    @FindBy(id = "username")
    @CacheLookup WebElement userName;

    @FindBy(id = "password")
    @CacheLookup WebElement passWord;

    @FindBy(id = "Login")
    @CacheLookup WebElement loginBtn;

    @FindBy(id = "ttt")
    @CacheLookup WebElement moodysLogo;

    //    Initializing page objects in CommonObjects class
    public LoginLPO()  {
        super();
        initElements();
//        PageFactory.initElements(new AjaxElementLocatorFactory(wd,TIMEOUT),this);
    }

    //Actions:
    public String validateLoginPageTitle() {
        return driver.getTitle();
    }

    public boolean validateMoodysImage() {
        return moodysLogo.isDisplayed();
    }

    public boolean fillOutLoginForm(String username, String password) {
        try {
            System.out.println("SFDC Lightning Login");
//        wd.get(ConfigUtil.config.get("env.base.url"));//https://test.salesforce.com
            openWebPage(ConfigUtil.config.get("env.base.url"));
            userName.clear();
            userName.sendKeys(username);
            passWord.clear();
            passWord.sendKeys(new Utils().decryptPassword(password));
            loggerManager.startTransactionTimer("LoginToSFDC");
            loginBtn.click();
            waitForPageToLoad(TIMEOUT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

 