package com.gng.api.constants;

public final class TestConstant {

    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String REPORT_PATH = BASE_PATH + "/target/extent-reports/";
    public static final String PATH_RESOURCES = BASE_PATH + "/src/test/resources/";
    public static final String PATH_CONFIG = PATH_RESOURCES + "config/";
    public static final String SPACE = "&nbsp;";
    public static final String TEST_DATE_FILES_DIRECTORY = PATH_RESOURCES + "testDataFiles/";
    public static final String EXPERIAN_DATA =TEST_DATE_FILES_DIRECTORY +"Experian.xlsx";
    public static final String CUSTOMER_DATA =TEST_DATE_FILES_DIRECTORY +"Customer.xlsx";
    public static final String EXPERIAN_SHEET_NAME = "Experian Business Credit new Bi";
    public static final String CUSTOMER_SHEET_NAME = "SPLIT1K.GENSDEMO.D220920";

    public static final String JSON = "json";
    public static final String PATH_PAYLOAD = PATH_RESOURCES + "payload/";

    private TestConstant() {
    }
}
