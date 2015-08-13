package com.meruvian.droidsigner.service;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by root on 8/13/15.
 */
public interface LoginService {
    @POST("/oauth/token")
    Map<String, String> login(@Header("Authorization") String authorization,@QueryMap Map<String, String> queryParam);
}
