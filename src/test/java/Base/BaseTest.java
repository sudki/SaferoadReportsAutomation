package Base;

import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;


public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("headless=new");
            options.addArguments("no-sandbox");
            options.addArguments("disable-dev-shm-usage");
            options.addArguments("window-size=1920,1080");
        }
        // تشغيل Chrome بوضع Guest (بدون Google Account / Sync)
        options.addArguments("--guest");

        // تعطيل أي نوافذ أو تنبيهات
        options.addArguments("disable-notifications");
        options.addArguments("disable-save-password-bubble");
        options.addArguments("disable-features=PasswordLeakDetection,PasswordManagerOnboarding,PasswordChange");

        // تعطيل Password Manager نهائياً
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://track.saferoad.net/auth/signin");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
