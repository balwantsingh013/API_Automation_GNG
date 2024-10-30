package com.gng.api.steps.base;


import com.gng.api.context.RunContext;
import com.gng.api.db.DBAction;

public class BaseStep {
    protected DBAction dbAction;

    public BaseStep() {
        dbAction = RunContext.get().getDbAction();
    }

}
