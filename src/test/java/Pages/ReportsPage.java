package Pages;

import Tests.WHM_Reports;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class ReportsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ReportsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }
    private By sidebarToggleBtn = By.xpath("//div[@class='sidebar-toggle' and @data-toggle='sidebar']");
    private By reportsMenu = By.xpath("//span[@class='item-name' and normalize-space()='Reports']");
    private By utilizationReports = By.id("Utilization_reports_key");
    private By tripReport = By.id("trip_Report");
    private By openOverSpeedReport = By.id("Speeding_reports_key");
    private By SpeedReport = By.id("Over_Speed_Report_key");
    private By WHMReport = By.id("hours_And_Mileage_Daily_Reports");
    private By selectAllVehicles = By.xpath("//span[contains(@class,'rc-tree-checkbox')][1]");
    private By showReportsBtn = By.xpath("//button[contains(@class,'btn-primary') and contains(.,'Show Reports')]");
    private By tableRows = By.cssSelector(".ag-center-cols-container .ag-row.ag-row-level-0");
    private By agNoRowsOverlay = By.cssSelector(".ag-overlay-no-rows-center");
    private By agLoadingOverlay = By.cssSelector(".ag-overlay-loading-center");

    private void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void openTripReport() {
        jsClick(sidebarToggleBtn);
        jsClick(reportsMenu);
        jsClick(utilizationReports);
        jsClick(tripReport);
    }
    //overspeeding report
    public void openOverSpeedReport() {
        jsClick(sidebarToggleBtn);
        jsClick(reportsMenu);
        jsClick(openOverSpeedReport);
        jsClick(SpeedReport);
    }
    //working hours mailage report
    public void openWHM_Reports(){
        jsClick(sidebarToggleBtn);
        jsClick(reportsMenu);
        jsClick(utilizationReports);
        jsClick(WHMReport);
    }

    public void selectAllVehicles() {
        jsClick(selectAllVehicles);
    }

    public void clickShowReports() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(showReportsBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        // جرّب click عادي، لو انحجب → JS click
        try {
            wait.until(ExpectedConditions.elementToBeClickable(showReportsBtn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }

        System.out.println("✅ Clicked: Show Reports");
    }

    public boolean isReportHasData() {
        try {
            wait.until(driver -> {

                // 1️⃣ لو لسه فيه Loading لا نعتبرها نتيجة
                boolean loading = !driver.findElements(agLoadingOverlay).isEmpty();
                if (loading) return false;

                // 2️⃣ تحقق من وجود صفوف أو رسالة no rows
                int rows = driver.findElements(tableRows).size();
                boolean noRows = !driver.findElements(agNoRowsOverlay).isEmpty();

                return rows > 0 || noRows;
            });

            int finalRowsCount = driver.findElements(tableRows).size();

            if (finalRowsCount > 0) {
                System.out.println("✅ SUCCESS: Report contains data. Rows count = " + finalRowsCount);
                return true;
            } else {
                System.out.println("⚠️ INFO: Report loaded but NO data found (No Rows Overlay shown)");
                return false;
            }

        } catch (TimeoutException e) {
            System.out.println("❌ ERROR: Timeout while waiting for report data to load");
            return false;
        }
    }



}
