package com.marshaarts.myapplication.network;

import com.marshaarts.myapplication.model.UsersResponse;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Fetch all featured posts/schools
    @POST("users/all/")
    Single<UsersResponse> getAllUsers(@Body JSONObject body);

    @POST("users/all/")
    Observable<UsersResponse> getUsers(@Body JSONObject body);
}
