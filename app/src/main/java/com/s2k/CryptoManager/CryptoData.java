package com.s2k.CryptoManager;

import java.util.ArrayList;
import java.util.List;

public class CryptoData {
    public String iconUrl; //maybe need to change
    public String name;
    public int oneHourChange;
    public int oneDayChange;
    public int oneWeekChange;
    public int price;

    public CryptoData(String iconUrl, String name, int oneHourChange, int oneDayChange, int oneWeekChange, int price) {
        this.iconUrl = iconUrl;
        this.name = name;
        this.oneHourChange = oneHourChange;
        this.oneDayChange = oneDayChange;
        this.oneWeekChange = oneWeekChange;
        this.price = price;
    }

    // mock data
    public static List<CryptoData> createCryptoDataList() {
        List<CryptoData> list = new ArrayList<>();
        CryptoData data1 = new CryptoData("https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/1200px-Bitcoin.svg.png", "BTC | Bitcoin", 0, -2, 2, 50000);
        CryptoData data2 = new CryptoData("https://icons-for-free.com/iconfiles/png/512/blockchain+cryptocurrency+currency+ethereum+money+icon-1320168258970240520.png", "ETH | Ethereum", 0, -2, 22, 1500);
        for (int i = 0; i < 5; i++) {
            list.add(data1);
        }
        for (int i = 0; i < 5; i++) {
            list.add(data2);
        }
        return list;
    }
}
