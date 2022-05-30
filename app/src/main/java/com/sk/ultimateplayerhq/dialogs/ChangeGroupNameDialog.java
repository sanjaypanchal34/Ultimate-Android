package com.sk.ultimateplayerhq.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.fragments.BaseDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeGroupNameDialog extends BaseDialogFragment {
    private EditText edt_title;
    private MaterialButton btn_save;
    private ImageButton ib_close;
    private String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_change_name, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_title = view.findViewById(R.id.edt_title);
        btn_save = view.findViewById(R.id.btn_save);
        ib_close = view.findViewById(R.id.ib_close);

        edt_title.setText(title);

        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_title.getText().toString().isEmpty()){
                    edt_title.setError("Enter Name");
                    edt_title.requestFocus();
                }else{
                    listener.onChange(edt_title.getText().toString());
                    dismiss();
                }
            }
        });
    }

    private  OnNameListener  listener;

    public void setListener(OnNameListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }

    public void setTitle(String title) {
        this.title= title;
    }

    public interface OnNameListener{
        void onChange(String name);
    }
}
