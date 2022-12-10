package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.ExploreActivity;
import edu.northeastern.numad22fateam26.finalProject.OnBoardingActivity;
import edu.northeastern.numad22fateam26.finalProject.ReplacerActivity;


public class CreateAccountFragment extends Fragment {

    public static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private TextInputLayout nameEt, emailEt, passwordEt;
    private RelativeLayout progressBar;
    private Button signup_login_button, nextBtn;
    private ImageView signup_back_button;
    private FirebaseAuth auth;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        clickListener();

    }

    private void init(View view) {

        nameEt = view.findViewById(R.id.signup_username);
        emailEt = view.findViewById(R.id.signup_email);
        passwordEt = view.findViewById(R.id.signup_password);
        signup_login_button = view.findViewById(R.id.signup_login_button);
        nextBtn = view.findViewById(R.id.signup_next_button);
        progressBar = view.findViewById(R.id.progressBar);
        signup_back_button = view.findViewById(R.id.signup_back_button);

        auth = FirebaseAuth.getInstance();

    }

    private void clickListener() {

        signup_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplacerActivity) getActivity()).setFragment(new LoginFragment());
            }
        });

        signup_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReplacerActivity) getActivity()).setFragment(new LoginFragment());
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getEditText().getText().toString().trim();
                String email = emailEt.getEditText().getText().toString().trim();
                String password = passwordEt.getEditText().getText().toString().trim();

                if (name.isEmpty() || name.equals(" ")) {
                    nameEt.setError("Please input valid name");
                    return;
                }

                if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                    emailEt.setError("Please input valid email");
                    return;
                }

                if (password.isEmpty() || password.length() < 6) {
                    passwordEt.setError("Please input valid password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                createAccount(name, email, password);

            }
        });

    }

    private void createAccount(final String name, final String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();

                            String image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRu856ko1lmKpPLBtLBqdx5-aS0gnhVvakZwVH3_uoEPN-KQIp7OSa-1JlBUR99vQZ6YMQ&usqp=CAU";

                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                            request.setDisplayName(name);
                            request.setPhotoUri(Uri.parse(image));

                            user.updateProfile(request.build());

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Email verification link send", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            uploadUser(user, name, email);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            String exception = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + exception, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void uploadUser(FirebaseUser user, String name, String email) {

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("email", email);
        map.put("profileImage", " ");
        map.put("uid", user.getUid());
        map.put("status", " ");
        map.put("search", name.toLowerCase());

        map.put("followers", list);
        map.put("following", list1);


        FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            assert getActivity() != null;
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getActivity().getApplicationContext(), OnBoardingActivity.class));
                            getActivity().finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}