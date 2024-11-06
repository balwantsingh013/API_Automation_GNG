package com.gng.api.runner;

import com.gng.api.config.LogConfig;
import com.gng.api.context.RunContext;
import com.gng.api.report.ExtentReportManager;
import com.gng.api.spec.SetApiSpecification;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.TestNGCucumberRunner;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.gng.api.spec.SetApiSpecification.getRequestSpec;

@CucumberOptions(
        features = "src/test/resources/features", glue = {"com.gng.api.steps"}, dryRun = false,
        //tags = "@GetAccountInfoInvalidParamLength",
        monochrome = true,
        plugin = {"pretty", "json:target/cucumberJson/cucumber.json"}
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
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        RunContext.get().loadEnvConfig();
        LogConfig.configureLogging();
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
    public void beforeMethod(Method method) {
        SetApiSpecification.setRequestSpec();
        Test test = method.getAnnotation(Test.class);
        ExtentReportManager.createTest(method, test);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        ExtentReportManager.addRequestDetailsToReport(getRequestSpec());
        ExtentReportManager.generateReport(result);
        SetApiSpecification.removeRequestSpec();
    }
}