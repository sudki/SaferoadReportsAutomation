package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    // تخمين للـ locators – عدّلها من الـ DevTools عندك إذا لزم
    private By usernameField = By.xpath("//input[@type='text' or @name='username' or contains(@placeholder,'User')]");
    private By passwordField = By.xpath("//input[@type='password' or @name='password']");
    private By loginButton   = By.xpath("//button[contains(.,'Sign in') or contains(.,'Login') or @type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    private void hideChatWidgetIfExists() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "const selectors = ['#zsiqchat', '.zsiq_float', '.zsiq_theme1', '.zsiqf', 'iframe[id*=zsiq]', 'iframe[src*=salesiq]', 'iframe[src*=zoho]'];" +
                            "selectors.forEach(s => document.querySelectorAll(s).forEach(el => el.style.display='none'));" +
                            "document.querySelectorAll('iframe').forEach(f => { try { if((f.src||'').includes('salesiq')||(f.src||'').includes('zoho')||(f.id||'').includes('zsiq')) f.style.display='none'; } catch(e){} });"
            );
        } catch (Exception ignored) {}
    }
    public void login(String username, String password) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);

        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        hideChatWidgetIfExists();
        WebElement btn = driver.findElement(loginButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn); // ✅ كليك JS يتجاوز overlay أحيانًاc
        driver.findElement(loginButton).click();
    }
}
