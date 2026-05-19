package com.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.selenium.pages.LoginPage;
import com.selenium.pages.ProductsPage;
import com.selenium.pages.CartPage;
import com.selenium.pages.CheckoutPage;
import java.time.Duration;
import java.util.Map;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");           // ← tambah ini
        options.addArguments("--no-sandbox");         // ← tambah ini
        options.addArguments("--disable-dev-shm-usage"); // ← tambah ini
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

    // ===== LOGIN TESTS =====

    @Test
    public void testLoginBerhasil() {
        loginPage.login("standard_user", "secret_sauce");
        String title = productsPage.getPageTitle();
        Assert.assertEquals(title, "Products");
        System.out.println("✅ Login berhasil!");
    }

    @Test
    public void testLoginPasswordSalah() {
        loginPage.login("standard_user", "salah123");
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("Username and password do not match"));
        System.out.println("✅ Login password salah berhasil divalidasi!");
    }

    @Test
    public void testLoginUsernameSalah() {
        loginPage.login("user_salah", "secret_sauce");
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("Username and password do not match"));
        System.out.println("✅ Login username salah berhasil divalidasi!");
    }

    @Test
    public void testLoginKosong() {
        loginPage.login("", "");
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("Username is required"));
        System.out.println("✅ Login kosong berhasil divalidasi!");
    }

    @Test
    public void testLoginUsernameKosong() {
        loginPage.login("", "secret_sauce");
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("Username is required"));
        System.out.println("✅ Login username kosong berhasil divalidasi!");
    }

    @Test
    public void testLoginPasswordKosong() {
        loginPage.login("standard_user", "");
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("Password is required"));
        System.out.println("✅ Login password kosong berhasil divalidasi!");
    }

    // ===== PRODUCTS TESTS =====

    @Test
    public void testAddToCart() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        System.out.println("✅ Add to cart berhasil!");
    }

    @Test
    public void testCartBadgeBertambah() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        String badge = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(badge, "1");
        System.out.println("✅ Cart badge bertambah!");
    }

    @Test
    public void testJumlahProduk() {
        loginPage.login("standard_user", "secret_sauce");
        int jumlah = driver.findElements(By.className("inventory_item")).size();
        Assert.assertEquals(jumlah, 6);
        System.out.println("✅ Jumlah produk sesuai: " + jumlah);
    }

    @Test
    public void testSortProdukAZ() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.sortBy("az");
        String firstProduct = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(firstProduct, "Sauce Labs Backpack");
        System.out.println("✅ Sort A-Z berhasil!");
    }

    @Test
    public void testSortProdukZA() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.sortBy("za");
        String firstProduct = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(firstProduct, "Test.allTheThings() T-Shirt (Red)");
        System.out.println("✅ Sort Z-A berhasil!");
    }

    @Test
    public void testSortHargaLowHigh() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.sortBy("lohi");
        String firstPrice = driver.findElement(By.className("inventory_item_price")).getText();
        Assert.assertEquals(firstPrice, "$7.99");
        System.out.println("✅ Sort harga low-high berhasil!");
    }

    @Test
    public void testSortHargaHighLow() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.sortBy("hilo");
        String firstPrice = driver.findElement(By.className("inventory_item_price")).getText();
        Assert.assertEquals(firstPrice, "$49.99");
        System.out.println("✅ Sort harga high-low berhasil!");
    }

    // ===== CART TESTS =====

    @Test
    public void testRemoveItemDariCart() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        int items = driver.findElements(By.className("cart_item")).size();
        Assert.assertEquals(items, 0);
        System.out.println("✅ Remove item dari cart berhasil!");
    }

    @Test
    public void testNamaProdukDiCart() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        String productName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(productName, "Sauce Labs Backpack");
        System.out.println("✅ Nama produk di cart sesuai!");
    }

    @Test
    public void testCancelDiCheckout() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        cartPage.checkout();
        driver.findElement(By.id("cancel")).click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart"));
        System.out.println("✅ Cancel checkout berhasil kembali ke cart!");
    }

    @Test
    public void testCheckoutTanpaForm() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        cartPage.checkout();
        driver.findElement(By.id("continue")).click();
        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("First Name is required"));
        System.out.println("✅ Validasi form checkout berhasil!");
    }

    // ===== CHECKOUT TESTS =====

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

    @Test
    public void testCekTotalHargaOverview() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        cartPage.checkout();
        checkoutPage.fillCheckoutInfo("Andhika", "QA", "12345");
        checkoutPage.clickContinue();
        String itemTotal = driver.findElement(By.className("summary_subtotal_label")).getText();
        Assert.assertTrue(itemTotal.contains("29.99"));
        System.out.println("✅ Total harga sesuai!");
    }

    @Test
    public void testCancelDiOverview() {
        loginPage.login("standard_user", "secret_sauce");
        productsPage.addToCart();
        productsPage.goToCart();
        cartPage.checkout();
        checkoutPage.fillCheckoutInfo("Andhika", "QA", "12345");
        checkoutPage.clickContinue();
        driver.findElement(By.id("cancel")).click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"));
        System.out.println("✅ Cancel di overview berhasil!");
    }

    @Test
    public void testLogout() {
        loginPage.login("standard_user", "secret_sauce");
        driver.findElement(By.id("react-burger-menu-btn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com"));
        System.out.println("✅ Logout berhasil!");
    }
    /*@Test
    public void testSengajaGagal() {
        loginPage.login("standard_user", "secret_sauce");
        String title = productsPage.getPageTitle();
        Assert.assertEquals(title, "Halaman Salah");
        System.out.println("ini tidak akan terprint");
    }*/
}