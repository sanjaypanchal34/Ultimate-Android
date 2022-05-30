package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.TrainigSessionModel;

import java.util.List;

public class TrainingSessionAdapter extends RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder> {
    private final OnSessionListener listener;
    private final Context context;
    private final List<TrainigSessionModel> list;

    public TrainingSessionAdapter(Context context, List<TrainigSessionModel> list, OnSessionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_training_session_list, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_thumb.setImageURI(list.get(position).getSession_image());
        holder.tv_title.setText(list.get(position).getSession_title());
        holder.tv_desc.setText(Html.fromHtml(list.get(position).getSession_description()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnSessionListener {
        void onSessionClick(TrainigSessionModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_desc;
        private final SimpleDraweeView iv_thumb;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            iv_thumb = itemView.findViewById(R.id.iv_thumb);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);


            iv_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSessionClick(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
