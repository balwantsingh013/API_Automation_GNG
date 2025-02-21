package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.report.ExtentReportManager.logErrorToReport;
import static com.gng.api.report.ExtentReportManager.logInfoToReport;

/**
 * Configure logging of Request/Response Details in Console
 * For FAIL case - Console logs are enabled by-default
 * For PASS case - Console logs are enabled only if enableLogsOnPass = true in config file
 */
@Slf4j
public class LogUtil {

    private LogUtil() {
    }

    public static void configureLogging() {
        log.info("Log Request And Response details in Console if Validation Fails");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        if (Boolean.TRUE.equals(ApplicationContext.get().getEnvConfig().getEnableLogsOnPass())) {
            log.info("Log Request And Response details in Console if Validation Pass");
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
    }

    public static void logInfo(String msg) {
        log.info(msg);
        logInfoToReport(msg);
    }

    public static void logError(String msg) {
        log.error(msg);
        logErrorToReport(msg);
    }

}
