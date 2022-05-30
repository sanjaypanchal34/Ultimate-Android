package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.EventModel;
import com.sk.ultimateplayerhq.utils.ColorUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final Context context;
    private final List<EventModel> list;
    private final OnEventListener listener;

    public EventAdapter(Context context, List<EventModel> list, OnEventListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_trainig, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        EventModel model = list.get(position);
       if(model.getLogo_image()==null || model.getLogo_image().isEmpty()){
           holder.iv_event.setImageResource(model.getIs_training() == 1 ? R.drawable.trainig : R.drawable.match);
       }else{
           holder.iv_event.setImageURI(model.getLogo_image());
       }
        holder.tv_event_name.setText(model.getTitle());
        holder.tv_event_desc.setText(model.getDescription());
        SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date startDate = startFormat.parse(model.getStart());
            Date endDate = startFormat.parse(model.getEnd());
            assert startDate != null;
            assert endDate != null;
            holder.tv_time.setText(String.format("%s - %s", displayFormat.format(startDate), displayFormat.format(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_location.setText(model.getLocation());
        holder.tv_result.setText(model.getWon_lost().isEmpty()?"":model.getWon_lost().substring(0,1).toUpperCase());
        holder.card_result.setVisibility(model.getWon_lost().isEmpty()?View.GONE:View.VISIBLE);
        holder.card_result.setCardBackgroundColor(getColor(model.getWon_lost().isEmpty()?"":model.getWon_lost().substring(0,1).toUpperCase()));
        PlayerNumberAdapter adapter = listener.setPlayerAdapter(model, position);
        PlayerNumberAdapter accepted = listener.setAcceptedPlayerAdapter(model, position);
        PlayerNumberAdapter ejected = listener.setRejectedPlayerAdapter(model, position);
        holder.rv_player.setAdapter(adapter);
        holder.rv_accepted.setAdapter(accepted);
        holder.rv_rejected.setAdapter(ejected);
        holder.rv_player.setAdapter(adapter);
        holder.tv_pending.setVisibility(adapter.getItemCount()==0?View.GONE:View.VISIBLE);
        holder.tv_rejected.setVisibility(ejected.getItemCount()==0?View.GONE:View.VISIBLE);
        holder.tv_accepted.setVisibility(accepted.getItemCount()==0?View.GONE:View.VISIBLE);
    }

    private int getColor(String s) {
        if(s.equalsIgnoreCase("w")){
            return ColorUtils.getColor("green");
        }else  if(s.equalsIgnoreCase("l")){
            return ColorUtils.getColor("red");
        }else  if(s.equalsIgnoreCase("d")){
            return ColorUtils.getColor("grey");
        }else{
            return ColorUtils.getColor("white");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnEventListener {
        void onEventView(EventModel model, int position);

        void onEventDelete(EventModel model, int position);

        void onEventEdit(EventModel model, int position);
        PlayerNumberAdapter setPlayerAdapter(EventModel model, int position);
        PlayerNumberAdapter setAcceptedPlayerAdapter(EventModel model, int position);
        PlayerNumberAdapter setRejectedPlayerAdapter(EventModel model, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView iv_event;
        private final TextView tv_event_name, tv_event_desc, tv_time, tv_location;
        private final TextView btn_view;
        private final ImageView btn_delete;
        private final ImageView btn_edit;
        private final CardView card_result;
        private final TextView tv_result,tv_pending,tv_rejected,tv_accepted;
        private final RecyclerView rv_player,rv_rejected,rv_accepted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_player = itemView.findViewById(R.id.rv_player);
            rv_rejected = itemView.findViewById(R.id.rv_rejected);
            rv_accepted = itemView.findViewById(R.id.rv_accepted);
            rv_player.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rv_rejected.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rv_accepted.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            card_result = itemView.findViewById(R.id.card_result);
            tv_pending = itemView.findViewById(R.id.tv_pending);
            tv_rejected = itemView.findViewById(R.id.tv_rejected);
            tv_accepted = itemView.findViewById(R.id.tv_accepted);
            tv_result = itemView.findViewById(R.id.tv_result);
            iv_event = itemView.findViewById(R.id.iv_event);
            tv_event_name = itemView.findViewById(R.id.tv_event_name);
            tv_event_desc = itemView.findViewById(R.id.tv_event_desc);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_view = itemView.findViewById(R.id.btn_view);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete.setVisibility(SessionManager.isPlayerLogin() ? View.GONE : View.VISIBLE);
            btn_edit.setVisibility(SessionManager.isPlayerLogin() ? View.GONE : View.VISIBLE);

            btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEventView(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEventDelete(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEventEdit(list.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
