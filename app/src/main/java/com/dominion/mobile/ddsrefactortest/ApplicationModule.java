package com.dominion.mobile.ddsrefactortest;

import android.app.Application;

import com.dominion.mobile.ddsrefactortest.inject.DaggerInjectComponent;
import com.dominion.mobile.ddsrefactortest.inject.InjectComponent;
import com.dominion.mobile.ddsrefactortest.inject.InjectModule;

/**
 * Created by rsakhinala on 1/16/2017.
 */
public class ApplicationModule extends Application {
    private InjectComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    InjectModule getApplicationModule() {
        return new InjectModule(this);
    }

    public InjectComponent getComponent() {
        return mComponent;
    }

    /**
     * Dagger Inject Component
     */
    void initComponent() {
        mComponent = DaggerInjectComponent.builder()
                .injectModule(getApplicationModule())
                .build();
    }
}
