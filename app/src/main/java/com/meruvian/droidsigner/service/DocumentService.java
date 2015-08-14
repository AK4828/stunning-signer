package com.meruvian.droidsigner.service;

import com.meruvian.droidsigner.entity.Document;

import java.io.InputStream;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by root on 8/14/15.
 */
public interface DocumentService {
    @POST("/api/documents")
    Document saveDocumentFile(@QueryMap Map<String, String> documents, InputStream stream) throws Exception;

    @PUT("/api/documents/{id}")
    Document updateDocument(@QueryMap Map<String, String> docId,Document document);

    @GET("/api/documents/{id}")
    Document getDocumentById(@Path("id")String id);
}
