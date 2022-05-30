package com.sk.ultimateplayerhq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.MultiSelectModel;
import com.sk.ultimateplayerhq.models.SessionModel;

import java.util.List;

public class MultiSessionAdapter extends RecyclerView.Adapter<MultiSessionAdapter.ViewHolder> {
    private final OnSessionListener listener;
    private final Context context;
    private final List<MultiSelectModel<SessionModel>> list;

    public MultiSessionAdapter(Context context, List<MultiSelectModel<SessionModel>> list, OnSessionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.iv.setImageURI(list.get(position).getItem().getThumbnail_url());
        holder.tv.setText(list.get(position).getItem().getPost_title());
        holder.edt.setText(Html.fromHtml(list.get(position).getItem().getPost_content()));
        holder.edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(position).getItem().setPost_content(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tv;
         SimpleDraweeView iv;
         EditText edt;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
            edt = itemView.findViewById(R.id.edt);


            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSessionClick(list.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface OnSessionListener {
        void onSessionClick(MultiSelectModel<SessionModel> model);
    }
}
