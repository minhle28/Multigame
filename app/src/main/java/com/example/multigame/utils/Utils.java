package com.example.multigame.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Utils {
    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String title, String message, int icon,DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setIcon(icon);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
        alertDialog.show();
    }

    public static void showConfirmDialog(Context context, String title, String message, int icon,DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setIcon(icon);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",listener);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",listener);
        alertDialog.show();
    }

    public static long millisecondToSecond(long milliSecond) {
        return milliSecond / 1000;
    }
}
