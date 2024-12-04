package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;
import lombok.Getter;


@Getter
public enum SaveEnrollmentLabels {
    HAPPY_FLOW_("SaveEnrollment_HappyFlow"),
    MISSING_REQUEST_ID("SaveEnrollment_MissingRequestId"),
    INVALID_CUSTOMER_CODE_LENGTH("SaveEnrollment_InvalidCustomerCodeLength"),
    INVALID_PROMO_CODE_LENGTH("SaveEnrollment_InvalidPromoCodeLength");


    private final String label;

    SaveEnrollmentLabels(String label) {
        this.label = label;
    }


}
