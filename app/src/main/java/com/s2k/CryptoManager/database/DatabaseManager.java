package com.s2k.CryptoManager.database;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;
import com.s2k.CryptoManager.MainActivity;
import com.s2k.CryptoManager.OHLC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
    private static DatabaseManager dbManager = null;
    private static final String CRYPTO_FILE = "crypto_data.txt";
    private static final String OHLC_DIR = "ohlc/";
    private static final OHLC[] MOCK_OHLC_LIST = new OHLC[0];
    public final DatabaseUtility dbUtility;

    private DatabaseManager(File databaseDir) {
        dbUtility = new DatabaseUtility(databaseDir);
//        try {
//            Scanner scanner = new Scanner(databaseDir.listFiles()[0]);
//            while (scanner.hasNextLine()) {
//                Log.d("DataBaseManager", scanner.nextLine());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public static void initDatabase(File databaseDir) {
        dbManager = new DatabaseManager(databaseDir);
    }

    public static DatabaseManager getInstance() {
        return dbManager;
    }

    public Runnable loadCryptoList(int groupNumber, Handler handler) {
        return () -> {
            CryptoData[] cryptoDataList = runLoadCrypto(groupNumber);
            Message message = new Message();
            message.what = MainActivity.DB_CRYPTO_LOAD;
            message.arg1 = 1;
            message.obj = cryptoDataList;
            handler.sendMessage(message);
        };
    }

    public Runnable updateCryptoList(int groupNumber, List<CryptoData> cryptoDataList, Handler handler) {
        return () -> {
            runUpdateCrypto(groupNumber, (CryptoData[]) cryptoDataList.toArray());
            Message message = new Message();
            message.what = MainActivity.DB_CRYPTO_UPDATE;
            message.arg1 = 1;
            handler.sendMessage(message);
        };
    }

    public Runnable loadOHLCList(String symbol, Handler handler) {
        return () -> {
            OHLC[] ohlcList = runLoadOHLC(symbol);
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_LOAD;
            message.arg1 = 1;
            message.obj = ohlcList;
            handler.sendMessage(message);
        };
    }

    public Runnable updateOHLCList(String symbol, List<OHLC> ohlcList, Handler handler) {
        return () -> {
            runUpdateOHLC(symbol, (OHLC[]) ohlcList.toArray());
            Message message = new Message();
            message.what = MainActivity.DB_OHLC_UPDATE;
            message.arg1 = 1;
            handler.sendMessage(message);
        };
    }

    private CryptoData[] runLoadCrypto(int groupNumber) {
        Scanner scanner = dbUtility.getScanner(CRYPTO_FILE);
        Gson gson = new Gson();
        CryptoData[] cryptoDataList;

        for (int i = 0; i < groupNumber; i++) {
            scanner.nextLine();
        }

        if (!scanner.hasNextLine()) {
            cryptoDataList = new CryptoData[0];
        } else {
            String line = scanner.nextLine();
            //Log.d("DataBaseManager", "runLoadCrypto: " + line);
            cryptoDataList = gson.fromJson(line, CryptoData[].class);
        }
        scanner.close();

        return cryptoDataList;
    }

    private synchronized void runUpdateCrypto(int groupNumber, CryptoData[] cryptoDataList) {
        if (groupNumber == 0) {
            PrintWriter printWriter = dbUtility.getPrintWriter(false, CRYPTO_FILE);
            Gson gson = new Gson();

            printWriter.println(gson.toJson(cryptoDataList, CryptoData[].class));
            for (CryptoData cryptoData : cryptoDataList) {
                runUpdateOHLC(cryptoData.getSymbol(), MOCK_OHLC_LIST);
            }

            printWriter.flush();
            printWriter.close();
        } else {
            Scanner scanner = dbUtility.getScanner(CRYPTO_FILE);
            int num = 1;
            StringBuilder data = new StringBuilder();
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine());
                num++;
                if (num > groupNumber) {
                    break;
                }
                data.append("\n");
            }
            Boolean hasNextLine = scanner.hasNextLine();
            scanner.close();

            PrintWriter printWriter;
            Gson gson = new Gson();
            if (!hasNextLine) {
                printWriter = dbUtility.getPrintWriter(true, CRYPTO_FILE);

                printWriter.println(gson.toJson(cryptoDataList, CryptoData[].class));
                for (CryptoData cryptoData : cryptoDataList) {
                    runUpdateOHLC(cryptoData.getSymbol(), MOCK_OHLC_LIST);
                }

            } else {
                printWriter = dbUtility.getPrintWriter(false, CRYPTO_FILE);

                printWriter.println(data);
                printWriter.println(gson.toJson(cryptoDataList, CryptoData[].class));
                for (CryptoData cryptoData : cryptoDataList) {
                    runUpdateOHLC(cryptoData.getSymbol(), MOCK_OHLC_LIST);
                }
            }
            printWriter.flush();
            printWriter.close();
        }
    }

    private OHLC[] runLoadOHLC(String symbol) {
        Scanner scanner = dbUtility.getScanner(OHLC_DIR + symbol + ".txt");
        Gson gson = new Gson();
        OHLC[] ohlcList = gson.fromJson(scanner.nextLine(), OHLC[].class);
        scanner.close();
        return ohlcList;
    }

    private synchronized void runUpdateOHLC(String symbol, OHLC[] ohlcList) {
        PrintWriter printWriter = dbUtility.getPrintWriter(false, OHLC_DIR + symbol + ".txt");
        Gson gson = new Gson();
        printWriter.println(gson.toJson(ohlcList, OHLC[].class));
        printWriter.flush();
        printWriter.close();
    }
}