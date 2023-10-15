package com.example.multigame.gameBasic.memory;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Chronometer;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.multigame.R;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.base.DialogInstruction;
import com.example.multigame.databinding.ActivityMemoryGameBinding;
import com.example.multigame.utils.Utils;

public class MemoryGameActivity extends BaseActivity<ActivityMemoryGameBinding> implements AdapterView.OnItemClickListener {
    private MediaPlayer player;
    private boolean play_music;
    private Menu menuList;
    private int pointCounter;
    public long totalTime = 181000;
    private boolean mIsWon;

    ArrayList<ImageView> activeCards;
    Integer[] gameArray;
    ImageAdapter adapter;
    int[] indexes;
    ArrayList<Integer> checkMarkIndexes;

    // Variable to save point for game
    long mStartTime = 0;
    private Chronometer timerChronometer;
    private long elapsedTime;
    private boolean gameCompleted;

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
        return R.layout.activity_memory_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_memory_game);
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
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_memory);
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

    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currentMillis = SystemClock.elapsedRealtime();
            elapsedTime = currentMillis - timerChronometer.getBase();
            timerHandler.postDelayed(this, 1000); // Update the timer every second

            if (gameCompleted) {
                timerChronometer.stop();
            }
        }
    };

    public void initGame() {
        adapter = new ImageAdapter(this, true);
        gameArray = adapter.getArray();
        binding.gameLayout.setAdapter(adapter);
        mIsWon = false;
        activeCards = new ArrayList<>();
        pointCounter = 0;
        indexes = new int[2];
        checkMarkIndexes = new ArrayList<>();
        for (int i = 0; i < binding.gameLayout.getAdapter().getCount(); i++) {
            binding.gameLayout.getAdapter().getView(i, null, binding.gameLayout).setTag(R.drawable.placeholder);
            ((ImageView) binding.gameLayout.getAdapter().getView(i, null, binding.gameLayout)).setImageResource(R.drawable.placeholder);
        }

        // Initialize the timer
        timerChronometer = findViewById(R.id.timer);
        timerChronometer.setBase(SystemClock.elapsedRealtime());
        timerChronometer.start();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void subscribeUi() {
        player = MediaPlayer.create(this, R.raw.audio_memory_game);
        player.setLooping(true);
        play_music = true;

        binding.setAction(this);
        initGame();
        binding.gameLayout.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mStartTime == 0) {
            mStartTime = System.currentTimeMillis();
        }
        checkGame(view, position);
    }

    public void checkGame(final View card, final int position) {
        if ((int) (((ImageView) card).getTag()) != R.drawable.checkmark && activeCards.size() < 1) {
            activeCards.add((ImageView) card);
            indexes[0] = position;
            ObjectAnimator flip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 180f);
            flip.setDuration(250);
            flip.start();
            new Handler(getMainLooper()).postDelayed((Runnable) () -> {
                ((ImageView) card).setImageResource(gameArray[position]);
                ((ImageView) card).setTag(gameArray[position]);
            }, 250);


        }
        // Player doesn't press same image twice
        else if ((int) (((ImageView) card).getTag()) != R.drawable.checkmark && !((ImageView) card).equals(activeCards.get(0))) {
            activeCards.add((ImageView) card);
            indexes[1] = position;
            ObjectAnimator flip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 180f);
            flip.setDuration(250);
            flip.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) card).setImageResource(gameArray[position]);
                    ((ImageView) card).setTag(gameArray[position]);
                }
            }, 250);
        }
        if (activeCards.size() >= 2) {
            binding.gameLayout.setEnabled(false);
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = activeCards.size();
                    if (!(activeCards.get(0).getTag().equals(activeCards.get(1).getTag()))) {
                        for (int i = 0; i < size; i++) {
                            ObjectAnimator flip = ObjectAnimator.ofFloat(activeCards.get(0), "rotationY", 0f, 180f);
                            flip.setDuration(250);
                            flip.start();
                            activeCards.get(0).setImageResource(R.drawable.placeholder);
                            activeCards.get(0).setTag(R.drawable.placeholder);
                            activeCards.remove(0);
                        }
                    }
                    // Player choose correct two cards
                    else if ((int) activeCards.get(0).getTag() != R.drawable.checkmark && (int) activeCards.get(1).getTag() != R.drawable.checkmark) {
                        pointCounter += 1;
                        if (pointCounter >= 10) {
                            gameCompleted = true;
                            timerChronometer.stop(); // Stop the timer

                            long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
                            //FirebaseHelper.getInstance().getUserDao().updateGamePoint(2, time);
                            mIsWon = true;
                            binding.shuffleButton.setText(getString(R.string.memory_game_new_game));
                            binding.shuffleButton.setVisibility(View.VISIBLE);

                            Utils.showAlertDialog(MemoryGameActivity.this, getString(R.string.game2_name), getString(R.string.alert_win, time),
                                    R.drawable.congra,
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                        } else {
                            // Do nothing
                        }
                        for (int i = 0; i < activeCards.size(); i++) {
                            binding.gameLayout.getAdapter().getView(indexes[i], null, binding.gameLayout).setTag(R.drawable.checkmark);
                            checkMarkIndexes.add(indexes[i]);
                            activeCards.get(i).setTag(R.drawable.checkmark);
                            ObjectAnimator flip = ObjectAnimator.ofFloat(activeCards.get(i), "rotationY", 180f, 0);
                            flip.setDuration(1);
                            flip.start();
                        }
                        activeCards.clear();
                    }
                    binding.gameLayout.setEnabled(true);

                }
            }, 500);
        }
    }

    public void shuffleGame() {
        if (mIsWon) {
            binding.shuffleButton.setVisibility(View.INVISIBLE);
            Utils.showConfirmDialog(this, "New game", "Do you want start new game?", R.drawable.newgame, (dialog, which) -> {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    onSupportNavigateUp();
                } else {
                    gameCompleted = false;
                    mStartTime = 0;
                    initGame();
                }
            });
        } else {
            int size = checkMarkIndexes.size();
            if (pointCounter > 0 && pointCounter < 10) {
                Integer[] newArray = new Integer[20];
                int index = 0;

                ArrayList<Integer> gameArrayList = new ArrayList<>(Arrays.asList(gameArray));

                for (Integer checkMarkIndex : checkMarkIndexes) {
                    newArray[index] = gameArray[checkMarkIndex];
                    gameArrayList.set(checkMarkIndex.intValue(), -1);
                    index++;
                }
                // Second pass through looking for unknown images
                for (Integer j : gameArrayList) {
                    if (j.intValue() != -1) {
                        newArray[index] = j;
                        index++;
                    }
                }
                gameArray = newArray;
                ((ImageAdapter) binding.gameLayout.getAdapter()).updateAdapter(newArray, size);
                for (int i = 0; i < (pointCounter * 2); i++) {
                    checkMarkIndexes.set(i, i);
                }
            } else {
                if (pointCounter >= 10) {
                    Toast.makeText(this, "You Win!!", Toast.LENGTH_SHORT).show();
                    long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
                    //FirebaseHelper.getInstance().getUserDao().updateGamePoint(2, time);
                    mIsWon = true;
                    binding.shuffleButton.setText(getString(R.string.memory_game_new_game));
                    Utils.showAlertDialog(MemoryGameActivity.this, getString(R.string.game3_name), getString(R.string.alert_win, time));
                } else {
                    Toast.makeText(this, "Score 1 point to shuffle", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
