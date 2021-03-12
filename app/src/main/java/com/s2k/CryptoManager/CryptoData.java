package com.s2k.CryptoManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CryptoData {
    private static final String LOGO_URL = "https://s2.coinmarketcap.com/static/img/coins/64x64/";
    private final String id;
    private final String symbol;
    private final String name;
    private final quote quote;

    public static class quote {
        public USD USD;

        public quote(USD USD) {
            this.USD = USD;
        }

        public static class USD {
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

        @NonNull
        @Override
        public String toString() {
            return USD.price + " - " + USD.percent_change_1h + " - " + USD.percent_change_24h + " - " + USD.percent_change_7d;
        }
    }

    public CryptoData(String id, String symbol, String name, CryptoData.quote quote) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.quote = quote;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return LOGO_URL + id + ".png";
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return quote.USD.price;
    }

    public float getPercentChange1h() {
        return quote.USD.percent_change_1h;
    }

    public float getPercentChange24h() {
        return quote.USD.percent_change_24h;
    }

    public float getPercentChange7d() {
        return quote.USD.percent_change_7d;
    }

    @NonNull
    @Override
    public String toString() {
        return id + " - " + symbol + " - " + name + " - " + quote.toString();
    }
}
