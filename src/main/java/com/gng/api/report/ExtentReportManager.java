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
import static net.masterthought.cucumber.util.Util.formatDuration;

@Slf4j
public class ExtentReportManager {

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

        // Enhanced CSS for professional appearance with timeline fixes - ONLY TIMELINE FIXES ADDED
        spark.config().setCss(getProfessionalCSSWithTimelineFix());
        spark.config().setJs(getTimelineFixJavaScript());

        extent.attachReporter(spark);
        setSystemInfo();
    }

    private static String getProfessionalCSSWithTimelineFix() {
        return """
        /* === CORE STYLES === */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important; 
            background: #f8fafc !important;
        }
        
        /* === TIMELINE FIXES ONLY - ADDED TO EXISTING STYLES === */
        /* Fix timeline display issues without affecting other functionality */
        .timeline-view .card-panel,
        .timeline-container .card-panel,
        .timeline .card-panel {
            background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%) !important;
            color: white !important;
            box-shadow: 0 4px 20px rgba(45, 55, 72, 0.3) !important;
            min-height: 60px !important;
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
            font-size: 14px !important;
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
        
        /* Prevent timeline layout issues */
        .timeline-container,
        .timeline-view,
        .timeline {
            overflow-x: auto !important;
            overflow-y: visible !important;
            min-height: 150px !important;
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
            font-size: 18px !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
        }
        
        /* Navbar brand specific styling */
        .navbar-brand { 
            font-size: 24px !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
        }
        
        /* Header text sizing hierarchy */
        :is(.container .card-panel:not(.stats-card), .row .col .card-panel:not(.stats-card)) :is(h1, h2, h3) {
            font-size: 22px !important;
            margin: 10px 0 !important;
            padding: 10px !important;
            text-align: center !important;
        }
        
        /* === COMPONENT STYLES === */
        /* Thread Information */
        .thread-info {
            background: linear-gradient(135deg, #7c3aed, #5b21b6) !important;
            color: white !important;
            padding: 10px 18px !important;
            border-radius: 25px !important;
            font: 600 13px 'Segoe UI', sans-serif !important;
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
            font: 700 32px 'Segoe UI', sans-serif !important;
            margin: 0 !important;
            color: white !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
            letter-spacing: 0.5px !important;
        }
        
        .execution-timestamp {
            font: 500 18px 'Segoe UI', sans-serif !important;
            margin-top: 12px !important;
            opacity: 0.95 !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
        }
        
        /* Dashboard Grid */
        .stats-dashboard {
            display: grid !important;
            grid-template-columns: repeat(auto-fit, minmax(450px, 1fr)) !important;
            gap: 30px !important;
            margin: 25px 0 !important;
        }
        
        /* Enhanced Card System */
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
        
        /* Card Headers */
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
            font: 700 22px 'Segoe UI', sans-serif !important;
            margin: 0 !important;
            color: white !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
            letter-spacing: 0.3px !important;
            text-decoration: none !important;
            line-height: 1.2 !important;
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
        
        .card-body { padding: 30px !important; background: white !important; }
        
        /* === STATISTICS LAYOUT SYSTEM === */
        /* Flex row system for all stat types */
        :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row) {
            display: flex !important;
            justify-content: space-between !important;
            align-items: center !important;
            padding: 20px 0 !important;
            border-bottom: 2px solid #f1f5f9 !important;
            margin-bottom: 18px !important;
            position: relative !important;
            min-height: 60px !important;
            background: linear-gradient(90deg, transparent, #f8fafc, transparent) !important;
            border-radius: 10px !important;
        }
        
        :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row):last-child {
            border-bottom: none !important;
            margin-bottom: 0 !important;
        }
        
        /* Label system */
        :is(.stat-label, .thread-label, .status-code-label, .perf-label) {
            font: 700 18px 'Segoe UI', sans-serif !important;
            color: #1f2937 !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.05) !important;
            display: flex !important;
            align-items: center !important;
        }
        
        .stat-label { flex: 0 0 40% !important; white-space: nowrap !important; }
        .thread-label { flex: 2 !important; font-size: 16px !important; }
        .status-code-label { flex: 2 !important; font-size: 16px !important; }
        
        /* Value containers and alignment */
        .stat-value-container {
            display: flex !important;
            align-items: center !important;
            gap: 20px !important;
            flex: 1 !important;
            justify-content: flex-end !important;
            min-height: 40px !important;
        }
        
        :is(.thread-value, .status-code-value) {
            flex: 1 !important;
            text-align: center !important;
            font: 800 18px 'Segoe UI', sans-serif !important;
            color: #1e40af !important;
            text-shadow: 0 1px 3px rgba(30, 64, 175, 0.3) !important;
        }
        
        .thread-value { color: #1e40af !important; }
        .status-code-value { color: #1f2937 !important; }
        
        /* === VALUE STYLING SYSTEM === */
        /* Base stat values */
        .stat-value {
            font: 800 28px 'Segoe UI', sans-serif !important;
            min-width: 60px !important;
            text-align: center !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
            display: flex !important;
            align-items: center !important;
            justify-content: center !important;
        }
        
        /* Stat value color variants */
        .total-count { color: #1e40af !important; font-size: 36px !important; text-shadow: 0 2px 6px rgba(30, 64, 175, 0.3) !important; }
        .passed-count { color: #059669 !important; text-shadow: 0 2px 4px rgba(5, 150, 105, 0.3) !important; }
        .failed-count { color: #dc2626 !important; text-shadow: 0 2px 4px rgba(220, 38, 38, 0.3) !important; }
        .skipped-count { color: #d97706 !important; text-shadow: 0 2px 4px rgba(217, 119, 6, 0.3) !important; }
        
        /* === PERCENTAGE BADGE SYSTEM === */
        /* Base percentage styling */
        :is(.stat-percentage, .thread-percentage, .status-code-percentage) {
            font: 700 16px 'Segoe UI', sans-serif !important;
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
        
        :is(.thread-percentage, .status-code-percentage) {
            flex: 1 !important;
            font-size: 15px !important;
            color: #6b7280 !important;
            background: none !important;
            box-shadow: none !important;
            border: none !important;
            text-shadow: none !important;
            font-weight: 600 !important;
        }
        
        /* Percentage color variants */
        .passed-percentage { background: linear-gradient(135deg, #059669, #10b981) !important; box-shadow: 0 4px 15px rgba(5, 150, 105, 0.4) !important; }
        .failed-percentage { background: linear-gradient(135deg, #dc2626, #ef4444) !important; box-shadow: 0 4px 15px rgba(220, 38, 38, 0.4) !important; }
        .skipped-percentage { background: linear-gradient(135deg, #d97706, #f59e0b) !important; box-shadow: 0 4px 15px rgba(217, 119, 6, 0.4) !important; }
        
        /* === PROGRESS BAR SYSTEM === */
        /* Base progress bar */
        :is(.progress-bar, .thread-progress-bar, .status-progress-bar) {
            height: 12px !important;
            background: #f1f5f9 !important;
            border-radius: 15px !important;
            overflow: hidden !important;
            border: 1px solid #e2e8f0 !important;
            box-shadow: inset 0 2px 4px rgba(0,0,0,0.05) !important;
        }
        
        .progress-bar { width: 100% !important; margin-top: 15px !important; }
        :is(.thread-progress-bar, .status-progress-bar) { 
            flex: 2 !important; 
            height: 10px !important; 
            margin-left: 20px !important; 
        }
        
        /* Progress fill variants */
        :is(.progress-fill, .thread-progress-fill, .status-progress-fill) {
            height: 100% !important;
            border-radius: 15px !important;
            transition: width 1s cubic-bezier(0.4, 0, 0.2, 1) !important;
            position: relative !important;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2) !important;
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
            padding: 25px !important;
            border-radius: 18px !important;
            text-align: center !important;
            margin-top: 30px !important;
            box-shadow: 0 10px 30px rgba(59, 130, 246, 0.4) !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        .success-rate-label {
            font: 700 20px 'Segoe UI', sans-serif !important;
            margin-bottom: 15px !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
        }
        
        .success-rate-value {
            font: 900 42px 'Segoe UI', sans-serif !important;
            padding: 15px 25px !important;
            border-radius: 30px !important;
            display: inline-block !important;
            margin-top: 12px !important;
            text-shadow: 0 3px 6px rgba(0,0,0,0.4) !important;
            border: 3px solid rgba(255,255,255,0.3) !important;
        }
        
        .success-rate-value.excellent { background: linear-gradient(135deg, #059669, #10b981) !important; box-shadow: 0 8px 20px rgba(5, 150, 105, 0.5) !important; }
        .success-rate-value.good { background: linear-gradient(135deg, #d97706, #f59e0b) !important; box-shadow: 0 8px 20px rgba(217, 119, 6, 0.5) !important; }
        .success-rate-value.needs-improvement { background: linear-gradient(135deg, #dc2626, #ef4444) !important; box-shadow: 0 8px 20px rgba(220, 38, 38, 0.5) !important; }
        
        /* === PERFORMANCE METRICS === */
        .perf-value {
            font: 800 16px 'Segoe UI', sans-serif !important;
            padding: 10px 20px !important;
            border-radius: 20px !important;
            color: white !important;
            text-shadow: 0 1px 3px rgba(0,0,0,0.3) !important;
            box-shadow: 0 4px 12px rgba(0,0,0,0.2) !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        .execution-mode { background: linear-gradient(135deg, #1e40af, #3b82f6) !important; }
        .thread-count { background: linear-gradient(135deg, #7c3aed, #8b5cf6) !important; }
        .parallel-count { background: linear-gradient(135deg, #059669, #10b981) !important; }
        .core-count { background: linear-gradient(135deg, #0891b2, #06b6d4) !important; }
        
        /* === STATUS CODE STYLING === */
        :is(.status-200, .status-201, .status-202, .status-204) { 
            background: linear-gradient(135deg, #059669, #10b981) !important; 
            color: white !important; 
            padding: 8px 16px !important; 
            border-radius: 20px !important; 
            font-weight: 700 !important;
            box-shadow: 0 4px 12px rgba(5, 150, 105, 0.4) !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        :is(.status-400, .status-401, .status-403, .status-404, .status-422) { 
            background: linear-gradient(135deg, #dc2626, #ef4444) !important; 
            color: white !important; 
            padding: 8px 16px !important; 
            border-radius: 20px !important; 
            font-weight: 700 !important;
            box-shadow: 0 4px 12px rgba(220, 38, 38, 0.4) !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        :is(.status-500, .status-502, .status-503) { 
            background: linear-gradient(135deg, #374151, #4b5563) !important; 
            color: white !important; 
            padding: 8px 16px !important; 
            border-radius: 20px !important; 
            font-weight: 700 !important;
            box-shadow: 0 4px 12px rgba(55, 65, 81, 0.4) !important;
            text-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        /* === TEST STATUS STYLING === */
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
        
        /* === SPECIALIZED SECTIONS === */
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
        
        :is(.api-request-section, .api-response-section) {
            color: white !important;
            padding: 25px !important;
            border-radius: 18px !important;
            margin: 20px 0 !important;
            border: 2px solid rgba(255,255,255,0.2) !important;
        }
        
        .api-request-section { 
            background: linear-gradient(135deg, #1e40af, #3b82f6) !important;
            box-shadow: 0 8px 25px rgba(59, 130, 246, 0.4) !important;
        }
        
        .api-response-section { 
            background: linear-gradient(135deg, #059669, #10b981) !important;
            box-shadow: 0 8px 25px rgba(5, 150, 105, 0.4) !important;
        }
        
        :is(.api-request-section, .api-response-section) h4 {
            color: white !important;
            margin-bottom: 20px !important;
            font: 700 20px 'Segoe UI', sans-serif !important;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3) !important;
        }
        
        :is(.request-detail-item, .response-detail-item) {
            background: rgba(255, 255, 255, 0.15) !important;
            padding: 18px !important;
            border-radius: 12px !important;
            margin: 12px 0 !important;
            border-left: 4px solid rgba(255,255,255,0.4) !important;
            backdrop-filter: blur(10px) !important;
        }
        
        /* === LOG STYLING === */
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
        
        /* === UTILITY CLASSES === */
        .code-block {
            background: #1f2937 !important;
            color: #f9fafb !important;
            padding: 25px !important;
            border-radius: 12px !important;
            margin: 15px 0 !important;
            overflow-x: auto !important;
            box-shadow: inset 0 2px 8px rgba(0,0,0,0.3) !important;
            border: 1px solid #374151 !important;
            font: 14px/1.6 'Fira Code', 'Monaco', 'Consolas', monospace !important;
        }
        
        .card-panel { 
            box-shadow: 0 10px 35px rgba(0,0,0,0.08) !important;
            border-radius: 18px !important;
            border: 1px solid #e5e7eb !important;
            margin-bottom: 25px !important;
            background: white !important;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
        }
        
        .card-panel:hover {
            transform: translateY(-4px) !important;
            box-shadow: 0 15px 45px rgba(0,0,0,0.12) !important;
            border-color: #3b82f6 !important;
        }
        
        .collapsible-content {
            transition: all 0.3s ease !important;
            border-radius: 8px !important;
            padding: 15px !important;
            background: rgba(255, 255, 255, 0.05) !important;
            margin-top: 10px !important;
        }
        
        /* === RESPONSIVE DESIGN === */
        @media (max-width: 768px) {
            .stats-dashboard {
                grid-template-columns: 1fr !important;
                gap: 20px !important;
            }
            
            :is(.stat-row, .thread-stat-row, .status-code-row, .performance-row) {
                flex-direction: column !important;
                text-align: center !important;
                gap: 12px !important;
            }
            
            .stat-value-container {
                justify-content: center !important;
                margin-top: 12px !important;
            }
            
            :is(.thread-progress-bar, .status-progress-bar) {
                margin: 12px 0 !important;
                width: 100% !important;
            }
            
            :is(.api-request-section, .api-response-section) {
                margin: 15px 5px !important;
                padding: 20px !important;
            }
            
            .stats-title { font-size: 26px !important; }
            .card-title { font-size: 18px !important; }
        }
        
        /* === ANIMATIONS === */
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
        """;
    }

    // TIMELINE FIX JAVASCRIPT - ONLY FIXES TIMELINE ISSUES, DOESN'T CHANGE EXISTING FUNCTIONALITY
    private static String getTimelineFixJavaScript() {
        return """
        // === TIMELINE FIX ONLY - PRESERVES ALL EXISTING FUNCTIONALITY ===
        class TimelineFixOnly {
            constructor() {
                this.init();
            }
            
            init() {
                console.log('🔧 Timeline Fix: Initializing timeline fix only...');
                
                if (document.readyState === 'loading') {
                    document.addEventListener('DOMContentLoaded', () => this.applyTimelineFixes());
                } else {
                    this.applyTimelineFixes();
                }
            }
            
            applyTimelineFixes() {
                console.log('🚀 Timeline Fix: Applying fixes...');
                
                // Fix 1: Remove offsetWidth styling issues
                this.fixOffsetWidthIssues();
                
                // Fix 2: Monitor for dynamic timeline changes
                this.setupTimelineMonitoring();
                
                // Fix 3: Apply proper timeline styling
                this.applyTimelineStyling();
                
                console.log('✅ Timeline Fix: All fixes applied successfully');
            }
            
            fixOffsetWidthIssues() {
                // Find all elements with offsetWidth in their styling or content
                const problematicElements = document.querySelectorAll('*');
                
                problematicElements.forEach(element => {
                    // Fix inline offsetWidth styling
                    if (element.style && element.style.cssText.includes('offsetWidth')) {
                        element.style.cssText = element.style.cssText.replace(/[^;]*offsetWidth[^;]*;?/g, '');
                        element.style.maxWidth = '350px';
                        element.style.overflow = 'hidden';
                        element.style.textOverflow = 'ellipsis';
                        element.style.whiteSpace = 'nowrap';
                    }
                    
                    // Fix text content containing offsetWidth
                    if (element.textContent && element.textContent.includes('offsetWidth')) {
                        // Only fix if this is clearly a corrupted test name, not legitimate content
                        if (element.classList.contains('card-title') || 
                            element.classList.contains('node-name') || 
                            element.classList.contains('test-title') ||
                            element.closest('.timeline-view')) {
                            
                            // Try to restore meaningful name or use fallback
                            const meaningfulName = this.getMeaningfulTestName(element);
                            element.textContent = meaningfulName;
                            console.log('🔧 Fixed offsetWidth issue: ' + meaningfulName);
                        }
                    }
                });
            }
            
            getMeaningfulTestName(element) {
                // Try to find a meaningful test name from context
                
                // Check if there's a parent with meaningful information
                const parentCard = element.closest('.card-panel');
                if (parentCard) {
                    const allTextElements = parentCard.querySelectorAll('*');
                    for (const textEl of allTextElements) {
                        if (textEl !== element && textEl.textContent && 
                            !textEl.textContent.includes('offsetWidth') &&
                            textEl.textContent.trim().length > 3 &&
                            !textEl.textContent.includes('Thread') &&
                            !textEl.textContent.includes('ms')) {
                            return textEl.textContent.trim();
                        }
                    }
                }
                
                // Fallback to a timestamped name
                const timestamp = new Date().toLocaleTimeString();
                return `API Test - ${timestamp}`;
            }
            
            applyTimelineStyling() {
                // Apply proper styling to timeline elements
                const timelineElements = document.querySelectorAll(
                    '.timeline-view, .timeline-container, .timeline, ' +
                    '.timeline-view .card-panel, .timeline-container .card-panel'
                );
                
                timelineElements.forEach(element => {
                    element.style.overflow = 'visible';
                    element.style.minHeight = '60px';
                    element.style.position = 'relative';
                });
                
                // Style timeline text elements
                const timelineTextElements = document.querySelectorAll(
                    '.timeline-view .card-title, .timeline-view .card-title *, ' +
                    '.timeline-view .node-name, .timeline-item .test-title'
                );
                
                timelineTextElements.forEach(element => {
                    element.style.maxWidth = '350px';
                    element.style.overflow = 'hidden';
                    element.style.textOverflow = 'ellipsis';
                    element.style.whiteSpace = 'nowrap';
                    element.style.color = 'white';
                    element.style.fontWeight = '600';
                });
            }
            
            setupTimelineMonitoring() {
                // Monitor for new timeline elements being added
                const observer = new MutationObserver((mutations) => {
                    mutations.forEach((mutation) => {
                        if (mutation.type === 'childList') {
                            mutation.addedNodes.forEach((node) => {
                                if (node.nodeType === Node.ELEMENT_NODE) {
                                    // Check if this is a timeline-related element
                                    if (node.classList && (
                                        node.classList.contains('timeline-view') ||
                                        node.classList.contains('timeline-container') ||
                                        node.classList.contains('card-panel') ||
                                        node.querySelector && node.querySelector('.timeline-view, .timeline-container')
                                    )) {
                                        // Apply fixes to new elements
                                        setTimeout(() => {
                                            this.fixOffsetWidthIssues();
                                            this.applyTimelineStyling();
                                        }, 100);
                                    }
                                }
                            });
                        }
                        
                        // Handle text content changes
                        if (mutation.type === 'characterData' && 
                            mutation.target.textContent && 
                            mutation.target.textContent.includes('offsetWidth')) {
                            setTimeout(() => {
                                if (mutation.target.parentElement) {
                                    const meaningfulName = this.getMeaningfulTestName(mutation.target.parentElement);
                                    mutation.target.textContent = meaningfulName;
                                }
                            }, 50);
                        }
                    });
                });
                
                observer.observe(document.body, {
                    childList: true,
                    subtree: true,
                    characterData: true
                });
                
                console.log('👁️ Timeline Monitor: Monitoring activated');
            }
            
            // Periodic check for timeline issues (runs for first 2 minutes)
            setupPeriodicCheck() {
                let checkCount = 0;
                const maxChecks = 24; // 2 minutes with 5-second intervals
                
                const periodicCheck = setInterval(() => {
                    checkCount++;
                    
                    // Check for offsetWidth issues
                    const hasOffsetWidthIssues = document.querySelector('*[style*="offsetWidth"]') || 
                                               document.evaluate('//*[contains(text(), "offsetWidth")]', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
                    
                    if (hasOffsetWidthIssues) {
                        console.log('🔍 Periodic Check: Found offsetWidth issues, fixing...');
                        this.fixOffsetWidthIssues();
                    }
                    
                    if (checkCount >= maxChecks) {
                        clearInterval(periodicCheck);
                        console.log('⏰ Periodic Check: Monitoring period completed');
                    }
                }, 5000);
            }
        }
        
        // Initialize the timeline fix when page loads
        window.timelineFixOnly = new TimelineFixOnly();
        
        // Expose fix function for manual use
        window.fixTimelineOffsetWidth = function() {
            window.timelineFixOnly.fixOffsetWidthIssues();
            window.timelineFixOnly.applyTimelineStyling();
        };
        
        // Auto-fix on page load complete
        window.addEventListener('load', () => {
            setTimeout(() => {
                console.log('🔄 Page Load Complete: Running timeline fix...');
                window.timelineFixOnly.fixOffsetWidthIssues();
                window.timelineFixOnly.applyTimelineStyling();
                window.timelineFixOnly.setupPeriodicCheck();
            }, 1000);
        });
        """;
    }

    // ALL EXISTING METHODS REMAIN UNCHANGED - PRESERVING ORIGINAL LOGIC
    private static void setSystemInfo() {
        try {
            extent.setSystemInfo("🖥️ System", InetAddress.getLocalHost().getHostName());
            extent.setSystemInfo("💻 OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            extent.setSystemInfo("☕ Java", System.getProperty("java.version"));
            extent.setSystemInfo("🌍 Environment", ApplicationContext.get().getEnvironment());
            extent.setSystemInfo("⚙️ Config", ApplicationContext.get().getEnvConfigFile());
            extent.setSystemInfo("🌐 Base URI", ApplicationContext.get().getEnvConfig().getBaseUri());
            extent.setSystemInfo("👤 Tester", System.getProperty("user.name"));
            extent.setSystemInfo("🕒 Execution Start", CommonUtil.getCurrentDateTimeFormatted());

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
        log.info("📊 Publishing Professional Extent Reports with Timeline Fix");

        addTestStatistics();

        // ✅ Add Total Execution Time
        long totalExecutionMillis = System.currentTimeMillis() - suiteStartTime;
        String duration = formatDuration(totalExecutionMillis);
        extent.setSystemInfo("⏱️ Total Execution Time", duration);

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