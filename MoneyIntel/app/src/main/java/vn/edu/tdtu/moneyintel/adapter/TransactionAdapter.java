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
import vn.edu.tdtu.moneyintel.model.Category;
import vn.edu.tdtu.moneyintel.model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final List<Transaction> transactionList;
    private boolean displayNote = false;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public TransactionAdapter(List<Transaction> transactionList, boolean displayNote) {
        this.transactionList = transactionList;
        this.displayNote = displayNote;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.name.setText(transaction.getCategory().getName());
        if (displayNote) {
            holder.sub.setText(transaction.getNote());
        }
        else {
            holder.sub.setText(UIFormatDate(transaction.getDate()));
        }
        holder.right.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(transaction.getAmount()));
        if (transaction.getCategory().getType() == 1) {
            holder.right.setTextColor(Color.parseColor("#1e81dc"));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView sub;
        public TextView right;

        public TransactionViewHolder(@NonNull View itemView) {
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

