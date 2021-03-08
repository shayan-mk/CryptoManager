package com.s2k.CryptoManager.database;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class DatabaseManager implements Callable<List<CryptoData>> {
    private static final String FILE_NAME = "";
    private Gson gson;

    public List<CryptoData> load(int start, int count) {
        Scanner scanner = DatabaseUtilities.getScanner(FILE_NAME);
        List<CryptoData> cryptoDataList = new ArrayList<>();
        for (int i = 0; i < start; i++) {
            if (!scanner.hasNextLine()) break;

            scanner.nextLine();
        }
        for (int i = 0; i < count; i++) {
            if (!scanner.hasNextLine()) break;

            String gsonString = scanner.nextLine();
            cryptoDataList.add(gson.fromJson(gsonString, CryptoData.class));
        }

        scanner.close();
        return cryptoDataList;
    }

    public synchronized void update(List<CryptoData> cryptoDataList, boolean append) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(FILE_NAME, append);

        for (CryptoData cryptoData : cryptoDataList) {
            printWriter.println(gson.toJson(cryptoData, CryptoData.class));
        }
        printWriter.flush();
        printWriter.close();
    }

    @Override
    public List<CryptoData> call() throws Exception {
        this.gson = DatabaseUtilities.getGson();

        return null;
    }
}