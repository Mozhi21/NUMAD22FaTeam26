package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad22fateam26.model.Sticker;
import edu.northeastern.numad22fateam26.service.FCMSendService;
import edu.northeastern.numad22fateam26.model.User;

public class StickActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Spinner spinner;
    DatabaseReference database;
    ArrayList<User> users;
    RecyclerView recyclerViewSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        spinner = findViewById(R.id.spinner);
        recyclerViewSticker = findViewById(R.id.recyclerSticker);

        auth = FirebaseAuth.getInstance();

        setSpinner();
        setRecyclerViewSticker();
    }

    public void sendC1(View view) {

    }

    public void back(View view){
        startActivity(new Intent(StickActivity.this, MainActivity.class));
    }

    public void toFCMActivity(View view){
        startActivity(new Intent(StickActivity.this, FCMActivity.class));
    }

    private void setSpinner() {
        database = FirebaseDatabase.getInstance().getReference("users");
        users = new ArrayList<>();
        UserListAdapter adapter = new UserListAdapter(StickActivity.this, users);
        spinner.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
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
        List<Sticker> stickers = List.of("b01", "c01", "g01","l01","m01","s01","x01","y01","z01").stream().map(name -> new Sticker(name, "5")).collect(Collectors.toList());
        RecyclerViewStickerAdapter stickerAdapter = new RecyclerViewStickerAdapter(stickers, this);
        recyclerViewSticker.setAdapter(stickerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewSticker.setLayoutManager(layoutManager);
        recyclerViewSticker.setNestedScrollingEnabled(true);
        //recipeAdapter.notifyDataSetChanged();

        stickerAdapter.setOnItemClickListener((view, position) ->  {
            User selectedUser = (User)spinner.getSelectedItem();
            FCMSendService.sendNotification(this, selectedUser.getFCMToken(), "from " + auth.getCurrentUser().getEmail(), "hey look this!!!!");
            Toast.makeText(this, "send to user:" + selectedUser.getUserName(), Toast.LENGTH_SHORT).show();
        });
    }

}