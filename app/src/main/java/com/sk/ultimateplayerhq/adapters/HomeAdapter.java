package com.sk.ultimateplayerhq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.HomeModel;
import com.sk.ultimateplayerhq.models.SessionModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final int WELCOME_TEXT = 1;
    private static final int SESSIONS = 2;
    private final Context context;
    private final List<HomeModel> list;
    private final OnHomeAdapterListener listener;

    public HomeAdapter(Context context, List<HomeModel> list, OnHomeAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == WELCOME_TEXT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_wecome_text, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_seeeions, parent, false);
        }
        assert view != null;
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder.getItemViewType() == SESSIONS) {
            holder.tv_session_title.setText(list.get(position).getTitle());
            holder.rv_sessions.setAdapter(new SessionAdapter(context, list.get(position).getList(), new SessionAdapter.OnSessionListener() {
                @Override
                public void onSessionClick(SessionModel model) {
                    listener.onSessionClick(model);
                }
            }));
        }else  if (holder.getItemViewType() == WELCOME_TEXT) {
         if(!SessionManager.getHomeBanner().isEmpty()){
             holder.iv_home_banner.setImageURI(SessionManager.getHomeBanner());
         }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return WELCOME_TEXT;
        } else {
            return SESSIONS;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  SimpleDraweeView iv_home_banner;
        private TextView tv_session_title;
        RecyclerView rv_sessions;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == SESSIONS) {
                tv_session_title = itemView.findViewById(R.id.tv_session_title);
                rv_sessions = itemView.findViewById(R.id.rv_sessions);
                rv_sessions.setLayoutManager(new GridLayoutManager(context, 2));

            }else if (viewType == WELCOME_TEXT) {
                iv_home_banner = itemView.findViewById(R.id.iv_home_banner);
            }
        }
    }


    public interface OnHomeAdapterListener {
        void onSessionClick(SessionModel model);
    }
}
