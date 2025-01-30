package com.gng.api.runner;

import com.gng.api.context.ApplicationContext;
import com.gng.api.util.LogUtil;
import com.gng.api.report.AllureRestAssuredFilter;
import com.gng.api.report.ExtentReportManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;

import static com.gng.api.context.ApplicationContext.getRequestSpec;
import static com.gng.api.context.ApplicationContext.removeRequestSpec;
import static com.gng.api.context.ApplicationContext.setRequestSpec;

@CucumberOptions(
        features = "src/test/resources/features/phase1/uat2",
        glue = {"com.gng.api.steps"},
        dryRun = false,
        monochrome = true,
      // tags = "@GetUserRolesExpiredPasswordInDB",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
@Slf4j
public class TestRunner extends AbstractTestNGCucumberTests {
    private TestNGCucumberRunner testNGCucumberRunner;

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("*** Setup ***");
        RestAssured.filters(new AllureRestAssuredFilter());
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        ApplicationContext.get().loadEnvConfig();
        LogUtil.configureLogging();
        ExtentReportManager.initialiseExtentReport();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("*** Tear Down ***");
        log.info("Database Connection AutoClosed by JDBCTemplate");
        ExtentReportManager.clearThreadLocals();
        ExtentReportManager.flushReports();
        testNGCucumberRunner.finish();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, Object[] testData) {
        setRequestSpec();
        String scenarioName = "";

        if (testData != null && testData.length > 0 && testData[0] instanceof PickleWrapper pickle) {
            scenarioName = pickle.getPickle().getName();
        }

        ExtentReportManager.createTest(scenarioName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        ExtentReportManager.addRequestDetailsToReport(getRequestSpec());
        ExtentReportManager.generateReport(result);
        removeRequestSpec();
    }
}
