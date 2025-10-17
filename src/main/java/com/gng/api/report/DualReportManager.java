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
 *   2. GNG-API-Report-Detailed-{datetime}.html
 */
@Slf4j
public class DualReportManager {

    private static boolean isDualMode = false;
    private static boolean initialized = false;

    /**
     * Initialize report managers based on configuration
     */
    public static synchronized void initialize() {
        if (initialized) {
            log.warn("⚠️ DualReportManager already initialized, skipping...");
            return;
        }

        // Read system property (set from TestNG parameter or Maven command)
        String generateBoth = System.getProperty("generateBothReports", "false");
        isDualMode = "true".equalsIgnoreCase(generateBoth);

        log.info("═══════════════════════════════════════════════════════════════");
        log.info("🔍 DUAL REPORT MANAGER INITIALIZATION");
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("   System Property 'generateBothReports': {}", generateBoth);
        log.info("   Dual Mode Enabled: {}", isDualMode);
        log.info("   Property Source: {}", System.getProperty("generateBothReports") != null ? "System" : "Default");

        if (isDualMode) {
            log.info("╔═══════════════════════════════════════════════════════════════╗");
            log.info("║   DUAL REPORT MODE - Both Simplified & Detailed               ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            try {
                log.info("🔄 Initializing Simplified Report Manager...");
                SimplifiedExtentReportManager.initialiseExtentReport();
                log.info("   ✅ Simplified report manager initialized");
            } catch (Exception e) {
                log.error("   ❌ Failed to initialize Simplified report: {}", e.getMessage(), e);
            }

            try {
                log.info("🔄 Initializing Detailed Report Manager...");
                DetailedExtentReportManager.initialiseExtentReport();
                log.info("   ✅ Detailed report manager initialized");
            } catch (Exception e) {
                log.error("   ❌ Failed to initialize Detailed report: {}", e.getMessage(), e);
            }

            log.info("✅ Both report managers initialized successfully");
        } else {
            log.info("╔═══════════════════════════════════════════════════════════════╗");
            log.info("║   SINGLE REPORT MODE - Simplified Only                        ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            try {
                SimplifiedExtentReportManager.initialiseExtentReport();
                log.info("✅ Simplified report initialized");
            } catch (Exception e) {
                log.error("❌ Failed to initialize Simplified report: {}", e.getMessage(), e);
            }
        }

        initialized = true;
        log.info("═══════════════════════════════════════════════════════════════\n");
    }

    /**
     * Create test
     */
    public static void createTest(String scenarioName) {
        log.debug("Creating test: {} (Dual Mode: {})", scenarioName, isDualMode);

        try {
            SimplifiedExtentReportManager.createTest(scenarioName);
            if (isDualMode) {
                DetailedExtentReportManager.createTest(scenarioName);
            }
        } catch (Exception e) {
            log.error("Error creating test: {}", e.getMessage(), e);
        }
    }

    /**
     * Create test with description
     */
    public static void createTest(String scenarioName, String description) {
        log.debug("Creating test with description: {} (Dual Mode: {})", scenarioName, isDualMode);

        try {
            SimplifiedExtentReportManager.createTest(scenarioName, description);
            if (isDualMode) {
                DetailedExtentReportManager.createTest(scenarioName, description);
            }
        } catch (Exception e) {
            log.error("Error creating test with description: {}", e.getMessage(), e);
        }
    }

    /**
     * Log test description
     */
    public static void logTestDescription(String description) {
        try {
            SimplifiedExtentReportManager.logTestDescription(description);
            // Description only in simplified report
        } catch (Exception e) {
            log.error("Error logging test description: {}", e.getMessage(), e);
        }
    }

    /**
     * Log info
     */
    public static void logInfo(String msg) {
        try {
            SimplifiedExtentReportManager.logInfoToReport(msg);
            if (isDualMode) {
                DetailedExtentReportManager.logInfoToReport(msg);
            }
        } catch (Exception e) {
            log.error("Error logging info: {}", e.getMessage(), e);
        }
    }

    /**
     * Log error
     */
    public static void logError(String msg) {
        try {
            SimplifiedExtentReportManager.logErrorToReport(msg);
            if (isDualMode) {
                DetailedExtentReportManager.logErrorToReport(msg);
            }
        } catch (Exception e) {
            log.error("Error logging error: {}", e.getMessage(), e);
        }
    }

