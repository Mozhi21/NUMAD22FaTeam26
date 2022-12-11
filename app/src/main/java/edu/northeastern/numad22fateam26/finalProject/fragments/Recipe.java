package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.RecipeModel;

public class Recipe extends Fragment {
    private static final String TAG = "Recommendation";
    private static final String ADMIN_ID = "YvXGXIeL8IXd8FJiPRJJPzWU2gF3";
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();

    private HomeAdapter postAdapter;
    private List<PostImageModel> adminPosts;
    private List<HomeModel> relevantPostList;
    private List<RecipeModel> recipeModelList;
    private List<String> recipeSteps;
    private TextView adminStory;
    private ImageView adminPic;
    private TextView adminRecipeSteps;
    private FirebaseUser user;
    private ArrayAdapter<String> recipeStepAdapter;

    public Recipe(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        initDailyPanel(view);
    }

    private void initDailyPanel(View view) {
        adminStory = view.findViewById(R.id.dish_description);
        adminRecipeSteps = view.findViewById(R.id.detailed_recipe);
        adminPic = view.findViewById(R.id.admin_pic);
        loadAdminPost();
    }

    public void loadAdminPost() {
        adminPosts = new ArrayList<>();
        CollectionReference postRef = FirebaseFirestore.getInstance().collection("Users")
                .document(ADMIN_ID).collection("Post Images");
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
                                    adminPosts.add(model);
                                }
                            }
                            if (!adminPosts.isEmpty()) {
                                setPostView(adminPosts);
                                loadAdminRecipe();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void loadAdminRecipe() {
        recipeModelList = new ArrayList<>();
        String adminPostId = adminPosts.get(0).getId();
        Query query = FirebaseFirestore.getInstance().collection("Users")
                .document(ADMIN_ID).collection("Recipe").whereEqualTo("postId", adminPostId);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                setRecipeView(recipeModelList.get(0));
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

    private void setRecipeView(RecipeModel adminRecipe) {
        List<String> steps = adminRecipe.getSteps();
        List<String> recipeSteps = new ArrayList<>();
        String recipeString;

        if (steps == null || steps.isEmpty()) {
            recipeString = "No recipe found!";
        } else {
            for (int i = 0; i < steps.size(); i++) {
                recipeSteps.add(String.format("%s. %s", i + 1, steps.get(i)));
            }
            recipeString = String.join(System.getProperty("line.separator"), recipeSteps);
        }
        adminRecipeSteps.setText(recipeString);
    }




}
