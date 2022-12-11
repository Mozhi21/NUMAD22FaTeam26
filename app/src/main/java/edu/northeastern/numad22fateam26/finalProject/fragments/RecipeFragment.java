package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.RecipeModel;

public class RecipeFragment extends Fragment {
    private static final String TAG = "Recipe";
    private static final String ADMIN_ID = "KAG9Tsfv4rUUtura8CLInb22ckV2";
    private List<RecipeModel> recipeModelList;
    private TextView adminRecipeSteps;
    private TextView adminRecipeIngredients;

    public RecipeFragment() {
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
        adminRecipeSteps = view.findViewById(R.id.detailed_recipe);
        adminRecipeIngredients = view.findViewById(R.id.dish_ingredients);
        loadAdminRecipe();
    }

    public void loadAdminRecipe() {
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
                        doLoadAdminRecipe(adminPosts.get(0).getId());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void doLoadAdminRecipe(String adminPostId) {
        recipeModelList = new ArrayList<>();
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

    private void setRecipeView(RecipeModel adminRecipe) {
        List<String> steps = adminRecipe.getSteps();
        List<String> recipeSteps = new ArrayList<>();
        String recipeString;
        List<String> ingredients = adminRecipe.getIngredients();
        List<String> recipeIngredients = new ArrayList<>();
        String ingredientString;

        if (steps == null || steps.isEmpty()) {
            recipeString = "No recipe found!";
            ingredientString = "No recipe found!";
        } else {
            for (int i = 0; i < steps.size(); i++) {
                recipeSteps.add(String.format("%s. %s", i + 1, steps.get(i)));

            }
            recipeString = String.join(System.getProperty("line.separator"), recipeSteps);
            recipeIngredients.addAll(ingredients);
            ingredientString = String.join(System.getProperty("line.separator"), recipeIngredients);

        }
        adminRecipeSteps.setText(recipeString);
        adminRecipeIngredients.setText(ingredientString);
    }
}
