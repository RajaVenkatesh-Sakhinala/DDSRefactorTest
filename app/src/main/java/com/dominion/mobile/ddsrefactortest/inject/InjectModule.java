package com.dominion.mobile.ddsrefactortest.inject;

import android.app.Application;

import com.dominion.mobile.ddsrefactortest.rest.UserPostClient;
import com.dominion.mobile.ddsrefactortest.rest.UsersClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rsakhinala on 1/16/2017.
 */
@Module
public class InjectModule {
    private Application mApp;

    /**
     * Application instance
     * @param app {@link Application}
     */
    public InjectModule(Application app) {
        mApp = app;
    }

    /**
     * Login service instance
     * @return UsersClient
     */
    @Provides
    @Singleton
    public UsersClient provideUsersClient() {
        return new UsersClient(mApp.getApplicationContext());
    }

    /**
     * Recent scan service instance
     * @return UserPostClient {@link UserPostClient}
     */
    @Provides
    @Singleton
    public UserPostClient provideUserPostsClient() {
        return new UserPostClient(mApp.getApplicationContext());
    }

    /**
     * Provide application instance
     * @return Application instance {@link Application}
     */
    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }
}
