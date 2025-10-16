package com.gng.api.report;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;

/**
 * Dual Report Manager - Generates BOTH Simplified and Detailed reports simultaneously
 *
 * USAGE: Set system property -DgenerateBothReports=true
 *
 * This will generate TWO reports:
 *   1. GNG-API-Report-Simplified-{datetime}.html
 *   2. GNG-API-Report-{datetime}.html
 */
@Slf4j
public class DualReportManager {

    private static boolean isDualMode = false;

    /**
     * Initialize report managers based on configuration
     */
    public static synchronized void initialize() {
        // Read system property (set from TestNG parameter or Maven command)
        String generateBoth = System.getProperty("generateBothReports", "true");
        isDualMode = "true".equalsIgnoreCase(generateBoth);

        log.info("🔍 Initializing Report Manager...");
        log.info("   System Property 'generateBothReports': {}", generateBoth);
        log.info("   Dual Mode Enabled: {}", isDualMode);

        if (isDualMode) {
            log.info("╔═══════════════════════════════════════════════════════════════╗");
            log.info("║   DUAL REPORT MODE - Both Simplified & Detailed               ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            SimplifiedExtentReportManager.initialiseExtentReport();
            log.info("   ✅ Simplified report manager initialized");

            DetailedExtentReportManager.initialiseExtentReport();
            log.info("   ✅ Detailed report manager initialized");

            log.info("✅ Both reports initialized successfully");
        } else {
            log.info("📊 Single Report Mode (Simplified)");
            SimplifiedExtentReportManager.initialiseExtentReport();
            log.info("✅ Simplified report initialized");
        }
    }

    /**
     * Create test
     */
    public static void createTest(String scenarioName) {
        if (isDualMode) {
            SimplifiedExtentReportManager.createTest(scenarioName);
            DetailedExtentReportManager.createTest(scenarioName);
        } else {
            SimplifiedExtentReportManager.createTest(scenarioName);
        }
    }

    /**
     * Create test with description
     */
    public static void createTest(String scenarioName, String description) {
        if (isDualMode) {
            SimplifiedExtentReportManager.createTest(scenarioName, description);
            DetailedExtentReportManager.createTest(scenarioName, description);
        } else {
            SimplifiedExtentReportManager.createTest(scenarioName, description);
        }
    }

    /**
     * Log test description
     */
    public static void logTestDescription(String description) {
        if (isDualMode) {
            SimplifiedExtentReportManager.logTestDescription(description);
        } else {
            SimplifiedExtentReportManager.logTestDescription(description);
        }
    }

    /**
     * Log info
     */
    public static void logInfo(String msg) {
        if (isDualMode) {
            SimplifiedExtentReportManager.logInfoToReport(msg);
            DetailedExtentReportManager.logInfoToReport(msg);
        } else {
            SimplifiedExtentReportManager.logInfoToReport(msg);
        }
    }

    /**
     * Log error
     */
    public static void logError(String msg) {
        if (isDualMode) {
            SimplifiedExtentReportManager.logErrorToReport(msg);
            DetailedExtentReportManager.logErrorToReport(msg);
        } else {
            SimplifiedExtentReportManager.logErrorToReport(msg);
        }
    }

    /**
     * Log database query
     */
    public static void logDatabaseQuery(String query, String result, long executionTimeMs) {
        if (isDualMode) {
            SimplifiedExtentReportManager.logDatabaseQuery(query, result, executionTimeMs);
        } else {
            SimplifiedExtentReportManager.logDatabaseQuery(query, result, executionTimeMs);
        }
    }

    /**
     * Add request details
     */
    public static void addRequestDetails(RequestSpecification reqSpec) {
        if (reqSpec == null) return;

        if (isDualMode) {
            SimplifiedExtentReportManager.addRequestDetailsToReport(reqSpec);
            DetailedExtentReportManager.addRequestDetailsToReport(reqSpec);
        } else {
            SimplifiedExtentReportManager.addRequestDetailsToReport(reqSpec);
        }
    }

    /**
     * Add response details
     */
    public static void addResponseDetails(Response resp, int statusCode) {
        if (resp == null) return;

        if (isDualMode) {
            SimplifiedExtentReportManager.addResponseDetailsToReport(resp, statusCode);
            DetailedExtentReportManager.addResponseDetailsToReport(resp, statusCode);
        } else {
            SimplifiedExtentReportManager.addResponseDetailsToReport(resp, statusCode);
        }
    }

    /**
     * Log API call details immediately
     */
    public static void logApiCallDetails(RequestSpecification reqSpec, Response resp,
                                         int expectedStatusCode, String endpoint) {
        if (isDualMode) {
            SimplifiedExtentReportManager.logApiCallDetails(reqSpec, resp, expectedStatusCode, endpoint);
        } else {
            SimplifiedExtentReportManager.logApiCallDetails(reqSpec, resp, expectedStatusCode, endpoint);
        }
    }

    /**
     * Generate report based on test result
     */
    public static void generateReport(ITestResult result) {
        if (result == null) return;

        if (isDualMode) {
            SimplifiedExtentReportManager.generateReport(result);
            DetailedExtentReportManager.generateReport(result);
        } else {
            SimplifiedExtentReportManager.generateReport(result);
        }
    }

    /**
     * Flush reports
     */
    public static void flush() {
        if (isDualMode) {
            log.info("\n╔═══════════════════════════════════════════════════════════════╗");
            log.info("║         GENERATING DUAL REPORTS                               ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            SimplifiedExtentReportManager.flushReports();
            DetailedExtentReportManager.flushReports();

            log.info("✅ Both reports generated!");
            log.info("📁 Check target/reports for both HTML files");
            log.info("╚═══════════════════════════════════════════════════════════════╝\n");
        } else {
            SimplifiedExtentReportManager.flushReports();
        }
    }

    /**
     * Clear thread locals
     */
    public static void clearThreadLocals() {
        if (isDualMode) {
            SimplifiedExtentReportManager.clearThreadLocals();
            DetailedExtentReportManager.clearThreadLocals();
        } else {
            SimplifiedExtentReportManager.clearThreadLocals();
        }
    }
}