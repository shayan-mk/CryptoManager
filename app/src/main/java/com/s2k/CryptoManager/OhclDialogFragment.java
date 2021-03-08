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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class OhclDialogFragment extends AppCompatDialogFragment {
    private CandleDataSet set;
    private Boolean isOneWeekShowed;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        isOneWeekShowed = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_ohcl, null);

        // setting chart parameters
        CandleStickChart chart = (CandleStickChart) view.findViewById(R.id.ohclChart);
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

        // one week mock data
        ArrayList<CandleEntry> yValsCandleStick= new ArrayList<CandleEntry>();
        yValsCandleStick.add(new CandleEntry(0, 225.0f, 219.84f, 224.94f, 221.07f));
        yValsCandleStick.add(new CandleEntry(1, 228.35f, 222.57f, 223.52f, 226.41f));
        yValsCandleStick.add(new CandleEntry(2, 226.84f,  222.52f, 225.75f, 223.84f));
        yValsCandleStick.add(new CandleEntry(3, 222.95f, 217.27f, 222.15f, 217.88f));
        set = new CandleDataSet(yValsCandleStick, "DataSet");

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

            // one month mock data
            ArrayList<CandleEntry> array = new ArrayList<CandleEntry>();
            array.add(new CandleEntry(0, 225.0f, 219.84f, 224.94f, 221.07f));
            array.add(new CandleEntry(1, 228.35f, 222.57f, 223.52f, 226.41f));
            array.add(new CandleEntry(2, 226.84f,  222.52f, 225.75f, 223.84f));
            array.add(new CandleEntry(3, 222.95f, 217.27f, 222.15f, 217.88f));
            array.add(new CandleEntry(4, 225.0f, 219.84f, 224.94f, 221.07f));
            array.add(new CandleEntry(5, 228.35f, 222.57f, 223.52f, 226.41f));
            array.add(new CandleEntry(6, 226.84f,  222.52f, 225.75f, 223.84f));
            array.add(new CandleEntry(7, 222.95f, 217.27f, 222.15f, 217.88f));

            set.setValues(array);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
        Button oneWeekButton = (Button) view.findViewById(R.id.oneWeekCandlesButton);
        oneWeekButton.setOnClickListener(view1 -> {
            // check if we are showing one week data, to avoid replacing exact same data again
            if (isOneWeekShowed) return;
            isOneWeekShowed = true;

            // one week mock data
            ArrayList<CandleEntry> array = new ArrayList<CandleEntry>();
            array.add(new CandleEntry(0, 225.0f, 219.84f, 224.94f, 221.07f));
            array.add(new CandleEntry(1, 228.35f, 222.57f, 223.52f, 226.41f));
            array.add(new CandleEntry(2, 226.84f,  222.52f, 225.75f, 223.84f));
            array.add(new CandleEntry(3, 222.95f, 217.27f, 222.15f, 217.88f));

            set.setValues(array);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        TextView symbol = (TextView) view.findViewById(R.id.ohclCryptoSymbol);

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
