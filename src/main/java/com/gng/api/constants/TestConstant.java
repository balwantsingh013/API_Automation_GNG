package com.gng.api.constants;

public final class TestConstant {
    public static final String JSON = "json";
    public static final String ACCEPT_HALJSON = "application/hal+json";
    public static final String BASEPATH = System.getProperty("user.dir");
    public static final String REPORTPATH = BASEPATH + "/target/extent-reports/";
    public static final String PATH_RESOURCES = BASEPATH + "/src/test/resources/";
    public static final String PATH_PAYLOAD = PATH_RESOURCES + "payload/";
    public static final String PATH_CONFIG = PATH_RESOURCES + "config/";
    public static final String PATH_SCHEMA = PATH_RESOURCES + "schema/";
    public static final String PATH_TESTDATA = PATH_RESOURCES + "testdata/";
    public static final String PATH_POJO_REQ = BASEPATH + "/src/main/con/retail/pojo/request/";
    public static final String PATH_POJO_RES = BASEPATH + "/src/main/con/retail/pojo/response/";
    public static final String APIPHASE1 = "ApiPhase1";
    public static final String SPACE = "&nbsp;";

    private TestConstant() {
    }
}
