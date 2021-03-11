package com.s2k.CryptoManager.database;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;
import com.s2k.CryptoManager.MainActivity;
import com.s2k.CryptoManager.OHLC;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
    private static DatabaseManager dbManager = null;
    private static final String CRYPTO_FILE = "crypto_data.txt";
    private static final String OHLC_DIR = "ohlc/";
    private final DatabaseUtility dbUtility;

    private DatabaseManager(File databaseDir) {
        dbUtility = new DatabaseUtility(databaseDir);
    }

    public static void initDatabase(File databaseDir) {
        dbManager = new DatabaseManager(databaseDir);
    }

    public static DatabaseManager getInstance() {
        return dbManager;
    }

    public Runnable loadCryptoList(int start, int count, Handler handler) {
        return () -> {
            List<CryptoData> cryptoDataList = runLoadCrypto(start, count);
            Message message = new Message();
            message.what = MainActivity.DB_CRYPTO_LOAD;
            message.arg1 = 1;
            message.obj = cryptoDataList.toArray();
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
            List<OHLC> ohlcList = runLoadOHCL(symbol);
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_LOAD;
            message.arg1 = 1;
            message.obj = ohlcList.toArray();
            handler.sendMessage(message);
        };
    }

    public Runnable updateOHLC(String symbol, List<OHLC> ohlcList, Handler handler) {
        return () -> {
            runUpdateOHLC(symbol, ohlcList);
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_UPDATE;
            message.arg1 = 1;
            handler.sendMessage(message);
        };
    }

    private List<CryptoData> runLoadCrypto(int start, int count) {
        Scanner scanner = dbUtility.getScanner(CRYPTO_FILE);
        List<CryptoData> cryptoDataList = new ArrayList<>();
        for (int i = 0; i < start; i++) {
            if (!scanner.hasNextLine()) break;

            scanner.nextLine();
        }
        for (int i = 0; i < count; i++) {
            if (!scanner.hasNextLine()) break;

            String gsonString = scanner.nextLine();
            cryptoDataList.add(new Gson().fromJson(gsonString, CryptoData.class));
        }

        scanner.close();
        return cryptoDataList;
    }

    private synchronized void runUpdateCrypto(List<CryptoData> cryptoDataList, boolean append) {
        PrintWriter printWriter = dbUtility.getPrintWriter(append, CRYPTO_FILE);

        for (CryptoData cryptoData : cryptoDataList) {
            printWriter.println(new Gson().toJson(cryptoData, CryptoData.class));
        }
        printWriter.flush();
        printWriter.close();
    }

    private List<OHLC> runLoadOHCL(String symbol) {
        Scanner scanner = dbUtility.getScanner(OHLC_DIR + symbol + ".txt");
        List<OHLC> ohlcList = Arrays.asList(new Gson().fromJson(scanner.nextLine(), OHLC[].class));
        scanner.close();
        return ohlcList;
    }

    private synchronized void runUpdateOHLC(String symbol, List<OHLC> ohlcList) {
        PrintWriter printWriter = dbUtility.getPrintWriter(false, OHLC_DIR + symbol + ".txt");
        printWriter.println(new Gson().toJson(ohlcList.toArray(), OHLC[].class));
        printWriter.flush();
        printWriter.close();
    }
}