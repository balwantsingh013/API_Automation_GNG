package com.gng.api.report;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;

/**
 * Backward Compatibility Wrapper for ExtentReportManager
 *
 * This class maintains the old ExtentReportManager API while routing all calls
 * to the new DualReportManager. This allows existing code to work without modification.
 *
 * USAGE: Old code can continue using ExtentReportManager.logInfoToReport("message")
 *        It will automatically work with the dual report system.
 */
@Slf4j
public class ExtentReportManager {

    /**
     * Private constructor - this is a utility class with static methods only
     */
    private ExtentReportManager() {
        // Utility class
    }

    /**
     * Initialize extent report - delegates to DualReportManager
     */
    public static synchronized void initialiseExtentReport() {
        DualReportManager.initialize();
    }

    /**
     * Create test - delegates to DualReportManager
     */
    public static void createTest(String scenarioName) {
        DualReportManager.createTest(scenarioName);
    }

    /**
     * Create test with description - delegates to DualReportManager
     */
    public static void createTest(String scenarioName, String description) {
        DualReportManager.createTest(scenarioName, description);
    }

    /**
     * Log info to report - delegates to DualReportManager
     *
     * USAGE: ExtentReportManager.logInfoToReport("Some message")
     */
    public static void logInfoToReport(String msg) {
        DualReportManager.logInfo(msg);
    }

    /**
     * Log error to report - delegates to DualReportManager
     *
     * USAGE: ExtentReportManager.logErrorToReport("Error message")
     */
    public static void logErrorToReport(String msg) {
        DualReportManager.logError(msg);
    }

    /**
     * Log test description - delegates to DualReportManager
     */
    public static void logTestDescription(String description) {
        DualReportManager.logTestDescription(description);
    }

    /**
     * Add request details - delegates to DualReportManager
     */
    public static void addRequestDetailsToReport(RequestSpecification reqSpec) {
        DualReportManager.addRequestDetails(reqSpec);
    }

    /**
     * Add response details - delegates to DualReportManager
     */
    public static void addResponseDetailsToReport(Response resp, int statusCode) {
        DualReportManager.addResponseDetails(resp, statusCode);
    }

    /**
     * Log database query - delegates to DualReportManager
     * This method forwards to SimplifiedExtentReportManager since DualReportManager
     * handles it internally
     */
    public static void logDatabaseQuery(String query, String result, long executionTimeMs) {
        SimplifiedExtentReportManager.logDatabaseQuery(query, result, executionTimeMs);
    }

    /**
     * Log database query with success/error details
     */
    public static void logDatabaseQuery(String query, String result, long executionTimeMs,
                                        boolean isSuccess, String errorMessage) {
        SimplifiedExtentReportManager.logDatabaseQuery(query, result, executionTimeMs, isSuccess, errorMessage);
    }

    /**
     * Generate report - delegates to DualReportManager
     */
    public static void generateReport(ITestResult result) {
        DualReportManager.generateReport(result);
    }

    /**
     * Flush reports - delegates to DualReportManager
     */
    public static synchronized void flushReports() {
        DualReportManager.flush();
    }

    /**
     * Clear thread locals - delegates to DualReportManager
     */
    public static void clearThreadLocals() {
        DualReportManager.clearThreadLocals();
    }

    /**
     * Log API call details immediately
     */
    public static void logApiCallDetails(RequestSpecification reqSpec, Response resp,
                                         int expectedStatusCode, String endpoint) {
        DualReportManager.logApiCallDetails(reqSpec, resp, expectedStatusCode, endpoint);
    }
}