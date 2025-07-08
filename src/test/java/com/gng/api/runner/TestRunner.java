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
        features = {
                "src/test/resources/features/phase1/turnOff"
        },
        glue = {"com.gng.api.steps"},
        dryRun = false,
        monochrome = true,
        //tags = "@validAGLCAccountNumberRSActiveAccount",
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
        log.info("*** Test Suite Setup ***");

        // Log parallel execution configuration
        logParallelExecutionConfig();

        // Initialize components
        RestAssured.filters(new AllureRestAssuredFilter());
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        ApplicationContext.get().loadEnvConfig();
        LogUtil.configureLogging();
        ExtentReportManager.initialiseExtentReport();

        log.info("*** Test Suite Setup Complete ***");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("*** Test Suite Teardown ***");
        log.info("Database Connection AutoClosed by JDBCTemplate");
        ExtentReportManager.clearThreadLocals();
        ExtentReportManager.flushReports();
        testNGCucumberRunner.finish();
        log.info("*** Test Suite Teardown Complete ***");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, Object[] testData) {
        setRequestSpec();
        String scenarioName = "";

        if (testData != null && testData.length > 0 && testData[0] instanceof PickleWrapper pickle) {
            scenarioName = pickle.getPickle().getName();
        }

        // Log thread information for parallel execution monitoring
        logThreadInfo(scenarioName);

        ExtentReportManager.createTest(scenarioName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        ExtentReportManager.addRequestDetailsToReport(getRequestSpec());
        ExtentReportManager.generateReport(result);
        removeRequestSpec();
    }

    /**
     * Logs the parallel execution configuration at suite startup
     */
    private void logParallelExecutionConfig() {
        String parallelMode = System.getProperty("parallel", "none");
        String threadCount = System.getProperty("threadcount", "1");
        String parallelCount = System.getProperty("parallelcount", "1");

        log.info("═══════════════════════════════════════════════════════════════");
        log.info("               PARALLEL EXECUTION CONFIGURATION");
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("🔧 Parallel Mode: {}", parallelMode);
        log.info("🧵 Thread Count: {}", threadCount);
        log.info("📊 Parallel Count: {}", parallelCount);
        log.info("💻 Available Processors: {}", Runtime.getRuntime().availableProcessors());

        if ("none".equalsIgnoreCase(parallelMode) || "1".equals(threadCount)) {
            log.info("🔄 Execution Mode: SEQUENTIAL");
        } else {
            log.info("🚀 Execution Mode: PARALLEL");
            log.info("⚡ Expected Performance Improvement: {}x",
                    Math.min(Integer.parseInt(threadCount), Runtime.getRuntime().availableProcessors()));
        }
        log.info("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Logs thread information for each test method execution
     */
    private void logThreadInfo(String scenarioName) {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        log.debug("🧵 [Thread-{}] [{}] Executing scenario: {}", threadId, threadName, scenarioName);
    }
}