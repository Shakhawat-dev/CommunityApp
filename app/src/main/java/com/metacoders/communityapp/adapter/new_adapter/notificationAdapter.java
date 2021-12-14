package com.metacoders.communityapp.adapter.new_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.newModels.NotificationData;
import com.metacoders.communityapp.utils.ConvertTime;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Rahat Shovo on 12/13/2021 
 */
public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.viewholder> {

    private final Context context;
    private List<NotificationData> items;
    private ItemClickListener itemClickListener;

    public notificationAdapter(List<NotificationData> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NotNull
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        NotificationData item = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });

        Glide.with(context)
                .load(item.getData().getImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);

        holder.name.setText(item.getData().getName());
        holder.sub_title.setText(item.getData().getAction());
        holder.desc.setText(item.getData().getTitle());
        holder.date.setText(ConvertTime.covertTimeToText(item.getCreated_at()));


    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView name, sub_title, desc, date;
        CircleImageView imageView;

        viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            sub_title = itemView.findViewById(R.id.subTitle);
            desc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.timeTv);
        }


    }

    public interface ItemClickListener {
        void onItemClick(NotificationData model);
    }


}