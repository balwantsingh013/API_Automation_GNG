package com.gng.api.pojo.shared;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlansAndOffers {
    public DefaultPlanData defaultPlanDat;
    public EligiblePlanData eligiblePlanData;

    @Setter
    @Getter
    public static class DefaultPlanData {
        private int numberOfMatches;
        private List<GetDefaultPlansAndOffersResponse.Plan> plans;
    }

    @Setter
    @Getter
    public static class EligiblePlanData {
        private int numberOfMatches;
        private List<GetEligiblePlansAndOffersResponse.Plan> plans;
    }
}