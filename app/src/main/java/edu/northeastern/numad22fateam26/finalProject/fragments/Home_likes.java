package edu.northeastern.numad22fateam26.finalProject.fragments;

import static edu.northeastern.numad22fateam26.finalProject.ExploreActivity.SEARCH_CONTENT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.StoriesAdapter;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;


public class Home_likes extends Fragment {
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
    private RecyclerView recyclerView;
    private List<HomeModel> list;
    private FirebaseUser user;
    private FirebaseFirestore fireStore;
    Activity activity;
    private static final String TAG = "HOME FRAGMENT";
    private HomeAdapter postAdapter;


    public Home_likes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_explore, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = getActivity();

        init(view);

        loadDataFromFirestore();

    }

    private void init(View view) {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerExplore);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(postAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        postAdapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(uid).collection("Post Images").document(id);

                likeList.remove(user.getUid()); // unlike only
                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);
                reference.update(map).addOnSuccessListener(v -> postAdapter.notifyDataSetChanged());
            }

            @Override
            public void setCommentCount(final TextView textView) {
                commentCount.observe((LifecycleOwner) getActivity(), integer -> {
                    assert commentCount.getValue() != null;
                    if (commentCount.getValue() == 0) {
                        textView.setVisibility(View.GONE);
                    } else textView.setVisibility(View.VISIBLE);
                    StringBuilder builder = new StringBuilder();
                    builder.append("See all").append(commentCount.getValue()).append(" comments");
                    textView.setText(builder);
                    textView.setText("See all " + commentCount.getValue() + " comments");
                });
            }
        });

    }

    private void loadDataFromFirestore() {
        final DocumentReference reference = fireStore.collection("Users")
                .document(user.getUid());

        reference.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
                return;
            }

            if (value == null)
                return;


            fireStore
                    .collectionGroup("Post Images")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        list.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            HomeModel model = documentSnapshot.toObject(HomeModel.class);
                            if (model.getLikes().contains(user.getUid())) { // if user have not clicked like to a post, the post will not be added to view
                                list.add(new HomeModel(
                                        model.getName(),
                                        model.getProfileImage(),
                                        model.getImageUrl(),
                                        model.getUid(),
                                        model.getDescription(),
                                        model.getId(),
                                        model.getTimestamp(),
                                        model.getLikes()));

                                postAdapter.notifyDataSetChanged();

                                documentSnapshot.getReference().collection("Comments").get()
                                        .addOnCompleteListener(task -> {

                                            if (task.isSuccessful()) {

                                                Map<String, Object> map = new HashMap<>();
                                                for (QueryDocumentSnapshot commentSnapshot : task
                                                        .getResult()) {
                                                    map = commentSnapshot.getData();
                                                }

                                                commentCount.setValue(map.size());
                                            }

                                        });
                            }
                        }

                    });

        });
    }

}