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
import android.widget.Toast;

import com.s2k.CryptoManager.database.DatabaseManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CryptoListAdapter.OnItemClickListener {
    public static final String TAG = MainActivity.class.getName();
    private Boolean isRefreshing;
    private Boolean isLoadingMore;
    private Boolean isLoadingOhlc;
    private String loadingOhlcSymbol;
    private Handler handler;
    private ExecutorService threadPool;
    private Runnable updateProgressBarRunnable;

    public static final int DB_CRYPTO_LOAD = 1001;
    public static final int DB_OHLC_LOAD = DB_CRYPTO_LOAD + 1;
    public static final int DB_CRYPTO_UPDATE = DB_CRYPTO_LOAD + 2;
    public static final int DB_OHLC_UPDATE = DB_CRYPTO_LOAD + 3;
    public static final int NET_CRYPTO_LOAD = DB_CRYPTO_LOAD + 4;
    public static final int NET_OHLC_LOAD = DB_CRYPTO_LOAD + 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRefreshing = false;
        isLoadingMore = false;
        isLoadingOhlc = false;
        loadingOhlcSymbol = "";
        threadPool = Executors.newFixedThreadPool(5);
        DatabaseManager.initDatabase(getFilesDir());

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
        refreshButton.setOnClickListener(view -> {
            if (isRefreshing || isLoadingMore) {
                return;
            }
            isRefreshing = true;
            progressBar.setVisibility(View.VISIBLE);

            if (isConnectedToTheInternet()) {
                threadPool.execute(NetworkManager.getInstance()
                        .loadCryptoList(1, handler));
            } else {
                threadPool.execute(DatabaseManager.getInstance()
                        .loadCryptoList(0, 10, handler));
            }
            handler.post(updateProgressBarRunnable);
        });

        ImageButton loadMoreButton = (ImageButton) findViewById(R.id.cryptoLoadMoreButton);
        loadMoreButton.setOnClickListener(view -> {
            if (isRefreshing || isLoadingMore) {
                return;
            }
            isLoadingMore = true;
            progressBar.setVisibility(View.VISIBLE);

            if (isConnectedToTheInternet()) {
                threadPool.execute(NetworkManager.getInstance()
                        .loadCryptoList(adapter.getItemCount() / 10 + 1, handler));
            } else {
                threadPool.execute(DatabaseManager.getInstance()
                        .loadCryptoList(adapter.getItemCount(), 10, handler));
            }
            handler.post(updateProgressBarRunnable);
        });

        updateProgressBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (isLoadingMore || isRefreshing || isLoadingOhlc) {
                    progressBar.incrementProgressBy(1);
                    handler.postDelayed(this, 50);
                }
            }
        };

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {

                Log.d(TAG, "handleMessage: " + msg.what);
                switch (msg.what) {
                    case DB_CRYPTO_LOAD:
                        Log.d(TAG, "Message received: DB_CRYPTO_LOAD");
                        List<CryptoData> cryptoDataList = Arrays.asList((CryptoData[]) msg.obj);
                        adapter.loadMoreCryptoData(cryptoDataList);
                        isLoadingMore = false;

                        if (cryptoDataList.size() == 0 && adapter.getItemCount() == 0) {
                            Toast.makeText(MainActivity.this, "There is no data to show", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                    case DB_CRYPTO_UPDATE:
                        Log.d(TAG, "Message received: DB_CRYPTO_WRITE");
                        break;
                    case DB_OHLC_LOAD:
                        Log.d(TAG, "Message received: DB_OHLC_LOAD");
                        List<OHLC> ohclList = Arrays.asList((OHLC[]) msg.obj);
                        DialogFragment dialogFragment = new OhlcDialogFragment(loadingOhlcSymbol, ohclList);
                        dialogFragment.show(getSupportFragmentManager(), "ohcl chart");
                        isLoadingOhlc = false;
                        loadingOhlcSymbol = "";

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                    case DB_OHLC_UPDATE:
                        Log.d(TAG, "Message received: DB_OHLC_WRITE");
                        break;
                    case NET_CRYPTO_LOAD:
                        Log.d(TAG, "Message received: NET_CRYPTO_LOAD");
                        cryptoDataList = Arrays.asList((CryptoData[]) msg.obj);
                        if (isRefreshing) {
                            adapter.reloadCryptoData(cryptoDataList);
                            threadPool.execute(DatabaseManager.getInstance()
                                    .updateCryptoList(cryptoDataList, false, handler));
                        } else {
                            adapter.loadMoreCryptoData(cryptoDataList);
                            threadPool.execute(DatabaseManager.getInstance()
                                    .updateCryptoList(cryptoDataList, true, handler));
                        }
                        isRefreshing = false;
                        isLoadingMore = false;

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                    case NET_OHLC_LOAD:
                        Log.d(TAG, "Message received: NET_OHLC_LOAD");
                        ohclList = Arrays.asList((OHLC[]) msg.obj);
                        dialogFragment = new OhlcDialogFragment(loadingOhlcSymbol, ohclList);
                        dialogFragment.show(getSupportFragmentManager(), "ohcl chart");

                        threadPool.execute(DatabaseManager.getInstance()
                                .updateOHLCList(loadingOhlcSymbol, ohclList, handler));

                        isLoadingOhlc = false;
                        loadingOhlcSymbol = "";

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };

        isLoadingMore = true;
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "onCreate: internetConnection" + isConnectedToTheInternet());
        if (isConnectedToTheInternet()) {
            threadPool.execute(NetworkManager.getInstance()
                    .loadCryptoList(1, handler));
        } else {
            threadPool.execute(DatabaseManager.getInstance()
                    .loadCryptoList(0, 10, handler));
        }
        handler.post(updateProgressBarRunnable);
    }

    @Override
    public void onItemClick(String symbol) {
        if (isLoadingOhlc) return;
        loadingOhlcSymbol = symbol;
        isLoadingOhlc = true;

        if (isConnectedToTheInternet()) {
            threadPool.execute(NetworkManager.getInstance()
                    .loadOHLCList(symbol, NetworkManager.Range.oneMonth, handler));
        } else {
            threadPool.execute(DatabaseManager.getInstance()
                    .loadOHLCList(symbol, handler));
        }
        handler.post(updateProgressBarRunnable);
    }

    //Internet Connection!
    public boolean isConnectedToTheInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }
}