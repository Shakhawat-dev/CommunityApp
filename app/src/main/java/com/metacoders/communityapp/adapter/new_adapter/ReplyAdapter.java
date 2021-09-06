package com.metacoders.communityapp.adapter.new_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*** Created by Rahat Shovo on 8/11/2021 
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.replyviewHolder> {

    private final Context context;
    private List<CommentModel.Reply> items;
    private ItemClickListener itemClickListener;

    public ReplyAdapter(List<CommentModel.Reply> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public @NotNull
    replyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reply, parent, false);
        return new replyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull replyviewHolder holder, int position) {
        CommentModel.Reply item = items.get(position);

        holder.name.setText(item.getUser().getName() + "");
        holder.content.setText(item.getReply() + "");
        Glide.with(context)
                .load(item.getUser().getImage() + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.avater);


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

    class replyviewHolder extends RecyclerView.ViewHolder {

        public TextView name, content, commentTime, replyTv;
        public MaterialCardView cardView;
        public ImageView avater;

        replyviewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.comment_text);
            content = itemView.findViewById(R.id.comment);
            commentTime = itemView.findViewById(R.id.comments_time);
            avater = itemView.findViewById(R.id.comment_avatar);
        }


    }


}