package com.example.multigame.gameBasic.tictactoe.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.multigame.R;
public class Common {
    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void alertBox(String singleOrDuo, String chooseMark, String winningPlayer, Context context) {
        if ("tie".equals(singleOrDuo)) {
            return;
        }

        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(context);
        View alertBoxView = LayoutInflater.from(context).inflate(R.layout.tictactoe_dialog_box, null);
        alertDialogue.setView(alertBoxView);

        TextView winningTv = alertBoxView.findViewById(R.id.win_or_lose_tv);
        AlertDialog alertBox = alertDialogue.create();
        alertBox.setCanceledOnTouchOutside(true);

        if ("single".equals(singleOrDuo)) {
            int winTextResId = (chooseMark.equals(winningPlayer)) ? R.string.you_win : R.string.you_lose;
            winningTv.setText(winTextResId);
        } else {
            int winTextResId = ("x".equals(winningPlayer)) ? R.string.x_win : R.string.o_win;
            winningTv.setText(winTextResId);
        }

        alertBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertBox.show();
    }
}
