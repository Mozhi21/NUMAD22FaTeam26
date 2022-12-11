package edu.northeastern.numad22fateam26.finalProject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    OnUserClicked onUserClicked;
    List<Users> list;
    Context context;

    public UserAdapter(List<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        context = parent.getContext();
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {

        if (list.get(position).getUid().equals(user.getUid())) {
            holder.layout.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.layout.setVisibility(View.VISIBLE);
        }

        holder.nameTV.setText(list.get(position).getName());
        holder.statusTV.setText(list.get(position).getStatus());

        if (list.get(position).isOnline()) {
            holder.onlineTV.setVisibility(View.VISIBLE);
        } else {
            holder.onlineTV.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);

        holder.itemView.setOnClickListener(v -> onUserClicked.onClicked(context, list.get(position).getUid()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnUserClicked(OnUserClicked onUserClicked) {
        this.onUserClicked = onUserClicked;
    }

    public interface OnUserClicked {
        void onClicked(Context context, String uid);
    }

    static class UserHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView nameTV, statusTV, onlineTV;
        RelativeLayout layout;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nameTV = itemView.findViewById(R.id.nameTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            onlineTV = itemView.findViewById(R.id.onlineTV);
            layout = itemView.findViewById(R.id.relativeLayout);

        }


    }

}