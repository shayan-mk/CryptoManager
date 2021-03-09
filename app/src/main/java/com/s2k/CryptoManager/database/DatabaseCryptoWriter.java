package com.s2k.CryptoManager.database;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;
import com.s2k.CryptoManager.MainActivity;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseCryptoWriter implements Runnable{
    private Gson gson;
    private Handler handler;
    private boolean append;
    private List<CryptoData> cryptoDataList;


    DatabaseCryptoWriter(List<CryptoData> cryptoDataList, boolean append, Handler handler) {
        this.handler = handler;
        this.append = append;
        this.cryptoDataList = cryptoDataList;
    }

    @Override
    public void run() {
        write(cryptoDataList, append);
        Message message = new Message();
        message.what = MainActivity.DB_CRYPTO_WRITE;
        message.arg1 = 1;
        handler.sendMessage(message);
    }

    public void write(List<CryptoData> cryptoDataList, boolean append) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(append);

        for (CryptoData cryptoData : cryptoDataList) {
            printWriter.println(gson.toJson(cryptoData, CryptoData.class));
        }
        printWriter.flush();
        printWriter.close();
    }
}
