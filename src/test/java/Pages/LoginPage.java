package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    // تخمين للـ locators – عدّلها من الـ DevTools عندك إذا لزم
    private By usernameField = By.xpath("//input[@type='text' or @name='username' or contains(@placeholder,'User')]");
    private By passwordField = By.xpath("//input[@type='password' or @name='password']");
    private By loginButton = By.xpath("//button[contains(.,'Sign in') or contains(.,'Login') or @type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void hideChatWidgetIfExists() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "const selectors = ['#zsiqchat', '.zsiq_float', '.zsiq_theme1', '.zsiqf', 'iframe[id*=zsiq]', 'iframe[src*=salesiq]', 'iframe[src*=zoho]'];" +
                            "selectors.forEach(s => document.querySelectorAll(s).forEach(el => el.style.display='none'));" +
                            "document.querySelectorAll('iframe').forEach(f => { try { if((f.src||'').includes('salesiq')||(f.src||'').includes('zoho')||(f.id||'').includes('zsiq')) f.style.display='none'; } catch(e){} });"
            );
        } catch (Exception ignored) {
        }
    }

    public void login(String user, String pass) {

        // اكتب البيانات
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(user);

        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(pass);

        // حاول مرتين (لأن الـ chat أحياناً يظهر بعد ثانية)
        for (int i = 0; i < 2; i++) {
            try {
                hideZohoChatHard(); // ✅ يخفي الـ chat بالكامل

                WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(15))
                        .until(ExpectedConditions.presenceOfElementLocated(loginButton));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

                // ✅ JS click يتجاوز أي overlay
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

                return; // نجاح

            } catch (ElementClickInterceptedException e) {
                // لو رجع overlay، نخفيه مرة ثانية ونعيد المحاولة
                hideZohoChatHard();
            }
        }

        throw new RuntimeException("Login button still intercepted after hiding Zoho chat.");
    }

    /**
     * إخفاء Zoho SalesIQ / zsiq widgets + iframes بشكل قوي
     */
    private void hideZohoChatHard() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "const sels = [" +
                            "'#zsiqchat'," +
                            "'.zsiq_float'," +
                            "'.zsiq_theme1'," +
                            "'.zsiqf'," +
                            "'.zsiq-m0'," +
                            "'.zsiq-clr6'," +
                            "'[id*=zsiq]'," +
                            "'[class*=zsiq]'," +
                            "'iframe[id*=zsiq]'," +
                            "'iframe[src*=salesiq]'," +
                            "'iframe[src*=zoho]'" +
                            "];" +
                            "sels.forEach(s => document.querySelectorAll(s).forEach(el => {" +
                            "el.style.display='none'; el.style.visibility='hidden'; el.style.pointerEvents='none';" +
                            "}));" +
                            "document.querySelectorAll('iframe').forEach(f => {" +
                            "try {" +
                            "const src=(f.getAttribute('src')||''); const id=(f.id||'');" +
                            "if(src.includes('salesiq') || src.includes('zoho') || id.includes('zsiq')) {" +
                            "f.style.display='none'; f.style.visibility='hidden'; f.style.pointerEvents='none';" +
                            "}" +
                            "} catch(e){}" +
                            "});"
            );
        } catch (Exception ignored) {
        }
    }
}

