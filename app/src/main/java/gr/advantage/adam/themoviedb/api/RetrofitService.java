package gr.advantage.adam.themoviedb.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService instance;

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.MINUTES).writeTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES).build();
    }

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(okClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Api createService(){
        return retrofit.create(Api.class);
    }

    public static synchronized RetrofitService getInstance() {
        if (instance == null) {
            instance = new RetrofitService();
        }
        return instance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
