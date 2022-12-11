package edu.northeastern.numad22fateam26.finalProject.adapter;

import static edu.northeastern.numad22fateam26.finalProject.ViewStoryActivity.FILE_TYPE;
import static edu.northeastern.numad22fateam26.finalProject.ViewStoryActivity.VIDEO_URL_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.StoryAddActivity;
import edu.northeastern.numad22fateam26.finalProject.ViewStoryActivity;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> {

    List<StoriesModel> list;
    Activity context;

    public StoriesAdapter(List<StoriesModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_layout, parent, false);
        return new StoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesAdapter.StoriesHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position == 0) {

            Glide.with(context.getApplicationContext())
                    .load(context.getResources().getDrawable(R.drawable.ic_add_small))
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, StoryAddActivity.class));

                }
            });

        } else {

            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getUrl())
                    .timeout(6500)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(v -> {

                if (position == 0) {
                    //new story

                    Dexter.withContext(context)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                        context.startActivity(new Intent(context, StoryAddActivity.class));

                                    } else {
                                        Toast.makeText(context, "Please allow permission from settings.", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();

                } else {
                    //open story
                    Intent intent = new Intent(context, ViewStoryActivity.class);
                    intent.putExtra(VIDEO_URL_KEY, list.get(position).getUrl());
                    intent.putExtra(FILE_TYPE, list.get(position).getType());
                    context.startActivity(intent);

                }

            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StoriesHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;

        public StoriesHolder(@NonNull View itemView) {
            super(itemView);


            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);

        }
    }

}