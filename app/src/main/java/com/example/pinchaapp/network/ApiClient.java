package com.example.pinchaapp.network;

import com.example.pinchaapp.session.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://api.nodesv.com/";
    private static Retrofit retrofit;

    // SOLUCIÓN: Le quitamos el parámetro Context. Ya no es necesario.
    public static Retrofit getInstance() {
        if (retrofit == null) {

            // Añadimos un interceptor de log opcional por si quieren ver las peticiones en consola
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        // SOLUCIÓN: Jalamos el token directamente del SessionManager global de forma limpia
                        String token = SessionManager.getToken();

                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}