package edu.northeastern.numad22fateam26.finalProject.adapter;

import android.app.Activity;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.model.ChatUserModel;
import edu.northeastern.numad22fateam26.finalProject.utils.Common;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ChatUserHolder> {

    public OnStartChat startChat;
    Activity context;
    List<ChatUserModel> list;

    public ChatUsersAdapter(Activity context, List<ChatUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_items, parent, false);
        return new ChatUserHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {

        ChatUserModel chatUser = list.get(position);

        fetchImageUrl(chatUser.getUid(), holder);


        holder.time.setText(Common.calculateTime(chatUser.getTime()));

        holder.lastMessage.setText(chatUser.getLastMessage());

        holder.itemView.setOnClickListener(v ->
                startChat.clicked(position, chatUser.getUid(), chatUser.getId()));

        if (chatUser.isUnread() && chatUser.getLastMessageTo().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

    }


    void fetchImageUrl(List<String> uids, ChatUserHolder holder) {

        String oppositeUID;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (!uids.get(0).equalsIgnoreCase(user.getUid())) {
            oppositeUID = uids.get(0);
        } else {
            oppositeUID = uids.get(1);
        }

        FirebaseFirestore.getInstance().collection("Users").document(oppositeUID)
                .get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        DocumentSnapshot snapshot = task.getResult();
                        String profileImage = snapshot.getString("profileImage");
                        if (profileImage!=null && profileImage.trim().length() > 0) {
                            Glide.with(context.getApplicationContext()).load(snapshot.getString("profileImage")).into(holder.imageView);
                        }
                        holder.name.setText(snapshot.getString("name"));
                    } else {
                        assert task.getException() != null;
                        Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnStartChat(OnStartChat startChat) {
        this.startChat = startChat;
    }

    public interface OnStartChat {
        void clicked(int position, List<String> uids, String chatID);
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, lastMessage, time, count;


        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.nameTV);
            lastMessage = itemView.findViewById(R.id.messageTV);
            time = itemView.findViewById(R.id.timeTv);
            count = itemView.findViewById(R.id.messageCountTV);

            count.setVisibility(View.GONE);
        }
    }

}