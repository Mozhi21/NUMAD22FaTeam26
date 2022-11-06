package edu.northeastern.numad22fateam26;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.northeastern.numad22fateam26.model.StickerHistory;

public class RecyclerViewStickerHistoryAdapter extends RecyclerView.Adapter<RecyclerViewStickerHistoryAdapter.RecyclerViewHolder> {
    Context context;
    List<StickerHistory> stickerHistoryList;
    private static RecyclerViewStickerHistoryAdapter.ClickListener clickListener;

    public RecyclerViewStickerHistoryAdapter(List<StickerHistory> stickerHistoryList, Context context) {
        this.stickerHistoryList = stickerHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewStickerHistoryAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_history_sticker_item,
                viewGroup, false);
        return new RecyclerViewStickerHistoryAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewStickerHistoryAdapter.RecyclerViewHolder viewHolder, int i) {
        String fromWhom = stickerHistoryList.get(i).getSenderName();
        String date = stickerHistoryList.get(i).getDate();
        String id = stickerHistoryList.get(i).getId();
        String message = stickerHistoryList.get(i).getMessage();

        int drawableResourceId = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        viewHolder.stickerImage.setImageResource(drawableResourceId);
        viewHolder.fromWhom.setText(fromWhom);
        viewHolder.sendDate.setText(date);
        viewHolder.description.setText(message);
    }

    @Override
    public int getItemCount() {
        return stickerHistoryList.size();
    }


    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView stickerImage;
        @BindView(R.id.fromWhom)
        TextView fromWhom;
        @BindView(R.id.sendDate)
        TextView sendDate;
        @BindView(R.id.description)
        TextView description;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
            //Toast.makeText(v.getContext(), stickerCount.getText().toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public void setOnItemClickListener(RecyclerViewStickerHistoryAdapter.ClickListener clickListener) {
        RecyclerViewStickerHistoryAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }
}
