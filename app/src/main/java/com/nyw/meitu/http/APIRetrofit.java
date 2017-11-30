package com.nyw.meitu.http;

import android.util.Log;


import com.nyw.meitu.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIRetrofit {

    private static Retrofit mInstance;
    private static Retrofit mInstance2;

    private static OkHttpClient mOKHttpClient;

    public static Retrofit getInstance2(){

        if(mInstance2 == null){
            synchronized (APIRetrofit.class){
                if(mInstance2 == null){
                    mInstance2 = new Retrofit.Builder()
                            .baseUrl(Constants.HTTP_URL2)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(getOkHttpClient())
                            .build();
                }
            }
        }

        return mInstance2;

    }

    public static Retrofit getInstance(){

       if(mInstance == null){
           synchronized (APIRetrofit.class){

               if(mInstance == null){
                   mInstance = new Retrofit.Builder()
                           .baseUrl(Constants.HTTP_URL)
                           .addConverterFactory(GsonConverterFactory.create())
                           .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                           .client(getOkHttpClient())
                           .build();
               }
           }
       }

       return mInstance;

    }

    private static OkHttpClient getOkHttpClient(){
        if(mInstance == null){
            synchronized (APIRetrofit.class) {

                if (mOKHttpClient == null) {
                    HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
//                            LogUtils.e(message);
                            Log.e("Logger",message);
                        }
                    });
                    logger.setLevel(HttpLoggingInterceptor.Level.BODY);
                    mOKHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .addInterceptor(logger)
                            .build();
                }
            }
        }

        return mOKHttpClient;
    }


}
