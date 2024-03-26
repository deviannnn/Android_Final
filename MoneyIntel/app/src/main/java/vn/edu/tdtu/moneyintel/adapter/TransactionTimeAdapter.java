package vn.edu.tdtu.moneyintel.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.edu.tdtu.moneyintel.R;
import vn.edu.tdtu.moneyintel.model.OverallTransaction;

public class TransactionTimeAdapter extends RecyclerView.Adapter<TransactionTimeAdapter.ViewHolder> {
    private List<OverallTransaction> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_date;
        public TextView item_totalSpending;
        public RecyclerView item_spendingDetail;

        public ViewHolder(View v) {
            super(v);
            item_date = v.findViewById(R.id.item_date);
            item_totalSpending = v.findViewById(R.id.item_totalSpending);
            item_spendingDetail = v.findViewById(R.id.item_spendingDetail);
        }
    }

    public TransactionTimeAdapter(List<OverallTransaction> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public TransactionTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_spendingtime, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OverallTransaction ot = mDataset.get(position);
        holder.item_date.setText(UIFormatDate(ot.getDate()));
        holder.item_totalSpending.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(ot.getTotalAmount()));

        TransactionAdapter adapter_spendingDetail = new TransactionAdapter(ot.getTransactions(), true);
        holder.item_spendingDetail.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.item_spendingDetail.setAdapter(adapter_spendingDetail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String UIFormatDate(String DBFormat) {
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));

        Date date = null;
        try {
            date = currentFormat.parse(DBFormat);
        } catch (ParseException e) {
            date = Calendar.getInstance().getTime();
        }

        String UIFormat = newFormat.format(date);

        return UIFormat;
    }
}
