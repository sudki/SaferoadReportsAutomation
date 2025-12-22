package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ✅ Locators أدق من DOM (يغطي أكثر من احتمال)
    private final By username = By.xpath("//input[@id='username' or @name='username' or @placeholder='Username' or @type='text']");
    private final By password = By.xpath("//input[@id='password' or @name='password' or @placeholder='Password' or @type='password']");

    // زر تسجيل الدخول (أكثر من شرط لتفادي اختلاف النص/اللغة)
    private final By signInBtn = By.xpath("//button[@type='submit' or contains(.,'Sign In') or contains(.,'Login') or contains(.,'تسجيل')]");

    // عنصر يؤكد نجاح الدخول (من الـ sidebar حسب DOM اللي أرسلته)
    private final By reportsSidebarLink = By.xpath("//aside//a[contains(@href,'/reports') and .//span[contains(@class,'item-name') and normalize-space()='Reports']]");

    public void login(String user, String pass) {
        driver.get("https://track.saferoad.net/auth/signin");

        // ✅ إزالة/إخفاء أي overlays قبل التفاعل
        killOverlays();

        WebElement u = wait.until(ExpectedConditions.visibilityOfElementLocated(username));
        u.click();
        u.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        u.sendKeys(user);

        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        p.click();
        p.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        p.sendKeys(pass);

        // ✅ (1) جرّب Submit مضمون عن طريق Enter
        p.sendKeys(Keys.ENTER);

        // لو ما تحركت الصفحة خلال ثواني، جرّب الضغط على الزر + submit form
        try {
            wait.withTimeout(Duration.ofSeconds(6))
                    .until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/signin")));
        } catch (TimeoutException ignored) {
            // ✅ (2) جرّب click عادي بعد إزالة overlays
            killOverlays();
            safeClick(signInBtn);

            // ✅ (3) كخطة أخيرة: submit الفورم مباشرة (أكثر شيء مضمون)
            trySubmitForm();
        } finally {
            // رجّع timeout الأساسي
            wait.withTimeout(Duration.ofSeconds(30));
        }

        // ✅ تأكيد نجاح الدخول: يا URL تغيّر + ظهور عنصر من داخل النظام
        wait.until(driver -> {
            String url = driver.getCurrentUrl();
            if (!url.contains("/auth/signin")) return true;
            return !driver.findElements(reportsSidebarLink).isEmpty();
        });

        // أحيانًا الـ URL يبقى يحتوي params بعد submit فاشل
        // إذا بقيت في صفحة signin بعد كل المحاولات → نرمي خطأ واضح
        if (driver.getCurrentUrl().contains("/auth/signin")) {
            throw new RuntimeException("فشل تسجيل الدخول: ما زلت في صفحة /auth/signin. تحقق من بيانات الدخول أو وجود رسالة خطأ في الصفحة.");
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            // إزالة overlays ثم click بالـ JS
            killOverlays();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void trySubmitForm() {
        try {
            WebElement btn = driver.findElement(signInBtn);
            ((JavascriptExecutor) driver).executeScript(
                    "var f = arguments[0].closest('form'); if(f){f.submit();} else {arguments[0].click();}", btn
            );
        } catch (Exception ignored) {
        }
    }

    /**
     * إخفاء/إزالة Zoho chat + أي overlays تسبب ElementClickIntercepted
     */
    private void killOverlays() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "try {" +
                            // Zoho chat elements
                            "document.querySelectorAll('[class*=\"zsiq\"], [id*=\"zsiq\"], .zsiq_float, .zsiq_theme1, .zsiq_custom').forEach(e=>e.remove());" +
                            // common overlays/modals
                            "document.querySelectorAll('.modal, .overlay, [role=\"dialog\"], [aria-modal=\"true\"]').forEach(e=>e.style.display='none');" +
                            "} catch(e) {}"
            );
        } catch (Exception ignored) {}
    }
}
