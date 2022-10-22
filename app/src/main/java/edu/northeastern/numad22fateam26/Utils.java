package edu.northeastern.numad22fateam26;

import android.app.AlertDialog;
import android.content.Context;

import edu.northeastern.numad22fateam26.api.FoodApi;
import edu.northeastern.numad22fateam26.api.FoodClient;

public class Utils {

    public static FoodApi getApi() {
        return FoodClient.getFoodClient().create(FoodApi.class);
    }

    public static AlertDialog showDialogMessage(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        return alertDialog;
    }
}