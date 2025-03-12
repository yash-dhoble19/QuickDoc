package com.example.myapplication34;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import  com.example.myapplication34.GeminiResponse;


public class chatbot extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyBjYIPBok7bbSbQ-HjFaa-pNtuT_v6oRTo"; // Replace with your API key
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        responseText = findViewById(R.id.responseText);

        GeminiAPI apiService = RetrofitClient.getClient().create(GeminiAPI.class);

        GeminiRequest request = new GeminiRequest(
                Collections.singletonList(new GeminiRequest.Content(
                        Collections.singletonList(new GeminiRequest.Part("Hello! How are you?"))
                ))
        );

        apiService.getChatResponse("Bearer " + API_KEY, request).enqueue(new Callback<GeminiResponse>() {

            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseText.setText(response.body().candidates.get(0).content.parts.get(0).text);
                } else {
                    responseText.setText("Error: " + response.code() + " - " + response.message());
                }
            }


            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                responseText.setText("Failed: " + t.getMessage());
            }
        });

    }
    }

