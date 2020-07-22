package com.example.qartal.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.qartal.R;

public class LoadingDialoge {
    private Activity  activity;
    private AlertDialog alertDialog;
    public LoadingDialoge(Activity activity){
        this.activity = activity;
    }

    public void StartLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialoge_layout,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void DismissDialog(){
        if(!activity.isFinishing()){
            alertDialog.dismiss();
        }

    }
}
