package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fateam26.model.UserHelper;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout name, password, phone;
    Button signUp, backToLogIn;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.edtRealName);
        password = findViewById(R.id.edtPassword);

        signUp = findViewById(R.id.SignUpbtn);
        backToLogIn = findViewById(R.id.BackToSignInbtn);
        mAuth = FirebaseAuth.getInstance();



        //Save data in Firebase on signup btn
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //FirebaseUser user = mAuth.getCurrentUser();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all values
                String username = name.getEditText().getText().toString();
                String userpassword = password.getEditText().getText().toString();

                UserHelper helper = new UserHelper(username, userpassword);
                reference.child(username).setValue(helper);
            }
        });
    }

    private void signUpSucceed(View view) {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }

    public void toSignIn(View view){
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
    }
}
