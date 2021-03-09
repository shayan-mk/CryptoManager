package com.s2k.CryptoManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.github.mikephil.charting.components.Description;
import com.s2k.CryptoManager.CryptoData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkManager implements Callable<List<CryptoData>> {

    private static String apiKey = "b83b3e60-3bf0-41ed-b117-d38ec00b216d";

    @Override
    public List<CryptoData> call() throws Exception {

        return null;
    }

    //Page one Activities!

    //Internet Connection!
    //TODO:
    /*public static boolean isConnectedToTheInternet(){
        ConnectivityManager cm =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else {
            return false;
        }

    }*/


    //Crypto coins' information
    public void getCryptoDataGroupInformation(int groupNumber){
        String url = buildURL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest");

        // your coin IO API key...
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    //TODO: pass the CryptoDataGroupInformation to the handler
                }
            }
        });
    }

    //Candles' data
    public enum Range {
        weekly,
        oneMonth,
    }

    // برای دریافت کندل های روزانه به مدت یک هفته پارامتر دوم را "هفته ای" بدهید و
    // برای دریافت کندل های روزانه به مدت یک ماه پارامتر دوم را "یک ماه" بدهید
    // پارامتر اول هم نماد سکه مورد نظر خواهد بود

    public void getCandles(String symbol,Range range) {

        String miniUrl;
        final String description;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=7"));
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=30"));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

        }



        String url = buildURL("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl)));

        // your coin IO API key...
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", "YOUR_COIN_IO_API_KEY")
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    } else {
                    extractCandlesFromResponse(response.body().string(), description);
                }
            }
        });

    }

    //TODO: pass candles to the handler
    private void extractCandlesFromResponse(String body, String description){
    }

    private String buildURL(String string){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(string)
                .newBuilder();

        String url = urlBuilder.build().toString();

        return url;
    }


    private String getCurrentDate() {
        return null;
    }

}
