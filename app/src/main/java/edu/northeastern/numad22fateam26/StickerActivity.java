package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.northeastern.numad22fateam26.model.Message;
import edu.northeastern.numad22fateam26.model.Sticker;
import edu.northeastern.numad22fateam26.service.FCMSendService;
import edu.northeastern.numad22fateam26.model.User;

public class StickerActivity extends AppCompatActivity implements Dialog.DialogListener {

    private FirebaseAuth auth;
    private Spinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference userTable;
    private DatabaseReference messageTable;
    private ArrayList<User> users;
    private RecyclerView recyclerViewSticker;
    private Map<String, Sticker> idToSticker;
    private List<Sticker> stickers;

    private static final String TAG = "StickerActivity";
    private static final List<String> STICKER_IDS = List.of("b01", "c01", "g01", "l01", "m01", "s01", "x01", "y01", "z01");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        spinner = findViewById(R.id.spinner);
        recyclerViewSticker = findViewById(R.id.recyclerSticker);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userTable = database.getReference("users");
        messageTable = database.getReference("messages");

        setSpinner();
        setRecyclerViewSticker();
    }

    public void back(View view) {
        startActivity(new Intent(StickerActivity.this, MainActivity.class));
    }

    public void toFCMActivity(View view) {
        startActivity(new Intent(StickerActivity.this, FCMActivity.class));
    }

    private void setSpinner() {
        users = new ArrayList<>();
        UserListAdapter adapter = new UserListAdapter(StickerActivity.this, users);
        spinner.setAdapter(adapter);

        userTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    users.add(user);
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
        RecyclerViewStickerAdapter stickerAdapter = new RecyclerViewStickerAdapter(stickers, this);
        recyclerViewSticker.setAdapter(stickerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewSticker.setLayoutManager(layoutManager);
        recyclerViewSticker.setNestedScrollingEnabled(true);
        //recipeAdapter.notifyDataSetChanged();

        // read count from database
        DatabaseReference userRef = userTable.child(auth.getCurrentUser().getUid()).child("sticker_count");

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
            openDialog();
        });
    }

    @Override
    public void applyTexts(String title, String detail) {
        User selectedUser = (User) spinner.getSelectedItem();
        FCMSendService.sendNotification(this, selectedUser.getFCMToken(), title, detail);
        Message message = new Message(auth.getCurrentUser().getUid(), selectedUser.getUserId(),
                Instant.now().toString(), false, title, detail, "c01"); //TODO: using hard-coded sticker id for now
        storeMessage(message);
        Toast.makeText(this, "Sticker sent to user " + selectedUser.getUserName(), Toast.LENGTH_SHORT).show();
    }

    public void openDialog() {
        Dialog dialog = new Dialog();
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

    private void storeMessage(Message message) {
        DatabaseReference newMessageRef = messageTable.push();
        newMessageRef.setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(TAG, "Message was not saved because " + databaseError.getMessage(),
                            new Exception("Internal error when storing the message"));
                } else {
                    String messageId = newMessageRef.getKey();
                    Log.i(TAG, "Message " + messageId + " is saved successfully");
                }
            }
        });
    }
}