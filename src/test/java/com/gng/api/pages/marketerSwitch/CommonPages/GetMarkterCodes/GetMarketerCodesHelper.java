package com.gng.api.pages.marketerSwitch.CommonPages.GetMarkterCodes;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMarketerCodes.request.GetMarketerCodesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.Common.GetMarketerCodes.GetMarketerCodesApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;


@Slf4j
public class GetMarketerCodesHelper {

    private final TestContext testContext;

    public GetMarketerCodesHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetMarketerCodesRequest preparePayload(GetMarketerCodesApiLabel apiLabel) {
        log.info("Preparing Market Switch payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetMarketerCodesApiLabel.get_marketer_codes)
                ? GetMarketerCodesApiLabel.get_marketer_codes.toString()
                : GetMarketerCodesApiLabel.get_marketer_codes_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetMarketerCodesRequest.class);
    }

    public void setSupportingDefaultParameters(GetMarketerCodesRequest payload, GetMarketerCodesApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));

    }

    public void setParametersBasedOnType(GetMarketerCodesRequest payload, GetMarketerCodesApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case GM_MRK_SWT_DUPLICATE_REQUEST_ID_TC_59 ->
                    payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
            case GM_MRK_SWT_MISSING_REQUEST_ID_TC_60 -> payload.setRequestID("");
            case GM_MRK_SWT_MAX_LENGTH_REQUEST_ID_TC_61 -> payload.setRequestID(FakerDataGenerator.getRandomNumericString(39));
            case GM_MRK_SWT_MISSING_LOGIN_ID_TC_62 -> payload.setLoginID("");
            case GM_MRK_SWT_MAX_LENGTH_LOGIN_ID_TC_63 -> payload.setLoginID(FakerDataGenerator.getRandomString(39));
            case GM_MRK_SWT_NOT_ALPHA_NUM_LOGIN_ID_TC_64 -> payload.setLoginID(FakerDataGenerator.getRandomNumericString(4));
            case GM_MRK_SWT_INVALID_LOGIN_ID_TC_65 -> payload.setLoginID(GlobalEnums.InvalidValues.INVALID_LOGIN_ID.getValue());
        }
    }


    private static String get(Map<String, String> data, String key) {
        return Optional.ofNullable(data.get(key)).orElse("");
    }


    public static <E extends Enum<E>> Map<String, String> loadRowFromExcelToCustomerData(String excelPath, String sheetName, E testLabel) {
        try {
            ExcelReader reader = new ExcelReader(excelPath);
            List<Map<String, String>> sheetData = reader.getSheetData(sheetName);

            return sheetData.stream()
                    .filter(row -> {
                        String condition = row.get("testCondition");
                        return condition != null && condition.contains(testLabel.name());
                    }).findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No matching testConditions found containing: " + testLabel.name()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from Excel", e);
        }
    }


}
