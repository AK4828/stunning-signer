package id.rootca.sivion.dsigner.service;

import id.rootca.sivion.dsigner.entity.user.User;

import retrofit.http.GET;

/**
 * Created by root on 8/14/15.
 */
public interface UserService {
    @GET("/api/users/me")
    User getCurrentUser();
}
