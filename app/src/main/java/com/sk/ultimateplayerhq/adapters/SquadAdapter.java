package com.sk.ultimateplayerhq.adapters;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.SquadModel;
import com.sk.ultimateplayerhq.utils.ColorUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;

import java.util.List;
import java.util.Locale;

import in.appsaint.communication.Api;

public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.ViewHolder> {
    private final Context context;
    private final List<SquadModel> list;
    private final OnSquadListener listener;

    public SquadAdapter(Context context, List<SquadModel> list, OnSquadListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SquadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_football_team, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SquadModel model = list.get(position);
        holder.iv_profile.setImageURI(String.format("%s/%s", Api.C_HOST, model.getProfile_url()));
        holder.tv_appreance.setText(String.format("%s: %s", "Appearance", model.getAppearances() == null ? "" : model.getAppearances()));
        holder.tv_assists.setText(String.format("%s: %s", "Assist", model.getAssists() == null ? "" : model.getAssists()));
        holder.tv_goal.setText(String.format("%s: %s", "Goal", model.getGoals() == null ? "" : model.getGoals()));
        holder.tv_mom.setText(String.format("%s: %s", "MOM", model.getMom() == null ? "" : model.getMom()));
        holder.tv_name.setText(model.getName());
        holder.tv_num.setText(model.getJersey_number());
        holder.tv_num.setTextColor(((model.getKit_color() == null ? "" : model.getKit_color()).toLowerCase().equalsIgnoreCase("white"))?Color.BLACK:Color.WHITE);
        holder.tv_num.setBackgroundColor(ColorUtils.getColor(model.getKit_color() == null ? "" : model.getKit_color()));
        holder.tv_position.setText(model.getPosition());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnSquadListener {
        void onEditClick(SquadModel model, int position);

        void onDeleteClick(SquadModel model, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name, tv_num, tv_position;
        private final TextView tv_appreance, tv_assists, tv_goal, tv_mom;
        private final ImageButton ib_edit, ib_delete;
        private final SimpleDraweeView iv_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_appreance = itemView.findViewById(R.id.tv_appreance);
            tv_assists = itemView.findViewById(R.id.tv_assists);
            tv_goal = itemView.findViewById(R.id.tv_goal);
            tv_mom = itemView.findViewById(R.id.tv_mom);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_position = itemView.findViewById(R.id.tv_position);
            ib_edit = itemView.findViewById(R.id.ib_edit);
            ib_delete = itemView.findViewById(R.id.ib_delete);

            ib_delete.setVisibility(SessionManager.isPlayerLogin() ? GONE : View.VISIBLE);
            ib_edit.setVisibility(SessionManager.isPlayerLogin() ? GONE : View.VISIBLE);

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

        }
    }
}
