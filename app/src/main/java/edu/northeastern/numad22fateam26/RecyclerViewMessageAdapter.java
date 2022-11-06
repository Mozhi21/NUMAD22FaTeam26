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
import edu.northeastern.numad22fateam26.model.Message;

public class RecyclerViewMessageAdapter extends RecyclerView.Adapter<RecyclerViewMessageAdapter.RecyclerViewHolder> {
    private List<Message> messages;
    private Context context;

    private static RecyclerViewRecipeAdapter.ClickListener clickListener;

    public RecyclerViewMessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewMessageAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_history_sticker_item,
                viewGroup, false);
        return new RecyclerViewMessageAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMessageAdapter.RecyclerViewHolder viewHolder, int position) {
        Message message = messages.get(position);
        String sentAt = message.getSentAt();
        String sender = message.getSenderId();
        String title = message.getTitle();
        String detail = message.getDetail();
        String stickerId = message.getStickerId();

        int drawableResourceId = context.getResources().getIdentifier(stickerId, "drawable", context.getPackageName());
        viewHolder.stickerImage.setImageResource(drawableResourceId);
        viewHolder.messageTitle.setText(title);
        viewHolder.messageDetail.setText(detail);
        viewHolder.messageSender.setText(sender); // TODO: get user name from user id
        viewHolder.sentAt.setText(sentAt);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.historySticker)
        ImageView stickerImage;
        @BindView(R.id.fromWhom)
        TextView messageSender;
        @BindView(R.id.messageTitle)
        TextView messageTitle;
        @BindView(R.id.messageDetail)
        TextView messageDetail;
        @BindView(R.id.sentAt)
        TextView sentAt;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
