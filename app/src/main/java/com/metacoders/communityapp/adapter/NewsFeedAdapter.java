package com.metacoders.communityapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.utils.Utils;


import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private Context ctx;
    private List<Post_Model> newsfeedList;
    private ItemClickListenter itemClickListenter;

    public NewsFeedAdapter(Context ctx, List<Post_Model> newsfeedList, ItemClickListenter itemClickListenter) {
        this.ctx = ctx;
        this.newsfeedList = newsfeedList;
        this.itemClickListenter = itemClickListenter;
    }

    @NonNull
    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_home, parent, false);

        return new ViewHolder(view, itemClickListenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.ViewHolder holder, final int position) {

        Post_Model newsFeed = newsfeedList.get(position);

        holder.title.setText(newsFeed.getTitle());
//        holder.price.setText("$" + newsFeed.getProduct_price().toString());
//        holder.description.setText(newsFeed.getDescription());

        Glide.with(ctx)
                .load(Utils.IMAGE_URL +newsFeed.getImageMid())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(/*sizeMultiplier=*/ 0.25f)
               // .placeholder(R.drawable.placeholder)
                .into(holder.image);

        if(newsFeed.getPostType().contains("video"))
        {
           // holder.price.setVisibility(View.VISIBLE);
        }
//        else  holder.price.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return newsfeedList.size();
    }

    public interface ItemClickListenter {
        void onItemClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public ImageView image , playBtn;
        public TextView description;
        public TextView price;

        ItemClickListenter itemClickListenter;

        public ViewHolder(@NonNull View itemView, ItemClickListenter itemClickListenter) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);
            playBtn = itemView.findViewById(R.id.play_btn);
            this.itemClickListenter = itemClickListenter;
        }

        @Override
        public void onClick(View v) {


           itemClickListenter.onItemClick( v, getAdapterPosition());

//            if (newsfeedList.get(position).getIs_product().equals("false")) {
//                Intent intent = new Intent(ctx, NewsDetailsActivity.class);
//                intent.putExtra("image", newsfeedList.get(position).getImage());
//                intent.putExtra("title", newsfeedList.get(position).getTitle());
//                intent.putExtra("desc" , newsfeedList.get(position).getDescription()) ;
//
//                ctx.startActivity(intent);
//            }
//            else {
//                Intent intent = new Intent(ctx, ProductDetailActivity.class);
//                intent.putExtra("isSingle" , true) ;
//                intent.putExtra("productID" ,newsfeedList.get(position).getProduct_id()) ;
//                ctx.startActivity(intent);
//            }


        }
    }
}
