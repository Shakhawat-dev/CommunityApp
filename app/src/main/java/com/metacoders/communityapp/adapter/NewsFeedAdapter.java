package com.metacoders.communityapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.utils.ConvertTime;
import com.metacoders.communityapp.utils.Constants;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> implements  Filterable {
    private Context ctx;
    // private List<Post_Model> newsfeedList;
    private List<Post_Model> mData;
    private List<Post_Model> mDataFiltered;
    private ItemClickListenter itemClickListenter;
    private  static  final  int BIG_ROW = 1 ;
    private  static  final  int SMALL_ROW = 0 ;

    public NewsFeedAdapter(Context ctx, List<Post_Model> mData, ItemClickListenter itemClickListenter ) {
        this.ctx = ctx;
        this.mData = mData;
        this.itemClickListenter = itemClickListenter;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null ;

        if(viewType == BIG_ROW)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_video_home ,  parent, false);
        }
        else if ( viewType == SMALL_ROW)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_video_mini ,  parent, false);
        }

        return new ViewHolder(view, itemClickListenter);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.ViewHolder holder, final int position) {


       // holder.itemView.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.item_animation_fall_down));

        Post_Model newsFeed = mData.get(position);
        //viewHolder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_fall_down)


        holder.title.setText(newsFeed.getTitle());
//        holder.price.setText("$" + newsFeed.getProduct_price().toString());
//        holder.description.setText(newsFeed.getDescription());
        holder.viewCount.setText(newsFeed.getHit() + "");
        holder.commentCount.setText("0");
        // convert time
        SimpleDateFormat df = new SimpleDateFormat(Constants.CREATED_AT_FORMAT);
        try {
            Date date = df.parse(newsFeed.getCreatedAt());
            holder.date.setText(ConvertTime.getTimeAgo(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.date.setText(newsFeed.getCreatedAt());
        }
                 String link;

            link = Constants.IMAGE_URL + newsFeed.getImageMid()+"";
            Glide.with(ctx)
                    .load(link)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(/*sizeMultiplier=*/ 0.25f)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);



        // setting details

        try{
            holder.desc.setText(newsFeed.getContent()+"");
        }
        catch (Exception e ){

        }


        if (newsFeed.getPostType().contains("video") )  {
            holder.playBtn.setVisibility(View.VISIBLE);
        }
        else if(newsFeed.getPostType().contains("audio")){
            holder.playBtn.setVisibility(View.VISIBLE);
        }
        else  holder.playBtn.setVisibility(View.GONE);

    }



    @Override
    public int getItemViewType(int position) {



        if( position == 0 )
        {

            return BIG_ROW;

        }


        else
        {
            return  SMALL_ROW ;
        }


    }


    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }



    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData;

                } else {
                    List<Post_Model> lstFiltered = new ArrayList<>();
                    for (Post_Model row : mData) {

                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mDataFiltered = (List<Post_Model>) results.values;
                notifyDataSetChanged();

            }
        };


    }


    public interface ItemClickListenter {
        void onItemClick(View view, int pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title ,desc;
        public ImageView image, playBtn;
        public TextView author, viewCount, date;
        public TextView commentCount;
        public CardView container;


        ItemClickListenter itemClickListenter;

        public ViewHolder(@NonNull View itemView, ItemClickListenter itemClickListenter) {
            super(itemView);

            itemView.setOnClickListener(this);
            ///  viewHolder.itemView.animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_fall_down)
            desc = itemView.findViewById(R.id.title_short_details) ;
            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.video_thumb);
            playBtn = itemView.findViewById(R.id.play_btn);
            author = itemView.findViewById(R.id.video_author);
            viewCount = itemView.findViewById(R.id.video_view);
            date = itemView.findViewById(R.id.video_date);
            container = itemView.findViewById(R.id.container);
            commentCount = itemView.findViewById(R.id.video_comment);
            this.itemClickListenter = itemClickListenter;

        }

        @Override
        public void onClick(View v) {


            itemClickListenter.onItemClick(v, getAdapterPosition());

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
