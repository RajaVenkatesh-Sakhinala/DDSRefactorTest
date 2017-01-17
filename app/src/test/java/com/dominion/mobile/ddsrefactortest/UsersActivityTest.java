package com.dominion.mobile.ddsrefactortest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dominion.mobile.ddsrefactortest.api.UsersResponse;
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
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowLooper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by rsakhinala on 1/16/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = TestApplicationModule.class)
public class UsersActivityTest {

    private MyUsersActivity myUsersActivity;
    private ListView listView;
    private UsersResponse usersMocked;

    @Before
    public void setUp() throws Exception {
        TestApplicationModule app = (TestApplicationModule) RuntimeEnvironment.application;
        // Setting up the mock module
        MockInjectModule module = new MockInjectModule(app);
        usersMocked = createMockedUsersList();
        module.setUsersResult(usersMocked);
        app.setApplicationModule(module);

        // Initiate the activity with robolectric
        myUsersActivity = Robolectric.setupActivity(MyUsersActivity.class);
        listView = (ListView) myUsersActivity.findViewById(R.id.users);
    }

    @Test
    public void activityNotNull() throws Exception {
        // check for activity created
        assertNotNull(myUsersActivity);
    }

    @Test
    public void testUsersListAdapter() throws Exception {
        // mocked response is having only 2 users, where the list view also should have 2 items
        assertEquals(listView.getAdapter().getCount(), 2);

        // testing get view method and verifying first element is matching with mocked object
        View view = listView.getAdapter().getView(0, null, listView);
        TextView title = (TextView) view.findViewById(android.R.id.text1);
        Assert.assertEquals(title.getText().toString(), usersMocked.get(0).getName());
    }

    @Test
    public void testFailedResponseFromServer() throws Exception {
        // call getUsers method for the 2nd time
        // where it should return mocked failed response in MockInjectModule class
        myUsersActivity.getUsersFromServer();

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // Check the alert dialog for failed response is triggered
        AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
        Dialog alertDialog = ShadowAlertDialog.getLatestDialog();
        assertNotNull(alertDialog);
        assertTrue(alertDialog.isShowing());
    }

    @Test
    public void testUsersListItemClick() throws Exception {

        // Performing click on first list item
        boolean clicked = listView.performItemClick(listView, 0, listView.getItemIdAtPosition(0));
        assertTrue(clicked);

        // Check whether the next activity is started or not
        Intent expectedIntent = new Intent(myUsersActivity, MyUserPostsActivity.class);
        ShadowActivity shadowActivity = shadowOf(myUsersActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @After
    public void tearDown() throws Exception {
        // release any resources used for testing
    }

    /**
     * Creating a mocked list of users for testing
     *
     * @return array of users created
     */
    private UsersResponse createMockedUsersList() {
        String mockResponse = "[{\"id\":1,\"name\":\"Leanne Graham\",\"username\":\"Bret\",\"email\":\"Sincere@april.biz\",\"address\":{\"street\":\"Kulas Light\",\"suite\":\"Apt. 556\",\"city\":\"Gwenborough\",\"zipcode\":\"92998-3874\",\"geo\":{\"lat\":\"-37.3159\",\"lng\":\"81.1496\"}},\"phone\":\"1-770-736-8031 x56442\",\"website\":\"hildegard.org\",\"company\":{\"name\":\"Romaguera-Crona\",\"catchPhrase\":\"Multi-layered client-server neural-net\",\"bs\":\"harness real-time e-markets\"}},{\"id\":2,\"name\":\"Ervin Howell\",\"username\":\"Antonette\",\"email\":\"Shanna@melissa.tv\",\"address\":{\"street\":\"Victor Plains\",\"suite\":\"Suite 879\",\"city\":\"Wisokyburgh\",\"zipcode\":\"90566-7771\",\"geo\":{\"lat\":\"-43.9509\",\"lng\":\"-34.4618\"}},\"phone\":\"010-692-6593 x09125\",\"website\":\"anastasia.net\",\"company\":{\"name\":\"Deckow-Crist\",\"catchPhrase\":\"Proactive didactic contingency\",\"bs\":\"synergize scalable supply-chains\"}}]";
        UsersResponse usersMockedResponse = new Gson().fromJson(mockResponse, UsersResponse.class);
        return usersMockedResponse;
    }

}