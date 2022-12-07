package edu.northeastern.numad22fateam26.finalProject.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.ExploreActivity;
import edu.northeastern.numad22fateam26.finalProject.adapter.ChatUsersAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.ChatUserModel;

public class ChatUsersActivity extends AppCompatActivity {

    ChatUsersAdapter adapter;
    List<ChatUserModel> list;
    FirebaseUser user;
    ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);

        init();

        fetchUserData();

        clickListener();

        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    void init() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        backbtn = findViewById(R.id.backBtn);


        list = new ArrayList<>();
        adapter = new ChatUsersAdapter(this, list);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();


    }


    void fetchUserData() {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", user.getUid())
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;

                    if (value == null)
                        return;



                    if (value.isEmpty())
                        return;


                    list.clear();
                    for (QueryDocumentSnapshot snapshot : value) {

                        if (snapshot.exists()) {
                            ChatUserModel model = snapshot.toObject(ChatUserModel.class);
                            list.add(model);
                        }

                    }

                    adapter.notifyDataSetChanged();

                });


    }


    void clickListener() {

        adapter.OnStartChat((position, uids, chatID) -> {

            String oppositeUID;
            if (!uids.get(0).equalsIgnoreCase(user.getUid())) {
                oppositeUID = uids.get(0);
            } else {
                oppositeUID = uids.get(1);
            }

            Intent intent = new Intent(ChatUsersActivity.this, ChatActivity.class);
            intent.putExtra("uid", oppositeUID);
            intent.putExtra("id", chatID);
            startActivity(intent);


        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatUsersActivity.this, ExploreActivity.class);
        startActivity(intent);
    }
}