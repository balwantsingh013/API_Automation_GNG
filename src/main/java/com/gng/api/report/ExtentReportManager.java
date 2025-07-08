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

    private ExtentReportManager() {
    }

    public static synchronized void initialiseExtentReport() {
        if (extent == null) {
            extent = new ExtentReports();
            spark = new ExtentSparkReporter(REPORT_PATH + "GNG-API-Report-" + CommonUtil.getCurrentDateTime() + ".html");
            setConfig();
            log.info("📊 Extent Report initialized for parallel execution");
        }
    }

    private static void setConfig() {
        // Professional configuration with parallel execution enhancements
        spark.config().setDocumentTitle("GNG API Test Report - Professional Dashboard");
        spark.config().setReportName("GNG API Automation Test Results");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setOfflineMode(true);
        spark.config().setEncoding("utf-8");
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        // Enhanced CSS for professional appearance with better visibility
        spark.config().setCss(getProfessionalCSS());
        spark.config().setJs(getProfessionalJavaScript());

        extent.attachReporter(spark);
        setSystemInfo();
    }

    private static String getProfessionalCSS() {
        return """
            /* Main Layout Improvements */
            body { 
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important; 
                background: #f8fafc !important;
            }
            
            /* FIXED: Consistent Header Styling - Match Tests Header Exactly */
            .brand-logo { display: none; }
            .nav-wrapper { 
                background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
                box-shadow: 0 4px 20px rgba(45, 55, 72, 0.3) !important;
            }
            .navbar-brand { 
                color: white !important; 
                font-weight: 700 !important; 
                font-size: 24px !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
            }
            
            /* Force ALL headers to match Tests header styling exactly */
            .container .card-panel,
            .container .row .col .card-panel,
            .card-panel[style*="background"],
            .card-panel:not(.stats-card),
            div[class*="card-panel"],
            .container > div:not(.stats-dashboard) .card-panel,
            .container .card-panel:first-child,
            .container .card-panel:first-of-type,
            .container > .card-panel,
            .row .col .card-panel {
                background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
                color: white !important;
                box-shadow: 0 4px 20px rgba(45, 55, 72, 0.3) !important;
            }
            
            /* Force ALL header text to match Tests header text styling */
            .container .card-panel h1,
            .container .card-panel h2, 
            .container .card-panel h3,
            .container .card-panel h4,
            .container .card-panel h5,
            .container .card-panel h6,
            .container .card-panel .collection-item,
            .container .card-panel .collection-item a,
            .container .card-panel .collection-item span,
            .container .card-panel span,
            .container .card-panel p,
            .container .card-panel div,
            .container .card-panel strong,
            .container .card-panel b,
            .container .card-panel .title,
            .container .card-panel .header,
            .card-panel:not(.stats-card) *,
            .row .col .card-panel *,
            .container > .card-panel * {
                color: white !important;
                font-weight: 700 !important;
                font-size: 18px !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
            }
            
            /* Specific header text sizing to match Tests exactly */
            .container .card-panel h1,
            .container .card-panel h2,
            .container .card-panel h3 {
                font-size: 22px !important;
                font-weight: 700 !important;
                margin: 10px 0 !important;
                padding: 10px !important;
                text-align: center !important;
            }
            
            /* Override any inline styles */
            .container .card-panel[style] {
                background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
            }
            
            .container .card-panel[style] * {
                color: white !important;
                font-weight: 700 !important;
                font-size: 18px !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            }
            
            /* Ensure navigation and system info headers match */
            .nav-wrapper,
            .navbar-brand,
            .container:first-child .card-panel,
            .container .card-panel:contains("System"),
            .container .card-panel:contains("Timeline"),
            .container .card-panel:contains("Test") {
                background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
                color: white !important;
            }
            
            /* ADDED: Specific targeting for Test Execution Steps, Timeline, and System/Environment headers */
            .test-node-name,
            .node-name,
            .test-name,
            .category-name,
            .timeline-header,
            .system-info-header,
            .environment-header,
            .execution-steps-header,
            /* Target any text containing these keywords */
            *:contains("Test Execution Steps"),
            *:contains("Timeline"),
            *:contains("System"),
            *:contains("Environment"),
            /* Target parent containers of these sections */
            .test-node .node-name,
            .timeline-view .card-panel,
            .category-view .card-panel,
            .dashboard-view .card-panel,
            /* More specific selectors for Extent Report structure */
            .test-detail .node-name,
            .test-detail .category-name,
            .test-content .node-name,
            .extent-test-node .node-name,
            .extent-category .category-name {
                color: white !important;
                font-weight: 700 !important;
                font-size: 18px !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
            }
            
            /* ADDED: Force white color on all test node and category elements */
            .test-node,
            .test-node *,
            .category-node,
            .category-node *,
            .node-name,
            .node-name *,
            .category-name,
            .category-name *,
            .test-detail-header,
            .test-detail-header *,
            .test-step-name,
            .test-step-name *,
            .extent-node-name,
            .extent-node-name * {
                color: white !important;
                font-weight: 700 !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            }
            
            /* ADDED: Target Timeline and System sections specifically */
            [data-toggle="timeline"] .card-panel,
            [data-toggle="timeline"] .card-panel *,
            [data-toggle="system-view"] .card-panel,
            [data-toggle="system-view"] .card-panel *,
            .timeline-container .card-panel,
            .timeline-container .card-panel *,
            .system-container .card-panel,
            .system-container .card-panel * {
                color: white !important;
                font-weight: 700 !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            }
            
            /* Thread Information Styling */
            .thread-info {
                background: linear-gradient(135deg, #7c3aed, #5b21b6) !important;
                color: white !important;
                padding: 10px 18px !important;
                border-radius: 25px !important;
                font-size: 13px !important;
                font-weight: 600 !important;
                display: inline-block !important;
                margin: 8px 0 !important;
                box-shadow: 0 4px 12px rgba(124, 58, 237, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
            }
            
            /* Enhanced Statistics Header */
            .enhanced-stats-header {
                background: linear-gradient(135deg, #1e40af 0%, #3b82f6 50%, #60a5fa 100%) !important;
                color: white !important;
                padding: 30px !important;
                border-radius: 20px !important;
                margin-bottom: 30px !important;
                text-align: center !important;
                box-shadow: 0 12px 35px rgba(59, 130, 246, 0.4) !important;
                border: 1px solid rgba(255, 255, 255, 0.2) !important;
            }
            
            .stats-title {
                font-size: 32px !important;
                font-weight: 700 !important;
                margin: 0 !important;
                color: white !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
                letter-spacing: 0.5px !important;
            }
            
            .execution-timestamp {
                font-size: 18px !important;
                margin-top: 12px !important;
                opacity: 0.95 !important;
                font-weight: 500 !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
            }
            
            /* Dashboard Grid */
            .stats-dashboard {
                display: grid !important;
                grid-template-columns: repeat(auto-fit, minmax(450px, 1fr)) !important;
                gap: 30px !important;
                margin: 25px 0 !important;
            }
            
            /* Enhanced Card Styling */
            .stats-card {
                background: white !important;
                border-radius: 20px !important;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08) !important;
                border: 1px solid #e5e7eb !important;
                overflow: hidden !important;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
            }
            
            .stats-card:hover {
                transform: translateY(-8px) scale(1.02) !important;
                box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15) !important;
                border-color: #3b82f6 !important;
            }
            
            /* Professional Card Headers */
            .card-header {
                background: linear-gradient(135deg, #1f2937, #374151) !important;
                color: white !important;
                padding: 25px !important;
                text-align: center !important;
                position: relative !important;
            }
            
            .card-header::before {
                content: '' !important;
                position: absolute !important;
                top: 0 !important;
                left: 0 !important;
                right: 0 !important;
                height: 4px !important;
                background: linear-gradient(90deg, #3b82f6, #8b5cf6, #06b6d4) !important;
            }
            
            .card-title {
                font-size: 22px !important;
                font-weight: 700 !important;
                margin: 0 !important;
                color: white !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
                letter-spacing: 0.3px !important;
            }
            
            .card-body {
                padding: 30px !important;
                background: white !important;
            }
            
            /* FIXED: Improved Statistics Rows - Better alignment */
            .stat-row {
                display: flex !important;
                justify-content: space-between !important;
                align-items: center !important;
                padding: 20px 0 !important;
                border-bottom: 2px solid #f1f5f9 !important;
                margin-bottom: 18px !important;
                position: relative !important;
                min-height: 60px !important;
            }
            
            .stat-row:last-child {
                border-bottom: none !important;
                margin-bottom: 0 !important;
            }
            
            /* FIXED: Better label styling */
            .stat-label {
                font-size: 18px !important;
                font-weight: 700 !important;
                color: #1f2937 !important;
                flex: 0 0 40% !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.05) !important;
                display: flex !important;
                align-items: center !important;
                white-space: nowrap !important;
            }
            
            /* FIXED: Better value container alignment */
            .stat-value-container {
                display: flex !important;
                align-items: center !important;
                gap: 20px !important;
                flex: 1 !important;
                justify-content: flex-end !important;
                min-height: 40px !important;
            }
            
            /* Enhanced Stat Values */
            .stat-value {
                font-size: 28px !important;
                font-weight: 800 !important;
                min-width: 60px !important;
                text-align: center !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
            }
            
            .total-count {
                color: #1e40af !important;
                font-size: 36px !important;
                text-shadow: 0 2px 6px rgba(30, 64, 175, 0.3) !important;
            }
            
            .passed-count {
                color: #059669 !important;
                text-shadow: 0 2px 4px rgba(5, 150, 105, 0.3) !important;
            }
            
            .failed-count {
                color: #dc2626 !important;
                text-shadow: 0 2px 4px rgba(220, 38, 38, 0.3) !important;
            }
            
            .skipped-count {
                color: #d97706 !important;
                text-shadow: 0 2px 4px rgba(217, 119, 6, 0.3) !important;
            }
            
            /* Professional Percentage Badges */
            .stat-percentage {
                font-size: 16px !important;
                font-weight: 700 !important;
                padding: 8px 16px !important;
                border-radius: 25px !important;
                color: white !important;
                min-width: 80px !important;
                text-align: center !important;
                box-shadow: 0 4px 12px rgba(0,0,0,0.2) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
            }
            
            .passed-percentage {
                background: linear-gradient(135deg, #059669, #10b981) !important;
                box-shadow: 0 4px 15px rgba(5, 150, 105, 0.4) !important;
            }
            
            .failed-percentage {
                background: linear-gradient(135deg, #dc2626, #ef4444) !important;
                box-shadow: 0 4px 15px rgba(220, 38, 38, 0.4) !important;
            }
            
            .skipped-percentage {
                background: linear-gradient(135deg, #d97706, #f59e0b) !important;
                box-shadow: 0 4px 15px rgba(217, 119, 6, 0.4) !important;
            }
            
            /* Enhanced Progress Bars */
            .progress-bar {
                width: 100% !important;
                height: 12px !important;
                background: #f1f5f9 !important;
                border-radius: 15px !important;
                overflow: hidden !important;
                margin-top: 15px !important;
                border: 1px solid #e2e8f0 !important;
                box-shadow: inset 0 2px 4px rgba(0,0,0,0.05) !important;
            }
            
            .progress-fill {
                height: 100% !important;
                border-radius: 15px !important;
                transition: width 1s cubic-bezier(0.4, 0, 0.2, 1) !important;
                position: relative !important;
                box-shadow: 0 2px 8px rgba(0,0,0,0.2) !important;
            }
            
            .passed-progress {
                background: linear-gradient(90deg, #059669, #10b981, #34d399) !important;
            }
            
            .failed-progress {
                background: linear-gradient(90deg, #dc2626, #ef4444, #f87171) !important;
            }
            
            .skipped-progress {
                background: linear-gradient(90deg, #d97706, #f59e0b, #fbbf24) !important;
            }
            
            /* Success Rate Section */
            .success-rate-section {
                background: linear-gradient(135deg, #1e40af, #3b82f6, #60a5fa) !important;
                color: white !important;
                padding: 25px !important;
                border-radius: 18px !important;
                text-align: center !important;
                margin-top: 30px !important;
                box-shadow: 0 10px 30px rgba(59, 130, 246, 0.4) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .success-rate-label {
                font-size: 20px !important;
                font-weight: 700 !important;
                margin-bottom: 15px !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
            }
            
            .success-rate-value {
                font-size: 42px !important;
                font-weight: 900 !important;
                padding: 15px 25px !important;
                border-radius: 30px !important;
                display: inline-block !important;
                margin-top: 12px !important;
                text-shadow: 0 3px 6px rgba(0,0,0,0.4) !important;
                border: 3px solid rgba(255,255,255,0.3) !important;
            }
            
            .success-rate-value.excellent {
                background: linear-gradient(135deg, #059669, #10b981) !important;
                box-shadow: 0 8px 20px rgba(5, 150, 105, 0.5) !important;
            }
            
            .success-rate-value.good {
                background: linear-gradient(135deg, #d97706, #f59e0b) !important;
                box-shadow: 0 8px 20px rgba(217, 119, 6, 0.5) !important;
            }
            
            .success-rate-value.needs-improvement {
                background: linear-gradient(135deg, #dc2626, #ef4444) !important;
                box-shadow: 0 8px 20px rgba(220, 38, 38, 0.5) !important;
            }
            
            /* Thread Statistics */
            .thread-stat-row {
                display: flex !important;
                align-items: center !important;
                padding: 18px 0 !important;
                border-bottom: 2px solid #f1f5f9 !important;
                margin-bottom: 16px !important;
                background: linear-gradient(90deg, transparent, #f8fafc, transparent) !important;
                border-radius: 10px !important;
            }
            
            .thread-stat-row:last-child {
                border-bottom: none !important;
            }
            
            .thread-label {
                flex: 2 !important;
                font-weight: 700 !important;
                color: #1f2937 !important;
                font-size: 16px !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.05) !important;
            }
            
            .thread-value {
                flex: 1 !important;
                text-align: center !important;
                font-weight: 800 !important;
                color: #1e40af !important;
                font-size: 18px !important;
                text-shadow: 0 1px 3px rgba(30, 64, 175, 0.3) !important;
            }
            
            .thread-percentage {
                flex: 1 !important;
                text-align: center !important;
                font-size: 15px !important;
                color: #6b7280 !important;
                font-weight: 600 !important;
            }
            
            .thread-progress-bar {
                flex: 2 !important;
                height: 10px !important;
                background: #f1f5f9 !important;
                border-radius: 12px !important;
                margin-left: 20px !important;
                overflow: hidden !important;
                border: 1px solid #e5e7eb !important;
                box-shadow: inset 0 1px 3px rgba(0,0,0,0.1) !important;
            }
            
            .thread-progress-fill {
                height: 100% !important;
                background: linear-gradient(90deg, #7c3aed, #8b5cf6, #a855f7) !important;
                border-radius: 12px !important;
                transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1) !important;
                box-shadow: 0 2px 6px rgba(124, 58, 237, 0.4) !important;
            }
            
            /* Status Code Statistics */
            .status-code-row {
                display: flex !important;
                align-items: center !important;
                padding: 20px 0 !important;
                border-bottom: 2px solid #f1f5f9 !important;
                margin-bottom: 18px !important;
                background: linear-gradient(90deg, transparent, #f8fafc, transparent) !important;
                border-radius: 12px !important;
            }
            
            .status-code-row:last-child {
                border-bottom: none !important;
            }
            
            .status-code-label {
                flex: 2 !important;
                font-weight: 700 !important;
                font-size: 16px !important;
            }
            
            .status-code-value {
                flex: 1 !important;
                text-align: center !important;
                font-weight: 800 !important;
                color: #1f2937 !important;
                font-size: 18px !important;
            }
            
            .status-code-percentage {
                flex: 1 !important;
                text-align: center !important;
                font-size: 15px !important;
                color: #6b7280 !important;
                font-weight: 600 !important;
            }
            
            .status-progress-bar {
                flex: 2 !important;
                height: 10px !important;
                background: #f1f5f9 !important;
                border-radius: 12px !important;
                margin-left: 20px !important;
                overflow: hidden !important;
                border: 1px solid #e5e7eb !important;
                box-shadow: inset 0 1px 3px rgba(0,0,0,0.1) !important;
            }
            
            .status-progress-fill {
                height: 100% !important;
                border-radius: 12px !important;
                transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1) !important;
                box-shadow: 0 2px 6px rgba(0,0,0,0.2) !important;
            }
            
            .success-status {
                background: linear-gradient(90deg, #059669, #10b981) !important;
            }
            
            .redirect-status {
                background: linear-gradient(90deg, #1e40af, #3b82f6) !important;
            }
            
            .client-error-status {
                background: linear-gradient(90deg, #dc2626, #ef4444) !important;
            }
            
            .server-error-status {
                background: linear-gradient(90deg, #374151, #4b5563) !important;
            }
            
            .unknown-status {
                background: linear-gradient(90deg, #6b7280, #9ca3af) !important;
            }
            
            /* Performance Statistics */
            .performance-row {
                display: flex !important;
                justify-content: space-between !important;
                align-items: center !important;
                padding: 18px 0 !important;
                border-bottom: 2px solid #f1f5f9 !important;
                margin-bottom: 16px !important;
                background: linear-gradient(90deg, transparent, #f8fafc, transparent) !important;
                border-radius: 10px !important;
            }
            
            .performance-row:last-child {
                border-bottom: none !important;
            }
            
            .perf-label {
                font-weight: 700 !important;
                color: #1f2937 !important;
                font-size: 16px !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.05) !important;
            }
            
            .perf-value {
                font-weight: 800 !important;
                padding: 10px 20px !important;
                border-radius: 20px !important;
                color: white !important;
                font-size: 16px !important;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
                box-shadow: 0 4px 12px rgba(0,0,0,0.2) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .execution-mode {
                background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
            }
            
            .thread-count {
                background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
            }
            
            .parallel-count {
                background: linear-gradient(135deg, #059669, #10b981) !important;
            }
            
            .core-count {
                background: linear-gradient(135deg, #0891b2, #06b6d4) !important;
            }
            
            /* Enhanced Status Code Styling */
            .status-200, .status-201, .status-202, .status-204 { 
                background: linear-gradient(135deg, #059669, #10b981) !important; 
                color: white !important; 
                padding: 8px 16px !important; 
                border-radius: 20px !important; 
                font-weight: 700 !important;
                box-shadow: 0 4px 12px rgba(5, 150, 105, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .status-400, .status-401, .status-403, .status-404, .status-422 { 
                background: linear-gradient(135deg, #dc2626, #ef4444) !important; 
                color: white !important; 
                padding: 8px 16px !important; 
                border-radius: 20px !important; 
                font-weight: 700 !important;
                box-shadow: 0 4px 12px rgba(220, 38, 38, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .status-500, .status-502, .status-503 { 
                background: linear-gradient(135deg, #374151, #4b5563) !important; 
                color: white !important; 
                padding: 8px 16px !important; 
                border-radius: 20px !important; 
                font-weight: 700 !important;
                box-shadow: 0 4px 12px rgba(55, 65, 81, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            /* Response Time Badge */
            .response-time-badge {
                background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
                color: white !important;
                padding: 10px 20px !important;
                border-radius: 25px !important;
                font-weight: 700 !important;
                display: inline-block !important;
                margin: 8px 0 !important;
                box-shadow: 0 4px 15px rgba(124, 58, 237, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            /* Enhanced Test Status Colors */
            .test-node .status-pass { 
                background: linear-gradient(135deg, #059669, #10b981) !important;
                color: white !important;
                padding: 10px 20px !important;
                border-radius: 25px !important;
                font-weight: 700 !important;
                box-shadow: 0 6px 15px rgba(5, 150, 105, 0.4) !important;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .test-node .status-fail { 
                background: linear-gradient(135deg, #dc2626, #ef4444) !important;
                color: white !important;
                padding: 10px 20px !important;
                border-radius: 25px !important;
                font-weight: 700 !important;
                box-shadow: 0 6px 15px rgba(220, 38, 38, 0.4) !important;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
             .test-node .status-skip { 
                background: linear-gradient(135deg, #d97706, #f59e0b) !important;
                color: white !important;
                padding: 10px 20px !important;
                border-radius: 25px !important;
                font-weight: 700 !important;
                box-shadow: 0 6px 15px rgba(217, 119, 6, 0.4) !important;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            /* Enhanced Section Styling */
            .api-request-section {
                background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
                color: white !important;
                padding: 25px !important;
                border-radius: 18px !important;
                margin: 20px 0 !important;
                box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .api-response-section {
                background: linear-gradient(135deg, #059669, #10b981) !important;
                color: white !important;
                padding: 25px !important;
                border-radius: 18px !important;
                margin: 20px 0 !important;
                box-shadow: 0 8px 25px rgba(5, 150, 105, 0.4) !important;
                border: 2px solid rgba(255,255,255,0.2) !important;
            }
            
            .api-request-section h4, .api-response-section h4 {
                color: white !important;
                margin-bottom: 20px !important;
                font-size: 20px !important;
                font-weight: 700 !important;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
            }
            
            .request-detail-item, .response-detail-item {
                background: rgba(255, 255, 255, 0.15) !important;
                padding: 18px !important;
                border-radius: 12px !important;
                margin: 12px 0 !important;
                border-left: 4px solid rgba(255,255,255,0.4) !important;
                backdrop-filter: blur(10px) !important;
            }
            
            /* Enhanced Info/Warning/Error Logs */
            .info-log {
                background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
                color: white !important;
                padding: 15px 25px !important;
                border-radius: 30px !important;
                margin: 12px 0 !important;
                border-left: 4px solid #60a5fa !important;
                box-shadow: 0 6px 18px rgba(59, 130, 246, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
                font-weight: 600 !important;
            }
            
            .warning-log {
                background: linear-gradient(135deg, #d97706, #f59e0b) !important;
                color: white !important;
                padding: 15px 25px !important;
                border-radius: 30px !important;
                margin: 12px 0 !important;
                border-left: 4px solid #fbbf24 !important;
                box-shadow: 0 6px 18px rgba(217, 119, 6, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
                font-weight: 600 !important;
            }
            
            .error-section {
                background: linear-gradient(135deg, #dc2626, #ef4444) !important;
                color: white !important;
                padding: 25px !important;
                border-radius: 18px !important;
                margin: 20px 0 !important;
                border-left: 6px solid #f87171 !important;
                box-shadow: 0 8px 25px rgba(220, 38, 38, 0.4) !important;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
            }
            
            .db-log {
                background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important;
                color: white !important;
                padding: 15px 25px !important;
                border-radius: 30px !important;
                margin: 12px 0 !important;
                border-left: 4px solid #a855f7 !important;
                box-shadow: 0 6px 18px rgba(124, 58, 237, 0.4) !important;
                text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
                font-weight: 600 !important;
            }
            
            /* Code Block Styling */
            .code-block {
                background: #1f2937 !important;
                color: #f9fafb !important;
                padding: 25px !important;
                border-radius: 12px !important;
                margin: 15px 0 !important;
                overflow-x: auto !important;
                box-shadow: inset 0 2px 8px rgba(0,0,0,0.3) !important;
                border: 1px solid #374151 !important;
                font-family: 'Fira Code', 'Monaco', 'Consolas', monospace !important;
                font-size: 14px !important;
                line-height: 1.6 !important;
            }
            
            /* Card and Panel Styling */
            .card-panel { 
                box-shadow: 0 10px 35px rgba(0,0,0,0.08) !important;
                border-radius: 18px !important;
                border: 1px solid #e5e7eb !important;
                margin-bottom: 25px !important;
                background: white !important;
            }
            
            /* Hover Effects */
            .card-panel:hover {
                transform: translateY(-4px) !important;
                box-shadow: 0 15px 45px rgba(0,0,0,0.12) !important;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
                border-color: #3b82f6 !important;
            }
            
            /* Collapsible Content */
            .collapsible-content {
                transition: all 0.3s ease !important;
                border-radius: 8px !important;
                padding: 15px !important;
                background: rgba(255, 255, 255, 0.05) !important;
                margin-top: 10px !important;
            }
            
            /* Responsive Design */
            @media (max-width: 768px) {
                .stats-dashboard {
                    grid-template-columns: 1fr !important;
                    gap: 20px !important;
                }
                
                .stat-row, .thread-stat-row, .status-code-row, .performance-row {
                    flex-direction: column !important;
                    text-align: center !important;
                    gap: 12px !important;
                }
                
                .stat-value-container {
                    justify-content: center !important;
                    margin-top: 12px !important;
                }
                
                .thread-progress-bar, .status-progress-bar {
                    margin: 12px 0 !important;
                    width: 100% !important;
                }
                
                .api-request-section, .api-response-section {
                    margin: 15px 5px !important;
                    padding: 20px !important;
                }
                
                .stats-title {
                    font-size: 26px !important;
                }
                
                .card-title {
                    font-size: 18px !important;
                }
            }
            
            /* Animation Effects */
            @keyframes countUp {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
            
            @keyframes progressFill {
                from { width: 0%; }
                to { width: var(--target-width); }
            }
            
            @keyframes slideIn {
                from { opacity: 0; transform: translateY(-10px); }
                to { opacity: 1; transform: translateY(0); }
            }
            
            @keyframes fadeInUp {
                from { opacity: 0; transform: translateY(30px); }
                to { opacity: 1; transform: translateY(0); }
            }
            
            @keyframes shimmer {
                0% { left: -100%; }
                100% { left: 100%; }
            }
            
            @keyframes pulse {
                0% { opacity: 1; transform: scale(1); }
                50% { opacity: 0.9; transform: scale(1.03); }
                100% { opacity: 1; transform: scale(1); }
            }
            
            .stat-value {
                animation: countUp 0.8s ease-out !important;
            }
            
            .progress-fill {
                animation: progressFill 1.2s ease-out !important;
            }
            
            .stats-card {
                animation: fadeInUp 0.6s ease-out !important;
            }
            
            .progress-fill::after {
                content: '' !important;
                position: absolute !important;
                top: 0 !important;
                left: -100% !important;
                width: 100% !important;
                height: 100% !important;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent) !important;
                animation: shimmer 2.5s infinite !important;
            }
            """;    }

    private static String getProfessionalJavaScript() {
        return """
            document.addEventListener('DOMContentLoaded', function() {
                // Add smooth animations
                const cards = document.querySelectorAll('.card-panel, .stats-card');
                cards.forEach((card, index) => {
                    card.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
                    card.style.animationDelay = (index * 0.1) + 's';
                });
                
                // Animate progress bars with staggered timing
                const progressBars = document.querySelectorAll('.progress-fill, .thread-progress-fill, .status-progress-fill');
                progressBars.forEach((bar, index) => {
                    const targetWidth = bar.style.width;
                    bar.style.width = '0%';
                    setTimeout(() => {
                        bar.style.width = targetWidth;
                        bar.style.transition = 'width 1.2s cubic-bezier(0.4, 0, 0.2, 1)';
                    }, 500 + (index * 200));
                });
                
                // Enhanced counter animation with better easing
                const statValues = document.querySelectorAll('.stat-value, .success-rate-value');
                statValues.forEach((element, index) => {
                    const finalText = element.textContent;
                    const finalNumber = parseInt(finalText) || parseFloat(finalText) || 0;
                    
                    if (!isNaN(finalNumber) && finalNumber > 0) {
                        element.textContent = '0';
                        setTimeout(() => {
                            animateCounter(element, 0, finalNumber, finalText, 1500);
                        }, 300 + (index * 150));
                    }
                });
                
                // Enhanced hover effects for cards
                const statsCards = document.querySelectorAll('.stats-card');
                statsCards.forEach(card => {
                    card.addEventListener('mouseenter', function() {
                        this.style.transform = 'translateY(-10px) scale(1.03)';
                        this.style.boxShadow = '0 25px 60px rgba(0, 0, 0, 0.18)';
                        this.style.borderColor = '#3b82f6';
                    });
                    
                    card.addEventListener('mouseleave', function() {
                        this.style.transform = 'translateY(0) scale(1)';
                        this.style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.08)';
                        this.style.borderColor = '#e5e7eb';
                    });
                });
                
                // Enhanced click-to-expand functionality
                const expandableSections = document.querySelectorAll('.api-request-section, .api-response-section');
                expandableSections.forEach(section => {
                    const header = section.querySelector('h4');
                    if (header) {
                        header.style.cursor = 'pointer';
                        header.style.userSelect = 'none';
                        header.title = 'Click to toggle details';
                        header.style.transition = 'all 0.3s ease';
                        
                        // Add enhanced expand/collapse indicator
                        const indicator = document.createElement('span');
                        indicator.innerHTML = ' ▼';
                        indicator.style.fontSize = '14px';
                        indicator.style.transition = 'transform 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
                        indicator.style.display = 'inline-block';
                        indicator.style.marginLeft = '8px';
                        header.appendChild(indicator);
                        
                        header.addEventListener('click', function() {
                            const content = section.querySelector('.collapsible-content');
                            if (content) {
                                const isVisible = content.style.display !== 'none';
                                content.style.display = isVisible ? 'none' : 'block';
                                indicator.style.transform = isVisible ? 'rotate(-90deg)' : 'rotate(0deg)';
                                
                                // Add scale effect to header
                                this.style.transform = 'scale(0.98)';
                                setTimeout(() => {
                                    this.style.transform = 'scale(1)';
                                }, 150);
                                
                                if (!isVisible) {
                                    content.style.animation = 'slideIn 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
                                }
                            }
                        });
                        
                        header.addEventListener('mouseenter', function() {
                            this.style.opacity = '0.9';
                            this.style.transform = 'translateX(5px)';
                        });
                        
                        header.addEventListener('mouseleave', function() {
                            this.style.opacity = '1';
                            this.style.transform = 'translateX(0)';
                        });
                    }
                });
                
                // Add pulse animation for important metrics
                const importantMetrics = document.querySelectorAll('.total-count, .success-rate-value');
                importantMetrics.forEach((metric, index) => {
                    setTimeout(() => {
                        metric.style.animation = 'pulse 3s infinite';
                    }, 2000 + (index * 500));
                });
                
                // Add loading effect simulation
                const dashboard = document.querySelector('.stats-dashboard');
                if (dashboard) {
                    dashboard.style.opacity = '0';
                    dashboard.style.transform = 'translateY(20px)';
                    setTimeout(() => {
                        dashboard.style.transition = 'all 0.8s cubic-bezier(0.4, 0, 0.2, 1)';
                        dashboard.style.opacity = '1';
                        dashboard.style.transform = 'translateY(0)';
                    }, 200);
                }
                
                // Enhanced CSS animations
                const style = document.createElement('style');
                style.textContent = `
                    .progress-fill {
                        position: relative;
                        overflow: hidden;
                    }
                    
                    .thread-progress-fill, .status-progress-fill {
                        position: relative;
                        overflow: hidden;
                    }
                    
                    .thread-progress-fill::after, .status-progress-fill::after {
                        content: '';
                        position: absolute;
                        top: 0;
                        left: -100%;
                        width: 100%;
                        height: 100%;
                        background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
                        animation: shimmer 2s infinite;
                    }
                    
                    .stat-percentage {
                        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                    }
                    
                    .stat-percentage:hover {
                        transform: scale(1.05);
                        box-shadow: 0 6px 20px rgba(0,0,0,0.25) !important;
                    }
                    
                    .perf-value {
                        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                    }
                    
                    .perf-value:hover {
                        transform: scale(1.05) translateY(-2px);
                    }
                `;
                document.head.appendChild(style);
            });
            
            // Enhanced counter animation function
            function animateCounter(element, start, end, finalText, duration) {
                const startTime = performance.now();
                const isPercentage = finalText.includes('%');
                const suffix = isPercentage ? '%' : '';
                const actualEnd = isPercentage ? parseFloat(finalText) : end;
                
                function updateCounter(currentTime) {
                    const elapsed = currentTime - startTime;
                    const progress = Math.min(elapsed / duration, 1);
                    
                    // Enhanced easing function for smoother animation
                    const easeOutCubic = 1 - Math.pow(1 - progress, 3);
                    const current = start + (actualEnd - start) * easeOutCubic;
                    
                    if (isPercentage) {
                        element.textContent = current.toFixed(1) + suffix;
                    } else {
                        element.textContent = Math.floor(current) + suffix;
                    }
                    
                    if (progress < 1) {
                        requestAnimationFrame(updateCounter);
                    } else {
                        element.textContent = finalText;
                        // Add a subtle scale effect when animation completes
                        element.style.transform = 'scale(1.1)';
                        setTimeout(() => {
                            element.style.transition = 'transform 0.3s ease';
                            element.style.transform = 'scale(1)';
                        }, 200);
                    }
                }
                
                requestAnimationFrame(updateCounter);
            }
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

            // Add parallel execution info with enhanced details
            String parallelMode = System.getProperty("parallel", "none");
            String threadCount = System.getProperty("threadcount", "1");
            String parallelCount = System.getProperty("parallelcount", "1");

            extent.setSystemInfo("🚀 Parallel Mode", parallelMode);
            extent.setSystemInfo("🧵 Thread Count", threadCount);
            extent.setSystemInfo("📊 Parallel Count", parallelCount);
            extent.setSystemInfo("💾 Available Processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
            extent.setSystemInfo("🔧 Max Parallelism", threadCount + " × " + parallelCount + " = " + (Integer.parseInt(threadCount) * Integer.parseInt(parallelCount)));

        } catch (UnknownHostException e) {
            log.error("Error setting system info: {}", e.getMessage());
        }
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

        // Add request and response details if enabled
        if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnFail())) {
            failureLog.append(getEnhancedRequestDetails());
            failureLog.append(getEnhancedResponseDetails());
        }

        logger.fail(failureLog.toString());
    }

    private static void logSuccessDetails(ExtentTest logger, long executionTime, String threadName) {
        StringBuilder successLog = new StringBuilder();

        // Thread information
        successLog.append("<div class='thread-info'>");
        successLog.append("🧵 Thread: ").append(threadName).append(" | ID: ").append(Thread.currentThread().getId());
        successLog.append("</div>");

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

    private static void logSkipDetails(ExtentTest logger, long executionTime, String threadName) {
        StringBuilder skipLog = new StringBuilder();

        // Thread information
        skipLog.append("<div class='thread-info'>");
        skipLog.append("🧵 Thread: ").append(threadName).append(" | ID: ").append(Thread.currentThread().getId());
        skipLog.append("</div>");

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
        stats.append("<div class='execution-timestamp'>").append(CommonUtil.getCurrentDateTime()).append("</div>");
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

        // Enhanced Performance Summary Card with Parallel Count
        stats.append("<div class='stats-card performance-stats'>");
        stats.append("<div class='card-header'>");
        stats.append("<h3 class='card-title'>⚡ Performance Summary</h3>");
        stats.append("</div>");
        stats.append("<div class='card-body'>");

        // Calculate test efficiency
        String parallelMode = System.getProperty("parallel", "none");
        String threadCount = System.getProperty("threadcount", "1");
        String parallelCount = System.getProperty("parallelcount", "1");

        stats.append("<div class='performance-row'>");
        stats.append("<div class='perf-label'>🚀 Execution Mode</div>");
        stats.append("<div class='perf-value execution-mode'>").append(parallelMode.toUpperCase()).append("</div>");
        stats.append("</div>");

        stats.append("<div class='performance-row'>");
        stats.append("<div class='perf-label'>🧵 Thread Count</div>");
        stats.append("<div class='perf-value thread-count'>").append(threadCount).append("</div>");
        stats.append("</div>");

        stats.append("<div class='performance-row'>");
        stats.append("<div class='perf-label'>📊 Parallel Count</div>");
        stats.append("<div class='perf-value parallel-count'>").append(parallelCount).append("</div>");
        stats.append("</div>");

        stats.append("<div class='performance-row'>");
        stats.append("<div class='perf-label'>💻 Available Cores</div>");
        stats.append("<div class='perf-value core-count'>").append(Runtime.getRuntime().availableProcessors()).append("</div>");
        stats.append("</div>");

        // Add max parallelism calculation
        int maxParallelism = Integer.parseInt(threadCount) * Integer.parseInt(parallelCount);
        stats.append("<div class='performance-row'>");
        stats.append("<div class='perf-label'>🔧 Max Parallelism</div>");
        stats.append("<div class='perf-value execution-mode'>").append(threadCount).append(" × ").append(parallelCount).append(" = ").append(maxParallelism).append("</div>");
        stats.append("</div>");

        stats.append("</div>"); // End card-body
        stats.append("</div>"); // End performance-stats card

        stats.append("</div>"); // End stats-dashboard

        statsTest.info(stats.toString());
    }

    private static String getStatusClass(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return "success-status";
        if (statusCode >= 300 && statusCode < 400) return "redirect-status";
        if (statusCode >= 400 && statusCode < 500) return "client-error-status";
        if (statusCode >= 500) return "server-error-status";
        return "unknown-status";
    }

    public static synchronized void flushReports() {
        log.info("📊 Publishing Fixed Professional Extent Reports with Consistent Header Colors and Perfect Text Alignment");
        addTestStatistics();
        extent.flush();
    }

    public static void addRequestDetailsToReport(RequestSpecification reqSpec) {
        if (reqSpec != null) {
            QueryableRequestSpecification queryable = SpecificationQuerier.query(reqSpec);
            qReqSpec.set(queryable);
        }
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