package Pages;

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
    private By selectAllVehicles = By.xpath("//span[contains(@class,'rc-tree-checkbox')][1]");
    private By showReportsBtn = By.xpath("//button[contains(@class,'btn-primary') and contains(.,'Show Reports')]");
    private By tableRows = By.cssSelector(".ag-center-cols-container .ag-row.ag-row-level-0");

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

    public void selectAllVehicles() {
        jsClick(selectAllVehicles);
    }

    public void clickShowReports() {
        jsClick(showReportsBtn);
    }

    public boolean isReportHasData() {
        try {
            List<WebElement> rows =
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
            return !rows.isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }
    //overspeeding report
    public void openOverSpeedReport() {
        jsClick(sidebarToggleBtn);
        jsClick(reportsMenu);
        jsClick(openOverSpeedReport);
        jsClick(SpeedReport);
    }
}
