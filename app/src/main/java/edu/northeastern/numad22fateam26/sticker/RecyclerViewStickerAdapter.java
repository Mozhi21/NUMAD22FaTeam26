package edu.northeastern.numad22fateam26.sticker;

import static java.lang.String.valueOf;

import android.content.Context;
import android.util.Log;
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
import edu.northeastern.numad22fateam26.model.Sticker;

public class RecyclerViewStickerAdapter extends RecyclerView.Adapter<RecyclerViewStickerAdapter.RecyclerViewHolder> {
    private static RecyclerViewStickerAdapter.ClickListener clickListener;
    private List<Sticker> stickers;
    private Context context;

    public RecyclerViewStickerAdapter(List<Sticker> stickers, Context context) {
        this.stickers = stickers;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewStickerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_sticker,
                viewGroup, false);
        return new RecyclerViewStickerAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewStickerAdapter.RecyclerViewHolder viewHolder, int i) {
        String id = stickers.get(i).getId();
        String count = stickers.get(i).getCount();

        int drawableResourceId = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        viewHolder.stickerImage.setImageResource(drawableResourceId);
        viewHolder.stickerCount.setText(count);
        Log.v("sticker id: ", valueOf(id));
    }


    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public void setOnItemClickListener(RecyclerViewStickerAdapter.ClickListener clickListener) {
        RecyclerViewStickerAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.stickerImage)
        ImageView stickerImage;
        @BindView(R.id.stickerCount)
        TextView stickerCount;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
            //Toast.makeText(v.getContext(), stickerCount.getText().toString(),Toast.LENGTH_SHORT).show();

//            Log.v("clicked sticker id: ", valueOf(id));
        }
    }
}
