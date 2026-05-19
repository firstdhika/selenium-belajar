package com.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.selenium.pages.LoginPage;
import com.selenium.pages.ProductsPage;
import com.selenium.pages.CartPage;
import com.selenium.pages.CheckoutPage;
import java.util.Map;

public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.setExperimentalOption("prefs", Map.of(
            "credentials_enable_service", false,
            "profile.password_manager_enabled", false
        ));

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testLoginBerhasil() {
        loginPage.login("standard_user", "secret_sauce");
        String title = productsPage.getPageTitle();
        Assert.assertEquals(title, "Products");
        System.out.println("✅ Login berhasil!");
    }

    @Test
    public void testAddToCart() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        System.out.println("✅ Add to cart berhasil!");
    }

    @Test
    public void testCheckout() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        cartPage.checkout();
        checkoutPage.fillCheckoutInfo("Andhika", "QA", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        String message = checkoutPage.getSuccessMessage();
        Assert.assertEquals(message, "Thank you for your order!");
        System.out.println("✅ Checkout berhasil!");
    }
}