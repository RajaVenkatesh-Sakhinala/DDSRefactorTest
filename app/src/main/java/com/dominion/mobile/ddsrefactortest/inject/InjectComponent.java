package com.dominion.mobile.ddsrefactortest.inject;

import com.dominion.mobile.ddsrefactortest.MyUserPostsActivity;
import com.dominion.mobile.ddsrefactortest.MyUsersActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by rsakhinala on 1/16/2017.
 */
@Singleton
@Component(modules = InjectModule.class)
public interface InjectComponent {
    /**
     * Inject dependency to MyUsersActivity
     * @param activity of {@link MyUsersActivity}
     */
    void inject(MyUsersActivity activity);

    /**
     * Inject dependency to MyUserPostsActivity
     * @param activity of {@link MyUserPostsActivity}
     */
    void inject(MyUserPostsActivity activity);
}
