package com.metacoders.communityapp.adapter.new_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.utils.AppPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 8/11/2021 
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.categoryViewmodel> {

    private final Context context;
    private List<CommentModel.comments> items;
    private ItemClickListener itemClickListener;

    public CommentAdapter(List<CommentModel.comments> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    categoryViewmodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false);
        return new categoryViewmodel(v);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewmodel holder, int position) {
        CommentModel.comments item = items.get(position);

        holder.replyTv.setOnClickListener(v -> itemClickListener.onItemClick(item));

        holder.name.setText(item.getUser().getName() + "");
        holder.content.setText(item.getComment() + "");


        Glide.with(context)
                .load(item.getUser().getImage() + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.avater);

        holder.replyList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ReplyAdapter adapter = new ReplyAdapter(item.getReply(), context);
        Log.d("TAG", "onBindViewHolder: " + item.getReply().size());
        holder.replyList.setAdapter(adapter);
        holder.replyList.setHasFixedSize(true);

        holder.commentTime.setText(AppPreferences.covertTime(item.getCreated_at()));

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(CommentModel.comments model);
    }

    class categoryViewmodel extends RecyclerView.ViewHolder {

        public TextView name, content, commentTime, replyTv;
        public MaterialCardView cardView;
        public ImageView avater;
        public RecyclerView replyList;


        categoryViewmodel(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.comment_text);
            content = itemView.findViewById(R.id.comment);
            commentTime = itemView.findViewById(R.id.comments_time);
            replyTv = itemView.findViewById(R.id.reply_text);
            avater = itemView.findViewById(R.id.comment_avatar);
            replyList = itemView.findViewById(R.id.replyList);

        }


    }


}