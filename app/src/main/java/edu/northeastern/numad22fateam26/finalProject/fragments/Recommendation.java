package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.RecipeModel;


public class Recommendation extends Fragment {

    private static final String TAG = "Recommendation";
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
    List<PostImageModel> postsModelList;
    List<RecipeModel> recipeModelList;
    List<PostImageModel> recommendedPostList;
    HomeAdapter adapter;
    private List<HomeModel> list;
    private RecyclerView recyclerView;
    private TextView adminStory;
    private ImageView adminPic;
    private FirebaseUser user;
    private ListView adminRecipeSteps;
    Activity activity;


    public Recommendation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        initAdminPost(view);
        initRecommendations(view);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        list = new ArrayList<>();

        adapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);


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
    }



    private void initAdminPost(View view) {
        adminStory = view.findViewById(R.id.dish_description);
        adminRecipeSteps = view.findViewById(R.id.detailed_recipe);


        adminPic = view.findViewById(R.id.admin_pic);

        postsModelList = new ArrayList<>();
        recipeModelList = new ArrayList<>();
        loadAdminPosts();
        loadAdminRecipe();
    }

    private void initRecommendations(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_rc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recommendedPostList = new ArrayList<>();
        loadRelevantPosts(adminStory.getText().toString());
        recommendedPostList.forEach(System.out::println);
    }

    public void loadAdminPosts() {
        CollectionReference postRef = FirebaseFirestore.getInstance().collection("Users")
                .document("YvXGXIeL8IXd8FJiPRJJPzWU2gF3").collection("Post Images");
        postRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    PostImageModel model = document.toObject(PostImageModel.class);
                                    postsModelList.add(model);
                                }
                            }
                            if (!postsModelList.isEmpty()) {
                                setPostView(postsModelList);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void loadAdminRecipe() {
        CollectionReference recipeRef = FirebaseFirestore.getInstance().collection("Users")
                .document("YvXGXIeL8IXd8FJiPRJJPzWU2gF3").collection("Recipe");
        recipeRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    RecipeModel model = document.toObject(RecipeModel.class);
                                    recipeModelList.add(model);
                                }
                            }
                            if (!recipeModelList.isEmpty()) {
                                setRecipeView(recipeModelList);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void setPostView(List<PostImageModel> postsModelList) {
        PostImageModel adminPost = postsModelList.get(0);
        adminStory.setText(adminPost.getDescription());
        Glide.with(this)
                .load(adminPost.getImageUrl())
                .timeout(6500)
                .into(adminPic);
    }

    private void setRecipeView(List<RecipeModel> recipeModelList) {
        RecipeModel adRecipe = recipeModelList.get(0);
        // set adminRecipeSteps
    }

    private void loadRelevantPosts(String description) {
        FirebaseFirestore.getInstance().collectionGroup("Post Images")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    PostImageModel model = document.toObject(PostImageModel.class);
                                    recommendedPostList.add(model);

                                    list.add(new HomeModel(model.getName(),
                                            model.getProfileImage(),
                                            model.getImageUrl(),
                                            model.getUid(),
                                            model.getDescription(),
                                            model.getId(),
                                            model.getTimestamp(),
                                            model.getLikes()));

                                }

                            }
                            if (!recommendedPostList.isEmpty()) {
                                sortByRelevancy(description, recommendedPostList);
                            }
                            for(PostImageModel model : recommendedPostList)
                                System.out.println(model.getDescription());
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }

                });
    }


    private void sortByRelevancy(String description, List<PostImageModel> posts) {
        List<String> adminKeywords = Arrays.asList(description.split(" "));
        posts.sort(Comparator.comparingLong(p -> getRelevancy(adminKeywords, p)));
    }

    private long getRelevancy(List<String> adminKeywords, PostImageModel post) {
        String[] target = post.getDescription().split(" ");
        return Arrays.stream(target).filter(adminKeywords::contains).count() * 10;
    }


}