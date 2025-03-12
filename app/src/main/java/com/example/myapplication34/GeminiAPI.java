package com.example.myapplication34;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiAPI {
    Call<GeminiResponse> getChatResponse(
            @Header("AIzaSyAAHq_paMJMb4cU-4KTIN1L1cC8PTI1ENU") String apiKey,
            @Body GeminiRequest request
    );
    Call<GeminiResponse> getChatResponse(@Body GeminiRequest request);
}
