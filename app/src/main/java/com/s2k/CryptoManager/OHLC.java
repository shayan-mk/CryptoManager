package com.s2k.CryptoManager;



public class OHLC {

    private float timeOpen;
    private float timeClose;
    private float priceOpen;
    private float priceClose;
    private float priceHigh;
    private float priceLow;

    public OHLC(float timeOpen, float timeClose, float priceOpen, float priceClose, float priceHigh, float priceLow) {
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
    }
}
