package com.s2k.CryptoManager.database;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.s2k.CryptoManager.CryptoData;
import com.s2k.CryptoManager.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DatabaseCryptoLoader implements Runnable {
    private Handler handler;
    private int start;
    private int count;

    DatabaseCryptoLoader(int start, int count, Handler handler) {
        this.handler = handler;
        this.start = start;
        this.count = count;
    }

    @Override
    public void run() {
        List<CryptoData> cryptoDataList = load(start, count);
        Message message = new Message();
        message.what = MainActivity.DB_CRYPTO_LOAD;
        message.arg1 = 1;
        message.obj = cryptoDataList;
        handler.sendMessage(message);
    }

    private List<CryptoData> load(int start, int count) {
        Scanner scanner = DatabaseUtilities.getScanner();
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
}
