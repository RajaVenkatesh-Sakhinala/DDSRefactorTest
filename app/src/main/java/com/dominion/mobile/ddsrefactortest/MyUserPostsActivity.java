package com.dominion.mobile.ddsrefactortest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dominion.mobile.ddsrefactortest.adapters.UserPostsAdapter;
import com.dominion.mobile.ddsrefactortest.api.UserPostsRequest;
import com.dominion.mobile.ddsrefactortest.api.UserPostsResponse;
import com.dominion.mobile.ddsrefactortest.api.UsersResponse;
import com.dominion.mobile.ddsrefactortest.api.entities.Post;
import com.dominion.mobile.ddsrefactortest.api.entities.User;
import com.dominion.mobile.ddsrefactortest.rest.UserPostClient;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rsakhinala on 1/17/2017.
 */

public class MyUserPostsActivity extends Activity {

    public static final String EXTRA_USER = "extra_user";
    private static final String CACHEKEY_USERS_POST = "cache-key-user-posts";

    private UserPostsAdapter adapter;
    private User user;
    private ProgressBar loadingIndicator;
    private final List<Post> posts = new ArrayList<>();

    @Inject
    UserPostClient userPostsClient;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_posts);

        // Inject Dependencies here
        ((ApplicationModule) getApplication()).getComponent().inject(this);

        adapter = new UserPostsAdapter(this, posts);

        ListView listView = (ListView) findViewById(R.id.posts);
        listView.setAdapter(adapter);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        user = (User) getIntent().getExtras().get(EXTRA_USER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadingIndicator.setVisibility(View.VISIBLE);

        getUserPostsFromServer();
    }

    public void getUserPostsFromServer(){
        Call<UserPostsResponse> getAllUserPosts = userPostsClient.getUserPosts("https://jsonplaceholder.typicode.com/posts?userId=" + user.getId());
        getAllUserPosts.enqueue(new Callback<UserPostsResponse>() {
            @Override
            public void onResponse(Call<UserPostsResponse> call, Response<UserPostsResponse> response) {

                // reset the users array, which was missing in the original code provided
                posts.clear();

                posts.addAll(response.body());

                adapter.notifyDataSetChanged();

                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserPostsResponse> call, Throwable t) {
                loadingIndicator.setVisibility( View.INVISIBLE );

                new AlertDialog.Builder( MyUserPostsActivity.this ).setTitle( R.string.error ).setMessage( R.string.something_went_wrong ).show();
            }
        });
    }
}