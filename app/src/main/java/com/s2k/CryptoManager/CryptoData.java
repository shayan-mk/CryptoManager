package com.s2k.CryptoManager;

import java.util.ArrayList;
import java.util.List;

public class CryptoData {
    public String id;
    public String logo; //maybe need to change
    public String symbol;
    public String name;
    public quote quote;

    public class quote{
        public quote() {
        }

        public class USD{
            public USD(float price, float percent_change_1h, float percent_change_24h, float percent_change_7d) {
                this.price = price;
                this.percent_change_1h = percent_change_1h;
                this.percent_change_24h = percent_change_24h;
                this.percent_change_7d = percent_change_7d;
            }

            public float price;
            public float percent_change_1h;
            public float percent_change_24h;
            public float percent_change_7d;

        }
    }

    public CryptoData(String id, String logo, String symbol, String name, CryptoData.quote quote) {
        this.id = id;
        this.logo = logo;
        this.symbol = symbol;
        this.name = name;
        this.quote = quote;
    }



    /*// mock data
    public static List<CryptoData> createCryptoDataList() {
        List<CryptoData> list = new ArrayList<>();
        CryptoData data1 = new CryptoData("0", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/1200px-Bitcoin.svg.png", "BTC", "Bitcoin", 0, -2, 2, 50000);
        CryptoData data2 = new CryptoData("1", "https://icons-for-free.com/iconfiles/png/512/blockchain+cryptocurrency+currency+ethereum+money+icon-1320168258970240520.png", "ETH", "Ethereum", 0, -2, 22, 1500);
        for (int i = 0; i < 5; i++) {
            list.add(data1);
        }
        for (int i = 0; i < 5; i++) {
            list.add(data2);
        }
        return list;
    }*/
}
