package com.gng.api.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.gng.api.context.ApplicationContext;
import com.gng.api.util.CommonUtil;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gng.api.constants.TestConstant.REPORT_PATH;
import static com.gng.api.util.CommonUtil.getCurrentDateTimeFormatted;

@Slf4j
public class SimplifiedExtentReportManager {

    private static final long suiteStartTime = System.currentTimeMillis();

    // Thread-safe ThreadLocal variables for parallel execution
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> extentLogger = new ThreadLocal<>();
    private static final ThreadLocal<QueryableRequestSpecification> qReqSpec = new ThreadLocal<>();
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> expectedStatusCode = new ThreadLocal<>();
    private static final ThreadLocal<Long> testStartTime = new ThreadLocal<>();

    // Thread-safe statistics tracking for parallel execution
    private static final AtomicInteger totalTests = new AtomicInteger(0);
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);
    private static final AtomicInteger skippedTests = new AtomicInteger(0);
    private static final Map<String, Integer> statusCodeCounts = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> threadTestCounts = new ConcurrentHashMap<>();

    private static ExtentReports extent;
    private static ExtentSparkReporter spark;

//    private ExtentReportManager() {
//    }

    // Store database query details in ThreadLocal
    private static final ThreadLocal<List<DatabaseQuery>> databaseQueries = ThreadLocal.withInitial(ArrayList::new);

    // Inner class to store database query information
    private static class DatabaseQuery {
        String query;
        String result;
        long executionTime;
        boolean isSuccess;
        String errorMessage;

        DatabaseQuery(String query, String result, long executionTime, boolean isSuccess, String errorMessage) {
            this.query = query;
            this.result = result;
            this.executionTime = executionTime;
            this.isSuccess = isSuccess;
            this.errorMessage = errorMessage;
        }
    }

    // Method to log database query execution
    public static void logDatabaseQuery(String query, String result, long executionTimeMs, boolean isSuccess, String errorMessage) {
        DatabaseQuery dbQuery = new DatabaseQuery(query, result, executionTimeMs, isSuccess, errorMessage);
        databaseQueries.get().add(dbQuery);

        // Immediately log to current test
        ExtentTest logger = extentLogger.get();
        if (logger != null) {
            logger.info(formatDatabaseQueryLog(dbQuery));
        }
    }

    // Overloaded method for successful queries
    public static void logDatabaseQuery(String query, String result, long executionTimeMs) {
        logDatabaseQuery(query, result, executionTimeMs, true, null);
    }

    // Format database query for display
    private static String formatDatabaseQueryLog(DatabaseQuery dbQuery) {
        StringBuilder log = new StringBuilder();

        log.append("<div class='db-log'>");
        log.append("  <div class='db-log-header'>");
        log.append("    <h4>").append(dbQuery.isSuccess ? "✅" : "❌").append(" Database Query</h4>");
        log.append("  </div>");
        log.append("  <div class='collapsible-content'>");


        // Query Details
        log.append("<div class='request-detail-item'>");
        log.append("<strong>📋 SQL Query:</strong><br/>");
        log.append("<div class='code-block'>").append(formatSqlQuery(dbQuery.query)).append("</div>");
        log.append("</div>");

        // Result or Error
        if (dbQuery.isSuccess) {
            log.append("<div class='response-detail-item'>");
            log.append("<strong>📊 Query Result:</strong><br/>");
            log.append("<div class='code-block'>").append(formatDatabaseResult(dbQuery.result)).append("</div>");
            log.append("</div>");
        } else {
            log.append("<div class='error-section'>");
            log.append("<strong>❌ Error:</strong><br/>");
            log.append("<div style='background: rgba(255,255,255,0.15); padding: 15px; border-radius: 10px; margin-top: 10px;'>");
            log.append(dbQuery.errorMessage != null ? dbQuery.errorMessage : "Unknown error");
            log.append("</div>");
            log.append("</div>");
        }

        log.append("</div></div>");
        return log.toString();
    }

    // Format SQL query with basic syntax highlighting
    private static String formatSqlQuery(String query) {
        if (query == null || query.isEmpty()) {
            return "No query provided";
        }

        // Basic SQL formatting
        return query
                .replaceAll("(?i)\\b(SELECT|FROM|WHERE|JOIN|LEFT|RIGHT|INNER|OUTER|ON|AND|OR|ORDER BY|GROUP BY|HAVING|LIMIT|OFFSET|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP)\\b",
                        "<span style='color: #3b82f6; font-weight: bold;'>$1</span>")
                .replace("\n", "<br/>")
                .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
    }

    // Format database result (handles different result types)
    private static String formatDatabaseResult(String result) {
        if (result == null || result.isEmpty()) {
            return "No results";
        }

        // If result looks like JSON, format it
        if (result.trim().startsWith("{") || result.trim().startsWith("[")) {
            return formatJson(result);
        }

        return result.replace("\n", "<br/>");
    }

    // Get all database queries for current test
    public static String getAllDatabaseQueriesLog() {
        List<DatabaseQuery> queries = databaseQueries.get();
        if (queries.isEmpty()) {
            return "";
        }

        StringBuilder allQueries = new StringBuilder();
        allQueries.append("<div class='db-summary'>");
        allQueries.append("<h3>📊 Database Queries Summary (").append(queries.size()).append(" queries)</h3>");

        for (int i = 0; i < queries.size(); i++) {
            allQueries.append("<div style='margin: 10px 0;'>");
            allQueries.append("<strong>Query #").append(i + 1).append(":</strong>");
            allQueries.append(formatDatabaseQueryLog(queries.get(i)));
            allQueries.append("</div>");
        }

        allQueries.append("</div>");
        return allQueries.toString();
    }

    public static void logTestDescription(String description) {
        ExtentTest currentTest = test.get();
        if (currentTest != null) {
            String html = "<div class='test-description-block'>"
                    + "<div class='test-description-title'>📝 Test Description:</div>"
                    + "<div class='test-description-text'>" + description + "</div>"
                    + "</div>";
            currentTest.getModel().setDescription(html);
        }
    }




    public static synchronized void initialiseExtentReport(String config) {
        if (extent == null) {
            extent = new ExtentReports();
            if (config != null && !config.isEmpty()) {
                spark = new ExtentSparkReporter(REPORT_PATH + "GNG- " + config + ".html");
            }
            else{
                spark = new ExtentSparkReporter(REPORT_PATH + "GNG-API-Report-Client-" + CommonUtil.getCurrentDateTime() + ".html");

            }
            setConfig();
            log.info("📊 Simplified Extent Report initialized");
        }
    }

    private static void setConfig() {
        spark.config().setDocumentTitle("GNG API Test Report - Professional Dashboard");
        spark.config().setReportName("GNG API Automation Test Results");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setOfflineMode(true);
        spark.config().setEncoding("utf-8");

        // ❌ remove timestamp format (we don’t need it at all)
        // spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        // ✅ CSS override to hide timestamp column
        spark.config().setCss(getProfessionalCSSWithUpdatedColumns() + """
    /* Force hide timestamp column header & cells */
    th.timestamp,
    td.timestamp,
    td[class*="timestamp"],
    th[class*="timestamp"],
    .status.timestamp {
        display: none !important;
        visibility: hidden !important;
        width: 0 !important;
        max-width: 0 !important;
        overflow: hidden !important;
    }

    /* Stretch the log message cell full width */
    .test-steps-table td.step-details {
        width: 100% !important;
        display: block !important;
    }
""");

        spark.config().setJs(getTimelineFixJavaScript() + """
    document.addEventListener("DOMContentLoaded", function() {
        function removeTimestamps() {
            // Remove header if present
            document.querySelectorAll("th.timestamp, td.timestamp").forEach(el => el.remove());

            // Fix each event row
            document.querySelectorAll("tr.event-row").forEach(row => {
                // If row has exactly 3 cells → [status][time][details]
                if (row.children.length === 3) {
                    row.children[1].remove(); // remove only the time cell
                }
            });
        }
        removeTimestamps();

        // Watch for future dynamic updates
        const observer = new MutationObserver(removeTimestamps);
        observer.observe(document.body, { childList: true, subtree: true });
    });
""");

        extent.attachReporter(spark);
        setSystemInfo();
    }


    private static String getProfessionalCSSWithUpdatedColumns() {
        return """
    /* === CORE STYLES === */
    body { 
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important; 
        background: #f8fafc !important;
        font-size: 13px !important; /* Increased from 12px for better readability */
    }
    
    /* === TIMELINE FIXES ONLY - ADDED TO EXISTING STYLES === */
    /* Fix timeline display issues without affecting other functionality */
    .timeline-view .card-panel,
    .timeline-container .card-panel,
    .timeline .card-panel {
        background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
        color: white !important;
        box-shadow: 0 4px 20px rgba(45, 55, 72, 0.3) !important;
        min-height: 40px !important; /* Slightly increased from 35px */
        overflow: visible !important;
    }
    
    /* CRITICAL: Fix test name display in timeline - preserve existing test names */
    .timeline-view .card-panel .card-title,
    .timeline-view .card-panel .card-title *,
    .timeline-view .card-panel .card-title a,
    .timeline-view .card-panel .card-title a.node,
    .timeline-view .card-panel .card-title span,
    .timeline-view .card-panel .card-title .node span,
    .timeline .test-name,
    .timeline .scenario-name,
    .timeline-item .test-title,
    .timeline-item .node-name,
    .timeline-item .card-title,
    .timeline-item .card-title * {
        color: white !important;
        font-weight: 600 !important;
        font-size: 14px !important; /* Increased from 9px */
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
        text-decoration: none !important;
        display: inline-block !important;
        max-width: 400px !important;
        overflow: hidden !important;
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
        line-height: 1.4 !important;
    }
    
                /* === FORCE ERROR SECTION TEXT TO BE WHITE === */
                .error-section,
                .error-section *,
                .error-section h1,
                .error-section h2,
                .error-section h3,
                .error-section h4,
                .error-section h5,
                .error-section h6,
                .error-section p,
                .error-section span,
                .error-section div,
                .error-section strong {
                    color: white !important;
                    text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
                }
    
    /* Prevent timeline layout issues */
    .timeline-container,
    .timeline-view,
    .timeline {
        overflow-x: auto !important;
        overflow-y: visible !important;
        min-height: 110px !important; /* Increased from 100px */
        position: relative !important;
    }
    
    .timeline-bar,
    .timeline-item {
        position: relative !important;
        display: block !important;
        min-width: 0 !important;
        word-wrap: break-word !important;
        overflow: visible !important;
    }
    
    /* CRITICAL: Fix offsetWidth issues - this is the main problem */
    [style*="offsetWidth"] {
        width: auto !important;
        max-width: 350px !important;
        overflow: hidden !important;
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
    
    /* Prevent JavaScript width calculation issues */
    .timeline-view [style*="offsetWidth"],
    .timeline-container [style*="offsetWidth"],
    .timeline [style*="offsetWidth"] {
        width: auto !important;
        max-width: 300px !important;
        overflow: hidden !important;
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
    
    /* === UNIFIED HEADER SYSTEM === */
    /* Primary header gradient and styling */
    :is(.brand-logo) { display: none; }
    
    :is(
        .nav-wrapper,
        .navbar-brand,
        .container .card-panel:not(.stats-card),
        .container .row .col .card-panel:not(.stats-card),
        .card-panel[style*="background"]:not(.stats-card),
        .container > .card-panel:not(.stats-card),
        .row .col .card-panel:not(.stats-card),
        .test-node .node-name,
        .timeline-view .card-panel,
        .category-view .card-panel,
        .dashboard-view .card-panel,
        .test-detail .node-name,
        .extent-test-node .node-name,
        .extent-category .category-name,
        [data-toggle="timeline"] .card-panel,
        [data-toggle="system-view"] .card-panel,
        .timeline-container .card-panel,
        .system-container .card-panel
    ) {
        background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
        color: white !important;
        box-shadow: 0 4px 20px rgba(45, 55, 72, 0.3) !important;
    }
    
    /* FIXED: Universal header text styling - covers ALL header scenarios */
    :is(
        .navbar-brand,
        .container .card-panel:not(.stats-card) *,
        .row .col .card-panel:not(.stats-card) *,
        .card-panel[style*="background"]:not(.stats-card) *,
        .test-node *, .category-node *, .node-name *, .category-name *,
        .test-detail-header *, .test-step-name *, .extent-node-name *,
        [data-toggle="timeline"] .card-panel *,
        [data-toggle="system-view"] .card-panel *,
        .timeline-container .card-panel *,
        .system-container .card-panel *,
        .card-header,
        .card-header *,
        .card-header p,
        .card-header span,
        .card-header div,
        .card-header h1,
        .card-header h2,
        .card-header h3,
        .card-header h4,
        .card-header h5,
        .card-header h6
    ) {
        color: white !important;
        font-weight: 700 !important;
        font-size: 10px !important; /* Increased from 9px */
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
    }
    
    /* Navbar brand specific styling */
    .navbar-brand { 
        font-size: 14px !important; /* Increased from 12px */
        text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
    }
    
    /* Header text sizing hierarchy */
    :is(.container .card-panel:not(.stats-card), .row .col .card-panel:not(.stats-card)) :is(h1, h2, h3) {
        font-size: 12px !important; /* Increased from 11px */
        margin: 7px 0 !important; /* Increased from 6px */
        padding: 7px !important; /* Increased from 6px */
        text-align: center !important;
    }
    
    /* === ENHANCED TEST DETAILS VISIBILITY - UPDATED COLUMN STYLING === */
    /* Better column separation and visibility for STATUS, TIMESTAMP, DETAILS */
    .test-details-container,
    .test-info-container,
    .test-execution-details {
        display: grid !important;
        grid-template-columns: 130px 220px 1fr !important; /* Increased column widths */
        gap: 25px !important; /* Increased gap */
        padding: 18px !important; /* Increased padding */
        background: linear-gradient(135deg, #f8fafc, #e2e8f0) !important;
        border-radius: 14px !important; /* Increased radius */
        margin: 12px 0 !important; /* Increased margin */
        border: 1px solid #cbd5e1 !important;
        box-shadow: 0 3px 10px rgba(0,0,0,0.08) !important; /* Enhanced shadow */
    }
    
    /* FIXED: Column headers with light, subtle backgrounds and PROMINENT text */
                    .status-column, .timestamp-column, .details-column,
                    .test-status, .test-timestamp, .test-details {
                        padding: 10px 15px !important; /* Increased padding */
                        border-radius: 10px !important; /* Increased radius */
                        font-weight: 500 !important; /* Bold but not too heavy */
                        font-size: 14px !important; /* Increased from 14px for better visibility */
                        text-transform: uppercase !important;
                        letter-spacing: 0.6px !important;
                        color: #1f2937 !important; /* Dark gray for high contrast and visibility */
                        text-shadow: 0 1px 1px rgba(0,0,0,0.1) !important; /* Minimal shadow for depth */
                    }
    
    /* STATUS column styling - Light blue background */
    .status-column, .test-status {
        background: rgba(59, 130, 246, 0.1) !important; /* Very light blue */
        color: #1e40af !important; /* Dark blue text */
        text-align: center !important;
        border: 1px solid rgba(59, 130, 246, 0.2) !important;
        box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1) !important;
        text-transform: uppercase !important;
        letter-spacing: 0.6px !important;
        font-weight: 800 !important;
    }
    
    /* TIMESTAMP column styling - Light green background (FIXED: removed heavy background) */
    .timestamp-column, .test-timestamp {
        background: rgba(5, 150, 105, 0.1) !important; /* Very light green */
        color: #059669 !important; /* Dark green text */
        text-align: center !important;
        border: 1px solid rgba(5, 150, 105, 0.2) !important;
        box-shadow: 0 2px 4px rgba(5, 150, 105, 0.1) !important;
        font-family: 'Consolas', 'Monaco', 'Courier New', monospace !important;
        font-size: 10px !important; /* Increased size */
        font-weight: 700 !important;
        letter-spacing: 0.6px !important;
    }
    
    /* DETAILS column styling - Light purple background */
    .details-column, .test-details {
        background: rgba(124, 58, 237, 0.1) !important; /* Very light purple */
        color: #7c3aed !important; /* Dark purple text */
        border: 1px solid rgba(124, 58, 237, 0.2) !important;
        box-shadow: 0 2px 4px rgba(124, 58, 237, 0.1) !important;
        font-weight: 600 !important;
        font-size: 10px !important;
    }
    
    /* === TABLE HEADER STYLING === */
    /* Specific styling for table headers in the three columns */
    th.status-col, .status-col {
        background: rgba(59, 130, 246, 0.15) !important; /* Slightly more visible than body */
        color: #1e40af !important;
        font-weight: 800 !important;
        text-align: center !important;
        padding: 12px 8px !important;
        border-radius: 8px !important;
        border: 1px solid rgba(59, 130, 246, 0.3) !important;
        text-transform: uppercase !important;
        letter-spacing: 0.8px !important;
        font-size: 9px !important;
    }
    
    th.timestamp-col, .timestamp-col {
        background: rgba(5, 150, 105, 0.15) !important; /* Slightly more visible than body */
        color: #059669 !important;
        font-weight: 800 !important;
        text-align: center !important;
        padding: 12px 8px !important;
        border-radius: 8px !important;
        border: 1px solid rgba(5, 150, 105, 0.3) !important;
        font-family: 'Consolas', 'Monaco', monospace !important;
        text-transform: uppercase !important;
        letter-spacing: 0.8px !important;
        font-size: 9px !important;
    }
    
    th.details-col, .details-col {
        background: rgba(124, 58, 237, 0.15) !important; /* Slightly more visible than body */
        color: #7c3aed !important;
        font-weight: 800 !important;
        text-align: center !important;
        padding: 12px 8px !important;
        border-radius: 8px !important;
        border: 1px solid rgba(124, 58, 237, 0.3) !important;
        text-transform: uppercase !important;
        letter-spacing: 0.8px !important;
        font-size: 9px !important;
    }
    
    /* === TABLE BODY CELL STYLING === */
    /* Light backgrounds for table body cells */
    td.status-col, tbody .status-col {
        background: rgba(59, 130, 246, 0.05) !important; /* Very subtle blue */
        color: #1e40af !important;
        text-align: center !important;
        padding: 10px 8px !important;
        border-radius: 6px !important;
        font-weight: 600 !important;
    }
    
    td.timestamp-col, tbody .timestamp-col {
        background: rgba(5, 150, 105, 0.05) !important; /* Very subtle green */
        color: #059669 !important;
        text-align: center !important;
        padding: 10px 8px !important;
        border-radius: 6px !important;
        font-family: 'Consolas', 'Monaco', monospace !important;
        font-weight: 600 !important;
        letter-spacing: 0.5px !important;
    }
    
    td.details-col, tbody .details-col {
        background: rgba(124, 58, 237, 0.05) !important; /* Very subtle purple */
        color: #7c3aed !important;
        padding: 10px 8px !important;
        border-radius: 6px !important;
        font-weight: 500 !important;
    }
    
    /* === ENHANCED TIMESTAMP FORMATTING - FIXED === */
    /* Universal timestamp styling for proper DD-MM-YYYY HH:MM:SS format */
    .execution-timestamp,
    .test-timestamp,
    .time-display,
    .duration-display,
    .timestamp,
    .test-time,
    .start-time,
    .end-time,
    .suite-timestamp,
    .report-timestamp,
    [class*="time"],
    [class*="timestamp"],
    [class*="duration"] {
        font-family: 'Consolas', 'Monaco', 'Courier New', monospace !important;
        font-size: 11px !important; /* Increased from 9px for better readability */
        font-weight: 700 !important; /* Made bolder */
        letter-spacing: 0.8px !important; /* Increased spacing */
        background: rgba(5, 150, 105, 0.1) !important; /* Light green background instead of heavy gradient */
        color: #059669 !important; /* Dark green text for better contrast */
        padding: 6px 12px !important; /* Increased padding */
        border-radius: 10px !important; /* Increased border radius */
        display: inline-block !important;
        border: 1px solid rgba(5, 150, 105, 0.2) !important;
        box-shadow: 0 2px 4px rgba(5, 150, 105, 0.1) !important; /* Light shadow */
        text-shadow: none !important; /* Removed text shadow for cleaner look */
        white-space: nowrap !important;
        min-width: 140px !important; /* Minimum width for proper display */
        text-align: center !important;
    }
    
    /* FIXED: Special timestamp styling for main header - lighter approach */
    .enhanced-stats-header .execution-timestamp {
        background: rgba(255, 255, 255, 0.15) !important; /* Light background instead of heavy styling */
        color: white !important;
        font-size: 12px !important; /* Larger for main header */
        font-weight: 700 !important; /* Reduced from 800 */
        padding: 8px 16px !important; /* More padding */
        border: 1px solid rgba(255, 255, 255, 0.2) !important; /* Thinner border */
        backdrop-filter: blur(10px) !important; /* Reduced blur */
        letter-spacing: 0.8px !important; /* Reduced spacing */
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important; /* Lighter shadow */
        border-radius: 10px !important; /* Reduced radius */
    }
    
    /* === TEST EXECUTION STEPS HEADER REDUCTION === */
    /* Specific targeting for Test Execution Steps headers */
    .test-node .node-name,
    .test-detail .node-name,
    .test-step .node-name,
    .test-execution .node-name,
    .extent-test-node .node-name,
    .test-node h1, .test-node h2, .test-node h3, .test-node h4,
    .test-detail h1, .test-detail h2, .test-detail h3, .test-detail h4 {
        font-size: 14px !important; /* Increased from 9px */
        padding: 5px 10px !important; /* Increased padding */
        margin: 4px 0 !important; /* Increased from 3px */
        line-height: 1.3 !important;
    }
    
    /* Test case names - make them readable but compact */
    .test-name, .scenario-name, .feature-name,
    .test-node .node-name, .test-node .test-name,
    .test-node .scenario-name, .test-node .feature-name,
    .test-node a, .test-node span, .test-node div {
        font-size: 14px !important; /* Increased from 9px */
        font-weight: 500 !important;
        line-height: 1.4 !important;
        max-width: 350px !important; /* Increased width */
        overflow: hidden !important;
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
.test-description-block {
    margin-top: 25px;     /* smaller top margin to avoid overlap */
    margin-bottom: 0px; /* reduced bottom gap */
    padding: 15px;       /* keep padding comfortable */
    background: #ffffff;
    border: 2px solid #2E86C1;
    border-radius: 10px;
    box-shadow: 0 3px 8px rgba(0,0,0,0.1);
    font-size: 15px !important;
    font-weight: 600 !important;
    color: #2E86C1 !important;
    line-height: 1.4 !important;
}


.test-description-title {
    font-size: 15px !important;    /* reduced from 17px */
    font-weight: 700 !important;
    margin-bottom: 8px;            /* reduced from 10px */
    color: #1f2937 !important;
}

.test-description-text {
    font-size: 14px !important;    /* reduced from 16px */
    color: #2E86C1 !important;
}


                
    
    /* === COMPONENT STYLES === */
    /* Thread Information */
    .thread-info {
        background: linear-gradient(135deg, #7c3aed, #5b21b6) !important;
        color: white !important;
        padding: 7px 14px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased radius */
        font: 600 9px 'Segoe UI', sans-serif !important; /* Increased from 8px */
        display: inline-block !important;
        margin: 5px 0 !important; /* Increased margin */
        box-shadow: 0 3px 10px rgba(124, 58, 237, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
    }
    
    /* Enhanced Statistics Header */
    .enhanced-stats-header {
        background: linear-gradient(135deg, #1e40af 0%, #3b82f6 50%, #60a5fa 100%) !important;
        color: white !important;
        padding: 18px !important; /* Increased from 15px */
        border-radius: 14px !important; /* Increased from 12px */
        margin-bottom: 18px !important; /* Increased from 15px */
        text-align: center !important;
        box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4) !important; /* Enhanced shadow */
        border: 1px solid rgba(255, 255, 255, 0.2) !important;
    }
    
    .stats-title {
        font: 700 17px 'Segoe UI', sans-serif !important; /* Increased from 15px */
        margin: 0 !important;
        color: white !important;
        text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
        letter-spacing: 0.3px !important; /* Increased from 0.2px */
    }
    
    .execution-timestamp {
        font: 500 11px 'Segoe UI', sans-serif !important; /* Increased from 10px */
        margin-top: 8px !important; /* Increased from 6px */
        opacity: 0.95 !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
    }
    
    /* Dashboard Grid */
    .stats-dashboard {
        display: grid !important;
        grid-template-columns: repeat(auto-fit, minmax(380px, 1fr)) !important; /* Increased from 350px */
        gap: 18px !important; /* Increased from 15px */
        margin: 18px 0 !important; /* Increased from 15px */
    }
    
    /* Enhanced Card System */
    .stats-card {
        background: white !important;
        border-radius: 14px !important; /* Increased from 12px */
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08) !important; /* Enhanced shadow */
        border: 1px solid #e5e7eb !important;
        overflow: hidden !important;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
    }
    
    .stats-card:hover {
        transform: translateY(-5px) scale(1.01) !important; /* Increased from -4px */
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12) !important; /* Enhanced shadow */
        border-color: #3b82f6 !important;
    }
    
    /* Card Headers */
    .card-header {
        background: linear-gradient(135deg, #1f2937, #374151) !important;
        color: white !important;
        padding: 15px !important; /* Increased from 12px */
        text-align: center !important;
        position: relative !important;
    }
    
    .card-header::before {
        content: '' !important;
        position: absolute !important;
        top: 0 !important;
        left: 0 !important;
        right: 0 !important;
        height: 3px !important; /* Increased from 2px */
        background: linear-gradient(90deg, #3b82f6, #8b5cf6, #06b6d4) !important;
    }
    
    /* FIXED: Card Title - Target all nested elements including links and spans */
    :is(
        .card-title,
        .card-title *,
        .card-title a,
        .card-title a.node,
        .card-title span,
        .card-title .node,
        .card-title .node span,
        .card .card-header .card-title,
        .card .card-header .card-title *,
        .card .card-header .card-title a,
        .card .card-header .card-title a.node,
        .card .card-header .card-title span,
        .card .card-header .card-title .node,
        .card .card-header .card-title .node span,
        div.card div.card-header div.card-title a.node span
    ) {
        font: 700 12px 'Segoe UI', sans-serif !important; /* Increased from 11px */
        margin: 0 !important;
        color: white !important;
        text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
        letter-spacing: 0.2px !important; /* Increased from 0.1px */
        text-decoration: none !important;
        line-height: 1.3 !important;
        display: block !important;
        border: none !important;
        outline: none !important;
        background: none !important;
        box-shadow: none !important;
    }
    
    /* FIXED: Override any link styling within card headers */
    :is(
        .card-header a,
        .card-header a:link,
        .card-header a:visited,
        .card-header a:hover,
        .card-header a:active,
        .card-header a.node,
        .card-header .card-title a,
        .card-header .card-title a.node
    ) {
        color: white !important;
        text-decoration: none !important;
        border: none !important;
        outline: none !important;
        background: transparent !important;
        box-shadow: none !important;
    }
    
    .card-body { padding: 18px !important; background: white !important; } /* Increased from 15px */
    
    /* === STATISTICS LAYOUT SYSTEM === */
    /* Flex row system for all stat types */
    :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row) {
        display: flex !important;
        justify-content: space-between !important;
        align-items: center !important;
        padding: 15px 0 !important; /* Increased from 12px */
        border-bottom: 2px solid #f1f5f9 !important;
        margin-bottom: 12px !important; /* Increased from 10px */
        position: relative !important;
        min-height: 40px !important; /* Increased from 35px */
        background: linear-gradient(90deg, transparent, #f8fafc, transparent) !important;
        border-radius: 8px !important; /* Increased from 6px */
    }
    
    :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row):last-child {
        border-bottom: none !important;
        margin-bottom: 0 !important;
    }
    
    /* Label system */
    :is(.stat-label, .thread-label, .status-code-label, .perf-label) {
        font: 700 11px 'Segoe UI', sans-serif !important; /* Increased from 10px */
        color: #1f2937 !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.05) !important;
        display: flex !important;
        align-items: center !important;
    }
    
    .stat-label { flex: 0 0 40% !important; white-space: nowrap !important; }
    .thread-label { flex: 2 !important; font-size: 10px !important; } /* Increased from 9px */
    .status-code-label { flex: 2 !important; font-size: 10px !important; } /* Increased from 9px */
    
    /* Value containers and alignment */
    .stat-value-container {
        display: flex !important;
        align-items: center !important;
        gap: 15px !important; /* Increased from 12px */
        flex: 1 !important;
        justify-content: flex-end !important;
        min-height: 35px !important; /* Increased from 30px */
    }
    
    :is(.thread-value, .status-code-value) {
        flex: 1 !important;
        text-align: center !important;
        font: 800 11px 'Segoe UI', sans-serif !important; /* Increased from 10px */
        color: #1e40af !important;
        text-shadow: 0 1px 3px rgba(30, 64, 175, 0.3) !important;
    }
    
    .thread-value { color: #1e40af !important; }
    .status-code-value { color: #1f2937 !important; }
    
    /* === VALUE STYLING SYSTEM === */
    /* Base stat values */
    .stat-value {
        font: 800 17px 'Segoe UI', sans-serif !important; /* Increased from 15px */
        min-width: 50px !important; /* Increased from 45px */
        text-align: center !important;
        text-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
    }
    
    /* Stat value color variants */
    .total-count { color: #1e40af !important; font-size: 20px !important; text-shadow: 0 2px 6px rgba(30, 64, 175, 0.3) !important; } /* Increased from 18px */
    .passed-count { color: #059669 !important; text-shadow: 0 2px 4px rgba(5, 150, 105, 0.3) !important; }
    .failed-count { color: #dc2626 !important; text-shadow: 0 2px 4px rgba(220, 38, 38, 0.3) !important; }
    .skipped-count { color: #d97706 !important; text-shadow: 0 2px 4px rgba(217, 119, 6, 0.3) !important; }
    
    /* === PERCENTAGE BADGE SYSTEM === */
    /* Base percentage styling */
    :is(.stat-percentage, .thread-percentage, .status-code-percentage) {
        font: 700 10px 'Segoe UI', sans-serif !important; /* Increased from 9px */
        padding: 5px 12px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased from 16px */
        color: white !important;
        min-width: 60px !important; /* Increased from 55px */
        text-align: center !important;
        box-shadow: 0 3px 10px rgba(0,0,0,0.2) !important; /* Enhanced shadow */
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
    }
    
    :is(.thread-percentage, .status-code-percentage) {
        flex: 1 !important;
        font-size: 9px !important; /* Increased from 8px */
        color: #6b7280 !important;
        background: none !important;
        box-shadow: none !important;
        border: none !important;
        text-shadow: none !important;
        font-weight: 600 !important;
    }
    
    /* Percentage color variants */
    .passed-percentage { background: linear-gradient(135deg, #059669, #10b981) !important; box-shadow: 0 3px 12px rgba(5, 150, 105, 0.4) !important; }
    .failed-percentage { background: linear-gradient(135deg, #dc2626, #ef4444) !important; box-shadow: 0 3px 12px rgba(220, 38, 38, 0.4) !important; }
    .skipped-percentage { background: linear-gradient(135deg, #d97706, #f59e0b) !important; box-shadow: 0 3px 12px rgba(217, 119, 6, 0.4) !important; }
    
    /* === PROGRESS BAR SYSTEM === */
    /* Base progress bar */
    :is(.progress-bar, .thread-progress-bar, .status-progress-bar) {
        height: 10px !important; /* Increased from 8px */
        background: #f1f5f9 !important;
        border-radius: 12px !important; /* Increased from 10px */
        overflow: hidden !important;
        border: 1px solid #e2e8f0 !important;
        box-shadow: inset 0 2px 4px rgba(0,0,0,0.05) !important;
    }
    
    .progress-bar { width: 100% !important; margin-top: 12px !important; } /* Increased from 10px */
    :is(.thread-progress-bar, .status-progress-bar) { 
        flex: 2 !important; 
        height: 8px !important; /* Increased from 6px */
        margin-left: 15px !important; /* Increased from 12px */
    }
    
    /* Progress fill variants */
    :is(.progress-fill, .thread-progress-fill, .status-progress-fill) {
        height: 100% !important;
        border-radius: 12px !important; /* Increased from 10px */
        transition: width 1s cubic-bezier(0.4, 0, 0.2, 1) !important;
        position: relative !important;
        box-shadow: 0 2px 6px rgba(0,0,0,0.2) !important; /* Enhanced shadow */
    }
    
    .passed-progress { background: linear-gradient(90deg, #059669, #10b981, #34d399) !important; }
    .failed-progress { background: linear-gradient(90deg, #dc2626, #ef4444, #f87171) !important; }
    .skipped-progress { background: linear-gradient(90deg, #d97706, #f59e0b, #fbbf24) !important; }
    .thread-progress-fill { background: linear-gradient(90deg, #7c3aed, #8b5cf6, #a855f7) !important; }
    
    /* Status-specific progress colors */
    .success-status { background: linear-gradient(90deg, #059669, #10b981) !important; }
    .redirect-status { background: linear-gradient(90deg, #1e40af, #3b82f6) !important; }
    .client-error-status { background: linear-gradient(90deg, #dc2626, #ef4444) !important; }
    .server-error-status { background: linear-gradient(90deg, #374151, #4b5563) !important; }
    .unknown-status { background: linear-gradient(90deg, #6b7280, #9ca3af) !important; }
    
    /* === SUCCESS RATE SECTION === */
    .success-rate-section {
        background: linear-gradient(135deg, #1e40af, #3b82f6, #60a5fa) !important;
        color: white !important;
        padding: 15px !important; /* Increased from 12px */
        border-radius: 12px !important; /* Increased from 10px */
        text-align: center !important;
        margin-top: 18px !important; /* Increased from 15px */
        box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4) !important; /* Enhanced shadow */
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    .success-rate-label {
        font: 700 12px 'Segoe UI', sans-serif !important; /* Increased from 10px */
        margin-bottom: 10px !important; /* Increased from 8px */
        text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
    }
    
    .success-rate-value {
        font: 900 24px 'Segoe UI', sans-serif !important; /* Increased from 20px */
        padding: 10px 20px !important; /* Increased padding */
        border-radius: 24px !important; /* Increased from 20px */
        display: inline-block !important;
        margin-top: 8px !important; /* Increased from 6px */
        text-shadow: 0 3px 6px rgba(0,0,0,0.4) !important;
        border: 2px solid rgba(255,255,255,0.3) !important;
    }
    
    .success-rate-value.excellent { background: linear-gradient(135deg, #059669, #10b981) !important; box-shadow: 0 6px 16px rgba(5, 150, 105, 0.5) !important; }
    .success-rate-value.good { background: linear-gradient(135deg, #d97706, #f59e0b) !important; box-shadow: 0 6px 16px rgba(217, 119, 6, 0.5) !important; }
    .success-rate-value.needs-improvement { background: linear-gradient(135deg, #dc2626, #ef4444) !important; box-shadow: 0 6px 16px rgba(220, 38, 38, 0.5) !important; }
    
    /* === PERFORMANCE METRICS === */
    .perf-value {
        font: 800 10px 'Segoe UI', sans-serif !important; /* Increased from 9px */
        padding: 7px 14px !important; /* Increased padding */
        border-radius: 14px !important; /* Increased from 12px */
        color: white !important;
        text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
        box-shadow: 0 3px 10px rgba(0,0,0,0.2) !important; /* Enhanced shadow */
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    .execution-mode { background: linear-gradient(135deg, #1e40af, #3b82f6) !important; }
    .thread-count { background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important; }
    .parallel-count { background: linear-gradient(135deg, #059669, #10b981) !important; }
    .core-count { background: linear-gradient(135deg, #0891b2, #06b6d4) !important; }
    
    /* === COMPREHENSIVE STATUS CODE STYLING === */
    
    /* 1xx Informational - Light Blue */
    :is(.status-100, .status-101, .status-102, .status-103) { 
        background: linear-gradient(135deg, #0ea5e9, #38bdf8) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(14, 165, 233, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* 2xx Success - Green (Enhanced your existing ones) */
    :is(.status-200, .status-201, .status-202, .status-203, .status-204, .status-205, .status-206, .status-207, .status-208, .status-226) { 
        background: linear-gradient(135deg, #059669, #10b981) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(5, 150, 105, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* 3xx Redirection - Orange/Yellow */
    :is(.status-300, .status-301, .status-302, .status-303, .status-304, .status-305, .status-307, .status-308) { 
        background: linear-gradient(135deg, #f59e0b, #fbbf24) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(245, 158, 11, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* 4xx Client Errors - Red (Enhanced your existing ones) */
    :is(.status-400, .status-401, .status-402, .status-403, .status-404, .status-405, .status-406, .status-407, .status-408, .status-409, .status-410, .status-411, .status-412, .status-413, .status-414, .status-415, .status-416, .status-417, .status-418, .status-421, .status-422, .status-423, .status-424, .status-425, .status-426, .status-428, .status-429, .status-431, .status-451) { 
        background: linear-gradient(135deg, #dc2626, #ef4444) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(220, 38, 38, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* 5xx Server Errors - Dark Gray (Enhanced your existing ones) */
    :is(.status-500, .status-501, .status-502, .status-503, .status-504, .status-505, .status-506, .status-507, .status-508, .status-510, .status-511) { 
        background: linear-gradient(135deg, #374151, #4b5563) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(55, 65, 81, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* Unknown Status Codes - Purple */
    .status-unknown { 
        background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important; 
        color: white !important; 
        padding: 4px 10px !important;
        border-radius: 14px !important;
        font-weight: 700 !important;
        font-size: 9px !important;
        box-shadow: 0 3px 10px rgba(124, 58, 237, 0.4) !important;
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* === PERFORMANCE INDICATOR STYLING === */
    .excellent-performance { 
        background: linear-gradient(135deg, #059669, #10b981) !important; 
        box-shadow: 0 3px 12px rgba(5, 150, 105, 0.5) !important; 
    }
    .good-performance { 
        background: linear-gradient(135deg, #0ea5e9, #38bdf8) !important; 
        box-shadow: 0 3px 12px rgba(14, 165, 233, 0.5) !important; 
    }
    .average-performance { 
        background: linear-gradient(135deg, #f59e0b, #fbbf24) !important; 
        box-shadow: 0 3px 12px rgba(245, 158, 11, 0.5) !important; 
    }
    .slow-performance { 
        background: linear-gradient(135deg, #dc2626, #ef4444) !important; 
        box-shadow: 0 3px 12px rgba(220, 38, 38, 0.5) !important; 
    }
    .very-slow-performance { 
        background: linear-gradient(135deg, #7c2d12, #dc2626) !important; 
        box-shadow: 0 3px 12px rgba(124, 45, 18, 0.5) !important; 
    }
    
    .content-type-badge {
        background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
        color: white !important;
        padding: 4px 10px !important;
        border-radius: 12px !important;
        font-weight: 600 !important;
        font-size: 9px !important;
        box-shadow: 0 2px 8px rgba(124, 58, 237, 0.3) !important;
    }
    
    .response-size-badge {
        background: linear-gradient(135deg, #0891b2, #06b6d4) !important;
        color: white !important;
        padding: 4px 10px !important;
        border-radius: 12px !important;
        font-weight: 600 !important;
        font-size: 9px !important;
        box-shadow: 0 2px 8px rgba(8, 145, 178, 0.3) !important;
    }
    
    /* === TEST STATUS STYLING === */
    .test-node .status-pass { 
        background: linear-gradient(135deg, #059669, #10b981) !important;
        color: white !important;
        padding: 5px 12px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased from 16px */
        font-weight: 700 !important;
        font-size: 14px !important; /* Increased from 9px */
        box-shadow: 0 4px 12px rgba(5, 150, 105, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    .test-node .status-fail { 
        background: linear-gradient(135deg, #dc2626, #ef4444) !important;
        color: white !important;
        padding: 5px 12px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased from 16px */
        font-weight: 700 !important;
        font-size: 14px !important; /* Increased from 9px */
        box-shadow: 0 4px 12px rgba(220, 38, 38, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    .test-node .status-skip { 
        background: linear-gradient(135deg, #d97706, #f59e0b) !important;
        color: white !important;
        padding: 5px 12px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased from 16px */
        font-weight: 700 !important;
        font-size: 14px !important; /* Increased from 9px */
        box-shadow: 0 4px 12px rgba(217, 119, 6, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    /* === NODE STATUS BADGE (Test Execution Steps Badge) === */
                        /* Target the badge that appears next to node names like "Test Execution Steps" */
                        .node-name .badge,
                        .extent-node .badge,
                        .test-node .badge,
                        .card-header .badge,
                        .node .badge,
                        [class*="node"] .badge,
                        .badge.fail,
                        .badge.pass,
                        .badge.skip,
                        .badge.warning,
                        .badge.info {
                            padding: 10px 24px !important;
                            border-radius: 20px !important;
                            font-size: 15px !important;
                            font-weight: 800 !important;
                            min-width: 90px !important;
                            text-align: center !important;
                            display: inline-block !important;
                            margin-left: 15px !important;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.3) !important;
                            letter-spacing: 0.5px !important;
                            text-transform: uppercase !important;
                        }
                
                        /* Specific colors for node badges */
                        .node-name .badge.fail,
                        .badge.fail {
                            background: linear-gradient(135deg, #dc2626, #ef4444) !important;
                            color: white !important;
                        }
                
                        .node-name .badge.pass,
                        .badge.pass {
                            background: linear-gradient(135deg, #059669, #10b981) !important;
                            color: white !important;
                        }
                        
                        /* === NAVIGATION HIGHLIGHTING - SUPER STRONG === */
                                            .nav-highlighted {
                                                position: relative !important;
                                                z-index: 1000 !important;
                                                outline: 5px solid #2563eb !important;
                                                outline-offset: 3px !important;
                                                background: rgba(37, 99, 235, 0.25) !important;
                                                box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.3),\s
                                                            0 0 20px rgba(37, 99, 235, 0.5),\s
                                                            inset 0 0 20px rgba(37, 99, 235, 0.1) !important;
                                                border-radius: 8px !important;
                                                transform: scale(1.02) !important;
                                                transition: all 0.3s ease !important;
                                            }
                
                                            /* Stronger border for highlighted test */
                                            .nav-highlighted::before {
                                                content: '👉 CURRENT TEST' !important;
                                                position: absolute !important;
                                                top: -30px !important;
                                                left: 50% !important;
                                                transform: translateX(-50%) !important;
                                                background: linear-gradient(135deg, #2563eb, #1e40af) !important;
                                                color: white !important;
                                                padding: 6px 16px !important;
                                                border-radius: 20px !important;
                                                font-size: 12px !important;
                                                font-weight: 800 !important;
                                                box-shadow: 0 4px 12px rgba(37, 99, 235, 0.5) !important;
                                                z-index: 1001 !important;
                                                animation: pulse 1.5s infinite !important;
                                                letter-spacing: 1px !important;
                                            }
                
                                            /* Pulse animation for the indicator */
                                            @keyframes pulse {
                                                0%, 100% {\s
                                                    opacity: 1;\s
                                                    transform: translateX(-50%) scale(1);\s
                                                }
                                                50% {\s
                                                    opacity: 0.8;\s
                                                    transform: translateX(-50%) scale(1.05);\s
                                                }
                                            }
                
                                            /* Make sure highlighted content is visible */
                                            .nav-highlighted * {
                                                position: relative !important;
                                                z-index: 1001 !important;
                                            }
    
    /* === SPECIALIZED SECTIONS === */
    .response-time-badge {
        background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
        color: white !important;
        padding: 5px 12px !important; /* Increased padding */
        border-radius: 18px !important; /* Increased from 16px */
        font-weight: 700 !important;
        font-size: 10px !important; /* Increased from 9px */
        display: inline-block !important;
        margin: 5px 0 !important; /* Increased from 4px */
        box-shadow: 0 3px 12px rgba(124, 58, 237, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    :is(.api-request-section, .api-response-section) {
        color: white !important;
        padding: 15px !important; /* Increased from 12px */
        border-radius: 12px !important; /* Increased from 10px */
        margin: 12px 0 !important; /* Increased from 10px */
        border: 2px solid rgba(255,255,255,0.2) !important;
    }
    
    .api-request-section { 
        background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
        box-shadow: 0 6px 18px rgba(59, 130, 246, 0.4) !important; /* Enhanced shadow */
    }
    
    .api-response-section { 
        background: linear-gradient(135deg, #059669, #10b981) !important;
        box-shadow: 0 6px 18px rgba(5, 150, 105, 0.4) !important; /* Enhanced shadow */
    }
    
    :is(.api-request-section, .api-response-section) h4 {
        color: white !important;
        margin-bottom: 12px !important; /* Increased from 10px */
        font: 700 11px 'Segoe UI', sans-serif !important; /* Increased from 10px */
        text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
    }
    
    :is(.request-detail-item, .response-detail-item) {
        background: rgba(255, 255, 255, 0.15) !important;
        padding: 12px !important; /* Increased from 10px */
        border-radius: 10px !important; /* Increased from 8px */
        margin: 10px 0 !important; /* Increased from 8px */
        border-left: 3px solid rgba(255,255,255,0.4) !important; /* Increased from 2px */
        backdrop-filter: blur(10px) !important;
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* === LOG STYLING === */
    .info-log {
        background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
        color: white !important;
        padding: 10px 18px !important; /* Increased padding */
        border-radius: 22px !important; /* Increased from 20px */
        margin: 10px 0 !important; /* Increased from 8px */
        border-left: 3px solid #60a5fa !important; /* Increased from 2px */
        box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
        font-weight: 600 !important;
        font-size: 13px !important; /* Increased from 9px */
    }
    
    .warning-log {
        background: linear-gradient(135deg, #d97706, #f59e0b) !important;
        color: white !important;
        padding: 10px 18px !important; /* Increased padding */
        border-radius: 22px !important; /* Increased from 20px */
        margin: 10px 0 !important; /* Increased from 8px */
        border-left: 3px solid #fbbf24 !important; /* Increased from 2px */
        box-shadow: 0 4px 12px rgba(217, 119, 6, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
        font-weight: 600 !important;
        font-size: 10px !important; /* Increased from 9px */
    }
    
    .error-section {
        background: linear-gradient(135deg, #dc2626, #ef4444) !important;
        color: white !important;
        padding: 15px !important; /* Increased from 12px */
        border-radius: 12px !important; /* Increased from 10px */
        margin: 12px 0 !important; /* Increased from 10px */
        border-left: 4px solid #f87171 !important; /* Increased from 3px */
        box-shadow: 0 6px 18px rgba(220, 38, 38, 0.4) !important; /* Enhanced shadow */
        text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
        font-size: 10px !important; /* Increased from 9px */
    }
    
 /* full-width container (keep this) */
                 .db-log {
                     background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
                     padding: 12px;
                     border-radius: 22px;
                     box-shadow: 0 6px 16px rgba(124, 58, 237, 0.35);
                 }
                
                 /* header pill — new class */
                 .db-log-header {
                     display: inline-block !important;
                     background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
                     padding: 10px 18px !important;
                     border-radius: 18px !important;
                     margin-bottom: 10px !important;
                 }
                
                 .db-log-header h4 {
                     margin: 0 !important;
                     color: white !important;
                 }
                
    
    /* === UTILITY CLASSES === */
    .code-block {
        background: #1f2937 !important;
        color: #f9fafb !important;
        padding: 15px !important; /* Increased from 12px */
        border-radius: 10px !important; /* Increased from 8px */
        margin: 10px 0 !important; /* Increased from 8px */
        overflow-x: auto !important;
        box-shadow: inset 0 2px 6px rgba(0,0,0,0.3) !important; /* Enhanced shadow */
        border: 1px solid #374151 !important;
        font: 12px/1.5 'Fira Code', 'Monaco', 'Consolas', monospace !important; /* Increased from 8px */
    }
    
    .card-panel { 
        box-shadow: 0 8px 30px rgba(0,0,0,0.08) !important; /* Enhanced shadow */
        border-radius: 12px !important; /* Increased from 10px */
        border: 1px solid #e5e7eb !important;
        margin-bottom: 18px !important; /* Increased from 15px */
        background: white !important;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
    }
    
    .card-panel:hover {
        transform: translateY(-3px) !important; /* Increased from -2px */
        box-shadow: 0 12px 35px rgba(0,0,0,0.12) !important; /* Enhanced shadow */
        border-color: #3b82f6 !important;
    }
    
    .collapsible-content {
        transition: all 0.3s ease !important;
        border-radius: 6px !important; /* Increased from 5px */
        padding: 10px !important; /* Increased from 8px */
        background: rgba(255, 255, 255, 0.05) !important;
        margin-top: 8px !important; /* Increased from 6px */
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* === GENERAL TEXT SIZE IMPROVEMENTS === */
    /* Apply better readable font sizes to common text elements */
    p, span, div, td, th, li, ul, ol {
        font-size: 10px !important; /* Increased from 9px for better readability */
    }
    
    h1 { font-size: 18px !important; } /* Increased from 16px */
    h2 { font-size: 16px !important; } /* Increased from 14px */
    h3 { font-size: 14px !important; } /* Increased from 12px */
    h4 { font-size: 12px !important; } /* Increased from 10px */
    h5 { font-size: 10px !important; } /* Increased from 9px */
    h6 { font-size: 9px !important; } /* Increased from 8px */
    
    /* Test details and step information */
    .test-detail, .step-detail, .scenario-detail {
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* Navigation and menu items */
    .nav-item, .menu-item, .nav-link {
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* Table content */
    table, table td, table th {
        font-size: 9px !important; /* Increased from 8px */
    }
    
    /* Button text */
    button, .btn, .button {
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* Form elements */
    input, select, textarea, label {
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* Test node content */
    .test-node, .category-node, .feature-node {
        font-size: 12px !important; /* Increased from 9px */
    }
    
    /* Timeline content */
    .timeline .node, .timeline .category {
        font-size: 9px !important; /* Increased from 8px */
    }
  
    
    /* Dashboard content */
    .dashboard-item, .widget-content {
        font-size: 10px !important; /* Increased from 9px */
    }
    
    /* Status indicators */
    .status-indicator, .badge, .label {
        font-size: 14px !important; /* Increased from 8px */
    }
    
    /* === RESPONSIVE DESIGN FOR COLUMNS === */
    @media (max-width: 768px) {
        .stats-dashboard {
            grid-template-columns: 1fr !important;
            gap: 12px !important; /* Increased from 10px */
        }
        
        .test-details-container {
            grid-template-columns: 110px 170px 1fr !important; /* Adjusted for mobile */
            gap: 12px !important; /* Increased gap */
            padding: 12px !important; /* Increased padding */
        }
        
        :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row) {
            flex-direction: column !important;
            text-align: center !important;
            gap: 10px !important; /* Increased from 8px */
        }
        
        .stat-value-container {
            justify-content: center !important;
            margin-top: 10px !important; /* Increased from 8px */
        }
        
        :is(.thread-progress-bar, .status-progress-bar) {
            margin: 10px 0 !important; /* Increased from 8px */
            width: 100% !important;
        }
        
        :is(.api-request-section, .api-response-section) {
            margin: 10px 3px !important; /* Increased margins */
            padding: 12px !important; /* Increased from 10px */
        }
        
        .stats-title { font-size: 15px !important; } /* Increased from 13px */
        .card-title { font-size: 11px !important; } /* Increased from 10px */
        
        /* Mobile text adjustments */
        body { font-size: 9px !important; } /* Increased from 8px */
        p, span, div, td, th, li, ul, ol { font-size: 9px !important; } /* Increased from 8px */
        
        /* Mobile specific improvements */
        .thread-info { font-size: 8px !important; } /* Increased from 7px */
        .response-time-badge { font-size: 8px !important; } /* Increased from 7px */
        .perf-value { font-size: 8px !important; } /* Increased from 7px */
        .code-block { font-size: 11px !important; } /* Increased from 7px */
        
        /* Mobile header improvements */
        .enhanced-stats-header { padding: 12px !important; } /* Increased from 10px */
        .card-header { padding: 10px !important; } /* Increased from 8px */
        .card-body { padding: 12px !important; } /* Increased from 10px */
        
        /* Mobile test names - better readable */
        .test-name, .scenario-name, .feature-name,
        .test-node .node-name, .test-node .test-name {
            font-size: 14px !important; /* Increased from 7px */
            max-width: 280px !important; /* Increased from 250px */
        }
        
        /* Mobile timestamp styling - better formatting */
        .test-timestamp-badge, .execution-timestamp {
            font-size: 8px !important; /* Increased from 7px */
            padding: 3px 8px !important; /* Increased padding */
            letter-spacing: 0.4px !important;
        }
        
        /* Mobile column headers */
        .status-column, .timestamp-column, .details-column,
        .test-status, .test-timestamp, .test-details {
            padding: 8px 6px !important;
            font-size: 8px !important;
        }
        
        /* Mobile table headers */
        th.status-col, th.timestamp-col, th.details-col {
            padding: 8px 4px !important;
            font-size: 7px !important;
        }
        
        /* Mobile table body cells */
        td.status-col, td.timestamp-col, td.details-col {
            padding: 6px 4px !important;
            font-size: 8px !important;
        }
    }
    
    /* === ANIMATIONS === */
    @keyframes countUp {
        from { opacity: 0; transform: translateY(12px); } /* Increased from 10px */
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes progressFill {
        from { width: 0%; }
        to { width: var(--target-width); }
    }
    
    @keyframes slideIn {
        from { opacity: 0; transform: translateY(-8px); } /* Increased from -6px */
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes fadeInUp {
        from { opacity: 0; transform: translateY(25px); } /* Increased from 20px */
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes shimmer {
        0% { left: -100%; }
        100% { left: 100%; }
    }
    
    @keyframes pulse {
        0% { opacity: 1; transform: scale(1); }
        50% { opacity: 0.9; transform: scale(1.02); } /* Increased from 1.01 */
        100% { opacity: 1; transform: scale(1); }
    }
    
    /* Apply animations */
    .stat-value { animation: countUp 0.8s ease-out !important; }
    .progress-fill { animation: progressFill 1.2s ease-out !important; }
    .stats-card { animation: fadeInUp 0.6s ease-out !important; }
    
    /* Shimmer effect for progress bars */
    :is(.progress-fill, .thread-progress-fill, .status-progress-fill)::after {
        content: '' !important;
        position: absolute !important;
        top: 0 !important;
        left: -100% !important;
        width: 100% !important;
        height: 100% !important;
        background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent) !important;
        animation: shimmer 2.5s infinite !important;
    }
                 /* Hide all buttons and elements that contain "failed" in onclick, title, or text */
                    button[onclick*="failed" i],\s
                    button[onclick*="Failed"],\s
                    .btn[onclick*="failed" i],
                    .btn[onclick*="Failed"],
                    *[title*="next failed" i],
                    *[title*="skip to failed" i],
                    *[onclick*="nextFailed" i],
                    *[onclick*="skipFailed" i] {
                        display: none !important;
                        visibility: hidden !important;
                        pointer-events: none !important;
                    }
                
                    /* Add navigation instructions */
                    body::after {
                        content: "Use Ctrl+F to search specific test and Up/Down keyboard arrows to navigate test cases" !important;
                        position: fixed !important;
                        bottom: 20px !important;
                        right: 20px !important;
                        background: rgba(59, 130, 246, 0.9) !important;
                        color: white !important;
                        padding: 10px 15px !important;
                        border-radius: 8px !important;
                        font-size: 11px !important;
                        font-weight: 600 !important;
                        z-index: 9999 !important;
                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2) !important;
                        border: 1px solid rgba(255, 255, 255, 0.2) !important;
                        backdrop-filter: blur(10px) !important;
                        animation: slideInUp 0.5s ease-out, fadeOut 1s ease-in 10s forwards !important;
                        max-width: 300px !important;
                        text-align: center !important;
                    }
                
                    @keyframes slideInUp {
                        from {
                            opacity: 0;
                            transform: translateY(30px);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }
                
                    @keyframes fadeOut {
                        from { opacity: 1; }
                        to { opacity: 0; }
                    }
                
                    /* Make test nodes more clickable and navigable */
                    .test-node, .scenario-node, .extent-node, .card-panel {
                        cursor: pointer !important;
                        transition: all 0.3s ease !important;
                        position: relative !important;
                    }
                
                    .test-node:hover, .scenario-node:hover, .extent-node:hover, .card-panel:hover {
                        transform: translateY(-2px) !important;
                        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12) !important;
                        border-left: 4px solid #3b82f6 !important;
                    }
                
                    /* Add test counter to each test */
                    .test-node::before, .scenario-node::before {
                        counter-increment: test-counter !important;
                        content: "Test #" counter(test-counter) !important;
                        position: absolute !important;
                        top: 5px !important;
                        right: 10px !important;
                        background: linear-gradient(135deg, #3b82f6, #1e40af) !important;
                        color: white !important;
                        padding: 2px 8px !important;
                        border-radius: 12px !important;
                        font-size: 9px !important;
                        font-weight: 600 !important;
                        z-index: 10 !important;
                    }
                
                    body {
                        counter-reset: test-counter !important;
                    }
                
                    @media (max-width: 768px) {
                        body::after {
                            bottom: 10px !important;
                            right: 10px !important;
                            left: 10px !important;
                            font-size: 10px !important;
                            padding: 8px 12px !important;
                        }
                    }
                    
                    /* Add this to the end of your CSS method */
                
                                /* Enhanced navigation visual feedback */
                                .nav-highlighted {
                                    position: relative !important;
                                    z-index: 100 !important;
                                }
                
                                .test-nav-indicator {
                                    animation: bounceIn 0.3s ease-out !important;
                                }
                
                                @keyframes bounceIn {
                                    0% { transform: scale(0.3); opacity: 0; }
                                    50% { transform: scale(1.05); }
                                    70% { transform: scale(0.9); }
                                    100% { transform: scale(1); opacity: 1; }
                                }
                
                                @keyframes fadeOut {
                                    from { opacity: 1; transform: scale(1); }
                                    to { opacity: 0; transform: scale(0.8); }
                                }
                
                                /* Smoother transitions for test elements */
                                .test-node, .scenario-node, .extent-node, .card-panel {
                                    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
                                }
                                       
    """;
    }

    // TIMELINE FIX JAVASCRIPT - ONLY FIXES TIMELINE ISSUES, DOESN'T CHANGE EXISTING FUNCTIONALITY
