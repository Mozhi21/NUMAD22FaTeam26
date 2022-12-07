package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.RecipeModel;


public class Recommendation extends Fragment {

    private static final String TAG = "Recommendation";
    List<PostImageModel> postsModelList;
    List<RecipeModel> recipeModelList;
    List<PostImageModel> recommendedPostList;
    private TextView adminStory;
    private ImageView adminPic;
    private ListView adminRecipeSteps;


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

        initAdminPost(view);
        initRecommendations(view);
    }


    private void initAdminPost(View view) {
        adminStory = view.findViewById(R.id.dish_description);
        adminRecipeSteps = view.findViewById(R.id.detailed_recipe);
        //TODO: create an adaptor class and initiate an instance here
        // adminRecipeSteps.setAdapter(adaptor);
        adminPic = view.findViewById(R.id.admin_pic);

        postsModelList = new ArrayList<>();
        recipeModelList = new ArrayList<>();
        loadAdminPosts();
        loadAdminRecipe();
    }

    private void initRecommendations(View view) {
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
                                }
                            }
                            if (!recommendedPostList.isEmpty()) {
                                sortByRelevancy(description, recommendedPostList);
                                //TODO: set RecyclerView data with recommendedPostList
                            }
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
        return Arrays.stream(target).filter(adminKeywords::contains).count() * 10
                + post.getLikes().size() * 5L;
    }
}