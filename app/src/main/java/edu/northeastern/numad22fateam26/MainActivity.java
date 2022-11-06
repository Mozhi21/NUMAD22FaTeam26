package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private TextView userName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userWelcome);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference("users").child(user.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()){
                    userName.setText("Please Sign In!");
                }
                else {
                    userName.setText("Welcome Back " +String.valueOf(task.getResult().getValue()));
                }
            }
        });


    }

    public void toSearchActivity(View view){
        startActivity(new Intent(MainActivity.this, SpoonacularSearchActivity.class));
    }
    public void toSignInActivity(View view){
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
    }
    public void toAboutActivity(View view){
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }
}