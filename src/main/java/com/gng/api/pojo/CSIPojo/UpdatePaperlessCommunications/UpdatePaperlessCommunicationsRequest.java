package com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gng.api.util.PaperlessEnrollmentUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UpdatePaperlessCommunicationsRequest {

    private String requestID;
    private String customerCode;
    private String premisesCode;
    private String updateBillDeliveryOption;
    private String updateCorrDeliveryOption;
    private String emailAddress;

    /**
     * Custadv stores email lowercase; Banner may return mixed case until platform bug is fixed.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = PaperlessEnrollmentUtil.normalizeBannerEmail(emailAddress);
    }
}
