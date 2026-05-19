package com.selenium;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        WebDriver driver = null;

        try {
            driver = (WebDriver) testInstance.getClass()
                .getDeclaredField("driver")
                .get(testInstance);
        } catch (Exception e) {
            System.out.println("Could not get driver: " + e.getMessage());
            return;
        }

        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = result.getName() + "_" + timestamp + ".png";
            Path path = Paths.get("screenshots", fileName);

            try {
                Files.createDirectories(path.getParent());
                Files.write(path, screenshot);
                System.out.println("📸 Screenshot saved: " + path);
            } catch (IOException e) {
                System.out.println("Failed to save screenshot: " + e.getMessage());
            }
        }
    }
}