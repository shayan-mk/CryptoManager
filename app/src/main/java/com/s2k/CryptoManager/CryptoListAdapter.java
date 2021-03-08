package com.s2k.CryptoManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CryptoListAdapter extends RecyclerView.Adapter<CryptoListAdapter.ViewHolder> {
    private List<CryptoData> cryptoDataList;

    private Context context;
    private OnItemClickListener listener;
    public CryptoListAdapter(Context context, List<CryptoData> cryptoDataList, OnItemClickListener listener) {
        this.context = context;
        this.cryptoDataList = cryptoDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View cryptoView = inflater.inflate(R.layout.crypto_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(cryptoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CryptoData data = cryptoDataList.get(position);

        GlideApp.with(context)
                .load(data.iconUrl)
                .into(holder.icon);
        holder.name.setText(data.symbol + " | " + data.name);
        holder.oneHourChange.setText("1h: " + data.oneHourChange + "%");
        holder.oneDayChange.setText("1d: " + data.oneDayChange + "%");
        holder.oneWeekChange.setText("7d: " + data.oneWeekChange + "%");
        holder.price.setText(data.price + "$");

        holder.itemView.setOnClickListener(view -> listener.onItemClick(data.symbol));
    }

    @Override
    public int getItemCount() {
        return cryptoDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView icon;
        public TextView name;
        public TextView oneHourChange;
        public TextView oneDayChange;
        public TextView oneWeekChange;
        public TextView price;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.cryptoIcon);
            name = (TextView) itemView.findViewById(R.id.cryptoName);
            oneHourChange = (TextView) itemView.findViewById(R.id.crypto1HourChange);
            oneDayChange = (TextView) itemView.findViewById(R.id.crypto1DayChange);
            oneWeekChange = (TextView) itemView.findViewById(R.id.crypto1WeekChange);
            price = (TextView) itemView.findViewById((R.id.cryptoPrice));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String symbol);
    }
}
