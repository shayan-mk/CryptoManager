package com.s2k.CryptoManager;


public class OHLC {

    private final float timeOpen;
    private final float timeClose;
    private final float priceOpen;
    private final float priceClose;
    private final float priceHigh;
    private final float priceLow;

    public OHLC(float timeOpen, float timeClose, float priceOpen, float priceClose, float priceHigh, float priceLow) {
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
    }

    public float getPriceOpen() {
        return priceOpen;
    }

    public float getPriceClose() {
        return priceClose;
    }

    public float getPriceHigh() {
        return priceHigh;
    }

    public float getPriceLow() {
        return priceLow;
    }

    public float getTimeOpen() {
        return timeOpen;
    }

    public float getTimeClose() {
        return timeClose;
    }
}
