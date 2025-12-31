package Tests;

import Base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.ReportsPage;

public class SpeedReportTest extends BaseTest{

    @Test(enabled = false)
    public void checkOverSpeedReport() {

        LoginPage login = new LoginPage(driver);
        login.login("Demo1234", "12345678");

        ReportsPage reportsPage = new ReportsPage(driver);
        reportsPage.openOverSpeedReport();
        reportsPage.selectAllVehicles();
        reportsPage.clickShowReports();

        boolean hasData = reportsPage.isReportHasData();
        System.out.println("📌 Over Speed Report hasData = " + hasData);

        Assert.assertTrue(hasData, "Over Speed Report has NO data");
    }

}

