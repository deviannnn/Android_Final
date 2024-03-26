package vn.edu.tdtu.moneyintel.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
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
import vn.edu.tdtu.moneyintel.model.Transaction;

public class TransactionExpenseAdapter extends RecyclerView.Adapter<TransactionExpenseAdapter.TransactionExpenseViewHolder> {
    private final List<OverallTransaction> mDataset;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TransactionExpenseAdapter(List<OverallTransaction> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public TransactionExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new TransactionExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionExpenseViewHolder holder, int position) {
        OverallTransaction ot = mDataset.get(position);
        holder.name.setText(ot.getCategory().getName());
        holder.sub.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(ot.getTotalAmount()));
        holder.right.setText(getPercentTage(ot.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class TransactionExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView sub;
        public TextView right;

        public TransactionExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            sub = itemView.findViewById(R.id.item_sub);
            right = itemView.findViewById(R.id.item_right);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                onItemClickListener.onItemClick(view, position);
            }
        }
    }

    public String getPercentTage(int totalAmount) {
        int sum = 0;
        for (OverallTransaction i : mDataset) {
            sum += i.getTotalAmount();
        }
        int percentage = (totalAmount * 100) / sum;
        return percentage + "%";
    }
}
