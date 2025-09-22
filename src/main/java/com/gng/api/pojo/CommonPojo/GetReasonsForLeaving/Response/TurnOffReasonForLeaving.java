package com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnOffReasonForLeaving {
    private String reasonForTurnOff;
    private String subReasonForTurnOff;
    private String reasonForTurnOffAlert;
}
