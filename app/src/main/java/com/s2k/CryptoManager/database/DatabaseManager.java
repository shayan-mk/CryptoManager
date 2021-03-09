package com.s2k.CryptoManager.database;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;
import com.s2k.CryptoManager.MainActivity;
import com.s2k.CryptoManager.OHLC;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final String CRYPTO_FILE = "crypto_data";
    private static final String OHLC_DIR = "ohlc/";

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Runnable loadCryptoList(int start, int count, Handler handler) {
        return () -> {
            List<CryptoData> cryptoDataList = runLoadCrypto(start, count);
            Message message = new Message();
            message.what = MainActivity.DB_CRYPTO_LOAD;
            message.arg1 = 1;
            message.obj = cryptoDataList;
            handler.sendMessage(message);
        };
    }

    public Runnable updateCryptoList(List<CryptoData> cryptoDataList, boolean append, Handler handler) {
        return () -> {
            runUpdateCrypto(cryptoDataList, append);
            Message message = new Message();
            message.what = MainActivity.DB_CRYPTO_UPDATE;
            message.arg1 = 1;
            handler.sendMessage(message);
        };
    }

    public Runnable loadOHLC(String symbol, Handler handler) {
        return () -> {
            OHLC ohlc = runLoadOHCL(symbol);
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_LOAD;
            message.arg1 = 1;
            message.obj = ohlc;
            handler.sendMessage(message);
        };
    }

    public Runnable updateOHLC(String symbol, OHLC ohlc, Handler handler) {
        return () -> {
            runUpdateOHLC(symbol, ohlc);
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_UPDATE;
            message.arg1 = 1;
            handler.sendMessage(message);
        };
    }

    private List<CryptoData> runLoadCrypto(int start, int count) {
        Scanner scanner = DatabaseUtilities.getScanner(CRYPTO_FILE);
        List<CryptoData> cryptoDataList = new ArrayList<>();
        for (int i = 0; i < start; i++) {
            if (!scanner.hasNextLine()) break;

            scanner.nextLine();
        }
        for (int i = 0; i < count; i++) {
            if (!scanner.hasNextLine()) break;

            String gsonString = scanner.nextLine();
            cryptoDataList.add(DatabaseUtilities.getGson().fromJson(gsonString, CryptoData.class));
        }

        scanner.close();
        return cryptoDataList;
    }

    private synchronized void runUpdateCrypto(List<CryptoData> cryptoDataList, boolean append) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(append, CRYPTO_FILE);

        for (CryptoData cryptoData : cryptoDataList) {
            printWriter.println(DatabaseUtilities.getGson().toJson(cryptoData, CryptoData.class));
        }
        printWriter.flush();
        printWriter.close();
    }

    private OHLC runLoadOHCL(String symbol) {
        Scanner scanner = DatabaseUtilities.getScanner(OHLC_DIR + symbol + ".txt");
        OHLC ohlc = DatabaseUtilities.getGson().fromJson(scanner.nextLine(), OHLC.class);
        scanner.close();
        return ohlc;
    }

    private synchronized void runUpdateOHLC(String symbol, OHLC ohlc) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(false, OHLC_DIR + symbol + ".txt");
        printWriter.println(DatabaseUtilities.getGson().toJson(ohlc, OHLC.class));
        printWriter.flush();
        printWriter.close();
    }
}