package org.mytest.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Home {
    public WebDriver driver;

    public Home(WebDriver rdriver){
        driver=rdriver;
        PageFactory.initElements(driver,this);
    }
    @FindBy(id="Email")
    WebElement txtEmail;

    @FindBy(id="Password")
    WebElement txtPassword;

    @FindBy(xpath="//input[@value='Log in']")
    WebElement btnLogin;

    @FindBy(linkText="Logout")
    WebElement lnkLogout;

    public void setTxtEmail(String user) {
        txtEmail.clear();
        txtEmail.sendKeys(user);
    }

    public void setTxtPassword(String password) {
        txtPassword.clear();
        txtPassword.sendKeys(password);
    }

    public void clickBtnLogin() {
        btnLogin.click();
    }

    public void clickLnkLogout() {
        lnkLogout.click();
    }
}