    /**
     * Log database query
     */
    public static void logDatabaseQuery(String query, String result, long executionTimeMs) {
        try {
            SimplifiedExtentReportManager.logDatabaseQuery(query, result, executionTimeMs);
            // Database queries only in simplified report
        } catch (Exception e) {
            log.error("Error logging database query: {}", e.getMessage(), e);
        }
    }

    /**
     * Add request details
     */
    public static void addRequestDetails(RequestSpecification reqSpec) {
        if (reqSpec == null) return;

        try {
            SimplifiedExtentReportManager.addRequestDetailsToReport(reqSpec);
            if (isDualMode) {
                DetailedExtentReportManager.addRequestDetailsToReport(reqSpec);
            }
        } catch (Exception e) {
            log.error("Error adding request details: {}", e.getMessage(), e);
        }
    }

    /**
     * Add response details
     */
    public static void addResponseDetails(Response resp, int statusCode) {
        if (resp == null) return;

        try {
            SimplifiedExtentReportManager.addResponseDetailsToReport(resp, statusCode);
            if (isDualMode) {
                DetailedExtentReportManager.addResponseDetailsToReport(resp, statusCode);
            }
        } catch (Exception e) {
            log.error("Error adding response details: {}", e.getMessage(), e);
        }
    }

    /**
     * Log API call details immediately
     */
    public static void logApiCallDetails(RequestSpecification reqSpec, Response resp,
                                         int expectedStatusCode, String endpoint) {
        try {
            SimplifiedExtentReportManager.logApiCallDetails(reqSpec, resp, expectedStatusCode, endpoint);
            // API call details only in simplified report
        } catch (Exception e) {
            log.error("Error logging API call details: {}", e.getMessage(), e);
        }
    }

    /**
     * Generate report based on test result
     */
    public static void generateReport(ITestResult result) {
        if (result == null) return;

        try {
            SimplifiedExtentReportManager.generateReport(result);
            if (isDualMode) {
                DetailedExtentReportManager.generateReport(result);
            }
        } catch (Exception e) {
            log.error("Error generating report: {}", e.getMessage(), e);
        }
    }

    /**
     * Flush reports
     */
    public static void flush() {
        log.info("\n╔═══════════════════════════════════════════════════════════════╗");

        if (isDualMode) {
            log.info("║         GENERATING DUAL REPORTS                               ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            try {
                log.info("📄 Flushing Simplified Report...");
                SimplifiedExtentReportManager.flushReports();
                log.info("   ✅ Simplified report generated");
            } catch (Exception e) {
                log.error("   ❌ Error generating Simplified report: {}", e.getMessage(), e);
            }

            try {
                log.info("📄 Flushing Detailed Report...");
                DetailedExtentReportManager.flushReports();
                log.info("   ✅ Detailed report generated");
            } catch (Exception e) {
                log.error("   ❌ Error generating Detailed report: {}", e.getMessage(), e);
            }

            log.info("✅ Both reports generated!");
            log.info("📁 Check target/reports/ for:");
            log.info("   1. GNG-API-Report-Simplified-{datetime}.html");
            log.info("   2. GNG-API-Report-Detailed-{datetime}.html");
        } else {
            log.info("║         GENERATING SINGLE REPORT (Simplified)                 ║");
            log.info("╚═══════════════════════════════════════════════════════════════╝");

            try {
                SimplifiedExtentReportManager.flushReports();
                log.info("✅ Simplified report generated!");
                log.info("📁 Check target/reports/GNG-API-Report-Simplified-{datetime}.html");
            } catch (Exception e) {
                log.error("❌ Error generating Simplified report: {}", e.getMessage(), e);
            }
        }

        log.info("╚═══════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Clear thread locals
     */
    public static void clearThreadLocals() {
        try {
            SimplifiedExtentReportManager.clearThreadLocals();
            if (isDualMode) {
                DetailedExtentReportManager.clearThreadLocals();
            }
        } catch (Exception e) {
            log.error("Error clearing thread locals: {}", e.getMessage(), e);
        }
    }
}