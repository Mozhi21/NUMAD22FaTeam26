package edu.northeastern.numad22fateam26.finalProject.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.ChatAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.ChatModel;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore firestore;
    CircleImageView imageView;
    TextView name, status;
    EditText chatET;
    ImageView sendBtn;
    RecyclerView recyclerView;

    ChatAdapter adapter;
    List<ChatModel> list;


    String oppositeUID;
    String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        loadUserData();

        loadMessages();

        maybeMarkRead();

        sendBtn.setOnClickListener(v -> {

            String message = chatET.getText().toString().trim();

            if (message.isEmpty()) {
                return;
            }

            CollectionReference reference = firestore.collection("Messages");


            Map<String, Object> map = new HashMap<>();

            map.put("lastMessage", message);
            map.put("lastMessageTo",  oppositeUID);
            map.put("time", FieldValue.serverTimestamp());
            map.put("unread", true);


            reference.document(chatID).update(map);

            String messageID = reference
                    .document(chatID)
                    .collection("Messages")
                    .document()
                    .getId();

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("id", messageID);
            messageMap.put("message", message);
            messageMap.put("senderID", user.getUid());
            messageMap.put("time", FieldValue.serverTimestamp());


            reference.document(chatID).collection("Messages").document(messageID).set(messageMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            chatET.setText("");
                        } else {
                            Toast.makeText(ChatActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }

    void init() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        oppositeUID = getIntent().getStringExtra("uid");

        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        status = findViewById(R.id.statusTV);
        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.sendBtn);

        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapter = new ChatAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    void loadUserData() {

        firestore.collection("Users").document(oppositeUID)
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;

                    if (value == null)
                        return;


                    if (!value.exists())
                        return;

                    //
                    boolean isOnline = Boolean.TRUE.equals(value.getBoolean("online"));
                    status.setText(isOnline ? "Online" : "Offline");

                    Glide.with(getApplicationContext()).load(value.getString("profileImage")).into(imageView);

                    name.setText(value.getString("name"));


                });

    }

    void loadMessages() {

        chatID = getIntent().getStringExtra("id");


        CollectionReference reference = firestore
                .collection("Messages")
                .document(chatID)
                .collection("Messages");

        reference
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {


                    if (error != null) return;

                    if (value == null || value.isEmpty()) return;

                    list.clear();

                    for (QueryDocumentSnapshot snapshot : value) {
                        ChatModel model = snapshot.toObject(ChatModel.class);
                        list.add(model);

                    }
                    adapter.notifyDataSetChanged();

                });

    }

    void maybeMarkRead() {
        chatID = getIntent().getStringExtra("id");

        DocumentReference reference = firestore
                .collection("Messages")
                .document(chatID);

        reference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() &&
                    documentSnapshot.contains("lastMessageTo") &&
                    documentSnapshot.contains("unread") &&
                    documentSnapshot.get("lastMessageTo").equals(user.getUid()) &&
                    documentSnapshot.getBoolean("unread")) {
                reference.update("unread", false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, ChatUsersActivity.class);
        startActivity(intent);
    }
}