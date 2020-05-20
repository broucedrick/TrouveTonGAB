package com.example.trouvetongab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoadingDialog {

    private static final int SIGN_IN = 0;

    private Activity activity;
    private AlertDialog alertDialog;
    private GoogleApiClient googleApiClient;

    LoadingDialog(Activity mActivity){
        activity = mActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void startWarningDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.loadingfailed_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        TextView reessayer = view.findViewById(R.id.reload_bt);
        reessayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                activity.recreate();
            }
        });

    }
    void startWarningDialog_log(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.loadingfailed_dialog, null);
        builder.setView(view);

        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        TextView reessayer = view.findViewById(R.id.reload_bt);
        reessayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
             //   activity.recreate();
            }
        });

    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
