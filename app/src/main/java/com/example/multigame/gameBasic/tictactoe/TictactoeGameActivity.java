package com.example.multigame.gameBasic.tictactoe;

import android.os.Bundle;

import com.example.multigame.R;

import android.media.MediaPlayer;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.example.multigame.base.DialogInstruction;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.databinding.ActivityTictactoeGameBinding;

public class TictactoeGameActivity extends BaseActivity<ActivityTictactoeGameBinding> {
    private MediaPlayer player;
    private boolean play_music;
    private Menu menuList;

    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected boolean isHaveBackMenu() {
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
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_tictactoe);
                dialogInstruction.show(getSupportFragmentManager(), "instruction");
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
    protected void subscribeUi() {
        player = MediaPlayer.create(this, R.raw.audio_tictactoe_game);
        player.setLooping(true);
        play_music = true;

        //binding.setAction(this);
    }

    @Override
    public void onBackPressed() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isHaveBackMenu()) {
            NavDestination currentDestination = Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination();
            String className = ((FragmentNavigator.Destination) currentDestination).getClassName();
            if (className.equals(TictactoeMenuFragment.class.getName())) {
                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);
            } else {
                Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
            }
        }
        return true;
    }
}