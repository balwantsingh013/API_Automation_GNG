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

        // EXISTING LOGIC PRESERVED - Enhanced scenario name extraction with timeline safety
        String scenarioName = extractScenarioNameSafely(method, testData);

        // Log thread information for parallel execution monitoring
        logThreadInfo(scenarioName);

        // Create test with timeline-safe name
        ExtentReportManager.createTest(scenarioName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        ExtentReportManager.addRequestDetailsToReport(getRequestSpec());
        ExtentReportManager.generateReport(result);
        removeRequestSpec();
    }

    /**
     * ENHANCED: Extract scenario name with timeline safety - PRESERVES EXISTING LOGIC
     */
    private String extractScenarioNameSafely(Method method, Object[] testData) {
        String scenarioName = "";

        // EXISTING LOGIC PRESERVED
        if (testData != null && testData.length > 0 && testData[0] instanceof PickleWrapper pickle) {
            scenarioName = pickle.getPickle().getName();
        }

        // ADDED: Timeline safety check - only applied if scenarioName is empty or problematic
        if (scenarioName == null || scenarioName.trim().isEmpty()) {
            // Fallback to method name if no scenario name available
            scenarioName = method.getName();
        }

        // ADDED: Clean name to prevent timeline issues (doesn't change valid names)
        return cleanNameForTimeline(scenarioName);
    }

    /**
     * ADDED: Clean name to prevent timeline display issues - MINIMAL CHANGES
     */
    private String cleanNameForTimeline(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "API Test Scenario";
        }

        // Only clean if there are actual problems, preserve good names
        String cleanName = name.trim();

        // Remove only problematic characters that cause timeline issues
        if (cleanName.contains("offsetWidth") || cleanName.matches(".*[<>\"'&].*")) {
            cleanName = cleanName
                    .replaceAll("offsetWidth", "TestScenario")
                    .replaceAll("[<>\"'&]", "")
                    .replaceAll("\\s+", " ");
        }

        // Limit length only if it's excessive (over 150 chars)
        if (cleanName.length() > 150) {
            cleanName = cleanName.substring(0, 147) + "...";
        }

        return cleanName;
    }

    /**
     * EXISTING METHOD PRESERVED - Logs the parallel execution configuration at suite startup
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
     * EXISTING METHOD PRESERVED - Logs thread information for each test method execution
     */
    private void logThreadInfo(String scenarioName) {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        log.debug("🧵 [Thread-{}] [{}] Executing scenario: {}", threadId, threadName, scenarioName);
    }
}