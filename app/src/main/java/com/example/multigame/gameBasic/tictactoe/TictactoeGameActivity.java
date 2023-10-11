package com.example.multigame.gameBasic.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.multigame.R;
import android.media.MediaPlayer;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.Window;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import com.example.multigame.base.DialogInstruction;

import com.example.multigame.gameBasic.tictactoe.controller.Common;
import com.google.android.material.card.MaterialCardView;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.databinding.ActivityTictactoeGameBinding;

public class TictactoeGameActivity extends BaseActivity<ActivityTictactoeGameBinding> {
    private MediaPlayer player;
    private boolean play_music;
    private Menu menuList;
    private MaterialCardView clickX, clickO, singlePlayerButton, duoPlayerButton;
    private String playerChoose;
    private Context context;
    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tictactoe_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_tictactoe_game);
    }

    @Override
    protected void subscribeUi() {
        player = MediaPlayer.create(this, R.raw.audio_tictactoe_game);
        player.setLooping(true);
        play_music = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuList = menu;
        getMenuInflater().inflate(R.menu.menu_tictactoe_game, menu);
        if (play_music) {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
        } else {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
        }
        return isHaveRightMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction.newInstance(R.layout.dialog_instruction_tictactoe).show(getSupportFragmentManager(),"tictactoe_instruction");
                return true;
            case R.id.action_sound:
                if (play_music) {
                    player.pause();
                    play_music = false;
                    menuList.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
                } else {
                    player.start();
                    play_music = true;
                    menuList.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (play_music)
            player.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (play_music)
            player.start();
    }

    //Functions for game
    private void init() {
        clickX = findViewById(R.id.click_x);
        clickO = findViewById(R.id.click_o);
        singlePlayerButton = findViewById(R.id.single_player_button);
        duoPlayerButton = findViewById(R.id.duo_player_button);
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe_game);
        init();

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(context, R.color.home_bg));

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == clickX) {
                    playerChoose = "x";
                    clickX.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_white_text)));
                    clickO.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.home_bg)));
                } else if (view == clickO) {
                    playerChoose = "o";
                    clickO.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_white_text)));
                    clickX.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.home_bg)));
                }
            }
        };

        clickX.setOnClickListener(clickListener);
        clickO.setOnClickListener(clickListener);

        View.OnClickListener playerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerChoose == null || playerChoose.equals("")) {
                    Common.showToast("Please choose 'X' OR 'O'", context);
                } else {
                    Intent intent = new Intent(TictactoeGameActivity.this, TictactoeSingleAndDuoFragment.class);
                    String singleOrDuo = (view == singlePlayerButton) ? "single" : "duo";
                    intent.putExtra("SingleOrDuo", singleOrDuo);
                    intent.putExtra("playerChoose", playerChoose);
                    startActivity(intent);
                }
            }
        };

        singlePlayerButton.setOnClickListener(playerButtonClickListener);
        duoPlayerButton.setOnClickListener(playerButtonClickListener);
    }
}