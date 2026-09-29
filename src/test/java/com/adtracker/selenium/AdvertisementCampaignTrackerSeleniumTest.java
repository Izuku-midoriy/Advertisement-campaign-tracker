package com.adtracker.selenium;

import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import com.adtracker.advertisement_campaign_tracker.AdvertisementCampaignTrackerApplication;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AdvertisementCampaignTrackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
public class AdvertisementCampaignTrackerSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    private static final String CAMPAIGN_NAME =
            "Selenium Test Campaign";

   @BeforeAll
void setUp() {
    ChromeOptions options = new ChromeOptions();

    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");

    driver = new ChromeDriver(options);

    wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(15)
    );

    driver.get(getBaseUrl());
}

    @AfterAll
    void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void verifyApplicationLoads() {

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.tagName("body")
                )
        );

        String title = driver.getTitle();

        assertEquals(
                "INTENTIONAL FAILURE",
                title
        );

        WebElement heading =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("header h1")
                        )
                );

        assertEquals(
                "Advertisement Campaign Tracker",
                heading.getText()
        );
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void verifyCampaignsAreDisplayed() {

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("campaignTableBody")
                )
        );

        WebElement tableBody =
                driver.findElement(
                        By.id("campaignTableBody")
                );

        assertNotNull(
                tableBody,
                "Campaign table body should be present"
        );
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void createCampaign() {

        fillCampaignForm();

        WebElement submitButton =
                driver.findElement(
                        By.id("submitButton")
                );

        submitButton.click();

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("message"),
                        "Campaign created successfully"
                )
        );

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("campaignTableBody"),
                        CAMPAIGN_NAME
                )
        );

        assertTrue(
                driver.findElement(
                        By.id("campaignTableBody")
                )
                .getText()
                .contains(CAMPAIGN_NAME)
        );
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void verifyDashboardStatistics() {

        WebElement totalCampaigns =
                driver.findElement(
                        By.id("totalCampaigns")
                );

        WebElement activeCampaigns =
                driver.findElement(
                        By.id("activeCampaigns")
                );

        assertFalse(
                totalCampaigns.getText().isBlank()
        );

        assertFalse(
                activeCampaigns.getText().isBlank()
        );

        assertTrue(
                Integer.parseInt(
                        totalCampaigns.getText()
                ) >= 1
        );
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void editCampaign() {

        WebElement editButton =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath(
                                    "//tr[td/strong[text()='" +
                                    CAMPAIGN_NAME +
                                    "']]//button[contains(@class,'edit')]"
                                )
                        )
                );

        editButton.click();

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("formTitle"),
                        "Edit Campaign"
                )
        );

        WebElement advertiser =
                driver.findElement(
                        By.id("advertiser")
                );

        advertiser.clear();

        advertiser.sendKeys(
                "Selenium Updated Advertiser"
        );

        WebElement updateButton =
                driver.findElement(
                        By.id("submitButton")
                );

        assertEquals(
                "Update Campaign",
                updateButton.getText()
        );

        updateButton.click();

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("message"),
                        "Campaign updated successfully"
                )
        );

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("campaignTableBody"),
                        "Selenium Updated Advertiser"
                )
        );
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void deleteCampaign() {

        WebElement deleteButton =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath(
                                    "//tr[td/strong[text()='" +
                                    CAMPAIGN_NAME +
                                    "']]//button[contains(@class,'danger')]"
                                )
                        )
                );

        deleteButton.click();

        /*
         * Handle JavaScript confirmation dialog.
         */
        wait.until(
                ExpectedConditions.alertIsPresent()
        );

        driver.switchTo()
                .alert()
                .accept();

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("message"),
                        "Campaign deleted successfully"
                )
        );

        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.textToBePresentInElementLocated(
                                By.id("campaignTableBody"),
                                CAMPAIGN_NAME
                        )
                )
        );

        assertFalse(
                driver.findElement(
                        By.id("campaignTableBody")
                )
                .getText()
                .contains(CAMPAIGN_NAME)
        );
    }

    private void fillCampaignForm() {

        WebElement campaignName =
                driver.findElement(
                        By.id("campaignName")
                );

        campaignName.clear();

        campaignName.sendKeys(
                CAMPAIGN_NAME
        );

        WebElement advertiser =
                driver.findElement(
                        By.id("advertiser")
                );

        advertiser.clear();

        advertiser.sendKeys(
                "Selenium Test Advertiser"
        );

        WebElement budget =
                driver.findElement(
                        By.id("budget")
                );

        budget.clear();

        budget.sendKeys(
                "50000"
        );

        Select platform =
                new Select(
                        driver.findElement(
                                By.id("platform")
                        )
                );

        platform.selectByVisibleText(
                "Google Ads"
        );

        WebElement startDate =
                driver.findElement(
                        By.id("startDate")
                );

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '2026-09-29';", startDate
        );

        WebElement endDate =
                driver.findElement(
                        By.id("endDate")
                );

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '2026-10-29';", endDate
        );

        Select status =
                new Select(
                        driver.findElement(
                                By.id("status")
                        )
                );

        status.selectByVisibleText(
                "ACTIVE"
        );
    }
}