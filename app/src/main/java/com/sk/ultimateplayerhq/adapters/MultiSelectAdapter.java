package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.MultiSelectModel;
import com.sk.ultimateplayerhq.models.PlayerModel;

import java.util.List;

public abstract class MultiSelectAdapter<T> extends RecyclerView.Adapter<MultiSelectAdapter<T>.ViewHolder> {
    private final Context context;
    private final List<MultiSelectModel<T>> list;

    public MultiSelectAdapter(Context context, List<MultiSelectModel<T>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(getItem(), parent, false);
        return new ViewHolder(view, viewType);
    }

    protected abstract int getItem();

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list.get(position).getItem() instanceof PlayerModel){
            holder.iv.setVisibility(View.GONE);
        }else{
            holder.iv.setVisibility(View.VISIBLE);
        }

        bind(holder,position);
    }

    protected abstract void bind(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnMultiListener<T> {
        void onMultiClick(MultiSelectModel<T> model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCheckBox cb;
        public final SimpleDraweeView iv;
        public final TextView tv;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMultiClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    protected abstract void onMultiClick(MultiSelectModel<T> tMultiSelectModel);



}
