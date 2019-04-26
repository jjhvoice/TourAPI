package com.example.test.tourapi;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiService {

    //키워드 검색 조회
    @GET("searchKeyword")
    Call<Contributor> getRepo(
            @Query(value = "ServiceKey", encoded = true) String ServiceKey,
            @Query("keyword") String keyword,
            @Query("MobileOS") String MobileOS,
            @Query("MobileApp") String MobileApp,
            @Query("_type") String _type
    );
}
