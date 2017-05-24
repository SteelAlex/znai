package com.twosigma.testing.webtau.driver;

import com.twosigma.testing.webtau.cfg.WebUiTestConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class WebDriverCreator {
    private static final WebUiTestConfig cfg = WebUiTestConfig.INSTANCE;

    private static List<WebDriver> drivers = new ArrayList<>();

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        WebDriverCreatorListeners.beforeDriverCreation();

        ChromeDriver driver = createChromeDriver();
        initState(driver);

        return register(driver);
    }

    private static ChromeDriver createChromeDriver() {
        return new ChromeDriver();
    }

    public static void closeAll() {
        drivers.forEach(WebDriver::close);
        drivers.clear();
    }

    private static WebDriver register(WebDriver driver) {
        drivers.add(driver);
        WebDriverCreatorListeners.afterDriverCreation(driver);

        return driver;
    }

    private static void initState(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(cfg.getWindowWidth(), cfg.getWindowHeight()));
    }

    private static void registerCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(WebDriverCreator::closeAll));
    }
}