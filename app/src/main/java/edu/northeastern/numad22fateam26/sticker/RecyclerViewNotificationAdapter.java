package edu.northeastern.numad22fateam26.sticker;

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
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.model.Notification;

public class RecyclerViewNotificationAdapter extends RecyclerView.Adapter<RecyclerViewNotificationAdapter.RecyclerViewHolder> {
    Context context;
    List<Notification> notifications;
    private RecyclerViewNotificationAdapter.ClickListener clickListener;

    public RecyclerViewNotificationAdapter(List<Notification> stickerHistoryList, Context context) {
        this.notifications = stickerHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewNotificationAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_history_sticker_item,
                viewGroup, false);
        return new RecyclerViewNotificationAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewNotificationAdapter.RecyclerViewHolder viewHolder, int i) {
        String fromWhom = notifications.get(i).getSenderName();
        String date = notifications.get(i).getDate();
        String id = notifications.get(i).getId();
        String message = notifications.get(i).getMessage();

        int drawableResourceId = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        viewHolder.stickerImage.setImageResource(drawableResourceId);
        viewHolder.fromWhom.setText(fromWhom);
        viewHolder.sendDate.setText(date);
        viewHolder.description.setText(message);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setOnItemClickListener(RecyclerViewNotificationAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
}
