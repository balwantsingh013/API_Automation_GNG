package com.gng.api.constants;

public final class TestConstant {

    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String REPORT_PATH = BASE_PATH + "/target/extent-reports/";
    public static final String PATH_RESOURCES = BASE_PATH + "/src/test/resources/";
    public static final String PATH_CONFIG = PATH_RESOURCES + "config/";
    public static final String SPACE = "&nbsp;";


    public static final String JSON = "json";
    public static final String PATH_PAYLOAD = PATH_RESOURCES + "/payload/";

    private TestConstant() {
    }
}
