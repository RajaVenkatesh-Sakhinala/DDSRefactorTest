package com.dominion.mobile.ddsrefactortest.rest;

import com.dominion.mobile.ddsrefactortest.api.UsersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by rsakhinala on 1/16/2017.
 */
public interface UserService {

    /**
     * Rest API call for getting list of users
     * @return users array
     */
    @GET("users")
    Call<UsersResponse> getUsers();
}
