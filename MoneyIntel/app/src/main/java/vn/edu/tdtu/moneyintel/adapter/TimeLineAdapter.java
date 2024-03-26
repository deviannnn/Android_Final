package vn.edu.tdtu.moneyintel.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.edu.tdtu.moneyintel.R;
import vn.edu.tdtu.moneyintel.model.TimeLine;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<TimeLine> timeLineList;
    private OnItemClickListener onItemClickListener;
    public int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TimeLineAdapter(List<TimeLine> timeLineList) {
        this.timeLineList = timeLineList;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_timeline, parent, false);
        return new TimeLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        TimeLine timeLine = timeLineList.get(position);
        holder.tvtimeLine.setText(timeLine.getLabel());
        if (position == selectedPosition) {
            holder.tvtimeLine.setBackgroundColor(Color.parseColor("#3F51B5"));
            holder.tvtimeLine.setTextColor(Color.WHITE);
        } else {
            holder.tvtimeLine.setBackgroundColor(Color.TRANSPARENT);
            holder.tvtimeLine.setTextColor(Color.parseColor("#9a9a9a"));
        }
    }

    @Override
    public int getItemCount() {
        return timeLineList.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvtimeLine;

        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtimeLine = itemView.findViewById(R.id.item_timeline);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                onItemClickListener.onItemClick(view, position);
            }
        }
    }
}

