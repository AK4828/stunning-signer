package id.rootca.sivion.dsigner.service;

import id.rootca.sivion.dsigner.entity.Authentication;

import java.util.Map;

import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by root on 8/13/15.
 */
public interface LoginService {
    @POST("/oauth/token")
    Authentication login(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParam);
}
