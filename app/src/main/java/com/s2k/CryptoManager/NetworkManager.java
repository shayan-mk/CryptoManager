package com.s2k.CryptoManager;

import com.s2k.CryptoManager.CryptoData;

import java.util.List;
import java.util.concurrent.Callable;

public class NetworkManager implements Callable<List<CryptoData>> {

    @Override
    public List<CryptoData> call() throws Exception {

        return null;
    }

    public static boolean isConnectedToTheInternet(){
        return false;
    }

    public List<CryptoData> getCryptoDataGroupInformation(int groupNumber){
        return null;
    }

    // what it should return?
    public void getCryptoDataDialog(CryptoData cryptoData){
        return;
    }

}