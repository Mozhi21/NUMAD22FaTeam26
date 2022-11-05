package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.numad22fateam26.service.FCMSendService;
import edu.northeastern.numad22fateam26.model.User;

public class StickActivity extends AppCompatActivity {

    Spinner spinner;
    DatabaseReference database;
    UserListAdapter myAdapter;
    ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        spinner = findViewById(R.id.spinner);
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

    public void sendC1(View view) {
        User selectedUser = (User)spinner.getSelectedItem();
        FCMSendService.sendNotification(this, selectedUser.getFCMToken(), "hello", "awesome!!!!");

        Toast.makeText(this, "send to user:" + selectedUser.getUserName(), Toast.LENGTH_SHORT).show();
    }


    public void back(View view){
        startActivity(new Intent(StickActivity.this, MainActivity.class));
    }

    public void toFCMActivity(View view){
        startActivity(new Intent(StickActivity.this, FCMActivity.class));
    }
}