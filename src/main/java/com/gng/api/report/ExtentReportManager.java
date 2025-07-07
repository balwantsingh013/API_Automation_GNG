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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gng.api.constants.TestConstant.REPORT_PATH;

@Slf4j
public class ExtentReportManager {

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> extentLogger = new ThreadLocal<>();
    private static final ThreadLocal<QueryableRequestSpecification> qReqSpec = new ThreadLocal<>();
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> expectedStatusCode = new ThreadLocal<>();
    private static final ThreadLocal<Long> testStartTime = new ThreadLocal<>();

    // Statistics tracking
    private static final AtomicInteger totalTests = new AtomicInteger(0);
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);
    private static final AtomicInteger skippedTests = new AtomicInteger(0);
    private static final Map<String, Integer> statusCodeCounts = new ConcurrentHashMap<>();

    private static ExtentReports extent;
    private static ExtentSparkReporter spark;

    private ExtentReportManager() {
    }

    public static void initialiseExtentReport() {
        extent = new ExtentReports();
        spark = new ExtentSparkReporter(REPORT_PATH + "GNG-API-Report-" + CommonUtil.getCurrentDateTime() + ".html");
        setConfig();
    }

    private static void setConfig() {
        // Professional configuration
        spark.config().setDocumentTitle("GNG API Test Report");
        spark.config().setReportName("GNG API Automation Test Results");
        spark.config().setTheme(Theme.STANDARD); // Better readability with light theme
        spark.config().setOfflineMode(true);
        spark.config().setEncoding("utf-8");
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        // Enhanced CSS for professional appearance
        spark.config().setCss(getProfessionalCSS());

        // Enhanced JavaScript for better interactions
        spark.config().setJs(getProfessionalJavaScript());

        extent.attachReporter(spark);
        setSystemInfo();
    }

    private static String getProfessionalCSS() {
        return """
            /* Main Layout Improvements */
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important; }
            
            /* Header Styling */
            .brand-logo { display: none; }
            .nav-wrapper { 
                background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%) !important;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .navbar-brand { color: white !important; font-weight: bold; font-size: 24px; }
            
            /* Card and Panel Styling */
            .card-panel { 
                box-shadow: 0 8px 32px rgba(0,0,0,0.1) !important;
                border-radius: 12px !important;
                border: 1px solid #e0e6ed !important;
                margin-bottom: 20px !important;
            }
            
            /* Test Status Colors */
            .test-node .status-pass { 
                background: linear-gradient(135deg, #27ae60, #2ecc71) !important;
                color: white !important;
                padding: 8px 16px !important;
                border-radius: 20px !important;
                font-weight: bold !important;
                box-shadow: 0 4px 12px rgba(46, 204, 113, 0.3) !important;
            }
            
            .test-node .status-fail { 
                background: linear-gradient(135deg, #e74c3c, #c0392b) !important;
                color: white !important;
                padding: 8px 16px !important;
                border-radius: 20px !important;
                font-weight: bold !important;
                box-shadow: 0 4px 12px rgba(231, 76, 60, 0.3) !important;
            }
            
            .test-node .status-skip { 
                background: linear-gradient(135deg, #f39c12, #e67e22) !important;
                color: white !important;
                padding: 8px 16px !important;
                border-radius: 20px !important;
                font-weight: bold !important;
                box-shadow: 0 4px 12px rgba(243, 156, 18, 0.3) !important;
            }
            
            /* Request Details Styling */
            .api-request-section {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
                color: white !important;
                padding: 20px !important;
                border-radius: 12px !important;
                margin: 15px 0 !important;
                box-shadow: 0 6px 20px rgba(102, 126, 234, 0.3) !important;
            }
            
            .api-request-section h4 {
                color: white !important;
                margin-bottom: 15px !important;
                font-size: 18px !important;
                font-weight: bold !important;
            }
            
            .request-detail-item {
                background: rgba(255, 255, 255, 0.1) !important;
                padding: 12px !important;
                border-radius: 8px !important;
                margin: 8px 0 !important;
                border-left: 4px solid #74b9ff !important;
            }
            
            /* Response Details Styling */
            .api-response-section {
                background: linear-gradient(135deg, #00b894 0%, #00cec9 100%) !important;
                color: white !important;
                padding: 20px !important;
                border-radius: 12px !important;
                margin: 15px 0 !important;
                box-shadow: 0 6px 20px rgba(0, 184, 148, 0.3) !important;
            }
            
            .api-response-section h4 {
                color: white !important;
                margin-bottom: 15px !important;
                font-size: 18px !important;
                font-weight: bold !important;
            }
            
            .response-detail-item {
                background: rgba(255, 255, 255, 0.1) !important;
                padding: 12px !important;
                border-radius: 8px !important;
                margin: 8px 0 !important;
                border-left: 4px solid #55efc4 !important;
            }
            
            /* Status Code Styling */
            .status-200 { 
                background: #27ae60 !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(39, 174, 96, 0.3) !important;
            }
            
            .status-201 { 
                background: #2ecc71 !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(46, 204, 113, 0.3) !important;
            }
            
            .status-400 { 
                background: #e74c3c !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(231, 76, 60, 0.3) !important;
            }
            
            .status-401 { 
                background: #c0392b !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(192, 57, 43, 0.3) !important;
            }
            
            .status-404 { 
                background: #8e44ad !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(142, 68, 173, 0.3) !important;
            }
            
            .status-500 { 
                background: #2c3e50 !important; 
                color: white !important; 
                padding: 6px 12px !important; 
                border-radius: 15px !important; 
                font-weight: bold !important;
                box-shadow: 0 2px 8px rgba(44, 62, 80, 0.3) !important;
            }
            
            /* Response Time Badge */
            .response-time-badge {
                background: linear-gradient(135deg, #fd79a8, #fdcb6e) !important;
                color: white !important;
                padding: 8px 16px !important;
                border-radius: 20px !important;
                font-weight: bold !important;
                display: inline-block !important;
                margin: 5px 0 !important;
                box-shadow: 0 4px 12px rgba(253, 121, 168, 0.3) !important;
            }
            
            /* Error Highlighting */
            .error-section {
                background: linear-gradient(135deg, #ff7675, #d63031) !important;
                color: white !important;
                padding: 20px !important;
                border-radius: 12px !important;
                margin: 15px 0 !important;
                border-left: 6px solid #a29bfe !important;
                box-shadow: 0 6px 20px rgba(255, 118, 117, 0.3) !important;
            }
            
            /* Code Block Styling */
            .code-block {
                background: #2d3748 !important;
                color: #e2e8f0 !important;
                padding: 20px !important;
                border-radius: 8px !important;
                margin: 10px 0 !important;
                overflow-x: auto !important;
                box-shadow: inset 0 2px 4px rgba(0,0,0,0.1) !important;
                border: 1px solid #4a5568 !important;
            }
            
            /* Info Log Styling */
            .info-log {
                background: linear-gradient(135deg, #74b9ff, #0984e3) !important;
                color: white !important;
                padding: 12px 20px !important;
                border-radius: 25px !important;
                margin: 8px 0 !important;
                border-left: 4px solid #00b894 !important;
                box-shadow: 0 4px 12px rgba(116, 185, 255, 0.3) !important;
            }
            
            /* Warning Log Styling */
            .warning-log {
                background: linear-gradient(135deg, #fdcb6e, #e17055) !important;
                color: white !important;
                padding: 12px 20px !important;
                border-radius: 25px !important;
                margin: 8px 0 !important;
                border-left: 4px solid #f39c12 !important;
                box-shadow: 0 4px 12px rgba(253, 203, 110, 0.3) !important;
            }
            
            /* Database Log Styling */
            .db-log {
                background: linear-gradient(135deg, #a29bfe, #6c5ce7) !important;
                color: white !important;
                padding: 12px 20px !important;
                border-radius: 25px !important;
                margin: 8px 0 !important;
                border-left: 4px solid #fd79a8 !important;
                box-shadow: 0 4px 12px rgba(162, 155, 254, 0.3) !important;
            }
            
            /* Collapsible Content */
            .collapsible-content {
                transition: all 0.3s ease !important;
                border-radius: 8px !important;
                padding: 15px !important;
                background: rgba(255, 255, 255, 0.05) !important;
                margin-top: 10px !important;
            }
            
            /* Dashboard Statistics */
            .stats-container {
                background: linear-gradient(135deg, #636e72, #2d3436) !important;
                color: white !important;
                padding: 25px !important;
                border-radius: 15px !important;
                margin: 20px 0 !important;
                box-shadow: 0 8px 25px rgba(99, 110, 114, 0.3) !important;
            }
            
            .stat-item {
                background: rgba(255, 255, 255, 0.1) !important;
                padding: 15px !important;
                border-radius: 10px !important;
                margin: 10px 0 !important;
                border-left: 5px solid #74b9ff !important;
            }
            
            /* Hover Effects */
            .card-panel:hover {
                transform: translateY(-2px) !important;
                box-shadow: 0 12px 40px rgba(0,0,0,0.15) !important;
                transition: all 0.3s ease !important;
            }
            
            /* Mobile Responsiveness */
            @media (max-width: 768px) {
                .api-request-section, .api-response-section {
                    margin: 10px 5px !important;
                    padding: 15px !important;
                }
            }
            """;
    }

    private static String getProfessionalJavaScript() {
        return """
            document.addEventListener('DOMContentLoaded', function() {
                // Add smooth animations
                const cards = document.querySelectorAll('.card-panel');
                cards.forEach(card => {
                    card.style.transition = 'all 0.3s ease';
                });
                
                // Add click-to-expand functionality
                const sections = document.querySelectorAll('.api-request-section, .api-response-section');
                sections.forEach(section => {
                    section.style.cursor = 'pointer';
                    section.addEventListener('click', function() {
                        const content = this.querySelector('.collapsible-content');
                        if (content) {
                            if (content.style.display === 'none') {
                                content.style.display = 'block';
                                content.style.animation = 'fadeIn 0.3s ease';
                            } else {
                                content.style.display = 'none';
                            }
                        }
                    });
                });
                
                // Add fade-in animation
                const style = document.createElement('style');
                style.textContent = `
                    @keyframes fadeIn {
                        from { opacity: 0; transform: translateY(-10px); }
                        to { opacity: 1; transform: translateY(0); }
                    }
                    @keyframes slideIn {
                        from { transform: translateX(-20px); opacity: 0; }
                        to { transform: translateX(0); opacity: 1; }
                    }
                `;
                document.head.appendChild(style);
                
                // Add status code color coding
                const statusElements = document.querySelectorAll('[class*="status-"]');
                statusElements.forEach(element => {
                    element.style.animation = 'slideIn 0.5s ease';
                });
            });
            """;
    }

    private static void setSystemInfo() {
        try {
            extent.setSystemInfo("🖥️ System", InetAddress.getLocalHost().getHostName());
            extent.setSystemInfo("💻 OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            extent.setSystemInfo("☕ Java", System.getProperty("java.version"));
            extent.setSystemInfo("🌍 Environment", ApplicationContext.get().getEnvironment());
            extent.setSystemInfo("⚙️ Config", ApplicationContext.get().getEnvConfigFile());
            extent.setSystemInfo("🌐 Base URI", ApplicationContext.get().getEnvConfig().getBaseUri());
            extent.setSystemInfo("👤 Tester", System.getProperty("user.name"));
            extent.setSystemInfo("🕒 Execution Start", CommonUtil.getCurrentDateTime());
        } catch (UnknownHostException e) {
            log.error("Error setting system info: {}", e.getMessage());
        }
    }

    public static void generateReport(ITestResult result) {
        ExtentTest logger = extentLogger.get();
        long executionTime = System.currentTimeMillis() - testStartTime.get();

        totalTests.incrementAndGet();

        if (result.getStatus() == ITestResult.FAILURE) {
            failedTests.incrementAndGet();
            logFailureDetails(logger, result, executionTime);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            passedTests.incrementAndGet();
            logSuccessDetails(logger, executionTime);
        } else if (result.getStatus() == ITestResult.SKIP) {
            skippedTests.incrementAndGet();
            logSkipDetails(logger, executionTime);
        }

        cleanupThreadLocals();
        extent.flush();
    }

    private static void logFailureDetails(ExtentTest logger, ITestResult result, long executionTime) {
        StringBuilder failureLog = new StringBuilder();

        // Error section with professional styling
        failureLog.append("<div class='error-section'>");
        failureLog.append("<h3>❌ Test Execution Failed</h3>");
        failureLog.append("<div class='response-time-badge'>⏱️ Execution Time: ").append(executionTime).append(" ms</div><br/>");
        failureLog.append("<strong>🚨 Error Details:</strong><br/>");
        failureLog.append("<div style='background: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px; margin-top: 10px;'>");
        failureLog.append(result.getThrowable().toString());
        failureLog.append("</div>");
        failureLog.append("</div>");

        // Add request and response details if enabled
        if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnFail())) {
            failureLog.append(getEnhancedRequestDetails());
            failureLog.append(getEnhancedResponseDetails());
        }

        logger.fail(failureLog.toString());
    }

    private static void logSuccessDetails(ExtentTest logger, long executionTime) {
        StringBuilder successLog = new StringBuilder();

        successLog.append("<div class='info-log'>");
        successLog.append("✅ <strong>Test Passed Successfully</strong>");
        successLog.append("</div>");

        successLog.append("<div class='response-time-badge'>");
        successLog.append("⚡ Execution Time: ").append(executionTime).append(" ms");
        successLog.append("</div><br/>");

        if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnPass())) {
            successLog.append(getEnhancedRequestDetails());
            successLog.append(getEnhancedResponseDetails());
        }

        // Track response time and status codes
        Response resp = response.get();
        if (resp != null) {
            String statusCode = String.valueOf(resp.getStatusCode());
            statusCodeCounts.merge(statusCode, 1, Integer::sum);
        }

        logger.pass(successLog.toString());
    }

    private static void logSkipDetails(ExtentTest logger, long executionTime) {
        StringBuilder skipLog = new StringBuilder();

        skipLog.append("<div class='warning-log'>");
        skipLog.append("⏭️ <strong>Test Skipped</strong>");
        skipLog.append("</div>");

        skipLog.append("<div class='response-time-badge'>");
        skipLog.append("⏱️ Time: ").append(executionTime).append(" ms");
        skipLog.append("</div><br/>");

        skipLog.append(getEnhancedRequestDetails());

        logger.skip(skipLog.toString());
    }

    public static String getEnhancedRequestDetails() {
        QueryableRequestSpecification reqSpec = qReqSpec.get();
        if (reqSpec == null) {
            return "<div class='info-log'>ℹ️ No API Request - Database Validation Only</div>";
        }

        StringBuilder details = new StringBuilder();
        details.append("<div class='api-request-section'>");
        details.append("<h4>📤 API Request Details</h4>");
        details.append("<div class='collapsible-content'>");

        // Base URI
        details.append("<div class='request-detail-item'>");
        details.append("<strong>🌐 Base URI:</strong> ").append(reqSpec.getBaseUri());
        details.append("</div>");

        // Headers
        if (reqSpec.getHeaders() != null && !reqSpec.getHeaders().toString().isEmpty()) {
            details.append("<div class='request-detail-item'>");
            details.append("<strong>📋 Headers:</strong><br/>");
            details.append("<div class='code-block'>").append(formatJson(reqSpec.getHeaders().toString())).append("</div>");
            details.append("</div>");
        }

        // Parameters
        if (!reqSpec.getRequestParams().isEmpty()) {
            details.append("<div class='request-detail-item'>");
            details.append("<strong>🔧 Parameters:</strong><br/>");
            details.append("<div class='code-block'>").append(formatJson(reqSpec.getRequestParams().toString())).append("</div>");
            details.append("</div>");
        }

        // Request Body
        if (reqSpec.getBody() != null) {
            details.append("<div class='request-detail-item'>");
            details.append("<strong>📝 Request Body:</strong><br/>");
            details.append("<div class='code-block'>").append(formatJson(reqSpec.getBody().toString())).append("</div>");
            details.append("</div>");
        }

        details.append("</div></div>");
        return details.toString();
    }

    public static String getEnhancedResponseDetails() {
        Response resp = response.get();
        if (resp == null) {
            return "<div class='info-log'>ℹ️ No API Response - Database Validation Only</div>";
        }

        StringBuilder details = new StringBuilder();
        details.append("<div class='api-response-section'>");
        details.append("<h4>📥 API Response Details</h4>");
        details.append("<div class='collapsible-content'>");

        // Status Code with color coding
        details.append("<div class='response-detail-item'>");
        details.append("<strong>📊 Status Code:</strong> ");
        details.append(getColorCodedStatus(resp.getStatusCode()));
        details.append("</div>");

        // Response Time
        details.append("<div class='response-detail-item'>");
        details.append("<strong>⏱️ Response Time:</strong> ");
        details.append("<span class='response-time-badge'>").append(resp.getTime()).append(" ms</span>");
        details.append("</div>");

        // Expected Status
        String expectedStatus = expectedStatusCode.get();
        if (expectedStatus != null) {
            details.append("<div class='response-detail-item'>");
            details.append("<strong>✅ Expected Status:</strong> ");
            details.append("<span class='status-").append(expectedStatus).append("'>").append(expectedStatus).append("</span>");
            details.append("</div>");
        }

        // Response Headers
        if (resp.getHeaders() != null) {
            details.append("<div class='response-detail-item'>");
            details.append("<strong>📋 Response Headers:</strong><br/>");
            details.append("<div class='code-block'>").append(formatJson(resp.getHeaders().toString())).append("</div>");
            details.append("</div>");
        }

        // Response Body
        if (resp.getBody() != null) {
            details.append("<div class='response-detail-item'>");
            details.append("<strong>📄 Response Body:</strong><br/>");
            details.append("<div class='code-block'>").append(formatJson(resp.getBody().asPrettyString())).append("</div>");
            details.append("</div>");
        }

        details.append("</div></div>");
        return details.toString();
    }

    private static String getColorCodedStatus(int statusCode) {
        String statusClass = "status-" + statusCode;
        String statusText = statusCode + " - " + getStatusText(statusCode);
        return "<span class='" + statusClass + "'>" + statusText + "</span>";
    }

    private static String getStatusText(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            default -> "Unknown";
        };
    }

    private static String formatJson(String json) {
        // Basic JSON formatting for better readability
        try {
            return json.replace("{", "{\n  ")
                    .replace("}", "\n}")
                    .replace(",", ",\n  ")
                    .replace("[", "[\n  ")
                    .replace("]", "\n]");
        } catch (Exception e) {
            return json;
        }
    }

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

    public static void addTestStatistics() {
        ExtentTest statsTest = extent.createTest("📊 Test Execution Summary");

        StringBuilder stats = new StringBuilder();
        stats.append("<div class='stats-container'>");
        stats.append("<h3>📈 Overall Test Statistics</h3>");

        // Overall stats
        stats.append("<div class='stat-item'>");
        stats.append("<strong>🎯 Total Tests Executed:</strong> ").append(totalTests.get());
        stats.append("</div>");

        stats.append("<div class='stat-item'>");
        stats.append("<strong>✅ Passed:</strong> ").append(passedTests.get());
        double passPercentage = totalTests.get() > 0 ? (passedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append(" <span class='status-200'>(").append(String.format("%.1f", passPercentage)).append("%)</span>");
        stats.append("</div>");

        stats.append("<div class='stat-item'>");
        stats.append("<strong>❌ Failed:</strong> ").append(failedTests.get());
        double failPercentage = totalTests.get() > 0 ? (failedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append(" <span class='status-400'>(").append(String.format("%.1f", failPercentage)).append("%)</span>");
        stats.append("</div>");

        stats.append("<div class='stat-item'>");
        stats.append("<strong>⏭️ Skipped:</strong> ").append(skippedTests.get());
        double skipPercentage = totalTests.get() > 0 ? (skippedTests.get() * 100.0 / totalTests.get()) : 0;
        stats.append(" <span class='status-404'>(").append(String.format("%.1f", skipPercentage)).append("%)</span>");
        stats.append("</div>");

        // Status code distribution
        if (!statusCodeCounts.isEmpty()) {
            stats.append("<h4>🌐 HTTP Status Code Distribution</h4>");
            statusCodeCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        stats.append("<div class='stat-item'>");
                        stats.append("<strong>Status ").append(getColorCodedStatus(Integer.parseInt(entry.getKey()))).append(":</strong> ");
                        stats.append(entry.getValue()).append(" occurrences");
                        stats.append("</div>");
                    });
        }

        stats.append("</div>");
        statsTest.info(stats.toString());
    }

    public static void flushReports() {
        log.info("📊 Publishing Professional Extent Reports with Enhanced Styling");
        addTestStatistics();
        extent.flush();
    }

    public static void addRequestDetailsToReport(RequestSpecification reqSpec) {
        QueryableRequestSpecification queryable = SpecificationQuerier.query(reqSpec);
        qReqSpec.set(queryable);
    }

    public static void addResponseDetailsToReport(Response resp, int statusCode) {
        response.set(resp);
        expectedStatusCode.set(String.valueOf(statusCode));
    }

    private static void cleanupThreadLocals() {
        qReqSpec.remove();
        response.remove();
        expectedStatusCode.remove();
        testStartTime.remove();
    }

    public static void clearThreadLocals() {
        test.remove();
        extentLogger.remove();
        cleanupThreadLocals();
    }


}