package com.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private By checkoutBtn = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void checkout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
    }
}