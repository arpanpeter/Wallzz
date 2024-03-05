package com.example.wallz.Api;

import com.example.wallz.models.wallpaperResponse;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Query;


public interface Api {
    @GET("curated/?page=1&per_page=80")
    Call<wallpaperResponse> getWallpaper
            (@Header("Authorization") String credentials

    );
    @GET("search?")
    Call<wallpaperResponse> getSearch(

            @Header("Authorization") String credentials,
            @Query("query") String queryText
    );

}