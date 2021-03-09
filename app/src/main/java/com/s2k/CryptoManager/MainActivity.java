package com.s2k.CryptoManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements CryptoListAdapter.OnItemClickListener {
    private Boolean isRefreshing;
    private Boolean isLoadingMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRefreshing = false;
        isLoadingMore = false;

        RecyclerView rvCryptos = (RecyclerView) findViewById(R.id.rvCryptos);

        List<CryptoData> cryptoData = CryptoData.createCryptoDataList();
        CryptoListAdapter adapter = new CryptoListAdapter(this, cryptoData, this);

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
    }

    @Override
    public void onItemClick(String symbol) {
        DialogFragment dialogFragment = new OhclDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ohcl chart");
    }

    //Internet Connection!
    public static boolean isConnectedToTheInternet(){
        ConnectivityManager cm =  (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else {
            return false;
        }

    }
}