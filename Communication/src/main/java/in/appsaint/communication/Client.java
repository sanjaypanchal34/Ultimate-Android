package in.appsaint.communication;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class Client {


    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
//            .addInterceptor(new ConnectivityInterceptor(SmartApplication.getInstance()))
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    static Retrofit getRetrofitClient(final Object tag) {

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .baseUrl(Api.BASE_URL.toString())
                .callFactory(new Call.Factory() {
                    @NonNull
                    @Override
                    public Call newCall(@NonNull Request request) {
                        Request.Builder builder = request.newBuilder();

                        builder.tag(tag);

                        return client.newCall(builder.build());
                    }
                })
                .build();
    }

    static OkHttpClient getClient() {
        return client;
    }

}