package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.CoachModel;

import java.util.List;

import in.appsaint.communication.Api;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.ViewHolder> {
    private final Context context;
    private final List<CoachModel> list;
    private final OnSquadListener listener;

    public CoachAdapter(Context context, List<CoachModel> list, OnSquadListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoachAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coach, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoachModel model = list.get(position);
        holder.tv_name.setText(model.getName());
        holder.tv_email.setText(model.getEmail());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name, tv_email;
        private final ImageButton ib_edit, ib_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            ib_edit = itemView.findViewById(R.id.ib_edit);
            ib_delete = itemView.findViewById(R.id.ib_delete);


            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(list.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            ib_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(list.get(getAdapterPosition()),getAdapterPosition());
                }
            });

        }
    }


    public interface OnSquadListener {
        void onEditClick(CoachModel model, int position);
        void onDeleteClick(CoachModel model, int position);

    }
}
