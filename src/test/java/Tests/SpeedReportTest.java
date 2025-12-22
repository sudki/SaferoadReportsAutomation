package Tests;

import Base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.ReportsPage;

public class SpeedReportTest extends BaseTest{

    @Test
    public void checkOverSpeedReport() {

        LoginPage login = new LoginPage(driver);
        login.login("Demo1234", "12345678");

        ReportsPage reportsPage = new ReportsPage(driver);
        reportsPage.openOverSpeedReport();
        reportsPage.selectAllVehicles();
        reportsPage.clickShowReports();

        Assert.assertTrue(reportsPage.isReportHasData(), "Over Speed Report has no data");
    }

}

