package edu.northeastern.numad22fateam26.finalProject.fragments;

import static edu.northeastern.numad22fateam26.finalProject.ExploreActivity.SEARCH_CONTENT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.ExploreActivity;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.StoriesAdapter;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;


public class Home extends Fragment {
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
    HomeAdapter adapter;
    RecyclerView storiesRecyclerView;
    StoriesAdapter storiesAdapter;
    List<StoriesModel> storiesModelList;
    private RecyclerView recyclerView;
    private List<HomeModel> list;
    private FirebaseUser user;
    private FirebaseFirestore fireStore;
    Button followBtn, likeBtn, exploreBtn;
    ImageButton sendBtn;
    Activity activity;
    ImageView circle;
    private static final String TAG = "HOME FRAGMENT";

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = getActivity();

        init(view);

        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

        checkIfUnreadMessages();

        adapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(uid)
                        .collection("Post Images")
                        .document(id);

                if (likeList.contains(user.getUid())) {
                    likeList.remove(user.getUid()); // unlike
                } else {
                    likeList.add(user.getUid()); // like
                }

                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);

                reference.update(map);

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void setCommentCount(final TextView textView) {

                commentCount.observe((LifecycleOwner) activity, integer -> {

                    assert commentCount.getValue() != null;

                    if (commentCount.getValue() == 0) {
                        textView.setVisibility(View.GONE);
                    } else
                        textView.setVisibility(View.VISIBLE);

                    StringBuilder builder = new StringBuilder();
                    builder.append("See all")
                            .append(commentCount.getValue())
                            .append(" comments");

                    textView.setText(builder);
                    textView.setText("See all " + commentCount.getValue() + " comments");

                });

            }
        });

        sendBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
            startActivity(intent);
        });

        exploreBtn.setOnClickListener(v -> {
            exploreBtn.setPressed(true);
            exploreBtn.setBackgroundColor(getResources().getColor(com.marsad.stylishdialogs.R.color.main_orange_light_stroke_color));
            Intent intent = new Intent(getActivity(), ExploreActivity.class);
            intent.putExtra("search_content", 0);
            startActivity(intent);
        });

        likeBtn.setOnClickListener(v -> {
            likeBtn.setPressed(true);
            likeBtn.setBackgroundColor(getResources().getColor(com.marsad.stylishdialogs.R.color.main_orange_light_stroke_color));
            Intent intent = new Intent(getActivity(), ExploreActivity.class);
            intent.putExtra("search_content", 1);
            startActivity(intent);
        });

        followBtn.setOnClickListener(v -> {
            followBtn.setPressed(true);
            followBtn.setBackgroundColor(getResources().getColor(com.marsad.stylishdialogs.R.color.main_orange_light_stroke_color));
            Intent intent = new Intent(getActivity(), ExploreActivity.class);
            intent.putExtra("search_content", 2);
            startActivity(intent);
        });

    }

    private void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recyclerViewfp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        storiesRecyclerView = view.findViewById(R.id.storiesRecyclerView);
        storiesRecyclerView.setHasFixedSize(true);
        storiesRecyclerView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        storiesModelList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(storiesModelList, getActivity());
        storiesRecyclerView.setAdapter(storiesAdapter);

        circle = view.findViewById(R.id.circle);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        sendBtn = view.findViewById(R.id.sendBtn);
        followBtn = view.findViewById(R.id.followBtn);
        likeBtn = view.findViewById(R.id.likeBtn);
        exploreBtn = view.findViewById(R.id.exploreBtn);

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

            List<String> uidList = (List<String>) value.get("following");

            if (uidList == null || uidList.isEmpty())
                return;

            if (SEARCH_CONTENT == 0) { // Explore
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

                                    list.add(new HomeModel(
                                            model.getName(),
                                            model.getProfileImage(),
                                            model.getImageUrl(),
                                            model.getUid(),
                                            model.getDescription(),
                                            model.getId(),
                                            model.getTimestamp(),
                                            model.getLikes()));

                                    adapter.notifyDataSetChanged();

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

                            });

            } else if (SEARCH_CONTENT == 1) { // like
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

                                        adapter.notifyDataSetChanged();

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
            } else if (SEARCH_CONTENT == 2) { // follow
                fireStore
                        .collectionGroup("Post Images")
                        .whereIn("uid", uidList)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                                list.clear();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    HomeModel model = documentSnapshot.toObject(HomeModel.class);
                                    list.add(new HomeModel(
                                            model.getName(),
                                            model.getProfileImage(),
                                            model.getImageUrl(),
                                            model.getUid(),
                                            model.getDescription(),
                                            model.getId(),
                                            model.getTimestamp(),
                                            model.getLikes()));

                                    adapter.notifyDataSetChanged();

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
                        });
            }



            // todo: fetch stories
            loadStories(uidList);
        });

    }

    void loadStories(List<String> followingList) {

        Query query = FirebaseFirestore.getInstance().collection("Stories");
        query.whereIn("uid", followingList).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            storiesModelList.clear();
            storiesModelList.add(new StoriesModel("", "", "", "", ""));

            for (QueryDocumentSnapshot snapshot : value) {

                if (!value.isEmpty()) {
                    StoriesModel model = snapshot.toObject(StoriesModel.class);
                    storiesModelList.add(model);
                }

            }
            storiesAdapter.notifyDataSetChanged();

        });

    }

    private void checkIfUnreadMessages() {
        Query query = FirebaseFirestore.getInstance().collection("Messages");
        query.whereEqualTo("unread", true).whereEqualTo("lastMessageTo", user.getUid()).addSnapshotListener(((value, error) -> {
            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            if (value.isEmpty()) {
                circle.setVisibility(View.GONE);
            } else {
                circle.setVisibility(View.VISIBLE);
            }
        }));
    }

}
