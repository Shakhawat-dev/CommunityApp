package com.metacoders.communityapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.models.newModels.CategoryModel;
import com.metacoders.communityapp.utils.AppPreferences;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context ctx;
    private List<CategoryModel> categoryList;
    private ItemClickListenter itemClickListenter;

    public CategoryAdapter(Context ctx, List<CategoryModel> newsfeedList, ItemClickListenter itemClickListenter) {
        this.ctx = ctx;
        this.categoryList = newsfeedList;
        this.itemClickListenter = itemClickListenter;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);

        return new CategoryViewHolder(view, itemClickListenter);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, final int position) {


        // single cateogry ;
        CategoryModel singleCategory = categoryList.get(position);

        //set name
        holder.title.setText(singleCategory.getCategory_name());
        holder.cardView.setCardBackgroundColor(AppPreferences.getRandomMaterialColor("500", ctx));


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface ItemClickListenter {
        void onItemClick(View view, int pos);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public MaterialCardView cardView;


        ItemClickListenter itemClickListenter;

        public CategoryViewHolder(@NonNull View itemView, ItemClickListenter itemClickListenter) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.category_name_text);
            cardView = itemView.findViewById(R.id.category_card);

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
