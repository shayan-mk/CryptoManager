package com.s2k.CryptoManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.mikephil.charting.components.Description;
import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkManager {

    private static String apiKey = "b83b3e60-3bf0-41ed-b117-d38ec00b216d";



    //Crypto coins' information
    public void getCryptoDataList(int groupNumber, Handler handler){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("start", String.valueOf(groupNumber*10 - 9));
        parameters.put("limit", "10");
        String url = buildURL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest", parameters);

        // your coin IO API key...
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", apiKey).build();

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Message message = new Message();
                    message.what = MainActivity.DB_OHLC_LOAD;
                    message.arg1 = 0;
                    handler.sendMessage(message);
                    throw new IOException("Unexpected code " + response);
                } else {
                    Gson gson = new Gson();
                    CryptoData[] cryptoData = gson.fromJson(response.body().string(), CryptoData[].class);
                    List<CryptoData> cryptoDataList = Arrays.asList(cryptoData);
                    Message message = new Message();
                    message.what = MainActivity.DB_OHLC_LOAD;
                    message.arg1 = 1;
                    message.obj = cryptoDataList;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //Candles' data
    public enum Range {
        weekly,
        oneMonth,
    }


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


        HashMap<String, String> parameters = new HashMap<>();

        String url = buildURL("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl)), parameters);

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
        Gson gson = new Gson();
        OHLC[] ohlcs = gson.fromJson(body, OHLC[].class);
        List<OHLC> OHLCList = Arrays.asList(ohlcs);
        //TODO: pass it to the handler, description

    }

    private String buildURL(String string, HashMap<String, String> queryParameters){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(string)
                .newBuilder();

        for (String param : queryParameters.keySet()) {
            urlBuilder.addQueryParameter(param, queryParameters.get(param));
        }

        return urlBuilder.build().toString();
    }


    private String getCurrentDate() {
        return null;
    }

}
