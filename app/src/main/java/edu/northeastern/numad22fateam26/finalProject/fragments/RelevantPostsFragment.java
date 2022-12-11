package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;

public class RelevantPostsFragment extends Fragment {
    private static final String TAG = "RelevantPosts";
    private static final String ADMIN_ID = "KAG9Tsfv4rUUtura8CLInb22ckV2";
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();

    private HomeAdapter postAdapter;
    private List<HomeModel> relevantPostList;
    private FirebaseUser user;

    public RelevantPostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relevant_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        initRecommendations(view);
    }

    private void initRecommendations(View view) {
        relevantPostList = new ArrayList<>();
        RecyclerView relevantPostView = view.findViewById(R.id.relevant_posts);
        relevantPostView.setHasFixedSize(true);
        relevantPostView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new HomeAdapter(relevantPostList, getActivity());
        relevantPostView.setAdapter(postAdapter);

        postAdapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(uid).collection("Post Images").document(id);

                if (likeList.contains(user.getUid())) {
                    likeList.remove(user.getUid()); // unlike
                } else {
                    likeList.add(user.getUid()); // like
                }
                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);
                reference.update(map);
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

        List<PostImageModel> adminPosts = new ArrayList<>();
        CollectionReference postRef = FirebaseFirestore.getInstance().collection("Users").document(ADMIN_ID).collection("Post Images");
        postRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            adminPosts.add(document.toObject(PostImageModel.class));
                        }
                    }
                    if (!adminPosts.isEmpty()) {
                        loadRelevantPosts(adminPosts.get(0));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void loadRelevantPosts(PostImageModel adminPost) {
        FirebaseFirestore.getInstance().collectionGroup("Post Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            PostImageModel model = document.toObject(PostImageModel.class);
                            if (model.getId().equals(adminPost.getId())) {
                                continue;
                            }
                            relevantPostList.add(new HomeModel(
                                    model.getName(),
                                    model.getProfileImage(),
                                    model.getImageUrl(),
                                    model.getUid(),
                                    model.getDescription(),
                                    model.getId(),
                                    model.getTimestamp(),
                                    model.getLikes()));
                        }
                    }
                    if (!relevantPostList.isEmpty()) {
                        sortByRelevancy(adminPost.getDescription());
                        postAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void sortByRelevancy(String description) {
        List<String> adminKeywords = Arrays.asList(description.toLowerCase().split(" "));
        relevantPostList.sort((a, b) -> getRelevancy(adminKeywords, b) - getRelevancy(adminKeywords, a));
    }

    private int getRelevancy(List<String> adminKeywords, HomeModel post) {
        String[] target = post.getDescription().toLowerCase().split(" ");
        int matchingScore = 0;
        for (String needle : target) {
            if (adminKeywords.contains(needle)) {
                matchingScore++;
            }
        }
        matchingScore = matchingScore * 10 / target.length + post.getLikes().size();
        return matchingScore;
    }
}
