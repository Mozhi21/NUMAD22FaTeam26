package edu.northeastern.numad22fateam26.sticker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.northeastern.numad22fateam26.MainActivity;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.model.Notification;
import edu.northeastern.numad22fateam26.model.Sticker;
import edu.northeastern.numad22fateam26.model.User;
import edu.northeastern.numad22fateam26.sticker.service.FCMSendService;

public class StickerActivity extends AppCompatActivity implements Dialog.DialogListener {

    private static final List<String> STICKER_IDS = List.of("b01", "c01", "g01", "l01", "m01", "s01", "x01", "y01", "z01");
    private FirebaseAuth auth;
    private Spinner spinner;
    private DatabaseReference database;
    private String clickedStickerId;
    private ArrayList<User> users;
    private RecyclerView recyclerViewSticker;
    private Map<String, Sticker> idToSticker;
    private List<Sticker> stickers;
    private RecyclerViewStickerAdapter stickerAdapter;
    private TextView userNameDisplay;

    public static void displayUserName(TextView userNameDisplay) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference("users").child(user.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    userNameDisplay.setText("Please Sign In!");
                } else {
                    userNameDisplay.setText("Welcome Back, " + String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        userNameDisplay = findViewById(R.id.textViewUserNameStick);
        displayUserName(userNameDisplay);

        spinner = findViewById(R.id.spinner);
        recyclerViewSticker = findViewById(R.id.recyclerSticker);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        setSpinner();
        setRecyclerViewSticker();
    }

    @Override
    public void applyTexts(String title, String message, int position) {
        User selectedUser = (User) spinner.getSelectedItem();
        FCMSendService.sendNotification(this, selectedUser.getFCMToken(), title, message, clickedStickerId);

        // update sticker count(+1)
        String id = STICKER_IDS.get(position);
        Sticker sticker = idToSticker.get(id);
        sticker.increaseCount();
        // notify adapter
        stickerAdapter.notifyDataSetChanged();
        // update database also
        DatabaseReference userStickerCountRef = database.child(auth.getCurrentUser().getUid()).child("sticker_count").child(sticker.getId());
        userStickerCountRef.setValue(sticker.getCount());

        //record this notification to database
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        writeNotificationToDatabase(selectedUser.getUserId(), auth.getCurrentUser().getEmail(), timeStamp, id, message);

        Toast.makeText(this, "send to user:" + selectedUser.getUserName(), Toast.LENGTH_SHORT).show();
    }

    private void writeNotificationToDatabase(String receiverUid, String senderEmail, String date, String id, String message) {
        String senderName = senderEmail.substring(0, senderEmail.length() - "@group26.com".length());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications").child(receiverUid).push();
        String uuid = ref.getKey();
        Notification notification = new Notification(uuid, senderName, date, id, message);
        ref.setValue(notification);
    }

    public void back(View view) {
        startActivity(new Intent(StickerActivity.this, MainActivity.class));
    }

    public void openDialog(int position) {
        Dialog dialog = new Dialog(position);
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(StickerActivity.this, MainActivity.class));

    }

    public void toStickerHistoryActivity(View view) {
        startActivity(new Intent(StickerActivity.this, StickerHistoryActivity.class));
    }

    public void BackToHomeActivity(View view) {
        startActivity(new Intent(StickerActivity.this, MainActivity.class));
    }

    private void setSpinner() {
        users = new ArrayList<>();
        UserListAdapter adapter = new UserListAdapter(StickerActivity.this, users);
        spinner.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    users.add(user);
                    Collections.sort(users, Comparator.comparing(a -> a.getUserName().toLowerCase()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setRecyclerViewSticker() {
        // init sticker list
        idToSticker = new HashMap<>();
        for (String stickerId : STICKER_IDS) {
            idToSticker.put(stickerId, new Sticker(stickerId, "0"));
        }
        stickers = STICKER_IDS.stream().map(idToSticker::get).collect(Collectors.toList());

        // set stickers recycler view
        stickerAdapter = new RecyclerViewStickerAdapter(stickers, this);
        recyclerViewSticker.setAdapter(stickerAdapter);
        int orientation = this.getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 9,
                    GridLayoutManager.VERTICAL, false);
        } else { // vertical
            layoutManager = new GridLayoutManager(this, 3,
                    GridLayoutManager.VERTICAL, false);
        }
        recyclerViewSticker.setLayoutManager(layoutManager);
        recyclerViewSticker.setNestedScrollingEnabled(true);

        // read count from database
        DatabaseReference userRef = database.child(auth.getCurrentUser().getUid()).child("sticker_count");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.getKey();
                    String times = dataSnapshot.getValue().toString();
                    idToSticker.get(id).setCount(times);
                }
                stickerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // set click listener
        stickerAdapter.setOnItemClickListener((view, position) -> {
            clickedStickerId = stickers.get(position).getId();
            Log.v("sticker clicked: ", String.valueOf(stickers.get(position).getId()));
            openDialog(position);
        });
    }
}