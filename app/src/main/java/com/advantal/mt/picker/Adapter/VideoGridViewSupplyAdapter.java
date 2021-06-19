package com.advantal.mt.picker.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.mt.picker.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoGridViewSupplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Uri> mUri;
    private Context mContext;

    public VideoGridViewSupplyAdapter(Context mContext, ArrayList<Uri> mUri) {
        this.mUri = mUri;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.show_in_grid_layout, parent, false);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        final ItemViewHolder ItemViewHolder = (ItemViewHolder) viewHolder;
        ItemViewHolder.setIsRecyclable(false);

        ItemViewHolder.mVideoIcon.setVisibility(View.VISIBLE);

        Glide.with(mContext).load(mUri.get(position))
                .into(ItemViewHolder.mImageRecyclerView);

        ItemViewHolder.mDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri.size() != 0) {
                    mUri.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Item Removed from List", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUri.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageRecyclerView;
        CardView mDelImg, mVideoIcon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImageRecyclerView = itemView.findViewById(R.id.im_url);
            mDelImg = itemView.findViewById(R.id.im_delete_image);
            mVideoIcon = itemView.findViewById(R.id.iv_Video);
        }
    }
}