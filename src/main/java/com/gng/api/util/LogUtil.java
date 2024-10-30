package com.gng.api.util;

import lombok.extern.slf4j.Slf4j;

import static com.gng.api.report.ExtentReportManager.logErrorToReport;
import static com.gng.api.report.ExtentReportManager.logInfoToReport;

@Slf4j
public class LogUtil {

    private LogUtil() {
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
