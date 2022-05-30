package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.SubCategoryModel;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    private Context context;
    private List<SubCategoryModel> list;
    private OnSubCategoryListener listener;

    public SubCategoryAdapter(Context context, List<SubCategoryModel> list, OnSubCategoryListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_sub, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.ViewHolder holder, int position) {
        holder.tv_sub_category.setText(list.get(position).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialButton tv_sub_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sub_category = itemView.findViewById(R.id.tv_sub_category);

            tv_sub_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSubCategoryClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnSubCategoryListener {
        void onSubCategoryClick(SubCategoryModel model);
    }
}
