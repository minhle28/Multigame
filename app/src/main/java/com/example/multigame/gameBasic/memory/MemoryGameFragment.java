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
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.multigame.R;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.base.DialogInstruction;
import com.example.multigame.databinding.MemoryGameFragmentBinding;
import com.example.multigame.utils.Utils;


public class MemoryGameFragment extends BaseFragment<MemoryGameFragmentBinding> implements AdapterView.OnItemClickListener {
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
    protected int getLayoutID() {
        return R.layout.memory_game_fragment;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_memory_game);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_memory);
                dialogInstruction.show(getChildFragmentManager(), "instruction");
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
        adapter = new ImageAdapter(requireContext(), true);
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
        timerChronometer = binding.getRoot().findViewById(R.id.timer);
        timerChronometer.setBase(SystemClock.elapsedRealtime());
        timerChronometer.start();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void subscribeUi() {
        player = MediaPlayer.create(requireContext(), R.raw.audio_memory_game);
        player.setLooping(true);
        play_music = true;

        binding.setAction(this);
        initGame();
        binding.gameLayout.setOnItemClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        int tag = (int) card.getTag();
        if (tag != R.drawable.checkmark && activeCards.size() < 2) {
            activeCards.add((ImageView) card);
            indexes[activeCards.size() - 1] = position;
            animateCardFlip(card, gameArray[position]);
        }

        if (activeCards.size() == 2) {
            binding.gameLayout.setEnabled(false);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (activeCards.get(0).getTag().equals(activeCards.get(1).getTag())) {
                    handleMatchingCards();
                } else {
                    handleMismatchedCards();
                }
            }, 500);
        }
    }

    private void animateCardFlip(View card, int imageResource) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 180f);
        flip.setDuration(250);
        flip.start();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ((ImageView) card).setImageResource(imageResource);
            card.setTag(imageResource);
        }, 250);
    }

    private void handleMatchingCards() {
        pointCounter += 1;
        if (pointCounter >= 10) {
            handleGameCompletion();
        } else {
            for (int i = 0; i < activeCards.size(); i++) {
                binding.gameLayout.getAdapter().getView(indexes[i], null, binding.gameLayout).setTag(R.drawable.checkmark);
                checkMarkIndexes.add(indexes[i]);
                animateCardFlip(activeCards.get(i), R.drawable.checkmark);
            }
            activeCards.clear();
        }
        binding.gameLayout.setEnabled(true);
    }

    private void handleMismatchedCards() {
        for (ImageView card : activeCards) {
            ObjectAnimator flip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 180f);
            flip.setDuration(250);
            flip.start();
            card.setImageResource(R.drawable.placeholder);
            card.setTag(R.drawable.placeholder);
        }
        activeCards.clear();
        binding.gameLayout.setEnabled(true);
    }

    private void handleGameCompletion() {
        gameCompleted = true;
        timerChronometer.stop();
        long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
        mIsWon = true;
        binding.shuffleButton.setText(getString(R.string.memory_game_new_game));
        binding.shuffleButton.setVisibility(View.VISIBLE);
        Utils.showAlertDialog(requireContext(), getString(R.string.game2_name), getString(R.string.alert_win, time),
                R.drawable.congra, (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    public void shuffleGame() {
        if (mIsWon) {
            binding.shuffleButton.setVisibility(View.INVISIBLE);
            Utils.showConfirmDialog(requireContext(), "New game", "Do you want to start a new game?", R.drawable.newgame, (dialog, which) -> {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    ((MemoryGameActivity) getContext()).onSupportNavigateUp();
                } else {
                    gameCompleted = false;
                    mStartTime = 0;
                    initGame();
                }
            });
        } else {
            if (pointCounter > 0 && pointCounter < 10) {
                shuffleGameArray();
                ((ImageAdapter) binding.gameLayout.getAdapter()).updateAdapter(gameArray, checkMarkIndexes.size());
                resetCheckMarkIndexes();
            } else {
                if (pointCounter >= 10) {
                    Toast.makeText(requireContext(), "You Win!!", Toast.LENGTH_SHORT).show();
                    long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
                    mIsWon = true;
                    binding.shuffleButton.setText(getString(R.string.memory_game_new_game));
                    Utils.showAlertDialog(requireContext(), getString(R.string.game3_name), getString(R.string.alert_win, time));
                } else {
                    Toast.makeText(requireContext(), "Score 1 point to shuffle", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void shuffleGameArray() {
        Integer[] newArray = new Integer[gameArray.length];
        ArrayList<Integer> gameArrayList = new ArrayList<>(Arrays.asList(gameArray));

        for (int i = 0; i < checkMarkIndexes.size(); i++) {
            int checkMarkIndex = checkMarkIndexes.get(i);
            newArray[i] = gameArray[checkMarkIndex];
            gameArrayList.set(checkMarkIndex, -1);
        }

        int index = checkMarkIndexes.size();
        for (Integer j : gameArrayList) {
            if (j != -1) {
                newArray[index++] = j;
            }
        }

        gameArray = newArray;
    }

    private void resetCheckMarkIndexes() {
        for (int i = 0; i < pointCounter * 2; i++) {
            checkMarkIndexes.set(i, i);
        }
    }
}
