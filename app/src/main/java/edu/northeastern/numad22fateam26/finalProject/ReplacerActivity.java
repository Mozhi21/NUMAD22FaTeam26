package edu.northeastern.numad22fateam26.finalProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;


import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.fragments.Comment;
import edu.northeastern.numad22fateam26.finalProject.fragments.CreateAccountFragment;
import edu.northeastern.numad22fateam26.finalProject.fragments.LoginFragment;

public class ReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacer);

        frameLayout = findViewById(R.id.frameLayout);

        boolean isComment = getIntent().getBooleanExtra("isComment", false);

        setFragment(new LoginFragment());

    }

    public void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragment instanceof CreateAccountFragment) {
            fragmentTransaction.addToBackStack(null);
        }

        if (fragment instanceof Comment){

            String id = getIntent().getStringExtra("id");
            String uid = getIntent().getStringExtra("uid");

            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("uid", uid);
            fragment.setArguments(bundle);
        }

        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}