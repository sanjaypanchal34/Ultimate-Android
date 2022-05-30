package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.ChatModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int LEFT_MSG = 782;
    private static final int RIGHT_MSG = 120;
    private static final int LEFT_IMAGE = 554;
    private static final int RIGHT_IMAGE = 321;
    private final Context context;
    private final List<ChatModel> list;
    private final OnChatListener listener;

    public ChatAdapter(Context context, List<ChatModel> list, OnChatListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case LEFT_MSG: {
                layout = R.layout.item_chat_left;
            }
            break;

            case LEFT_IMAGE: {
                layout = R.layout.item_chat_left_image;
            }
            break;
            case RIGHT_MSG: {
                layout = R.layout.item_chat_right;
            }
            break;
            case RIGHT_IMAGE: {
                layout = R.layout.item_chat_right_image;
            }
            break;
            default:
                layout = R.layout.item_chat_left;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(layout, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel model = list.get(position);
        holder.tv_date.setText(model.getTime());
        holder.tv_user.setText(model.getUser_role().equals("player")?model.getJersey_number():"C");
        if (holder.getItemViewType() == LEFT_MSG || holder.getItemViewType() == RIGHT_MSG) {
            holder.tv_msg.setText(model.getMessage());
        } else if (holder.getItemViewType() == LEFT_IMAGE || holder.getItemViewType() == RIGHT_IMAGE) {
            holder.iv_msg.setImageURI(model.getType().equalsIgnoreCase("image")?model.getUrl(): model.getV_thumbnail_url());
            holder.iv_play.setVisibility(model.getType().equalsIgnoreCase("image")?View.GONE:View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
       if(list.get(position).getType()==null){
           return RIGHT_MSG;
       }else{
           if (list.get(position).getType().equalsIgnoreCase("string")) {
               return list.get(position).getUser_id() == SessionManager.getNewUserID() ? RIGHT_MSG : LEFT_MSG;
           } else {
               return list.get(position).getUser_id() == SessionManager.getNewUserID() ? RIGHT_IMAGE : LEFT_IMAGE;
           }
       }
    }

    public interface OnChatListener {
        void onViewClick(ChatModel model, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_date,tv_user;
        private TextView tv_msg;
        private SimpleDraweeView iv_msg;
        private ImageView iv_play;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            tv_user = itemView.findViewById(R.id.tv_user);
            tv_date = itemView.findViewById(R.id.tv_date);

            if (viewType == LEFT_MSG || viewType == RIGHT_MSG) {
                tv_msg = itemView.findViewById(R.id.tv_msg);
            } else if (viewType == LEFT_IMAGE || viewType == RIGHT_IMAGE) {
                iv_play = itemView.findViewById(R.id.iv_play);
                iv_msg = itemView.findViewById(R.id.iv_msg);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewClick(list.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }
}
