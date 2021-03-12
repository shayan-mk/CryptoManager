package com.s2k.CryptoManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkManager {
    private static NetworkManager instance = null;
    private static final String TAG = "NetworkManager";

    private static final String COIN_MARKET_API_KEY = "b83b3e60-3bf0-41ed-b117-d38ec00b216d";
    private static final String COIN_IO_API_KEY ="ADA19C63-1822-48E2-AF02-ED8E661F02F5";

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public Runnable loadCryptoList(int groupNumber, Handler handler) {
        return () -> runLoadCrypto(groupNumber, handler);
    }

    public Runnable loadOHLCList(String symbol, Range range, Handler handler) {
        return () -> runLoadOHLC(symbol, range, handler);
    }

    //Crypto coins' information
    private void runLoadCrypto(int groupNumber, Handler handler) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("start", String.valueOf(groupNumber * 10 - 9));
        parameters.put("limit", "10");
        String url = buildURL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest", parameters);

        // your coin IO API key...
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", COIN_MARKET_API_KEY).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(TAG, "request: " + request.toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Message message = new Message();
                    message.what = MainActivity.NET_CRYPTO_LOAD;
                    message.arg1 = 0;
                    handler.sendMessage(message);
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseString = response.body().string();
                    Log.d(TAG, "onResponse: " + responseString);
                    JsonObject obj = JsonParser.parseString(responseString).getAsJsonObject();
                    Log.d(TAG, "onResponse: " + obj.get("data").toString());
                    Log.d(TAG, "onResponse: " + obj.toString());
                    String dataJson = obj.get("data").toString();
                    Gson gson = new Gson();
                    CryptoData[] cryptoData = gson.fromJson(dataJson, CryptoData[].class);
                    Message message = new Message();
                    message.what = MainActivity.NET_CRYPTO_LOAD;
                    message.arg1 = 1;
                    message.obj = cryptoData;
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


    private void runLoadOHLC(String symbol, Range range, Handler handler) {

        String miniUrl;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=7"));
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=30"));
                break;

            default:
                miniUrl = "";

        }


        HashMap<String, String> parameters = new HashMap<>();

        String url = buildURL("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl)), parameters);

        // your coin IO API key...
        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", COIN_IO_API_KEY).build();

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = MainActivity.NET_OHLC_LOAD;
                message.arg1 = 0;
                handler.sendMessage(message);
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Gson gson = new Gson();
                    OHLC[] ohlcList = gson.fromJson(response.body().string(), OHLC[].class);
                    Message message = new Message();
                    message.what = MainActivity.NET_OHLC_LOAD;
                    message.arg1 = 1;
                    message.obj = ohlcList;
                    handler.sendMessage(message);
                }
            }
        });

    }

    private String buildURL(String string, HashMap<String, String> queryParameters) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(string).newBuilder();

        for (String param : queryParameters.keySet()) {
            urlBuilder.addQueryParameter(param, queryParameters.get(param));
        }
        String url = urlBuilder.build().toString();
        Log.d(TAG, "buildURL: " + url);

        return url;

    }


    private String getCurrentDate() {
        Calendar calender = Calendar.getInstance();

        calender.add(Calendar.DATE, 1);

        Date date = calender.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);

        return dateString + "T" + timeString;
    }

}
