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
import com.sk.ultimateplayerhq.models.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final OnNotificationListener listener;
    private final Context context;
    private final List<CommentModel> list;

    public CommentAdapter(Context context, List<CommentModel> list, OnNotificationListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_date.setText(String.format("%s %s", list.get(position).getCommentAuthor(), list.get(position).getCommentDate()));
        holder.tv_comment.setText(list.get(position).getCommentContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_date;
        private final TextView tv_comment;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_comment = itemView.findViewById(R.id.tv_comment);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNotificationClick(list.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface OnNotificationListener {
        void onNotificationClick(CommentModel model);
    }
}
