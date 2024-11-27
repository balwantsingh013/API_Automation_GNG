package com.gng.api.guice;

import com.gng.api.pages.CreateAccountNoteApiPage;
import com.gng.api.pages.GetAccountInfoApiPage;
import com.gng.api.steps.CreateAccountNoteApiSteps;
import com.gng.api.steps.GetAccountInfoApiSteps;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public final class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetAccountInfoApiSteps.class).in(Scopes.SINGLETON);
        bind(GetAccountInfoApiPage.class).in(Scopes.SINGLETON);
        bind(CreateAccountNoteApiSteps.class).in(Scopes.SINGLETON);
        bind(CreateAccountNoteApiPage.class).in(Scopes.SINGLETON);
    }
}
