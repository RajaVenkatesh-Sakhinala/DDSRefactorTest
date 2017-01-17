package com.dominion.mobile.ddsrefactortest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dominion.mobile.ddsrefactortest.api.UserPostsResponse;
import com.dominion.mobile.ddsrefactortest.api.entities.User;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rsakhinala on 1/17/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = TestApplicationModule.class)

public class UserPostsActivityTest {

    private MyUserPostsActivity myUserPostsActivity;
    private UserPostsResponse userPostsMocked;

    // ActivityController is a Robolectric class that drives the Activity lifecycle
    private ActivityController<MyUserPostsActivity> controller;

    @Before
    public void setUp() throws Exception {
        TestApplicationModule app = (TestApplicationModule) RuntimeEnvironment.application;
        // Setting up the mock module
        MockInjectModule module = new MockInjectModule(app);
        userPostsMocked = createMockedUserPostsData();
        module.setUserPostsResult(userPostsMocked);
        app.setApplicationModule(module);


        // Call the "buildActivity" method so we get an ActivityController which we can use
        // to have more control over the activity lifecycle
        controller = Robolectric.buildActivity(MyUserPostsActivity.class);
        createWithIntent(getMockedUserData());
    }

    /**
     * Activity creation that allows intent extras to be passed in
     *
     * @param user object for intent extras
     */
    private void createWithIntent(User user) {
        Intent intent = new Intent();
        intent.putExtra("extra_user", user);
        myUserPostsActivity = controller.withIntent(intent)
                .create()
                .resume()
                .get();
    }

    @Test
    public void activityNotNull() throws Exception {
        // check for activity created
        assertNotNull(myUserPostsActivity);
    }

    @Test
    public void testUserPostsAdapter() throws Exception {
        // mocked response is having only 5 user posts, where the list view also should have 5 items
        ListView listView = (ListView) myUserPostsActivity.findViewById(R.id.posts);
        assertEquals(listView.getAdapter().getCount(), 5);

        // testing get view method and verifying first element is matching with mocked object
        View view = listView.getAdapter().getView(0, null, listView);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView body = (TextView) view.findViewById(R.id.body);
        Assert.assertEquals(title.getText().toString(), userPostsMocked.get(0).getTitle());
        Assert.assertEquals(body.getText().toString(), userPostsMocked.get(0).getBody());
    }

    @Test
    public void failedResponseFromServer() throws Exception {
        // call getUserPosts method for the 2nd time
        // where it should return mocked failed response in MockInjectModule class
        myUserPostsActivity.getUserPostsFromServer();

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // Check the alert dialog for failed response is triggered
        AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
        Dialog alertDialog = ShadowAlertDialog.getLatestDialog();
        assertNotNull(alertDialog);
        assertTrue(alertDialog.isShowing());
    }

    @After
    public void tearDown() throws Exception {
        // release any resources used for testing

        // Destroy activity after every test
        controller.pause()
                .stop()
                .destroy();
    }

    /**
     * Creating a mocked list of user posts for testing
     *
     * @return UserPostsResponse object
     */
    private UserPostsResponse createMockedUserPostsData() {
        String mockResponse = "[{\"userId\":1,\"id\":1,\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"},{\"userId\":1,\"id\":2,\"title\":\"qui est esse\",\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\"},{\"userId\":1,\"id\":3,\"title\":\"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\"body\":\"et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut\"},{\"userId\":1,\"id\":4,\"title\":\"eum et est occaecati\",\"body\":\"ullam et saepe reiciendis voluptatem adipisci\\nsit amet autem assumenda provident rerum culpa\\nquis hic commodi nesciunt rem tenetur doloremque ipsam iure\\nquis sunt voluptatem rerum illo velit\"},{\"userId\":1,\"id\":5,\"title\":\"nesciunt quas odio\",\"body\":\"repudiandae veniam quaerat sunt sed\\nalias aut fugiat sit autem sed est\\nvoluptatem omnis possimus esse voluptatibus quis\\nest aut tenetur dolor neque\"}]";
        UserPostsResponse userPostsMockedResponse = new Gson().fromJson(mockResponse, UserPostsResponse.class);
        return userPostsMockedResponse;
    }

    /**
     * Creating a mocked user object for testing
     *
     * @return User object
     */
    private User getMockedUserData() {
        String mockUser = "{\"id\":1,\"name\":\"LeanneGraham\",\"username\":\"Bret\",\"email\":\"Sincere@april.biz\",\"address\":{\"street\":\"KulasLight\",\"suite\":\"Apt.556\",\"city\":\"Gwenborough\",\"zipcode\":\"92998-3874\",\"geo\":{\"lat\":\"-37.3159\",\"lng\":\"81.1496\"}}}";
        User userMockedData = new Gson().fromJson(mockUser, User.class);
        return userMockedData;
    }

}