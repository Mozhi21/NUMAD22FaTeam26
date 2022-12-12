package edu.northeastern.numad22fateam26.finalProject.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.protobuf.Value;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.ChatAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.ChatModel;
import edu.northeastern.numad22fateam26.finalProject.services.PushNotificationService;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseDatabase database;
    CircleImageView imageView;
    TextView name, status;
    EditText chatET;
    ImageView sendBtn;
    RecyclerView recyclerView;
    ImageButton backbtn;


    ChatAdapter adapter;
    List<ChatModel> list;


    String oppositeUID;
    String chatID;
    String receiverToken;
    String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        loadUserData();

        loadMessages();

        maybeMarkRead();

        readReceiverToken();

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

            PushNotificationService.sendNotification(this, receiverToken, sender,message);
            reference.document(chatID).collection("Messages").document(messageID).set(messageMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            chatET.setText("");

                        } else {
                            Toast.makeText(ChatActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    });

//                recyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
//                    }
//                }, 1000);

        });

        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    void init() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        oppositeUID = getIntent().getStringExtra("uid");


        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        status = findViewById(R.id.statusTV);
        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.sendBtn);
        backbtn = findViewById(R.id.backBtn);

        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapter = new ChatAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    void readReceiverToken() {
        DatabaseReference reference =  database.getReference().child("users").child(oppositeUID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    receiverToken = snapshot.child("FCMToken").getValue().toString();
                    Log.v("TAG", "....." + receiverToken);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                    String profileImage = value.getString("profileImage");

                    if (profileImage != null && profileImage.trim().length() > 0) {
                        Glide.with(getApplicationContext()).load(profileImage).into(imageView);
                    }


                    name.setText(value.getString("name"));


                });

        firestore.collection("Users").document(user.getUid())
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;

                    if (value == null)
                        return;


                    if (!value.exists())
                        return;

                    sender = value.getString("name");


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
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

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