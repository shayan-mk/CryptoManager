package com.s2k.CryptoManager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvCryptos = (RecyclerView) findViewById(R.id.rvCryptos);

        List<CryptoData> cryptoData = CryptoData.createCryptoDataList();
        CryptoListAdapter adapter = new CryptoListAdapter(cryptoData);

        rvCryptos.setAdapter(adapter);
        rvCryptos.setLayoutManager(new LinearLayoutManager(this));

        // grey divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCryptos.getContext(),
                ((LinearLayoutManager) rvCryptos.getLayoutManager()).getOrientation());
        rvCryptos.addItemDecoration(dividerItemDecoration);
    }
}