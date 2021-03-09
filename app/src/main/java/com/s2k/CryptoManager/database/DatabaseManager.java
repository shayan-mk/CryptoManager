package com.s2k.CryptoManager.database;

import android.os.Handler;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class DatabaseManager implements Runnable {
    private static DatabaseManager instance = null;


    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void load(int start, int count, Handler handler) {
        handler.post(new DatabaseCryptoLoader(start, count, handler));
    }

    public synchronized void update(List<CryptoData> cryptoDataList, boolean append, Handler handler) {
        handler.post(new DatabaseCryptoWriter(cryptoDataList, append, handler));
    }

    @Override
    public void run() {


    }
}