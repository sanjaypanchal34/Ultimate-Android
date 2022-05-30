package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.ShopModel;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private final OnProductClick listener;
    private final Context context;
    private final List<ShopModel> list;

    public ShopAdapter(Context context, List<ShopModel> list, OnProductClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_thumb.setImageURI(list.get(position).getThumbnail_url());
        holder.tv_title.setText(list.get(position).getProduct_title());
        holder.tv_price.setText(String.format("£%s", list.get(position).getProduct_price()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView iv_thumb;
        private final TextView tv_title,tv_price;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            iv_thumb = itemView.findViewById(R.id.iv_thumb);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onProductClick(list.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface OnProductClick {
        void onProductClick(ShopModel model);
    }
}
