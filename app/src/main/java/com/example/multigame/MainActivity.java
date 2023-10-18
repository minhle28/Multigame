package com.example.multigame;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.example.multigame.databinding.NavHeaderMainBinding;
import com.example.multigame.gameBasic.memory.MemoryGameActivity;
import com.google.android.material.navigation.NavigationView;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.databinding.ActivityMainBinding;
import com.example.multigame.service.BackgroundSoundService;
import com.example.multigame.gameBasic.tictactoe.TictactoeGameActivity;
import com.example.multigame.gameBasic.sudoku.SudokuGameActivity;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    Intent intentMusicBg;

    ActionBarDrawerToggle mDrawerToggle;

    private boolean play_music = true;

    Menu menu;

    NavHeaderMainBinding mHeader;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.actionbar_game_name);
    }

    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentMusicBg = new Intent(MainActivity.this, BackgroundSoundService.class);

        setSupportActionBar(binding.toolbar);
        setTitle(getActionBarTitle());
        centerTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupNavigationView();

    }

    private void setupNavigationView() {
        mDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_menu_game1: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickGame1();
                }, 500);
                break;
            }
            case R.id.nav_menu_game2: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickGame2();
                }, 500);
                break;
            }
            case R.id.nav_menu_game3: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickGame3();
                }, 500);
                break;
            }
            case R.id.nav_menu_game4: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickGame4();
                }, 500);
                break;
            }
            case R.id.nav_menu_game5: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickGame5();
                }, 500);
                break;
            }
            /*
            case R.id.nav_menu_top_players: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onClickTopPlayer();
                }, 500);
                break;
            }

            case R.id.nav_menu_settings: {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    openSetting();
                }, 500);
                break;
            }*/
        }
        // Close navigation drawer
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_app_main, menu);

        this.menu = menu;
        if (play_music) {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
        } else {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sound:
                if (play_music) {
                    play_music = false;
                    stopService(intentMusicBg);
                    menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
                } else {
                    play_music = true;
                    startService(intentMusicBg);
                    menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentMusicBg);
    }

    @Override
    protected void subscribeUi() {
        binding.setOnClick(this);
        // Set email in menu
        mHeader = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (play_music) {
            startService(intentMusicBg);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(intentMusicBg);
    }

    public void onClickGame1() {
        Intent tictactoeGame = new Intent(this, TictactoeGameActivity.class);
        startActivityWithAnimation(tictactoeGame);
    }

    public void onClickGame2() {
        Intent sudokuGame = new Intent(this, SudokuGameActivity.class);
        startActivityWithAnimation(sudokuGame);
    }

    public void onClickGame3() {
        Intent memoryGame = new Intent(this, MemoryGameActivity.class);
        startActivityWithAnimation(memoryGame);
    }

    public void onClickGame4() {
        Intent Game4 = new Intent(this, SudokuGameActivity.class);
        startActivityWithAnimation(Game4);
    }

    public void onClickGame5() {
        Intent Game5 = new Intent(this, SudokuGameActivity.class);
        startActivityWithAnimation(Game5);
    }
/*
    public void onClickTopPlayer() {
        Intent top = new Intent(this, TopPlayerActivity.class);
        startActivityWithAnimation(top);
    }

    public void openSetting() {
        startActivityWithAnimation(new Intent(this, SettingActivity.class));
    }*/
}
