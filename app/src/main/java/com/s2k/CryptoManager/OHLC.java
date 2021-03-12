package com.s2k.CryptoManager;


public class OHLC {

    private final float time_open;
    private final float time_close;
    private final float price_open;
    private final float price_close;
    private final float price_high;
    private final float price_low;

    public OHLC(float time_open, float time_close, float price_open, float price_close, float price_high, float price_low) {
        this.time_open = time_open;
        this.time_close = time_close;
        this.price_open = price_open;
        this.price_close = price_close;
        this.price_high = price_high;
        this.price_low = price_low;
    }

    public float getPrice_open() {
        return price_open;
    }

    public float getPrice_close() {
        return price_close;
    }

    public float getPrice_high() {
        return price_high;
    }

    public float getPrice_low() {
        return price_low;
    }

    public float getTime_open() {
        return time_open;
    }

    public float getTime_close() {
        return time_close;
    }
}
