package com.sk.ultimateplayerhq.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.MultiSelectAdapter;

public class MultiSelectDialog extends BottomSheetDialogFragment {

    private RecyclerView rv_multi;
    private MultiSelectAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_multi_select, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.opt_done){
                    dismiss();
                }
                return false;
            }
        });
        rv_multi = view.findViewById(R.id.rv_multi);
        rv_multi.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (adapter != null) {
            rv_multi.setAdapter(adapter);
        }
    }


    public <T> void setAdapter(MultiSelectAdapter<T> adapter) {
        this.adapter = adapter;
    }
}
