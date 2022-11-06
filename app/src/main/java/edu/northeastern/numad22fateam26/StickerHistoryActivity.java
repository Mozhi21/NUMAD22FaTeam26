package edu.northeastern.numad22fateam26;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad22fateam26.model.Message;

public class StickerHistoryActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userTable;
    private DatabaseReference messageTable;
    private List<Message> readMessages;
    private List<Message> unreadMessages;

    private RecyclerView recyclerViewUnread;
    private RecyclerView recyclerViewRead;

    private static final String TAG = "StickerHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userTable = database.getReference("users");
        messageTable = database.getReference("messages");

        readMessages = new ArrayList<>();
        unreadMessages = new ArrayList<>();

        setContentView(R.layout.activity_sticker_history);
        recyclerViewUnread = findViewById(R.id.unreadRecycler);
        recyclerViewUnread.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRead = findViewById(R.id.readRecycler);
        recyclerViewRead.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMessageAdapter unreadMessageAdaptor = new RecyclerViewMessageAdapter(
                unreadMessages, StickerHistoryActivity.this);
        recyclerViewUnread.setAdapter(unreadMessageAdaptor);
        RecyclerViewMessageAdapter readMessageAdaptor = new RecyclerViewMessageAdapter(
                readMessages, StickerHistoryActivity.this);
        recyclerViewRead.setAdapter(readMessageAdaptor);

        setRecyclerViewMessage();
        setMessageRefresher();
    }

    private void setRecyclerViewMessage() {

        String userId = auth.getCurrentUser().getUid();
        Query query = messageTable.orderByChild("receiverId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot messageNode : snapshot.getChildren()) {
                        Message message = readMessageObject(messageNode.getValue());
                        if (message.isRead()) {
                            readMessages.add(message);
                            recyclerViewRead.getAdapter().notifyItemInserted(readMessages.size() - 1);
                        } else {
                            unreadMessages.add(message);
                            recyclerViewUnread.getAdapter().notifyItemInserted(unreadMessages.size() - 1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to pull messages from database " + error.getMessage());
            }
        });
    }

    private void setMessageRefresher() {
    }

    private String getDisplayName(String uid) {
        List<String> displayName = new ArrayList<>();
        userTable.child(uid).child("username").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        displayName.add(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        return displayName.isEmpty() ? "" : displayName.get(0);
    }

    private Message readMessageObject(Object messageObject) {
        Map<String, Object> messageMap = (HashMap<String, Object>) messageObject;
        String senderId = (String) messageMap.get("senderId");
        String receiverId = (String) messageMap.get("receiverId");
        String sentAt = (String) messageMap.get("sentAt");
        Boolean isRead = (Boolean) messageMap.get("isRead");
        String title = (String) messageMap.get("title");
        String detail = (String) messageMap.get("detail");
        String stickerId = (String) messageMap.get("stickerId");
        return new Message(senderId, receiverId, sentAt, Boolean.TRUE.equals(isRead), title, detail, stickerId);
    }

    public void back(View view) {
        startActivity(new Intent(StickerHistoryActivity.this, StickerActivity.class));
    }

}