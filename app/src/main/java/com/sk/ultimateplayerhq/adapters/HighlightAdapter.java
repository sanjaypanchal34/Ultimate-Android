package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.HighlightModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.ViewHolder> {
    private final Context context;
    private final List<HighlightModel> list;
    private final OnHighlightListener listener;

    public HighlightAdapter(Context context, List<HighlightModel> list, OnHighlightListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HighlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_highlight, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HighlightModel model = list.get(position);
        holder.iv_high.setImageURI(model.getType()==null?"":model.getType().equalsIgnoreCase("video") ? model.getV_thumbnail_url() : model.getUrl());
        holder.tv_name.setText(model.getName()==null?"":model.getName());
        holder.btn_view.setVisibility(SessionManager.isPlayerLogin()?View.GONE:View.VISIBLE);
        holder.tv_view.setVisibility(SessionManager.isPlayerLogin()?View.VISIBLE:View.GONE);
        holder.tv_view.setText(model.getType()==null?"":model.getType().equalsIgnoreCase("video") ?"Play":"View");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnHighlightListener {
        void onEditClick(HighlightModel model, int position);

        void onDeleteClick(HighlightModel model, int position);

        void onViewClick(HighlightModel model, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final ImageView ib_edit, ib_delete;
        private final SimpleDraweeView iv_high;
        private final ImageView btn_view;
        private final TextView tv_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_view = itemView.findViewById(R.id.tv_view);
            iv_high = itemView.findViewById(R.id.iv_high);
            tv_name = itemView.findViewById(R.id.tv_name);
            btn_view = itemView.findViewById(R.id.btn_view);
            ib_edit = itemView.findViewById(R.id.ib_edit);
            ib_delete = itemView.findViewById(R.id.ib_delete);
            ib_edit.setVisibility(SessionManager.isPlayerLogin() ? View.GONE : View.VISIBLE);
            ib_delete.setVisibility(SessionManager.isPlayerLogin() ? View.GONE : View.VISIBLE);

            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            ib_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewClick(list.get(getAdapterPosition()), getAdapterPosition());
                }
            }); tv_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewClick(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }
}
