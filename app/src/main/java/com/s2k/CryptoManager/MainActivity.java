package com.s2k.CryptoManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CryptoListAdapter.OnItemClickListener {
    public static final String TAG = MainActivity.class.getName();
    private Boolean isRefreshing;
    private Boolean isLoadingMore;
    private Handler handler;

    public static final int DB_CRYPTO_LOAD = 1;
    public static final int DB_OHLC_LOAD = 2;
    public static final int DB_CRYPTO_UPDATE = 3;
    public static final int DB_OHLC_UPDATE = 4;
    public static final int NET_CRYPTO_LOAD = 5;
    public static final int NET_OHLC_LOAD = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRefreshing = false;
        isLoadingMore = false;

        RecyclerView rvCryptos = (RecyclerView) findViewById(R.id.rvCryptos);

        CryptoListAdapter adapter = new CryptoListAdapter(this, this);

        rvCryptos.setAdapter(adapter);
        rvCryptos.setLayoutManager(new LinearLayoutManager(this));

        // grey divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCryptos.getContext(),
                ((LinearLayoutManager) rvCryptos.getLayoutManager()).getOrientation());
        rvCryptos.addItemDecoration(dividerItemDecoration);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // TODO week references
        ImageButton refreshButton = (ImageButton) findViewById(R.id.cryptoRefreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRefreshing || isLoadingMore) {
                    return;
                }
                isRefreshing = true;
                progressBar.setVisibility(View.VISIBLE);

                // TODO needs to call network api
                new Thread(() -> {
                    float progress = 0;
                    while (true) {
                        progress += 1;
                        runOnUiThread(() -> progressBar.incrementProgressBy(1));

                        if (progress > 100) {
                            isRefreshing = false;
                            runOnUiThread(() -> {
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        }

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        ImageButton loadMoreButton = (ImageButton) findViewById(R.id.cryptoLoadMoreButton);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRefreshing || isLoadingMore) {
                    return;
                }
                isLoadingMore = true;
                progressBar.setVisibility(View.VISIBLE);

                // TODO needs to call network api
                new Thread(() -> {
                    float progress = 0;
                    while (true) {
                        progress += 1;
                        runOnUiThread(() -> progressBar.incrementProgressBy(1));

                        if (progress > 100) {
                            isLoadingMore = false;
                            runOnUiThread(() -> {
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                            break;
                        }

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case DB_CRYPTO_LOAD:
                        Log.d(TAG, "Message received: DB_CRYPTO_LOAD");
                        List<CryptoData> cryptoDataList = (List<CryptoData>) msg.obj;
                        adapter.loadMoreCryptoData(cryptoDataList);
                        break;
                    case DB_CRYPTO_UPDATE:
                        Log.d(TAG, "Message received: DB_CRYPTO_WRITE");
                        break;
                    case DB_OHLC_LOAD:
                        Log.d(TAG, "Message received: DB_OHLC_LOAD");
                        break;
                    case DB_OHLC_UPDATE:
                        Log.d(TAG, "Message received: DB_OHLC_WRITE");
                        break;
                    case NET_CRYPTO_LOAD:
                        Log.d(TAG, "Message received: NET_CRYPTO_LOAD");
                        cryptoDataList = (List<CryptoData>) msg.obj;
                        if (isRefreshing) {
                            adapter.reloadCryptoData(cryptoDataList);
                        } else {
                            adapter.loadMoreCryptoData(cryptoDataList);
                        }
                        break;
                    case NET_OHLC_LOAD:
                        Log.d(TAG, "Message received: NET_OHLC_LOAD");
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(String symbol) {
        DialogFragment dialogFragment = new OhclDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ohcl chart");
    }

    //Internet Connection!
    public boolean isConnectedToTheInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else {
            return false;
        }

    }
}