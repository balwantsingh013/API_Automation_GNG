package com.gng.api.util;

import com.gng.api.pojo.TestContext.TestContext;

public class TestContextHolder {
    private static final ThreadLocal<TestContext> context = new ThreadLocal<>();

    public static void set(TestContext testContext) {
        context.set(testContext);
    }

    public static TestContext get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
