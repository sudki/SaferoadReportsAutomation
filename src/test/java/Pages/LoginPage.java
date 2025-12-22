package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== Locators =====
    private final By chatCloseBtn = By.id("zs-tip-close");
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By signInBtn = By.xpath("//button[@type='submit' or contains(.,'Sign In') or contains(.,'Login')]");

    // ===== Public API =====
    public void login(String user, String pass) {

        driver.get("https://track.saferoad.net/auth/signin");

        // اغلق الشات قبل أي تفاعل
        closeChatIfExists();

        WebElement u = wait.until(ExpectedConditions.visibilityOfElementLocated(username));
        u.clear();
        u.sendKeys(user);

        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        p.clear();
        p.sendKeys(pass);

        // أحياناً clickable تفشل بسبب overlay → استخدم JS click كخطة بديلة
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signInBtn)).click();
        } catch (TimeoutException e) {
            jsClick(signInBtn);
        }

        // تأكيد الخروج من صفحة الدخول
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/signin")));
    }

    // ===== Helpers =====
    private void closeChatIfExists() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement close = shortWait.until(ExpectedConditions.presenceOfElementLocated(chatCloseBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", close);
        } catch (Exception ignored) {
            // الشات غير موجود → كمل عادي
        }
    }

    private void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
