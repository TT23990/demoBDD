package com.moodys.pageobjects.lightning;

import com.moodys.interfaces.Home;
import com.moodys.selenium.base.configuration.ConfigUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class HomeLPO extends BaseObjectsLPO implements Home {
    //    Initializing page objects
    public HomeLPO() {
        super();
        initElements();
    }

    @FindBy(xpath = "//header[@id='oneHeader']//span[contains(@class,'photoContainer')]")
    WebElement ProfilePhoto;

    @FindBy(xpath = "//span[@class='appName slds-context-bar__label-action slds-context-bar__app-name']")
    WebElement ApplicationName;

    @FindBy(xpath = "//div[@class='slds-notify slds-notify_alert system-message level-info slds-theme_info']/child::span")
    WebElement ApplicationSandboxName;

    @FindBy(xpath = "//a[text()='Log Out']")
    WebElement LogoutLink;

    @FindBy(xpath = "//a[text()='Switch to Salesforce Classic']")
    WebElement switchToClassic;

    @FindBy(xpath = "//div/a[text()='Switch to Lightning Experience']")
    WebElement switchToLightning;

    @FindBy(xpath = "//input[@id='input-5']")
    WebElement GlobalSearchOption;

    @FindBy(xpath = "//input[@id='input-5']/following::input[1]")
    //| //input[@id='173:0;p']") @id='173:82;a' or @id='173:0;p' or //input[@id='143:0;p']
    WebElement GlobalSearchBox;

    @FindBy(xpath = "//div[@class='slds-icon-waffle']")
    WebElement ApplicationLaunchBtn;//span[@class='uiImage']/following::div[5]

    @FindBy(xpath = "//input[@class='slds-input'][@placeholder='Search apps and items...']")
    WebElement ApplicationSearchBox;

    @FindBy(xpath = "//button[text()='View All']")
    WebElement viewAllApplicationLink;

    @FindBy(xpath = "//a[@id='19:7415;a']")
    WebElement searchDataMatch;

    @FindBy(xpath = "//div[@class='setupGear']//a[@role='button']")
    WebElement setupGearIcon;

    @FindBy(xpath = "//mark[contains(@class,'data-match')]")
    WebElement SelectSearchItemFromList;


    //Actions:
    @Override
    public boolean validateSelectedSandbox(String value) {
        return ApplicationSandboxName.getText().contains(value);
    }

    @Override
    public boolean validateSelectedApplication(String application) {
        return ApplicationName.getText().equalsIgnoreCase(application);
    }

    @Override
    public boolean selectApplication(String appName) {
        try {
            ApplicationLaunchBtn.click();
            ApplicationSearchBox.sendKeys(appName);
            selectApplicationFromSearchList(appName).click();
            applicationData.put("Application",appName);
            applicationData.put("URL",getCurrentUrl());
//            outputData.put("Home",applicationData);
            Thread.sleep(POLLING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean searchItemList(String item, String value) {
        try {
            GlobalSearchOption.sendKeys(item + Keys.RETURN);
//        getHeaderSearchOption.sendKeys(Keys.TAB + value);
            GlobalSearchBox.clear();
            GlobalSearchBox.sendKeys(value);
            Thread.sleep(POLLING);
            waitForElementToAppear(SelectSearchItemFromList);
            SelectSearchItemFromList.click();

            return true;
        } catch (Exception e) {
            System.out.println("Item > " + item + " :" + value + " not found");
            return false;
        }
    }

    public boolean logoutFromApplication() {
        try {
            if(elementExists("\"photoContainer forceSocialPhoto\"")) {
                ProfilePhoto.click();
                LogoutLink.click();
            }
            wd.quit();
            return true;
        } catch (Exception e) {
            wd.quit();
            return false;
        }
    }

}

 