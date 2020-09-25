package org.mytest.stepdefinations;


import io.cucumber.java.en.*;
import org.junit.Assert;
import org.mytest.pageobjects.Home;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Steps {

    public WebDriver driver;
    public Home home;

    @Given("User launch the chrome browser")
    public void user_launch_the_chrome_browser() {
        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
        driver=new ChromeDriver();
        home=new Home(driver);
    }

    @When("User opens URL {string}")
    public void user_opens_URL(String url) {
        driver.get(url);
    }

    @When("User enters email as {string} and password as {string}")
    public void user_enters_email_as_and_password_as(String user, String password) {
        home.setTxtEmail(user);
        home.setTxtPassword(password);
    }

    @When("Click on login button")
    public void click_on_login_button() {
        home.clickBtnLogin();
    }

    @Then("Page title should be {string}")
    public void page_title_should_be(String title) {
        driver.getTitle();
        Assert.assertEquals("Login failed",title,driver.getTitle());

    }

    @When("User click on logout link")
    public void user_click_on_logout_link() throws Throwable {
        home.clickLnkLogout();
        Thread.sleep(3000);
    }

    @Then("Close browser")
    public void close_browser() {
        driver.quit();
    }

}
