package com.dominion.mobile.ddsrefactortest.rest;

import android.content.Context;

import com.dominion.mobile.ddsrefactortest.api.UsersResponse;

import retrofit2.Call;

/**
 * Created by rsakhinala on 1/16/2017.
 */
public class UsersClient {

    private UserService mClient;
    private Context context;

    /**
     * Creates the rest adapter instance through retrofit
     * @param ctx of calling activity
     */
    public UsersClient(Context ctx) {
        this.context = ctx;
        this.mClient = RetrofitInstance.getRestAdapter(ctx).create(UserService.class);
    }

    /**
     * Get list of users through retrofit
     * @return UsersResponse response
     */
    public Call<UsersResponse> getUsers() {
        return mClient.getUsers();
    }
}
