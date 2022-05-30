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
import com.sk.ultimateplayerhq.models.SessionModel;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {
    private final OnSessionListener listener;
    private final Context context;
    private final List<SessionModel> list;

    public SessionAdapter(Context context, List<SessionModel> list, OnSessionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_session_list, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_thumb.setImageURI(list.get(position).getThumbnail_url());
        holder.tv_video_title.setText(list.get(position).getPost_title());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_video_title;
        private final SimpleDraweeView iv_thumb;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            iv_thumb = itemView.findViewById(R.id.iv_thumb);
            tv_video_title = itemView.findViewById(R.id.tv_video_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSessionClick(list.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface OnSessionListener {
        void onSessionClick(SessionModel model);
    }
}
