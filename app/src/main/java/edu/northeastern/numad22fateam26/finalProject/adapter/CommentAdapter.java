package edu.northeastern.numad22fateam26.finalProject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.model.CommentModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    Context context;
    List<CommentModel> list;


    public CommentAdapter(Context context, List<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.comment_items, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getProfileImageUrl())
                .into(holder.profileImage);

        holder.nameTv.setText(list.get(position).getName());
        holder.commentTv.setText(list.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView nameTv, commentTv;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nameTv = itemView.findViewById(R.id.nameTV);
            commentTv = itemView.findViewById(R.id.commentTV);

        }
    }

}