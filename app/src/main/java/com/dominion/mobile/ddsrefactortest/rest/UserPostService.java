package com.dominion.mobile.ddsrefactortest.rest;

import com.dominion.mobile.ddsrefactortest.api.UserPostsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by rsakhinala on 1/17/2017.
 */
public interface UserPostService {
    /**
     * Rest API call for getting list of users
     * @return users array
     */
    @GET
    Call<UserPostsResponse> getUserPosts(@Url String url);
}
