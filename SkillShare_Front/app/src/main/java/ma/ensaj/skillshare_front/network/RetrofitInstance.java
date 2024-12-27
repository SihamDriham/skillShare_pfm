package ma.ensaj.skillshare_front.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;

    public static Retrofit getInstance() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.7:3600/skillShare/")
                    .addConverterFactory(GsonConverterFactory.create(gson));
            retrofit = builder.build();
        }
        return retrofit;
    }


}
