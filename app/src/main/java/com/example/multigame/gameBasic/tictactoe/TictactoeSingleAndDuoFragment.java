package com.example.multigame.gameBasic.tictactoe;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.base.DialogInstruction;
import com.example.multigame.gameBasic.tictactoe.controller.Common;
import com.google.android.material.card.MaterialCardView;

import java.util.Random;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.multigame.databinding.TictactoeSingleAndDuoFragmentBinding;

public class TictactoeSingleAndDuoFragment extends BaseFragment<TictactoeSingleAndDuoFragmentBinding> {
    private MediaPlayer player;
    private boolean play_music;
    private Menu menuList;

    private Context context;
    private MaterialCardView[][] boxes = new MaterialCardView[3][3];
    private ImageView[][] boxImages = new ImageView[3][3];
    private TextView xScoreTv, oScoreTv, tiesTv;
    private ImageView turnImage;
    private boolean[][] boxMarked = new boolean[3][3];
    private String[][] boxValues = new String[3][3];
    private String singleOrDuo;
    private String choosedMark, markOfPlayer;
    private int markedCount, winningCountX, winningCountO, tiedCount;
    private boolean xIsYou;
    private boolean isPlayerTurn = true;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tictactoe_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_tictactoe_game);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction.newInstance(R.layout.dialog_instruction_tictactoe).show(getChildFragmentManager(), "tictactoe_instruction");
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
        player = MediaPlayer.create(requireContext(), R.raw.audio_tictactoe_game);
        player.setLooping(true);
        play_music = true;
    }

    //Functions for game
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TictactoeSingleAndDuoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        xScoreTv = binding.xScore;
        oScoreTv = binding.oScore;
        tiesTv = binding.tieScore;
        turnImage = binding.turnImg;

        Bundle args = getArguments();
        if (args != null) {
            singleOrDuo = args.getString("SingleOrDuo");
            choosedMark = args.getString("playerChoose");
        }

        // Store oringinal mark of player
        markOfPlayer = choosedMark;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String boxId = "box" + (i * 3 + j + 1);
                int resId = getResources().getIdentifier(boxId, "id", requireContext().getPackageName());
                boxes[i][j] = getView().findViewById(resId);
                boxImages[i][j] = getView().findViewById(getResources().getIdentifier(boxId + "_img", "id", requireContext().getPackageName()));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                boxes[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isPlayerTurn && !boxMarked[row][col]) {
                            if (singleOrDuo.equals("duo") && markedCount == 0) {
                                choosedMark = "x";  //in Duo, no matter player choose the mark, set X always goes first
                            }
                            boxImages[row][col].setVisibility(View.VISIBLE);
                            boxMarked[row][col] = true;
                            markedOption(row, col);
                            markedCount++;

                            if (markedCount >= 5) {
                                gameEnd(checkWhoIsWin(), false);
                            }

                            if (singleOrDuo.equals("single")) {
                                if (markOfPlayer.equals("x") && markedCount == 0) {
                                    choosedMark = markOfPlayer;  // to make sure the mark doesn't change
                                    isPlayerTurn = true;
                                } else {
                                    isPlayerTurn = false;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            randomCpu();
                                        }
                                    }, 1000);
                                }
                            }
                        } else if (!isPlayerTurn && !boxMarked[row][col]) {
                            Common.showToast("Bot's Turn", context);
                        } else {
                            Common.showToast("Already Marked", context);
                        }
                    }
                });
            }
        }

        if (singleOrDuo.equals("single")) {
            if (choosedMark.equals("o")) {
                isPlayerTurn = false;
                xIsYou = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        randomCpu();
                    }
                }, 1000);
            } else {
                xIsYou = true;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        getView().findViewById(R.id.reset_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame(false);
            }
        });
    }

    private void markedOption(int row, int col) {
        String value = choosedMark;
        boxValues[row][col] = value;
        boxImages[row][col].setImageDrawable(ContextCompat.getDrawable(context, value.equals("x") ? R.drawable.cross : R.drawable.o_letter));
        choosedMark = value.equals("x") ? "o" : "x";
        turnImage.setImageDrawable(ContextCompat.getDrawable(context, choosedMark.equals("x") ? R.drawable.cross : R.drawable.o_letter));
    }


    private void randomCpu() {
        if (choosedMark.equals("x") && markedCount == 0) {
            // If the bot is "X" and it's the first move, place it in the center.
            boxImages[1][1].setVisibility(View.VISIBLE);
            boxMarked[1][1] = true;
            markedOption(1, 1);
            markedCount++;

            if (markedCount >= 5) {
                gameEnd(checkWhoIsWin(), true);
            }
        } else {
            if (choosedMark.equals("o") && xIsYou == false) {
                choosedMark = "x";
            }
            // Check for winning moves and blocking moves
            boolean moveMade = false;

            // First, check for winning moves
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!boxMarked[i][j]) {
                        // Try placing the bot's mark in this empty box
                        boxValues[i][j] = choosedMark;

                        // Check if this move results in a win
                        if (checkWhoIsWin().equals(choosedMark)) {
                            // Make the winning move
                            boxImages[i][j].setVisibility(View.VISIBLE);
                            boxMarked[i][j] = true;
                            markedOption(i, j);
                            markedCount++;
                            moveMade = true;
                            if (markedCount >= 5) {
                                gameEnd(checkWhoIsWin(), true);
                            }
                            break;
                        }

                        // If not a winning move, undo it for the next check
                        boxValues[i][j] = "";
                    }
                }
                if (moveMade) break; // Break if a winning move is made
            }

            // If no winning moves, check for blocking moves
            if (!moveMade) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (!boxMarked[i][j]) {
                            // Try placing the player's mark in this empty box
                            boxValues[i][j] = (choosedMark.equals("x")) ? "o" : "x";

                            // Check if this move blocks the player from winning
                            if (checkWhoIsWin().equals(boxValues[i][j])) {
                                // Make the blocking move
                                boxImages[i][j].setVisibility(View.VISIBLE);
                                boxMarked[i][j] = true;
                                markedOption(i, j);
                                markedCount++;
                                moveMade = true;
                                if (markedCount >= 5) {
                                    gameEnd(checkWhoIsWin(), true);
                                }
                                break;
                            }

                            // If not a blocking move, undo it for the next check
                            boxValues[i][j] = "";
                        }
                    }
                    if (moveMade) break; // Break if a blocking move is made
                }
            }

            // If no winning or blocking moves, make a random move
            if (!moveMade) {
                while (true) {
                    int randomRow = new Random().nextInt(3);
                    int randomCol = new Random().nextInt(3);
                    if (!boxMarked[randomRow][randomCol]) {
                        boxImages[randomRow][randomCol].setVisibility(View.VISIBLE);
                        boxMarked[randomRow][randomCol] = true;
                        markedOption(randomRow, randomCol);
                        markedCount++;

                        if (markedCount >= 5) {
                            gameEnd(checkWhoIsWin(), true);
                        }
                        break;
                    }
                }
            }
        }

        // Unblock player clicks after the bot's move
        isPlayerTurn = true;
    }


    private String checkWhoIsWin() {
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (boxValues[i][0] != null && boxValues[i][0].equals(boxValues[i][1]) && boxValues[i][0].equals(boxValues[i][2])) {
                return boxValues[i][0];
            }

            // Check columns
            if (boxValues[0][i] != null && boxValues[0][i].equals(boxValues[1][i]) && boxValues[0][i].equals(boxValues[2][i])) {
                return boxValues[0][i];
            }
        }

        // Check diagonals
        if (boxValues[0][0] != null && boxValues[0][0].equals(boxValues[1][1]) && boxValues[0][0].equals(boxValues[2][2])) {
            return boxValues[0][0];
        }
        if (boxValues[0][2] != null && boxValues[0][2].equals(boxValues[1][1]) && boxValues[0][2].equals(boxValues[2][0])) {
            return boxValues[0][2];
        }

        if (markedCount == 9) {
            return "tie";
        }

        return "";
    }

    private void gameEnd(String winningMark, boolean isFromRandom) {
        if (winningMark.equals("x")) {
            Common.showToast("'X' is won", context);
            if (singleOrDuo.equals("single")) {
                Common.alertBox("single", markOfPlayer, "x", context);
            } else {
                Common.alertBox("duo", "", "x", context);
            }
            winningCountX++;
            xScoreTv.setText(String.valueOf(winningCountX));
            resetGame(isFromRandom);
        } else if (winningMark.equals("o")) {
            Common.showToast("'O' is won", context);
            if (singleOrDuo.equals("single")) {
                Common.alertBox("single", markOfPlayer, "o", context);
            } else {
                Common.alertBox("duo", "", "o", context);
            }
            winningCountO++;
            oScoreTv.setText(String.valueOf(winningCountO));
            resetGame(isFromRandom);
        } else if (winningMark.equals("tie")) {
            Common.showToast("Game Tied", context);
            Common.alertBox("tie", "", "", context);
            tiedCount++;
            tiesTv.setText(String.valueOf(tiedCount));
            resetGame(isFromRandom);
        }
    }

    private void resetGame(boolean isFromRandom) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxImages[i][j].setVisibility(View.GONE);
                boxMarked[i][j] = false;
                boxValues[i][j] = "";
            }
        }

        turnImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cross));

        markedCount = 0;

        if (singleOrDuo.equals("single") && isFromRandom) {
            if (choosedMark.equals("o")) {
                xIsYou = false;
                isPlayerTurn = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        randomCpu();
                    }
                }, 1000);
            } else {
                xIsYou = true;
                isPlayerTurn = true;
            }
        } else {
            // When reset or refresh the game, make sure X always goes first
            choosedMark = "x";
        }
    }
}
