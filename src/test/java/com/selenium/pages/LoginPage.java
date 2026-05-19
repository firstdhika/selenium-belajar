package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private By username = By.id("user-name");
    private By password = By.id("password");
    private By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String user, String pass) {
        wait.until(ExpectedConditions.elementToBeClickable(username)).sendKeys(user);
        wait.until(ExpectedConditions.elementToBeClickable(password)).sendKeys(pass);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
}