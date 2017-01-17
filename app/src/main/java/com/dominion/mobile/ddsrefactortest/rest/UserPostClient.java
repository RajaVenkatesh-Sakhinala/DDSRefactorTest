package com.dominion.mobile.ddsrefactortest.rest;

import android.content.Context;

import com.dominion.mobile.ddsrefactortest.api.UserPostsResponse;
import com.dominion.mobile.ddsrefactortest.api.UsersResponse;

import retrofit2.Call;

/**
 * Created by rsakhinala on 1/17/2017.
 */

public class UserPostClient {
    private UserPostService mClient;
    private Context context;

    /**
     * Creates the rest adapter instance through retrofit
     * @param ctx of calling activity
     */
    public UserPostClient(Context ctx) {
        this.context = ctx;
        this.mClient = RetrofitInstance.getRestAdapter(ctx).create(UserPostService.class);
    }

    /**
     * Get list of users through retrofit
     * @return UsersResponse response
     */
    public Call<UserPostsResponse> getUserPosts(String url) {
        return mClient.getUserPosts(url);
    }
}