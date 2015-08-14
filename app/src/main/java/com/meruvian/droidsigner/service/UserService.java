package com.meruvian.droidsigner.service;

import com.meruvian.droidsigner.entity.user.User;

import retrofit.http.GET;

/**
 * Created by root on 8/14/15.
 */
public interface UserService {
    @GET("/api/users/me")
    User getCurrentUser();
}
