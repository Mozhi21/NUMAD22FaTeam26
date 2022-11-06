package edu.northeastern.numad22fateam26;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad22fateam26.model.Notification;
import edu.northeastern.numad22fateam26.model.Sticker;

public class StickerHistoryActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private RecyclerView unreadedRecyclerView;
    private RecyclerView readedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        unreadedRecyclerView = findViewById(R.id.unreadedRecycler);
        readedRecyclerView = findViewById(R.id.readedRecycler);

        setUnreadedNotifications();
        setReadedNotifications();
    }

    private void setUnreadedNotifications() {
        // init array and adapter
        List<Notification> notifications = new ArrayList<>();
        RecyclerViewNotificationAdapter notificationAdapter = new RecyclerViewNotificationAdapter(notifications, this);

        // read current user's unreaded notifications from database
        DatabaseReference userNotifications = database.getReference("notifications").child(auth.getCurrentUser().getUid());
        userNotifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if (!notification.isReviewed()) {
                        notifications.add(0, dataSnapshot.getValue(Notification.class));
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // set unread notifications recycler view
        unreadedRecyclerView.setAdapter(notificationAdapter);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.HORIZONTAL, false);
        unreadedRecyclerView.setLayoutManager(layoutManager);
        unreadedRecyclerView.setNestedScrollingEnabled(true);


        // set click listener
        notificationAdapter.setOnItemClickListener((view, position) ->  {
            Notification notification = notifications.get(position);
            notification.setReviewed(true);
            DatabaseReference ref = database.getReference("notifications").child(auth.getCurrentUser().getUid());
            ref.child(notification.getUuid()).setValue(notification);
        });
    }

    private void setReadedNotifications() {
        // init array and adapter
        List<Notification> notifications = new ArrayList<>();
        RecyclerViewNotificationAdapter notificationAdapter = new RecyclerViewNotificationAdapter(notifications, this);

        // read current user's readed notifications from database
        DatabaseReference userNotifications = database.getReference("notifications").child(auth.getCurrentUser().getUid());
        userNotifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if (notification.isReviewed()) {
                        notifications.add(0, dataSnapshot.getValue(Notification.class));
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // set readed notifications recycler view
        readedRecyclerView.setAdapter(notificationAdapter);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        readedRecyclerView.setLayoutManager(layoutManager);
        readedRecyclerView.setNestedScrollingEnabled(true);

        // set click listener
        notificationAdapter.setOnItemClickListener((view, position) ->  {
            Toast.makeText(this,
                    "Already read notification from " + notifications.get(position).getSenderName(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    public void back(View view) {
        finish();
    }

}