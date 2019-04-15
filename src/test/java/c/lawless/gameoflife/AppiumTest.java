package c.lawless.gameoflife;

import io.appium.java_client.PerformsActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.annotation.Order;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Onur Baskirt on 05.04.2016.
 * Appropriated and updated by Christopher Lawless 2019
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppiumTest {

    public static AndroidDriver driver;
    public static WebDriverWait wait;

   // @BeforeAll
   @Test
   @Order(1)
    public static void setUp() throws MalformedURLException {
        //Setup Appium
        DesiredCapabilities caps = DesiredCapabilities.android();
        caps.setCapability(MobileCapabilityType.APP, "c.lawless.gameoflife");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME,"AndroidOreo");
        caps.setCapability("platformName", "Android");
        caps.setCapability("appActivity", "MainActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        wait= new WebDriverWait(driver,20);
    }

    @Test
    @Order(2)
    public void SomeTaps() throws MalformedURLException, InterruptedException,URISyntaxException {

        synchronized (driver)
        {
            driver.wait(5000);
        }

        //We wait for Pause button to be visible to start
        wait.until(ExpectedConditions.elementToBeClickable(By.id("c.lawless.gameoflife:id/pause")));

        TouchAction touchAction = new TouchAction(driver).press(PointOption.point(429, 581)).release();


        driver.performTouchAction(touchAction);

        // driver.tap




        // driver.
        //  driver.tap(1, 581, 581,2);

        //just to let things play out for a few seconds.
        synchronized (driver)
        {
            driver.wait(5000);
        }
    }

    @Test
    @Order(3)
    public void PauseAndSave() throws MalformedURLException, InterruptedException,URISyntaxException {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("c.lawless.gameoflife:id/pause")));
        driver.findElement(By.id("c.lawless.gameoflife:id/pause")).click();
        driver.findElement(By.id("c.lawless.gameoflife:id/save")).click();
    }

    @Test
    @Order(4)
    public void LifeLoader() throws MalformedURLException, InterruptedException,URISyntaxException {

        wait.until(ExpectedConditions.elementToBeClickable(By.id("c.lawless.gameoflife:id/loader")));
        driver.findElement(By.id("c.lawless.gameoflife:id/loader")).click();

        synchronized (driver)
        {
            driver.wait(5000);
        }

    }

//    @AfterAll
//    public static void teardown(){
//        //Teardown operation
//        driver.quit();
//    }
}
