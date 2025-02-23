package com.gng.api.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.gng.api.context.ApplicationContext;
import com.gng.api.util.CommonUtil;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.gng.api.constants.TestConstant.REPORT_PATH;

@Slf4j
public class ExtentReportManager {

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> extentLogger = new ThreadLocal<>();
    private static final ThreadLocal<QueryableRequestSpecification> qReqSpec = new ThreadLocal<>();
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> expectedStatusCode = new ThreadLocal<>();
    private static final String REQUESTDETAILS = "<b>--- Request Details ---</b>";
    private static final String RESPONSEDETAILS = "<b>--- Response Details ---</b>";
    private static final String STATUS = "<br/><b>Actual Status:</b> ";
    private static final String STATUSCODEEXP = "<br/><b>Expected Status Code:</b> ";
    private static final String BASEURI = "<br/><b>BaseUri:</b> ";
    private static final String HEADERS = "<br/><b>Headers:</b> ";
    private static final String PARAMS = "<br/><b>Params:</b> ";
    private static final String BODY = "<br/><b>Body:</b> ";
    private static final String ERRORRETURNED = "<b>Error Returned:</b></br> ";
    private static final String BR2 = "<br><br>";
    private static ExtentReports extent;
    private static ExtentSparkReporter spark;

    private ExtentReportManager() {
    }

    public static void initialiseExtentReport() {
        extent = new ExtentReports();
        spark = new ExtentSparkReporter(REPORT_PATH + "apireport-" + CommonUtil.getCurrentDateTime() + ".html");
        setConfig();
    }

    private static void setConfig() {
        spark.config().setDocumentTitle("Extent Report");
        spark.config().setReportName("VxRetail Api Automation Report");
        spark.config().setOfflineMode(true);
        extent.attachReporter(spark);
        setSystemInfo();
    }

    private static void setSystemInfo() {
        try {
            extent.setSystemInfo("System Name", InetAddress.getLocalHost().getHostName());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Environment", ApplicationContext.get().getEnvironment());
            extent.setSystemInfo("Config File", ApplicationContext.get().getEnvConfigFile());
            extent.setSystemInfo("BaseUri", ApplicationContext.get().getEnvConfig().getBaseUri());
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
    }

    public static void generateReport(ITestResult result) {
        ExtentTest logger = extentLogger.get();

        if (result.getStatus() == ITestResult.FAILURE) {
            if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnFail())) {
                logger.log(Status.FAIL, ERRORRETURNED + result.getThrowable() + getResponseBodyIfExists()
                        + BR2 + getRequestDetailsIfExists());
            } else {
                logger.log(Status.FAIL, ERRORRETURNED + result.getThrowable());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnPass())) {
                logger.log(Status.PASS, getRequestDetailsIfExists() + getExpectedStatusCodeIfExists()
                        + BR2 + RESPONSEDETAILS + getSafeResponseStatusLine() + getResponseBodyIfExists());
            } else {
                logger.log(Status.PASS, getRequestDetailsIfExists() + getExpectedStatusCodeIfExists());
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.log(Status.SKIP, getRequestDetailsIfExists());
        }

        // Clean up after the test
        qReqSpec.remove();
        response.remove();
        expectedStatusCode.remove();
        extent.flush();
    }


    public static void clearThreadLocals() {
        test.remove();
        extentLogger.remove();
    }

    public static void createTest(String scenarioName) {
        String testName = scenarioName.isEmpty() ? "Test Scenario" : scenarioName;
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        extentLogger.set(extentTest.createNode("Test Steps"));
    }

    public static void flushReports() {
        log.info("Publish Extent Reports");
        extent.flush();
    }

    public static void addRequestDetailsToReport(RequestSpecification reqSpec) {
        qReqSpec.set(SpecificationQuerier.query(reqSpec));
    }

    public static void addResponseDetailsToReport(Response resp, int statusCode) {
        response.set(resp);
        expectedStatusCode.set(String.valueOf(statusCode));
    }

    public static void logInfoToReport(String msg) {
        ExtentTest logger = extentLogger.get();
        logger.log(Status.INFO, msg);
    }

    public static void logErrorToReport(String msg) {
        ExtentTest logger = extentLogger.get();
        logger.log(Status.FAIL, msg);
    }

    public static String getRequestDetailsIfExists() {
        return (qReqSpec.get() != null)
                ? REQUESTDETAILS + BASEURI + qReqSpec.get().getBaseUri()
                + HEADERS + qReqSpec.get().getHeaders()
                + PARAMS + qReqSpec.get().getRequestParams()
                : "No API Request Details (DB Validation Only)";
    }


    public static String getSafeResponseStatusLine() {
        Response resp = response.get();
        return (resp != null) ? STATUS + resp.getStatusLine() : STATUS + "No API Response (DB Validation Only)";
    }


    public static String getExpectedStatusCodeIfExists() {
        return (expectedStatusCode.get() != null)
                ? STATUSCODEEXP + expectedStatusCode.get()
                : STATUSCODEEXP + "No Expected Status Code (DB Validation Only)";
    }


    public static String getResponseBodyIfExists() {
        Response resp = response.get();
        return (resp != null) ? BODY + resp.getBody().asPrettyString() : "No API Response Body (DB Validation Only)";
    }



    public static ExtentTest getExtentLogger() {
        return extentLogger.get();
    }

}
