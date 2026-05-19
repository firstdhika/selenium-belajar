package com.selenium.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;

    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By postalCode = By.id("postal-code");
    private By continueBtn = By.id("continue");
    private By finishBtn = By.id("finish");
    private By successMessage = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillCheckoutInfo(String fName, String lName, String postal) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement fField = wait.until(ExpectedConditions.elementToBeClickable(firstName));
        js.executeScript("arguments[0].focus();", fField);
        fField.clear();
        fField.sendKeys(fName);

        WebElement lField = wait.until(ExpectedConditions.elementToBeClickable(lastName));
        js.executeScript("arguments[0].focus();", lField);
        lField.clear();
        lField.sendKeys(lName);

        WebElement pField = wait.until(ExpectedConditions.elementToBeClickable(postalCode));
        js.executeScript("arguments[0].focus();", pField);
        pField.clear();
        pField.sendKeys(postal);
    }

    public void clickContinue() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
    }

    public void clickFinish() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(finishBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }
}