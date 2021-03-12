package com.s2k.CryptoManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class OhlcDialogFragment extends AppCompatDialogFragment {
    private List<CandleEntry> oneMonthList;
    private List<CandleEntry> oneWeekList;
    private String symbol;
    private CandleDataSet set;
    private Boolean isOneWeekShowed;

    public OhlcDialogFragment(String symbol, List<OHLC> data) {
        this.symbol = symbol;
        Collections.reverse(data);
        oneMonthList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            OHLC candle = data.get(i);
            oneMonthList.add(new CandleEntry(i, candle.getPrice_high(), candle.getPrice_low(), candle.getPrice_open(), candle.getPrice_close()));
        }
        oneWeekList = new ArrayList<>();
        for (int i = oneMonthList.size() - 7; i < oneMonthList.size(); i++) {
            oneWeekList.add(oneMonthList.get(i));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        isOneWeekShowed = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_ohlc, null);

        // setting chart parameters
        CandleStickChart chart = (CandleStickChart) view.findViewById(R.id.ohlcChart);
        chart.setLogEnabled(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setDrawBorders(true);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        yAxis.setGranularity(1f);
        rightAxis.setGranularity(1f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        set = new CandleDataSet(oneWeekList, "DataSet");

        set.setColor(Color.rgb(80, 80, 80));
        set.setShadowColor(getResources().getColor(R.color.black));
        set.setShadowWidth(1.2f);
        set.setDecreasingColor(getResources().getColor(R.color.red));
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(getResources().getColor(R.color.green));
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.LTGRAY);
        set.setDrawValues(false);

        CandleData data = new CandleData(set);
        chart.setData(data);
        chart.invalidate();

        // setting listeners of oneMonth and oneWeek buttons
        Button oneMonthButton = (Button) view.findViewById(R.id.oneMonthCandlesButton);
        oneMonthButton.setOnClickListener(view1 -> {
            // check if we are showing one month data, to avoid replacing exact same data again
            if (!isOneWeekShowed) return;
            isOneWeekShowed = false;

            set.setValues(oneMonthList);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
        Button oneWeekButton = (Button) view.findViewById(R.id.oneWeekCandlesButton);
        oneWeekButton.setOnClickListener(view1 -> {
            // check if we are showing one week data, to avoid replacing exact same data again
            if (isOneWeekShowed) return;
            isOneWeekShowed = true;

            set.setValues(oneWeekList);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        TextView symbolTextView = (TextView) view.findViewById(R.id.ohlcCryptoSymbol);
        symbolTextView.setText(symbol);

        builder.setView(view);
        builder.setNegativeButton("Hide", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return builder.create();
    }
}
