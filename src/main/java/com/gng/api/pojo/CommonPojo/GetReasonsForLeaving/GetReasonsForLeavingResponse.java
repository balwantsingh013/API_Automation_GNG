package com.gng.api.pojo.CommonPojo.GetReasonsForLeaving;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReasonsForLeavingResponse {
    private List<TurnOffReason> turnOffReasons;
    private boolean success;
    private int errorCode;
    private String errorMessage;
    private String requestID;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TurnOffReason {
        private String reasonForTurnOff;
        private String subReasonForTurnOff;
        private String reasonForTurnOffAlert;
        }
}


