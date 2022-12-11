package edu.northeastern.numad22fateam26.finalProject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.ViewPagerAdapter;
import edu.northeastern.numad22fateam26.finalProject.fragments.CreateAccountFragment;
import edu.northeastern.numad22fateam26.finalProject.fragments.Notification;


public class ExploreActivity extends AppCompatActivity {

    public static String USER_ID;
    public static Integer SEARCH_CONTENT = 0;
    public static boolean IS_SEARCHED_USER = false;
    ViewPagerAdapter pagerAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button b1;
    private FirebaseMessaging mMessaging;
    private FirebaseDatabase mDatabase;
    private static final String TAG = "Replacer Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("search_content")) {
            SEARCH_CONTENT = extras.getInt("search_content");
        }

        init();

        refreshToken();

        addTabs();

        setViewPagerInitItem(savedInstanceState);


    }

    private void init() {

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        mMessaging = FirebaseMessaging.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

    }

    private void refreshToken() {
        mMessaging.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;

                        }

                        String token = task.getResult();
                        DatabaseReference userRef = mDatabase.getReference("users").child(user.getUid());
                        userRef.child("FCMToken").setValue(token);
                    }
                });
    }

    private void addTabs() {

//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));

        List<Integer> drawableResList = new ArrayList<>();
        drawableResList.add(R.drawable.ic_home);
        drawableResList.add(R.drawable.ic_search);
        drawableResList.add(R.drawable.ic_add);
        drawableResList.add(R.drawable.ic_baseline_star_outline_24);
        drawableResList.add(R.drawable.ic_outline_person);

        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(drawableResList.get(i)));
        }

//        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String directory = preferences.getString(PREF_DIRECTORY, "");
//
//        Bitmap bitmap = loadProfileImage(directory);
//        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//
//        tabLayout.addTab(tabLayout.newTab().setIcon(drawable));


        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_add);
                        break;

                    case 3:
                        tab.setIcon(R.drawable.ic_baseline_star_24);
                        break;

                    case 4:
                        tab.setIcon(R.drawable.ic_outline_person);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_add);
                        break;

                    case 3:
                        tab.setIcon(R.drawable.ic_baseline_star_outline_24);
                        break;

                    case 4:
                        tab.setIcon(R.drawable.ic_outline_person);
                        break;


                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_add);
                        break;

                    case 3:

                        tab.setIcon(R.drawable.ic_baseline_star_24);
                        break;

                    case 4:
                        tab.setIcon(R.drawable.ic_outline_person);
                        break;
                }

            }
        });

    }

    private void setViewPagerInitItem(Bundle savedInstanceState) {
//        if (savedInstanceState != null && savedInstanceState.containsKey("init_view_pager_item")) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                newString= extras.getString("STRING_I_NEED");
//            }
//            int pos = savedInstanceState.
//            viewPager.setCurrentItem(pos);
//        }

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("init_view_pager_item")) {
            viewPager.setCurrentItem(extras.getInt("init_view_pager_item"));
        }
    }

    private Bitmap loadProfileImage(String directory) {

        try {
            File file = new File(directory, "profile.png");

            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void onChange(String uid) {
        USER_ID = uid;
        IS_SEARCHED_USER = true;
        viewPager.setCurrentItem(4);
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 4) {
            viewPager.setCurrentItem(0);
            IS_SEARCHED_USER = false;
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(true);
    }

    @Override
    protected void onPause() {
        updateStatus(false);
        super.onPause();
    }

    void updateStatus(boolean status) {

        Map<String, Object> map = new HashMap<>();
        map.put("online", status);

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getUid())
                .update(map);
    }

}