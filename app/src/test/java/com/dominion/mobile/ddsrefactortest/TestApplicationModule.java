package com.dominion.mobile.ddsrefactortest;

import com.dominion.mobile.ddsrefactortest.inject.InjectModule;

/**
 * Created by rsakhinala on 1/16/2017.
 */
public class TestApplicationModule extends ApplicationModule {
    private InjectModule mApplicationModule;

    @Override
    InjectModule getApplicationModule() {
        if (mApplicationModule == null) {
            return super.getApplicationModule();
        }
        return mApplicationModule;
    }

    public void setApplicationModule(InjectModule mApplicationModule) {
        this.mApplicationModule = mApplicationModule;
        initComponent();
    }
}
