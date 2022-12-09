package edu.northeastern.numad22fateam26.finalProject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.ReplacerActivity;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<HomeModel> list;

    Activity context;
    OnPressed onPressed;

    private static int TYPE_LIKE = 0;
    private static int TYPE_FOLLOW = 1;
    private static int TYPE_FOR_YOU = 2;
    private FirebaseUser user;
    List<String> followingList;

    public HomeAdapter(List<HomeModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }


//    @NonNull
//    @Override
//    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
//        return new HomeHolder(view);
//    }


    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                followingList = (List<String>) document.get("following");
            }
        });



        if (list.get(position).getLikes().size() != 0) {
            return TYPE_LIKE;
        }

        if (list.get(position).getUid() != null ){
            return TYPE_FOLLOW;
        }

        return TYPE_FOR_YOU;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_LIKE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
            return new LikeViewHolder(view);
        } else if (viewType == TYPE_FOLLOW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
            return new FollowViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
            return new ForYouViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOLLOW) {

            FollowViewHolder followHolder = (FollowViewHolder) holder;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            followHolder.userNameTv.setText(list.get(position).getName());
            followHolder.timeTv.setText("" + list.get(position).getTimestamp());

            List<String> likeList = list.get(position).getLikes();

            int count = likeList.size();

            if (count == 0) {
                followHolder.likeCountTv.setText("0 Like");
            } else if (count == 1) {
                followHolder.likeCountTv.setText(count + " Like");
            } else {
                followHolder.likeCountTv.setText(count + " Likes");
            }

            //check if already like
            followHolder.likeCheckBox.setChecked(likeList.contains(user.getUid()));

            followHolder.descriptionTv.setText(list.get(position).getDescription());

            Random random = new Random();

            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getProfileImage())
                    .placeholder(R.drawable.ic_person)
                    .timeout(6500)
                    .into(followHolder.profileImage);

            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getImageUrl())
                    .placeholder(new ColorDrawable(color))
                    .timeout(7000)
                    .into(followHolder.imageView);

            followHolder.clickListener(position,
                    list.get(position).getId(),
                    list.get(position).getName(),
                    list.get(position).getUid(),
                    list.get(position).getLikes(),
                    list.get(position).getImageUrl()
            );
        } else if (getItemViewType(position) == TYPE_LIKE) {
            LikeViewHolder likeHolder = (LikeViewHolder) holder;


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            likeHolder.userNameTv.setText(list.get(position).getName());
            likeHolder.timeTv.setText("" + list.get(position).getTimestamp());

            List<String> likeList = list.get(position).getLikes();

            int count = likeList.size();

            if (count == 0) {
                likeHolder.likeCountTv.setText("0 Like");
            } else if (count == 1) {
                likeHolder.likeCountTv.setText(count + " Like");
            } else {
                likeHolder.likeCountTv.setText(count + " Likes");
            }

            //check if already like
            likeHolder.likeCheckBox.setChecked(likeList.contains(user.getUid()));

            likeHolder.descriptionTv.setText(list.get(position).getDescription());

            Random random = new Random();

            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getProfileImage())
                    .placeholder(R.drawable.ic_person)
                    .timeout(6500)
                    .into(likeHolder.profileImage);

            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getImageUrl())
                    .placeholder(new ColorDrawable(color))
                    .timeout(7000)
                    .into(likeHolder.imageView);

            likeHolder.clickListener(position,
                    list.get(position).getId(),
                    list.get(position).getName(),
                    list.get(position).getUid(),
                    list.get(position).getLikes(),
                    list.get(position).getImageUrl()
            );


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    public interface OnPressed {
        void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked);

        void setCommentCount(TextView textView);

    }

    class FollowViewHolder extends RecyclerView.ViewHolder {


        private final CircleImageView profileImage;
        private final TextView userNameTv;
        private final TextView timeTv;
        private final TextView likeCountTv;
        private final TextView descriptionTv;
        private final TextView commentTV;
        private final ImageView imageView;
        private final CheckBox likeCheckBox;
        private final ImageButton commentBtn;
        private final ImageButton shareBtn;


        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            userNameTv = itemView.findViewById(R.id.nameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            likeCheckBox = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            descriptionTv = itemView.findViewById(R.id.descTv);

            commentTV = itemView.findViewById(R.id.commentTV);


            onPressed.setCommentCount(commentTV);

        }

        public void clickListener(final int position, final String id, String name, final String uid, final List<String> likes, final String imageUrl) {

            commentBtn.setOnClickListener(v -> {

                Intent intent = new Intent(context, ReplacerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("isComment", true);

                context.startActivity(intent);

            });

            likeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> onPressed.onLiked(position, id, uid, likes, isChecked));

            shareBtn.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                intent.setType("text/*");
                context.startActivity(Intent.createChooser(intent, "Share link using..."));

            });


        }
    }

    class LikeViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView profileImage;
        private final TextView userNameTv;
        private final TextView timeTv;
        private final TextView likeCountTv;
        private final TextView descriptionTv;
        private final TextView commentTV;
        private final ImageView imageView;
        private final CheckBox likeCheckBox;
        private final ImageButton commentBtn;
        private final ImageButton shareBtn;

        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            userNameTv = itemView.findViewById(R.id.nameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            likeCheckBox = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            descriptionTv = itemView.findViewById(R.id.descTv);

            commentTV = itemView.findViewById(R.id.commentTV);


            onPressed.setCommentCount(commentTV);

        }

        public void clickListener(final int position, final String id, String name, final String uid, final List<String> likes, final String imageUrl) {

            commentBtn.setOnClickListener(v -> {

                Intent intent = new Intent(context, ReplacerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("isComment", true);

                context.startActivity(intent);

            });

            likeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> onPressed.onLiked(position, id, uid, likes, isChecked));

            shareBtn.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                intent.setType("text/*");
                context.startActivity(Intent.createChooser(intent, "Share link using..."));

            });

        }

    }

    class ForYouViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView profileImage;
        private final TextView userNameTv;
        private final TextView timeTv;
        private final TextView likeCountTv;
        private final TextView descriptionTv;
        private final TextView commentTV;
        private final ImageView imageView;
        private final CheckBox likeCheckBox;
        private final ImageButton commentBtn;
        private final ImageButton shareBtn;

        public ForYouViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            userNameTv = itemView.findViewById(R.id.nameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            likeCheckBox = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            descriptionTv = itemView.findViewById(R.id.descTv);

            commentTV = itemView.findViewById(R.id.commentTV);


            onPressed.setCommentCount(commentTV);

        }

        public void clickListener(final int position, final String id, String name, final String uid, final List<String> likes, final String imageUrl) {

            commentBtn.setOnClickListener(v -> {

                Intent intent = new Intent(context, ReplacerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("isComment", true);

                context.startActivity(intent);

            });

            likeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> onPressed.onLiked(position, id, uid, likes, isChecked));

            shareBtn.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                intent.setType("text/*");
                context.startActivity(Intent.createChooser(intent, "Share link using..."));

            });

        }
    }
}
