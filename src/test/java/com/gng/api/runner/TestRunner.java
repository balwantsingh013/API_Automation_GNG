package com.gng.api.runner;

import com.gng.api.context.ApplicationContext;
import com.gng.api.util.LogUtil;
import com.gng.api.report.AllureRestAssuredFilter;
import com.gng.api.report.DualReportManager;
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
                "src/test/resources/features/csi/GetAccountInfo.feature"
        },
        glue = {"com.gng.api.steps"},
        dryRun = false,
        monochrome = true,
        //tags = "@UpdateAccountNicknamePositive",
        plugin = {
                "pretty",
                "json:target/cucumberJson/cucumber.json", // ✅ Required for maven-cucumber-reporting
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
    @Parameters({"generateBothReports"})
    public void beforeSuite(@Optional("false") String generateBothReports) {
        log.info("╔═══════════════════════════════════════════════════════════════╗");
        log.info("║           GNG API TEST SUITE - INITIALIZATION                 ║");
        log.info("╚═══════════════════════════════════════════════════════════════╝");

        // CRITICAL: Set system property FIRST, before any initialization
//        log.info("🔍 TestNG Parameter 'generateBothReports': {}", generateBothReports);
//        if ("true".equalsIgnoreCase(generateBothReports)) {
        System.setProperty("generateBothReports", "true");
        log.info("✅ System property set: generateBothReports=true");
//        } else {
//            log.info("ℹ️ Single report mode (Simplified only)");
//        }

        // Configure parallel/sequential execution mode
        configureCompleteExecutionMode();

        // Log final configuration
        logParallelExecutionConfig();

        // Log report configuration (this will now show correct dual mode status)
        logReportConfiguration();

        // Initialize components
        RestAssured.filters(new AllureRestAssuredFilter());
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        ApplicationContext.get().loadEnvConfig();
        LogUtil.configureLogging();

        // ✅ Initialize reports - this reads the system property we just set
        DualReportManager.initialize("");

        log.info("✅ Test Suite Setup Complete");
        log.info("═══════════════════════════════════════════════════════════════\n");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("\n╔═══════════════════════════════════════════════════════════════╗");
        log.info("║           GNG API TEST SUITE - TEARDOWN                       ║");
        log.info("╚═══════════════════════════════════════════════════════════════╝");

        log.info("🔄 Database Connection AutoClosed by JDBCTemplate");

        // Clear thread locals
        DualReportManager.clearThreadLocals();

        // ✅ Flush reports (both or single based on configuration)
        DualReportManager.flush();

        testNGCucumberRunner.finish();

        log.info("✅ Test Suite Teardown Complete");
        log.info("═══════════════════════════════════════════════════════════════\n");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, Object[] testData) {
        setRequestSpec();

        // Extract scenario name safely
        String scenarioName = extractScenarioNameSafely(method, testData)
                .replace("_____"," + ").replace("____",")").replace("___","(").replace("__"," - ").replace("_"," ");

        // Log thread information for monitoring
        logThreadInfo(scenarioName);

        // ✅ Create test using DualReportManager
        DualReportManager.createTest(scenarioName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        try {
            // ✅ Add request and response details to reports using DualReportManager
            if (getRequestSpec() != null) {
                DualReportManager.addRequestDetails(getRequestSpec());
            }
            DualReportManager.generateReport(result);
        } catch (Exception e) {
            log.error("Error in afterMethod: {}", e.getMessage(), e);
        } finally {
            removeRequestSpec();
        }
    }

    /**
     * Complete execution mode configuration
     * Handles all parallel/sequential configuration
     */
    private void configureCompleteExecutionMode() {
        // Read runtime properties
        String parallelMode = System.getProperty("parallel", "none");
        String threadCount = System.getProperty("threadcount", "1");
        String dataProviderThreadCount = System.getProperty("dataproviderthreadcount", threadCount);
        String parallelCount = System.getProperty("parallelcount", threadCount);

        log.info("🔍 Input Configuration:");
        log.info("   parallel: {}", parallelMode);
        log.info("   threadcount: {}", threadCount);
        log.info("   dataproviderthreadcount: {}", dataProviderThreadCount);
        log.info("   parallelcount: {}", parallelCount);

        // Determine execution mode
        boolean shouldRunParallel = !"none".equalsIgnoreCase(parallelMode) &&
                !threadCount.equals("1") &&
                Integer.parseInt(threadCount) > 1;

        if (shouldRunParallel) {
            configureParallelExecution(parallelMode, threadCount, dataProviderThreadCount, parallelCount);
        } else {
            configureSequentialExecution();
        }
    }

    /**
     * Configure parallel execution - all properties set consistently
     */
    private void configureParallelExecution(String parallelMode, String threadCount,
                                            String dataProviderThreadCount, String parallelCount) {

        log.info("🚀 Configuring PARALLEL execution...");

        // Set all TestNG and system properties for parallel execution
        System.setProperty("parallel", parallelMode);
        System.setProperty("threadcount", threadCount);
        System.setProperty("dataproviderthreadcount", dataProviderThreadCount);
        System.setProperty("parallelcount", parallelCount);

        // TestNG internal properties
        System.setProperty("testng.parallel", parallelMode);
        System.setProperty("testng.thread.count", threadCount);
        System.setProperty("testng.data.provider.thread.count", dataProviderThreadCount);
        System.setProperty("testng.preserve.order", "false");
        System.setProperty("testng.use.unlimited.threads", "false");

        // Maven Surefire properties
        System.setProperty("maven.surefire.parallel", parallelMode);
        System.setProperty("maven.surefire.threadCount", threadCount);
        System.setProperty("maven.surefire.dataProviderThreadCount", dataProviderThreadCount);

        // Force TestNG DataProvider to respect parallel settings
        System.setProperty("testng.dataprovider.parallel", "true");

        log.info("✅ PARALLEL execution configured:");
        log.info("   Mode: {} | Threads: {} | DataProvider Threads: {}",
                parallelMode, threadCount, dataProviderThreadCount);
        log.info("   Max Parallelism: {} × {} = {}",
                threadCount, parallelCount,
                Integer.parseInt(threadCount) * Integer.parseInt(parallelCount));
    }

    /**
     * Configure sequential execution - force all parallel settings off
     */
    private void configureSequentialExecution() {
        log.info("🔄 Configuring SEQUENTIAL execution...");

        // Force all parallel settings to sequential values
        System.setProperty("parallel", "none");
        System.setProperty("threadcount", "1");
        System.setProperty("dataproviderthreadcount", "1");
        System.setProperty("parallelcount", "1");

        // TestNG internal properties
        System.setProperty("testng.parallel", "none");
        System.setProperty("testng.thread.count", "1");
        System.setProperty("testng.data.provider.thread.count", "1");
        System.setProperty("testng.preserve.order", "true");

        // Maven Surefire properties
        System.setProperty("maven.surefire.parallel", "none");
        System.setProperty("maven.surefire.threadCount", "1");
        System.setProperty("maven.surefire.dataProviderThreadCount", "1");

        // Force TestNG DataProvider to sequential
        System.setProperty("testng.dataprovider.parallel", "false");

        log.info("✅ SEQUENTIAL execution configured");
    }

    /**
     * Extract scenario name with timeline safety
     */
    private String extractScenarioNameSafely(Method method, Object[] testData) {
        String scenarioName = "";

        // Extract scenario name from Cucumber PickleWrapper
        if (testData != null && testData.length > 0 && testData[0] instanceof PickleWrapper) {
            PickleWrapper pickle = (PickleWrapper) testData[0];
            scenarioName = pickle.getPickle().getName();
        }

        // Fallback to method name if scenario name is empty
        if (scenarioName == null || scenarioName.trim().isEmpty()) {
            scenarioName = method.getName();
        }

        // Clean name for timeline display
        return cleanNameForTimeline(scenarioName);
    }

    /**
     * Clean name for timeline display
     */
    private String cleanNameForTimeline(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "API Test Scenario";
        }

        String cleanName = name.trim();

        // Remove problematic characters that cause timeline issues
        if (cleanName.contains("offsetWidth") || cleanName.matches(".*[<>\"'&].*")) {
            cleanName = cleanName
                    .replaceAll("offsetWidth", "TestScenario")
                    .replaceAll("[<>\"'&]", "")
                    .replaceAll("\\s+", " ");
        }

        // Limit length if excessive
        if (cleanName.length() > 150) {
            cleanName = cleanName.substring(0, 147) + "...";
        }

        return cleanName;
    }

    /**
     * Log report configuration
     */
    private void logReportConfiguration() {
        String generateBoth = System.getProperty("generateBothReports", "true");
        boolean isDualMode = "true".equalsIgnoreCase(generateBoth);

        log.info("╔═══════════════════════════════════════════════════════════════╗");
        log.info("║               REPORT CONFIGURATION                            ║");
        log.info("╚═══════════════════════════════════════════════════════════════╝");
        log.info("🔍 System Property: generateBothReports = {}", generateBoth);
        log.info("🔍 Dual Mode Status: {}", isDualMode);

        if (isDualMode) {
            log.info("📊 Report Mode: DUAL MODE - Generating BOTH reports");
            log.info("📄 Will generate:");
            log.info("   1. GNG-API-Report-Simplified-{datetime}.html");
            log.info("   2. GNG-API-Report-{datetime}.html");
            log.info("");
            log.info("🎯 Simplified Report:");
            log.info("   ✓ Clean UI, hidden timestamps");
            log.info("   ✓ Test descriptions, DB queries");
            log.info("   ✓ Keyboard navigation");
            log.info("");
            log.info("🎯 Detailed Report:");
            log.info("   ✓ Full system info");
            log.info("   ✓ Thread details, statistics");
            log.info("   ✓ Status codes, performance");
        } else {
            log.info("📊 Report Mode: SINGLE MODE (Simplified)");
            log.info("📄 File: GNG-API-Report-Simplified-{datetime}.html");
            log.info("🎯 Features: Clean UI, Keyboard nav");
        }

        log.info("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Enhanced logging - shows complete execution configuration
     */
    private void logParallelExecutionConfig() {
        String parallelMode = System.getProperty("parallel", "none");
        String threadCount = System.getProperty("threadcount", "1");
        String parallelCount = System.getProperty("parallelcount", "1");
        String dataProviderThreadCount = System.getProperty("dataproviderthreadcount", "1");

        log.info("╔═══════════════════════════════════════════════════════════════╗");
        log.info("║         FINAL EXECUTION CONFIGURATION                        ║");
        log.info("╚═══════════════════════════════════════════════════════════════╝");
        log.info("🔧 Parallel Mode: {}", parallelMode);
        log.info("🧵 Thread Count: {}", threadCount);
        log.info("📊 Parallel Count: {}", parallelCount);
        log.info("🔄 DataProvider Threads: {}", dataProviderThreadCount);
        log.info("💻 Available Processors: {}", Runtime.getRuntime().availableProcessors());

        if ("none".equalsIgnoreCase(parallelMode) || "1".equals(threadCount)) {
            log.info("🔄 Execution Mode: SEQUENTIAL");
            log.info("📝 All tests run one after another");
        } else {
            log.info("🚀 Execution Mode: PARALLEL");
            log.info("⚡ Expected Improvement: {}x",
                    Math.min(Integer.parseInt(threadCount), Runtime.getRuntime().availableProcessors()));
            log.info("🎯 Max Concurrent Tests: {}",
                    Integer.parseInt(threadCount) * Integer.parseInt(dataProviderThreadCount));
        }
        log.info("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Thread monitoring - logs thread information for each test
     */
    private void logThreadInfo(String scenarioName) {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        log.debug("🧵 [Thread-{}] [{}] Executing: {}", threadId, threadName, scenarioName);
    }
}