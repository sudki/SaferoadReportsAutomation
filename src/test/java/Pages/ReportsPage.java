package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ReportsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ReportsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }


    private By reportsMenu = By.xpath("/html/body/div[1]/aside/div[2]/div/ul/li[4]/a");

    private By utilizationReportsMenu = By.id("Utilization_reports_key");

    private By tripReportOption = By.xpath("//*[contains(.,'Trip Report') and (self::a or self::button or self::span)]");

    private By vehiclesDropdown = By.xpath("//span[contains(@class, 'rc-tree-checkbox')]");

    private By showReportsButton = By.xpath("//button[contains(.,'Show Reports') or contains(.,'Show Report')]");

    private By resultsTableRows = By.xpath("//div[contains(@class,'ag-center-cols-container')]//div[contains(@class,'ag-row') and contains(@class,'ag-row-level-0')]");

    public void openTripReport() {
        // 3- Reports
        wait.until(ExpectedConditions.elementToBeClickable(reportsMenu)).click();
        // 4- Utilization reports
        wait.until(ExpectedConditions.elementToBeClickable(utilizationReportsMenu)).click();
        // 5- Trip Report
        wait.until(ExpectedConditions.elementToBeClickable(tripReportOption)).click();
    }

    public void selectAllVehicles() {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(vehiclesDropdown));
            dropdown.click();
            //wait.until(ExpectedConditions.elementToBeClickable(vehiclesAllOption)).click();
        } catch (TimeoutException e) {

            throw new RuntimeException("لم يتم العثور على قائمة السيارات أو خيار All", e);
        }
    }

    public void clickShowReports() {
        wait.until(ExpectedConditions.elementToBeClickable(showReportsButton)).click();
    }

    public boolean isReportHasData() {
        try {
            List<WebElement> rows = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultsTableRows));
            return !rows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    // القائمة الرئيسية للتقارير (Already exists in your project)
    private By menuReports = By.xpath("/html/body/div[1]/aside/div[2]/div/ul/li[4]/a");

    // قائمة Speeding Reports
    private By speedingReportsMenu = By.id("Speeding_reports_key");

    // خيار Over Speed Report
    private By overSpeedReport = By.id("Over_Speed_Report_key");

    private By vehiclesSpeedDropdown = By.xpath("//span[contains(@class, 'rc-tree-checkbox')]");

    //  عرض التقرير
    private By showSpeedReportsButton = By.xpath("//button[contains(.,'Filter') or contains(.,'Show Reports')]");

    // صفوف الجدول
    private By tableSpeedRows = By.xpath("//div[contains(@class,'ag-center-cols-container')]//div[contains(@class,'ag-row') and contains(@class,'ag-row-level-0')]");

    public void openOverSpeedReport() {
        // فتح قائمة Reports
        wait.until(ExpectedConditions.elementToBeClickable(menuReports)).click();

        // فتح Speeding Reports
        wait.until(ExpectedConditions.elementToBeClickable(speedingReportsMenu)).click();

        // اختيار Over Speed Report
        wait.until(ExpectedConditions.elementToBeClickable(overSpeedReport)).click();

    }
    public void selectAllSpeedVehicles() {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(vehiclesSpeedDropdown));
            dropdown.click();
            //wait.until(ExpectedConditions.elementToBeClickable(vehiclesAllOption)).click();
        } catch (TimeoutException e) {

            throw new RuntimeException("لم يتم العثور على قائمة السيارات أو خيار All", e);
        }
    }
    public void clickFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(showSpeedReportsButton)).click();
    }
    public boolean speedHasData() {
        List<WebElement> rows = driver.findElements(tableSpeedRows);
        return rows.size() > 0;
    }

}
