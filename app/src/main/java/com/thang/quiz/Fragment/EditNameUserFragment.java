package com.thang.quiz.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.thang.quiz.Activity.HomeActivity;
import com.thang.quiz.R;

public class EditNameUserFragment extends DialogFragment implements View.OnClickListener{

    EditText edt_name;
    Button bt_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_name_user, container, false);
        edt_name = view.findViewById(R.id.edtName);
        bt_ok = view.findViewById(R.id.btFinalWriteName);
        bt_ok.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btFinalWriteName:{
                HomeActivity.putStringName(edt_name.getText().toString(),getContext());
                if(HomeActivity.txt_edtName!=null)
                {
                    HomeActivity.txt_edtName.setText(edt_name.getText());
                }
                this.dismiss();
            }break;
            default:{

            }
        }
    }
}