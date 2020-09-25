package org.mytest.services;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class HighlightElement {

    public static void highlightElement(WebElement element, WebDriver driver, Boolean show) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        if (show) {
            jse.executeScript("arguments[0].style.border='3px solid green'", element);
        } else {
            jse.executeScript("arguments[0].style.border=''", element);
        }
    }

    public static void highlightElementBy(By element, WebDriver driver, Boolean show) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        if (show) {
            jse.executeScript("arguments[0].style.border='3px solid green'", driver.findElement(element));
        } else {
            jse.executeScript("arguments[0].style.border=''", driver.findElement(element));
        }
    }
}

 