package com.sk.ultimateplayerhq.utils.alert;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.sk.ultimateplayerhq.R;


public class MyAlert {

    public static void show(Context context, String msg, final MyAlertDialogListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNegativeClick();
                    }
                })
                .show();
    }

    public static void show(Context context, String msg,boolean isCancelable, final MyAlertDialogListener listener) {
        new AlertDialog.Builder(context)
                .setCancelable(isCancelable)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveClick();
                    }
                })
                .show();
    }

    public static void show(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null)
                .show();
    }


    public static void showRetry(Context context, String msg, final RetryAlertDialogListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onRetryClick();
                    }
                })
                .setNegativeButton(R.string.ok, null)
                .show();
    }
}
