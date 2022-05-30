package com.sk.ultimateplayerhq.adapters;

import android.annotation.SuppressLint;
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

import com.google.android.material.button.MaterialButton;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.CategoryModel;
import com.sk.ultimateplayerhq.models.SubCategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Context context;
    private final List<CategoryModel> list;
    private final OnCategoryListener listener;

    public CategoryAdapter(Context context, List<CategoryModel> list, OnCategoryListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.iv_expand.setVisibility(list.get(position).getList().size() == 0 ? View.GONE : View.VISIBLE);
        holder.rv_sub_category.setVisibility(list.get(position).isVisible() ? View.VISIBLE : View.GONE);
        holder.rv_sub_category.setAdapter(new SubCategoryAdapter(context, list.get(position).getList(), new SubCategoryAdapter.OnSubCategoryListener() {
            @Override
            public void onSubCategoryClick(SubCategoryModel model) {
                Log.e("SUB_CLICK:::", model.getCategory_name());
                list.get(position).setVisible(!list.get(position).isVisible());
                notifyDataSetChanged();
                listener.onSubCategoryClick(model);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialButton tv_title;
        private final ImageView iv_expand;
        private final RecyclerView rv_sub_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_expand = itemView.findViewById(R.id.iv_expand);
            rv_sub_category = itemView.findViewById(R.id.rv_sub_category);
            rv_sub_category.setLayoutManager(new LinearLayoutManager(context));


            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (list.get(getAdapterPosition()).getList().size() > 0) {
                        list.get(getAdapterPosition()).setVisible(!list.get(getAdapterPosition()).isVisible());
                        notifyDataSetChanged();
                    } else {
                        listener.onCategoryClick(list.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
            iv_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_title.performClick();

                }
            })
            ;
        }
    }


    public interface OnCategoryListener {
        void onCategoryClick(CategoryModel model, int position);

        void onSubCategoryClick(SubCategoryModel model);
    }
}
