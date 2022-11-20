package edu.northeastern.numad22fateam26.sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.northeastern.numad22fateam26.model.User;

public class UserListAdapter extends ArrayAdapter<User> {

    Context context;
    ArrayList<User> users;

    public UserListAdapter(Context context, ArrayList<User> users) {
        super(context, android.R.layout.simple_spinner_dropdown_item, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(users.get(position).getUserName());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(users.get(position).getUserName());
        return textView;
    }


}
