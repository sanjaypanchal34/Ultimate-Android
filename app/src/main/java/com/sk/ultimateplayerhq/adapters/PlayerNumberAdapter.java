package com.sk.ultimateplayerhq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.EventModel;
import com.sk.ultimateplayerhq.utils.ColorUtils;

import java.util.List;

public class PlayerNumberAdapter extends RecyclerView.Adapter<PlayerNumberAdapter.ViewHolder> {
    private final Context context;
    private final List<EventModel.Player> list;
    private final OnSquadListener listener;
    private final int borderColor;

    public PlayerNumberAdapter(Context context, List<EventModel.Player> list, OnSquadListener listener,int borderColor) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.borderColor = borderColor;
    }

    @NonNull
    @Override
    public PlayerNumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_player, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel.Player model = list.get(position);
        holder.tv_jursey.setText(model.getJersey_number());
        holder.tv_jursey.setTextColor(model.getKit_color().toLowerCase().equalsIgnoreCase("white")?Color.BLACK:Color.WHITE);
        holder.tv_jursey.setBackgroundColor(ColorUtils.getColor(model.getKit_color()));

        if(borderColor == Color.RED){
            holder.card.setForeground(context.getResources().getDrawable(R.drawable.circle_b_red));
        }else if(borderColor == Color.GREEN){
            holder.card.setForeground(context.getResources().getDrawable(R.drawable.circle_b_green));
        }else if(borderColor == Color.YELLOW){
            holder.card.setForeground(context.getResources().getDrawable(R.drawable.circle_b_yellow));
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnSquadListener {
        void onClick(EventModel.Player model, int position);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView tv_jursey;
        private final CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_jursey = itemView.findViewById(R.id.tv_jursey);
            card = itemView.findViewById(R.id.card);

        }
    }
}