// Replace the getTimelineFixJavaScript() method with this enhanced version

// Replace the getTimelineFixJavaScript() method with this enhanced version

// Replace the getTimelineFixJavaScript() method with this enhanced version

// Replace the getTimelineFixJavaScript() method with this enhanced version

// Replace the getTimelineFixJavaScript() method with this enhanced version
// Replace your existing getTimelineFixJavaScript() method with this complete version:

// Replace your existing getTimelineFixJavaScript() method with this complete version:

// Replace your existing getTimelineFixJavaScript() method with this complete version:

    private static String getTimelineFixJavaScript() {
        return """
// === FULLY FIXED TIMELINE HIGHLIGHTING FOR li.test-item (thin borders) + KEYBOARD NAVIGATION + AUTO-EXPAND ===
class TimelineFixWithNavigation {
    constructor() {
        this.currentHighlightedElement = null;
        this.currentIndex = 0;
        this.init();
    }

    init() {
        console.log('🎯 Timeline Fix: Initializing...');
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => this.applyFixes());
        } else {
            this.applyFixes();
        }
    }

    applyFixes() {
        console.log('✅ Applying timeline fixes...');
        this.setupTimelineMonitoring();
        this.applyTimelineStyling();
        this.fixTimestampBadgeColors();
        this.setupKeyboardNavigation();

        setTimeout(() => this.overrideNavigation(), 2000);
        setTimeout(() => this.hideFailedTestButtons(), 3000);

        // Auto-highlight first test on load
        setTimeout(() => this.autoHighlightFirstTest(), 500);
        
        // ✅ Expand all test execution steps by default
        setTimeout(() => this.expandAllTestSteps(), 100);
    }
    
    // ✅ NEW: Expand all collapsed test execution steps
    expandAllTestSteps() {
        console.log('📂 Expanding all test execution steps...');
        
        // Find all collapse elements that are currently collapsed
        const collapsedElements = document.querySelectorAll('.collapse:not(.show)');
        
        collapsedElements.forEach(element => {
            // Add 'show' class to expand
            element.classList.add('show');
            
            // Also update the toggle button/link if it exists
            const toggleId = element.id;
            if (toggleId) {
                const toggleButton = document.querySelector(`[data-toggle="collapse"][href="#${toggleId}"], [data-toggle="collapse"][data-target="#${toggleId}"]`);
                if (toggleButton) {
                    toggleButton.setAttribute('aria-expanded', 'true');
                    toggleButton.classList.remove('collapsed');
                }
            }
        });
        
        // Also handle any node-collapsed classes
        document.querySelectorAll('.node.collapsed').forEach(node => {
            node.classList.remove('collapsed');
        });
        
        console.log(`✅ Expanded ${collapsedElements.length} test execution step sections`);
    }

    // ✅ FIXED: Setup Up/Down arrow key navigation WITHOUT wrapping
    setupKeyboardNavigation() {
        document.addEventListener('keydown', (e) => {
            // Only handle arrow keys
            if (e.key !== 'ArrowUp' && e.key !== 'ArrowDown') {
                return;
            }

            // Prevent default scrolling behavior
            e.preventDefault();

            const allTests = this.getAllTestElements();
            if (allTests.length === 0) return;

            // Store previous index for debugging
            const previousIndex = this.currentIndex;
            let newIndex = this.currentIndex;

            if (e.key === 'ArrowDown') {
                // Move to next test ONLY if not at the end
                if (this.currentIndex < allTests.length - 1) {
                    newIndex = this.currentIndex + 1;
                } else {
                    console.log('⚠️ Already at last test - cannot go down');
                    return; // Do nothing if at last test
                }
            } else if (e.key === 'ArrowUp') {
                // Move to previous test ONLY if not at the beginning
                if (this.currentIndex > 0) {
                    newIndex = this.currentIndex - 1;
                } else {
                    console.log('⚠️ Already at first test - cannot go up');
                    return; // Do nothing if at first test
                }
            }

            // Update index only if it changed
            this.currentIndex = newIndex;

            // Get the target element
            const targetTest = allTests[this.currentIndex];
            
            if (targetTest) {
                this.highlightTestElement(targetTest, true);
                console.log(`✅ Navigated from test ${previousIndex + 1} to test ${this.currentIndex + 1}/${allTests.length}`);
            } else {
                console.warn(`⚠️ No test element found at index ${this.currentIndex}`);
            }
        });
        
        console.log('✅ Keyboard navigation enabled (Use ↑↓ arrow keys, no wrapping)');
    }

    // ✅ INTELLIGENT TIMESTAMP BADGE COLOR FIX - NO HARDCODED DATES
    fixTimestampBadgeColors() {
        document.querySelectorAll('.badge-danger, .badge.badge-danger').forEach(badge => {
            const text = badge.textContent.trim();
            
            // Intelligent timestamp detection using patterns (no hardcoded years)
            const isTimestamp = (
                // Pattern: DD.MM.YYYY HH:MM:SS PM/AM
                /\\d{2}\\.\\d{2}\\.\\d{4}\\s+\\d{1,2}:\\d{2}:\\d{2}\\s+(AM|PM)/i.test(text) ||
                // Pattern: HH:MM:SS
                /^\\d{1,2}:\\d{2}:\\d{2}$/.test(text) ||
                // Pattern: DD.MM.YYYY
                /^\\d{2}\\.\\d{2}\\.\\d{4}$/.test(text) ||
                // Pattern: Time with AM/PM
                /\\d{1,2}:\\d{2}(:\\d{2})?\\s*(AM|PM)/i.test(text) ||
                // Pattern: ISO date format
                /^\\d{4}-\\d{2}-\\d{2}/.test(text) ||
                // Pattern: Time with milliseconds
                /\\d{2}:\\d{2}:\\d{2}\\.\\d{3}/.test(text)
            );
            
            // Exclude status badges
            const isStatusBadge = /fail|pass|skip|error|success|warning/i.test(text);
            
            // Only change timestamp badges, NOT status badges
            if (isTimestamp && !isStatusBadge) {
                badge.style.background = 'linear-gradient(135deg, #059669, #10b981)';
                badge.style.boxShadow = '0 3px 10px rgba(5, 150, 105, 0.4)';
                badge.style.color = 'white';
                badge.classList.remove('badge-danger');
                badge.classList.add('badge-success');
                console.log('✅ Fixed timestamp badge:', text);
            }
        });
        
        // Watch for dynamically added badges
        const observer = new MutationObserver(() => {
            setTimeout(() => this.fixTimestampBadgeColors(), 100);
        });
        observer.observe(document.body, { childList: true, subtree: true });
    }

    autoHighlightFirstTest() {
        const firstTest = this.getAllTestElements()[0];
        if (firstTest) {
            this.currentIndex = 0;
            this.highlightTestElement(firstTest, false);
        }
    }

    getAllTestElements() {
        const tests = Array.from(document.querySelectorAll('li.test-item'));
        return tests.filter(test => test.offsetHeight > 0);
    }

    // ✅ FIXED: Improved highlight with proper index tracking
    highlightTestElement(testElement, shouldScroll = true) {
        if (!testElement) {
            console.warn('⚠️ Cannot highlight - test element is null');
            return;
        }

        // Remove previous highlight
        if (this.currentHighlightedElement && this.currentHighlightedElement !== testElement) {
            this.removeHighlight(this.currentHighlightedElement);
        }

        this.currentHighlightedElement = testElement;

        const originalStyle = testElement.getAttribute('style') || '';
        testElement.setAttribute('data-original-style', originalStyle);

        testElement.style.cssText = originalStyle + `
            position: relative !important;
            z-index: 9999 !important;
            outline: 2px solid #2563eb !important;
            outline-offset: 2px !important;
            background: rgba(37, 99, 235, 0.15) !important;
            box-shadow:
                0 0 0 1px rgba(37, 99, 235, 0.4),
                0 0 10px rgba(37, 99, 235, 0.6),
                inset 0 0 10px rgba(37, 99, 235, 0.1) !important;
            border-radius: 10px !important;
            transform: scale(1.02) !important;
            transition: all 0.3s ease !important;
        `;

        if (shouldScroll) {
            testElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        
        // Update current index to match the element we just highlighted
        const allTests = this.getAllTestElements();
        const newIndex = allTests.indexOf(testElement);
        if (newIndex !== -1) {
            this.currentIndex = newIndex;
            console.log(`✅ Highlighted test at index ${this.currentIndex + 1}/${allTests.length}`);
        }
    }

    removeHighlight(element) {
        if (!element) return;
        const originalStyle = element.getAttribute('data-original-style');
        if (originalStyle !== null) {
            element.style.cssText = originalStyle;
            element.removeAttribute('data-original-style');
        }
        if (this.currentHighlightedElement === element) {
            this.currentHighlightedElement = null;
        }
    }

    overrideNavigation() {
        this.hideFailedTestButtons();
        this.addClickHighlighting();
    }

    addClickHighlighting() {
        document.addEventListener('click', (e) => {
            const testElement = e.target.closest('li.test-item');
            if (testElement) {
                this.highlightTestElement(testElement, false);
            }
        }, true);
    }

    hideFailedTestButtons() {
        const selectors = ['button', '.btn', 'a[role="button"]', '[onclick]', '[class*="btn"]', '[class*="button"]', '[class*="nav"]'];
        selectors.forEach(selector => {
            document.querySelectorAll(selector).forEach(el => {
                const text = (el.textContent || el.innerText || '').toLowerCase();
                const title = (el.title || '').toLowerCase();
                const onclick = (el.getAttribute('onclick') || '').toLowerCase();
                const className = (el.className || '').toLowerCase();
                if ((text.includes('failed') && (text.includes('next') || text.includes('skip'))) ||
                    text.includes('nextfailed') || text.includes('skipfailed') ||
                    (text.includes('fail') && text.includes('nav'))) {
                    el.style.display = 'none';
                    el.style.visibility = 'hidden';
                    el.style.pointerEvents = 'none';
                }
            });
        });
    }

    applyTimelineStyling() {
        document.querySelectorAll('li.test-item').forEach(el => {
            el.style.transition = 'all 0.3s ease';
        });
    }

    setupTimelineMonitoring() {
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.type === 'childList') {
                    mutation.addedNodes.forEach(node => {
                        if (node.nodeType === Node.ELEMENT_NODE && node.matches('li.test-item')) {
                            node.style.transition = 'all 0.3s ease';
                        }
                    });
                }
            });
        });
        observer.observe(document.body, { childList: true, subtree: true });
    }
}

// Initialize
window.timelineFixWithNavigation = new TimelineFixWithNavigation();
console.log('✅ Timeline highlighting with keyboard navigation (↑↓), intelligent timestamp badge fix, and auto-expand test steps initialized');
""";
    }
    private static void logSuccessDetails(ExtentTest logger, long executionTime, String threadName) {
        StringBuilder successLog = new StringBuilder();

        successLog.append("<div class='info-log'>");
        successLog.append("✅ Test Passed Successfully");
        successLog.append("</div>");

        // ❌ REMOVED: getAllApiCallsLog() - API calls are already logged during test execution

        logger.pass(successLog.toString());
    }

    private static void logSkipDetails(ExtentTest logger, long executionTime, String threadName) {
        StringBuilder skipLog = new StringBuilder();

        // Thread information
        skipLog.append("<div class='thread-info'>");
        skipLog.append("🧵 Thread: ").append(threadName).append(" | ID: ").append(Thread.currentThread().getId());
        skipLog.append("</div>");

        skipLog.append("<div class='warning-log'>");
        skipLog.append("⏭️ <strong>Test Skipped</strong>");
        skipLog.append("</div>");

        // ❌ REMOVED: getAllApiCallsLog() - API calls are already logged during test execution

        logger.skip(skipLog.toString());
    }

    private static void logFailureDetails(ExtentTest logger, ITestResult result, long executionTime, String threadName) {
        StringBuilder failureLog = new StringBuilder();

        // Thread information
        failureLog.append("<div class='thread-info'>");
        failureLog.append("🧵 Thread: ").append(threadName).append(" | ID: ").append(Thread.currentThread().getId());
        failureLog.append("</div>");

        // Error section with professional styling
        failureLog.append("<div class='error-section'>");
        failureLog.append("<h3>❌ Test Execution Failed</h3>");
        failureLog.append("<div class='response-time-badge'>⏱️ Execution Time: ").append(executionTime).append(" ms</div><br/>");
        failureLog.append("<strong>🚨 Error Details:</strong><br/>");
        failureLog.append("<div style='background: rgba(255,255,255,0.15); padding: 20px; border-radius: 12px; margin-top: 15px; backdrop-filter: blur(10px);'>");
        failureLog.append(result.getThrowable().toString());
        failureLog.append("</div>");
        failureLog.append("</div>");

        // ❌ REMOVED: getAllApiCallsLog() - API calls are already logged during test execution
        // ❌ REMOVED: getAllDatabaseQueriesLog() - Database queries are already logged during test execution

        logger.fail(failureLog.toString());
    }
    private static void setSystemInfo() {
        extent.setSystemInfo("🌍 Environment", ApplicationContext.get().getEnvironment());
        extent.setSystemInfo("🌐 Base URI", ApplicationContext.get().getEnvConfig().getBaseUri());
    }

    public static synchronized void generateReport(ITestResult result) {
        ExtentTest logger = extentLogger.get();
        long executionTime = System.currentTimeMillis() - testStartTime.get();
        String threadName = Thread.currentThread().getName();

        // Track thread-specific test counts
        threadTestCounts.computeIfAbsent(threadName, k -> new AtomicInteger(0)).incrementAndGet();
        totalTests.incrementAndGet();

        if (result.getStatus() == ITestResult.FAILURE) {
            failedTests.incrementAndGet();
            logFailureDetails(logger, result, executionTime, threadName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            passedTests.incrementAndGet();
            logSuccessDetails(logger, executionTime, threadName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            skippedTests.incrementAndGet();
            logSkipDetails(logger, executionTime, threadName);
        }

        cleanupThreadLocals();
        extent.flush();
    }


    // ✅ ENHANCED: Comprehensive status code text mapping with all HTTP status codes
    private static String getStatusText(int statusCode) {
        return switch (statusCode) {
            // 1xx Informational responses
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 102 -> "Processing";
            case 103 -> "Early Hints";

            // 2xx Success responses
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 203 -> "Non-Authoritative Information";
            case 204 -> "No Content";
            case 205 -> "Reset Content";
            case 206 -> "Partial Content";
            case 207 -> "Multi-Status";
            case 208 -> "Already Reported";
            case 226 -> "IM Used";

            // 3xx Redirection responses
            case 300 -> "Multiple Choices";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 303 -> "See Other";
            case 304 -> "Not Modified";
            case 305 -> "Use Proxy";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";

            // 4xx Client error responses
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Payment Required";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Payload Too Large";
            case 414 -> "URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 418 -> "I'm a teapot";
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Entity";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 425 -> "Too Early";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";

            // 5xx Server error responses
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";
            default -> "Unknown Status";
        };
    }

    // ✅ ENHANCED: Helper methods for enhanced response display
    private static String getPerformanceIndicator(long responseTime) {
        String performanceClass;
        String performanceText;

        if (responseTime < 200) {
            performanceClass = "excellent-performance";
            performanceText = "⚡ Excellent";
        } else if (responseTime < 500) {
            performanceClass = "good-performance";
            performanceText = "✅ Good";
        } else if (responseTime < 1000) {
            performanceClass = "average-performance";
            performanceText = "⚠️ Average";
        } else if (responseTime < 2000) {
            performanceClass = "slow-performance";
            performanceText = "🐌 Slow";
        } else {
            performanceClass = "very-slow-performance";
            performanceText = "🚨 Very Slow";
        }

        return "<span class='response-time-badge " + performanceClass + "'>" +
                responseTime + " ms (" + performanceText + ")</span>";
    }

    private static String formatBytes(int bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static String formatHeaders(String headers) {
        return headers.replace("{", "{\n  ")
                .replace("}", "\n}")
                .replace(",", ",\n  ")
                .replace("=", " = ");
    }

    private static String formatResponseBody(Response response) {
        try {
            String contentType = response.getContentType();
            String body = response.getBody().asString();

            // Handle JSON responses
            if (contentType != null && contentType.toLowerCase().contains("json")) {
                try {
                    return response.getBody().asPrettyString();
                } catch (Exception e) {
                    return body; // Fallback to raw if JSON parsing fails
                }
            }

            // Handle XML responses
            if (contentType != null && contentType.toLowerCase().contains("xml")) {
                return formatXml(body);
            }

            // Handle HTML responses (truncate if too long)
            if (contentType != null && contentType.toLowerCase().contains("html")) {
                if (body.length() > 1000) {
                    return body.substring(0, 1000) + "\n... (truncated, full length: " + body.length() + " characters)";
                }
            }

            return body;
        } catch (Exception e) {
            return "Error formatting response body: " + e.getMessage();
        }
    }

    private static String formatXml(String xml) {
        try {
            return xml.replace("><", ">\n<")
                    .replace(">", ">\n")
                    .replace("<", "\n<")
                    .replaceAll("\n+", "\n")
                    .trim();
        } catch (Exception e) {
            return xml;
        }
    }

    private static String getColorCodedStatus(int statusCode) {
        String statusClass = "status-" + statusCode;
        String statusText = statusCode + " - " + getStatusText(statusCode);
        return "<span class='" + statusClass + "'>" + statusText + "</span>";
    }

    // ✅ ENHANCED: Status class mapping with comprehensive coverage
    private static String getStatusClass(int statusCode) {
        if (statusCode >= 100 && statusCode < 200) return "informational-status";
        if (statusCode >= 200 && statusCode < 300) return "success-status";
        if (statusCode >= 300 && statusCode < 400) return "redirect-status";
        if (statusCode >= 400 && statusCode < 500) return "client-error-status";
        if (statusCode >= 500) return "server-error-status";
        return "unknown-status";
    }

    private static String formatJson(String json) {
        // Enhanced JSON formatting for better readability
        try {
            return json.replace("{", "{\n  ")
                    .replace("}", "\n}")
                    .replace(",", ",\n  ")
                    .replace("[", "[\n  ")
                    .replace("]", "\n]")
                    .replace("\":", "\": ");
        } catch (Exception e) {
            return json;
        }
    }

    // EXISTING METHODS UNCHANGED - PRESERVING ORIGINAL FUNCTIONALITY
    public static void createTest(String scenarioName) {
        String testName = scenarioName.isEmpty() ? "API Test Scenario" : scenarioName;
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        extentLogger.set(extentTest.createNode("🔄 Test Execution Steps"));
        testStartTime.set(System.currentTimeMillis());
    }

    public static void createTest(String scenarioName, String description) {
        String testName = scenarioName.isEmpty() ? "API Test Scenario" : scenarioName;
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        extentLogger.set(extentTest.createNode("🔄 Test Execution Steps"));
        testStartTime.set(System.currentTimeMillis());
    }

    public static void logInfoToReport(String msg) {
        ExtentTest logger = extentLogger.get();
        String formattedMsg = "<div class='info-log'>ℹ️ " + msg + "</div>";
        logger.info(formattedMsg);
    }

    public static void logErrorToReport(String msg) {
        ExtentTest logger = extentLogger.get();
        String formattedMsg = "<div class='error-section'>❌ " + msg + "</div>";
        logger.fail(formattedMsg);
    }

    public static synchronized void addTestStatistics() {
        ExtentTest statsTest = extent.createTest("📊 Test Execution Summary");

        StringBuilder stats = new StringBuilder();

        // Enhanced Header with better styling
        stats.append("<div class='enhanced-stats-header'>");
        stats.append("<h2 class='stats-title'>📊 Test Execution Summary</h2>");
        stats.append("<div class='execution-timestamp'>").append(getCurrentDateTimeFormatted()).append("</div>");
        stats.append("</div>");

        // Main Statistics Dashboard
        stats.append("<div class='stats-dashboard'>");

        // Overall Statistics Card
        stats.append("<div class='stats-card overall-stats'>");
        stats.append("<div class='card-header'>");
        stats.append("<h3 class='card-title'>📈 Overall Test Statistics</h3>");
        stats.append("</div>");
        stats.append("<div class='card-body'>");

        // Total Tests with enhanced styling
        stats.append("<div class='stat-row total-tests'>");
        stats.append("<div class='stat-label'>🎯 Total Tests Executed</div>");
        stats.append("<div class='stat-value total-count'>").append(totalTests.get()).append("</div>");
        stats.append("</div>");

        // Passed Tests - FIXED: Better inline alignment
        double passPercentage = totalTests.get() > 0 ? (passedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append("<div class='stat-row passed-tests'>");
        stats.append("<div class='stat-label'>✅ Passed</div>");
        stats.append("<div class='stat-value-container'>");
        stats.append("<div class='stat-value passed-count'>").append(passedTests.get()).append("</div>");
        stats.append("<div class='stat-percentage passed-percentage'>(").append(String.format("%.1f", passPercentage)).append("%)</div>");
        stats.append("</div>");
        stats.append("<div class='progress-bar'>");
        stats.append("<div class='progress-fill passed-progress' style='width: ").append(passPercentage).append("%;'></div>");
        stats.append("</div>");
        stats.append("</div>");

        // Failed Tests - FIXED: Better inline alignment
        double failPercentage = totalTests.get() > 0 ? (failedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append("<div class='stat-row failed-tests'>");
        stats.append("<div class='stat-label'>❌ Failed</div>");
        stats.append("<div class='stat-value-container'>");
        stats.append("<div class='stat-value failed-count'>").append(failedTests.get()).append("</div>");
        stats.append("<div class='stat-percentage failed-percentage'>(").append(String.format("%.1f", failPercentage)).append("%)</div>");
        stats.append("</div>");
        stats.append("<div class='progress-bar'>");
        stats.append("<div class='progress-fill failed-progress' style='width: ").append(failPercentage).append("%;'></div>");
        stats.append("</div>");
        stats.append("</div>");

        // Skipped Tests - FIXED: Better inline alignment
        double skipPercentage = totalTests.get() > 0 ? (skippedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append("<div class='stat-row skipped-tests'>");
        stats.append("<div class='stat-label'>⏭️ Skipped</div>");
        stats.append("<div class='stat-value-container'>");
        stats.append("<div class='stat-value skipped-count'>").append(skippedTests.get()).append("</div>");
        stats.append("<div class='stat-percentage skipped-percentage'>(").append(String.format("%.1f", skipPercentage)).append("%)</div>");
        stats.append("</div>");
        stats.append("<div class='progress-bar'>");
        stats.append("<div class='progress-fill skipped-progress' style='width: ").append(skipPercentage).append("%;'></div>");
        stats.append("</div>");
        stats.append("</div>");

        // Success Rate Indicator
        stats.append("<div class='success-rate-section'>");
        stats.append("<div class='success-rate-label'>🎯 Success Rate</div>");
        stats.append("<div class='success-rate-value ").append(passPercentage >= 90 ? "excellent" : passPercentage >= 70 ? "good" : "needs-improvement").append("'>");
        stats.append(String.format("%.1f", passPercentage)).append("%");
        stats.append("</div>");
        stats.append("</div>");

        stats.append("</div>"); // End card-body
        stats.append("</div>"); // End overall-stats card

        // Thread Distribution Card (only if parallel execution)
        if (threadTestCounts.size() > 1) {
            stats.append("<div class='stats-card thread-stats'>");
            stats.append("<div class='card-header'>");
            stats.append("<h3 class='card-title'>🧵 Thread-wise Distribution</h3>");
            stats.append("</div>");
            stats.append("<div class='card-body'>");

            threadTestCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, AtomicInteger>comparingByValue(
                            (a, b) -> Integer.compare(b.get(), a.get())))
                    .forEach(entry -> {
                        double threadPercentage = totalTests.get() > 0 ? (entry.getValue().get() * 100.0 / totalTests.get()) : 0;
                        stats.append("<div class='thread-stat-row'>");
                        stats.append("<div class='thread-label'>🔄 ").append(entry.getKey()).append("</div>");
                        stats.append("<div class='thread-value'>").append(entry.getValue().get()).append(" tests</div>");
                        stats.append("<div class='thread-percentage'>(").append(String.format("%.1f", threadPercentage)).append("%)</div>");
                        stats.append("<div class='thread-progress-bar'>");
                        stats.append("<div class='thread-progress-fill' style='width: ").append(threadPercentage).append("%;'></div>");
                        stats.append("</div>");
                        stats.append("</div>");
                    });

            stats.append("</div>"); // End card-body
            stats.append("</div>"); // End thread-stats card
        }

        // Status Code Distribution Card
        if (!statusCodeCounts.isEmpty()) {
            stats.append("<div class='stats-card status-code-stats'>");
            stats.append("<div class='card-header'>");
            stats.append("<h3 class='card-title'>🌐 HTTP Status Code Distribution</h3>");
            stats.append("</div>");
            stats.append("<div class='card-body'>");

            statusCodeCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        int statusCode = Integer.parseInt(entry.getKey());
                        String statusClass = getStatusClass(statusCode);
                        double statusPercentage = totalTests.get() > 0 ? (entry.getValue() * 100.0 / totalTests.get()) : 0;

                        stats.append("<div class='status-code-row'>");
                        stats.append("<div class='status-code-label'>");
                        stats.append(getColorCodedStatus(statusCode));
                        stats.append("</div>");
                        stats.append("<div class='status-code-value'>").append(entry.getValue()).append(" requests</div>");
                        stats.append("<div class='status-code-percentage'>(").append(String.format("%.1f", statusPercentage)).append("%)</div>");
                        stats.append("<div class='status-progress-bar'>");
                        stats.append("<div class='status-progress-fill ").append(statusClass).append("' style='width: ").append(statusPercentage).append("%;'></div>");
                        stats.append("</div>");
                        stats.append("</div>");
                    });

            stats.append("</div>"); // End card-body
            stats.append("</div>"); // End status-code-stats card
        }
        stats.append("</div>"); // End stats-dashboard

        statsTest.info(stats.toString());
    }

    public static synchronized void flushReports() {
        log.info("📊 Publishing Professional Extent Reports with Timeline Fix");

        //addTestStatistics();

        // ✅ Add Total Execution Time
        long totalExecutionMillis = System.currentTimeMillis() - suiteStartTime;
        String duration = formatDuration(totalExecutionMillis);
        //extent.setSystemInfo("⏱️ Total Execution Time", duration);

        extent.flush();
    }

    public static String formatDuration(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (1000 * 60) % 60;
        long hours = millis / (1000 * 60 * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void addRequestDetailsToReport(RequestSpecification reqSpec) {
        if (reqSpec != null) {
            QueryableRequestSpecification queryable = SpecificationQuerier.query(reqSpec);
            qReqSpec.set(queryable);
            // ✅ Just store, don't log yet
        }
    }

    public static void addResponseDetailsToReport(Response resp, int statusCode) {
        response.set(resp);
        expectedStatusCode.set(String.valueOf(statusCode));

        // ✅ Log ONLY ONCE when both request and response are available
        QueryableRequestSpecification reqSpec = qReqSpec.get();
        if (reqSpec != null && resp != null) {
            String endpoint = reqSpec.getURI() != null ? reqSpec.getURI().toString() : "Unknown Endpoint";
            logApiDetailsOnce(endpoint, reqSpec, resp, statusCode);
        }
    }

    private static void logApiDetailsOnce(String endpoint, QueryableRequestSpecification reqSpec, Response resp, int statusCode) {
        ExtentTest logger = extentLogger.get();
        if (logger == null) {
            log.warn("No active test logger found");
            return;
        }

        StringBuilder apiLog = new StringBuilder();

        // Endpoint header
        apiLog.append("<div class='info-log'>");
        apiLog.append("🌐 BaseURI: ").append(endpoint);
        apiLog.append("</div>");

        // REQUEST SECTION
        apiLog.append("<div class='api-request-section'>");
        apiLog.append("<h4>📤 Request</h4>");

        if (reqSpec.getBody() != null) {
            apiLog.append("<div class='request-detail-item'>");
            apiLog.append("<strong>📦 Request Body:</strong><br/>");
            apiLog.append("<div class='code-block'>");
            apiLog.append(formatJson(reqSpec.getBody().toString()));
            apiLog.append("</div>");
            apiLog.append("</div>");
        }

        apiLog.append("</div>");

        // RESPONSE SECTION
        apiLog.append("<div class='api-response-section'>");
        apiLog.append("<h4>📥 Response</h4>");

        // Status Code
        apiLog.append("<div class='response-detail-item'>");
        apiLog.append("<strong>Status Code:</strong> ");
        apiLog.append(getColorCodedStatus(resp.getStatusCode()));
        apiLog.append("</div>");

        // Response Body
        String responseBody = resp.getBody().asString();
        if (!responseBody.isEmpty()) {
            apiLog.append("<div class='response-detail-item'>");
            apiLog.append("<strong>📦 Response Body:</strong><br/>");
            apiLog.append("<div class='code-block'>");
            apiLog.append(formatResponseBody(resp));
            apiLog.append("</div>");
            apiLog.append("</div>");
        }

        apiLog.append("</div>");

        // ✅ Track for statistics only (not for summary display)
        statusCodeCounts.merge(String.valueOf(resp.getStatusCode()), 1, Integer::sum);

        // ✅ Log once
        logger.info(apiLog.toString());
    }

    private static void cleanupThreadLocals() {
        qReqSpec.remove();
        response.remove();
        expectedStatusCode.remove();
        testStartTime.remove();
        databaseQueries.remove();
    }

    public static void clearThreadLocals() {
        test.remove();
        extentLogger.remove();
        cleanupThreadLocals();
    }
}