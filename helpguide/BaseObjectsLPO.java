package com.moodys.pageobjects.lightning;


import com.moodys.pageobjects.listeners.WebEventListener;
import com.moodys.selenium.base.configuration.ConfigUtil;
import com.moodys.selenium.base.pageframework.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseObjectsLPO extends BasePage {
    protected static WebDriver chromeDriver;
    public static WebDriver wd;
    protected static WebDriverWait wait;
    protected static Actions action;
    protected static JavascriptExecutor js;
    protected static final int TIMEOUT = 15; //seconds
    protected static final int POLLING = 900;// milliseconds
    public static String profiles = "ALL";
    public static LinkedHashMap<String, LinkedHashMap<String, String>> outputData=new LinkedHashMap<>();
    public static LinkedHashMap<String, String> applicationData;
    public static final String TEST_CYCLE = "dryrun";
    public static final String SANDBOX = "R1QA";
    protected static String SwitchDriver = "Default";
    //Initialize event listener
    protected void initElements() {
        wd=SwitchDriver.equalsIgnoreCase("Private")?chromeDriver:webdriver;
        applicationData=new LinkedHashMap<>();
        applicationData.put("Status","No Run");
        applicationData.put("URL","Unknown");
        wd = new EventFiringWebDriver(wd).register(new WebEventListener());
        wait = new WebDriverWait(wd, TIMEOUT, POLLING);
        action = new Actions(wd);
        js=(JavascriptExecutor) wd;
        PageFactory.initElements(new AjaxElementLocatorFactory(wd, TIMEOUT), this);
    }

    protected static WebElement getActiveElement() {
        return wd.switchTo().activeElement();
    }
    protected static WebElement selectSpanImg(String value) {
        return wd.findElement(By.xpath("//span[contains(text(),'" + value + "')]/following::img[1]"));
    }
    protected static WebElement selectSearchItem(String value) {
        return wd.findElement(By.xpath("//input[@id='input-5']/following::ul[3]//mark[text()='" + value + "'][1]"));
    }
    protected static WebElement selectDiv(String value) {
        return wd.findElement(By.xpath("//div[text()='" + value + "']"));
    }
    protected static WebElement showMoreAction() {
        return wd.findElement(By.xpath("//div[text()='Create Quote/Proposal' or text()='Edit']/following::a[contains(@title,' more actions')][1]"));
    }
    protected static WebElement selectLinkText(String value) {
        return wd.findElement(By.xpath("//a[text()='"+value+"']"));
    }
    protected static WebElement selectParaLink(String value) {
        return wd.findElement(By.xpath("//p[text()='"+value+"']/following::a[1] |" +
                "//span[text()='"+value+"']/following::a[1]"));
    }
    protected static WebElement selectSpan(String value) {
        return wd.findElement(By.xpath("//span[text()='" + value + "']"));
    }
    protected static WebElement selectSpanPrecedingInput(String value) {
        return wd.findElement(By.xpath("//span[text()='" + value + "']/preceding::input[1]"));
    }
    protected static WebElement spanContainsPrecedingLabel(String value) {
        return wd.findElement(By.xpath("//span[contains(text(),'" + value + "')]/preceding::label[1]"));
    }
    protected static WebElement starContainsPrecedingLabel(String value) {
        return wd.findElement(By.xpath("//*[contains(text(),'" + value + "')]/preceding::label[1]"));
    }

    protected static WebElement spanTitle(String value) {
        return wd.findElement(By.xpath("//span[@title='" + value + "']"));
    }
    protected static WebElement buttonText(String value) {
        return wd.findElement(By.xpath("//button[text()='" + value + "']"));
    }
    protected static WebElement buttonTextContains(String value) {
        return wd.findElement(By.xpath("//button[contains(text(),'" + value + "')]"));
    }
   protected static By buttonTextContainsBy(String value) {
        return By.xpath("//button[contains(text(),'" + value + "')]");
    }
    protected static WebElement spanInputBox(String value) {
        return wd.findElement(By.xpath("//span[text()='" + value + "']/following::input[1]"));
    }
    protected static WebElement spanLightningText(String value) {
        return wd.findElement(By.xpath("//span[text()='"+value+"']/following::lightning-formatted-text[1]"));
    }
    protected static WebElement selectImgLink(String value) {
        return wd.findElement(By.xpath("//img[@alt='"+value+"']/parent::a"));
    }
    protected static WebElement spanLinkInputBox(String value) {
        return wd.findElement(By.xpath("//span[text()='"+value+"']/following::a[1]"));
//        return wd.findElement(By.xpath("//span[text()='"+value+"']/following::a[1]/following::li[1]//a[text()='"+item+"']"));
    }
    protected static WebElement spanImgButton(String value) {
        return wd.findElement(By.xpath("//span[text()='"+value+"']/following::img[1]"));
    }
    protected static WebElement linkContainsPrecedingInput(String value) {
        return wd.findElement(By.xpath("//a[contains(text(),'"+value+"')]/preceding::input[1]"));
    }
    protected static WebElement labelPrecedingInput(String value) {
        return wd.findElement(By.xpath("//label[text()='"+value+"']/preceding::input[1]"));
    }
    protected static WebElement selectTD(String value) {
        return wd.findElement(By.xpath("//td[text()='"+value+"']"));
    }
    protected static WebElement spanTextAreaBox(String value) {
        return wd.findElement(By.xpath("//span[text()='"+value+"']/following::textarea[1]"));
    }
    protected static WebElement labelInput(String value) {
        return wd.findElement(By.xpath("//label[text()[normalize-space()='" + value + "']]/../following-sibling::*//input"));
    }
    public WebElement labelSelect(String value) {
        return wd.findElement(By.xpath("//label[text()[normalize-space()='" + value + "']]/../following-sibling::*//input[contains(@class,'select')]"));
    }
    public WebElement labelLink(String value) {
        return wd.findElement(By.xpath("//label[text()[normalize-space()='" + value + "']]/../following-sibling::*//a[contains(@class,'select')]"));
    }

    public WebElement divSelect(String value) {
        return wd.findElement(By.xpath("//div[text()[normalize-space()='" + value + "']]"));
    }
    protected static WebElement headerText1(String item, String value) {
        return wd.findElement(By.xpath("//p[contains(text(),'" + item + "')]" +
                "//following::lightning-formatted-text[contains(text(),'" + value + "')]"));
    }
    protected static WebElement selectApplicationFromSearchList(String appName) {
        return wd.findElement(By.xpath("//b[contains(text(),'" + appName + "')]"));
    }
    protected static void waitForElementToBeClickable(WebElement locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    protected static void waitForElementToAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    protected static void waitForElementToAppear(WebElement locator) {
        wait.until(ExpectedConditions.visibilityOf(locator));
    }
    protected static void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    protected static void waitForElementToDisappear(WebElement locator) {
        wait.until(ExpectedConditions.invisibilityOf(locator));
    }
    protected static void waitForElementThread() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected static void waitForElementStaleness(WebElement locator) {
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(locator)));
    }
    protected static void waitForTextToAppear(By locator, String text) {
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, text)));
    }
    protected static void waitForTextToAppear(WebElement locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementValue(locator, text));
    }
    protected static String getCurrentUrl() {
        return wd.getCurrentUrl();
    }
    protected boolean elementExists(String  value) {
        try {
            return wd.getPageSource().contains(" "+value+" ")?true:false;
//            return !wd.findElements(locator).isEmpty();
//        } catch (NoSuchElementException e) {
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean getOutputValue(String value) {
        try {
            applicationData.put(value, spanLightningText(value).getText());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void incognitoChromeDriver(String url){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("-incognito");
        options.addArguments("--disable-notifications");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        chromeDriver=new ChromeDriver(capabilities);
        chromeDriver.get(url);
        chromeDriver.manage().window().maximize();
    }

}

 