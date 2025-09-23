package com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor

public class DataResult {
    private List<TransferTurnOffReason> turnOffReasons;
}
