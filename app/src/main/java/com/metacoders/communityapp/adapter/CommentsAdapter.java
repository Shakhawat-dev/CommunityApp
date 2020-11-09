package com.metacoders.communityapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.CommentModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CategoryViewHolder> {
    private Context ctx;
    private List<CommentModel.comments> commentsList;


    public CommentsAdapter(Context ctx, List<CommentModel.comments> newsfeedList) {
        this.ctx = ctx;
        this.commentsList = newsfeedList;
     //   this.itemClickListenter = itemClickListenter;
    }

    @NonNull
    @Override
    public CommentsAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CategoryViewHolder holder, final int position) {


        // single cateogry ;
        CommentModel.comments singleComment = commentsList.get(position);

        //set name
        holder.name.setText(singleComment.getName());
        holder.content.setText(singleComment.getComment());
//            holder.itemView.setBackgroundColor(Color.parseColor(singleCategory.getColor()));
  //      holder.cardView.setCardBackgroundColor(Color.parseColor(singleComment.getColor()));


    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

//    public interface ItemClickListenter {
//        void onItemClick(View view, int pos);
//    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name , content ;
        public MaterialCardView cardView;


       // ItemClickListenter itemClickListenter;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.comment_text);
            content = itemView.findViewById(R.id.comment);
           // cardView = itemView.findViewById(R.id.category_card);

          //  this.itemClickListenter = itemClickListenter;
        }

        @Override
        public void onClick(View v) {


           // itemClickListenter.onItemClick(v, getAdapterPosition());

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
