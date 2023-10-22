package com.example.multigame.gameBasic.sudoku;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.base.DialogInstruction;
import com.example.multigame.databinding.SudokuGamePlayFragmentBinding;
import com.example.multigame.gameBasic.sudoku.model.Board;
import com.example.multigame.utils.Utils;

public class SudokuGamePlayFragment extends BaseFragment<SudokuGamePlayFragmentBinding> {

    private final String TAG = this.getClass().getSimpleName();

    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private Board startBoard;
    private Board currentBoard;

    // Variable to save point for game
    long mStartTime = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.sudoku_game_play_fragment;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.sudoku_game_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
        int difficulty = getArguments().getInt("difficulty", 0);

        // Generate a random Sudoku board based on the difficulty level
        currentBoard = generateRandomSudoku(difficulty);

        // Populate the Sudoku board with values
        populateSudokuBoard();
    }

    private Board generateRandomSudoku(int difficulty) {
        // Create a new Sudoku board
        Board sudokuBoard = new Board();
        Random random = new Random();

        // Determine the number of empty cells based on the difficulty level
        int emptyCells = 0;
        switch (difficulty) {
            case 0: // Easy
                emptyCells = 1;
                break;
            case 1: // Normal
                emptyCells = 2;
                break;
            case 2: // Hard
                emptyCells = 3;
                break;
        }

        // Fill the Sudoku board with random numbers while keeping it a valid Sudoku puzzle
        while (emptyCells > 0) {
            int row = random.nextInt(9);
            int column = random.nextInt(9);
            int value = random.nextInt(9) + 1;

            // Check if the randomly chosen cell is empty
            if (sudokuBoard.getValue(row, column) == 0) {
                // Check if the value is valid for this cell
                if (isValidValue(sudokuBoard, row, column, value)) {
                    sudokuBoard.setValue(row, column, value);
                    emptyCells--;
                }
            }
        }

        return sudokuBoard;
    }

    private boolean isValidValue(Board board, int row, int column, int value) {
        // Check if the value is valid in the row, column, and 3x3 box
        return isValidRow(board, row, value) && isValidColumn(board, column, value) && isValidBox(board, row, column, value);
    }

    private boolean isValidRow(Board board, int row, int value) {
        for (int col = 0; col < 9; col++) {
            if (board.getValue(row, col) == value) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidColumn(Board board, int column, int value) {
        for (int row = 0; row < 9; row++) {
            if (board.getValue(row, column) == value) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidBox(Board board, int row, int column, int value) {
        int boxRow = (row / 3) * 3;
        int boxCol = (column / 3) * 3;

        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board.getValue(r, c) == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private void populateSudokuBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int cellGroupId = getCellGroupId(i, j);
                int cellId = getCellIdInGroup(i, j);

                // Find the CellGroupFragment by tag
                String tag = String.valueOf(cellGroupId);
                CellGroupFragment cellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentByTag(tag);

                if (cellGroupFragment != null) {
                    int cellValue = currentBoard.getValue(i, j);

                    if (cellValue != 0) {
                        cellGroupFragment.setValue(cellId, cellValue);
                    }
                } else {
                    // Handle the case where the fragment is null
                    Log.e(TAG, "CellGroupFragment is null for tag: " + tag);
                }
            }
        }
    }


    // Helper functions to map Sudoku board indices to UI fragment IDs
    private int getCellGroupId(int row, int column) {
        return (row / 3) * 3 + (column / 3) + 1;
    }

    private int getCellIdInGroup(int row, int column) {
        return (row % 3) * 3 + (column % 3);
    }

    private boolean isStartPiece(int group, int cell) {
        int row = ((group - 1) / 3) * 3 + (cell / 3);
        int column = ((group - 1) % 3) * 3 + ((cell) % 3);
        return startBoard.getValue(row, column) != 0;
    }

    private boolean checkAllGroups() {
        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 0; i < 9; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }

    public void onCheckBoardButtonClicked() {
        currentBoard.isBoardCorrect();
        if (checkAllGroups() && currentBoard.isBoardCorrect()) {
            Toast.makeText(getContext(), getString(R.string.board_correct), Toast.LENGTH_SHORT).show();
            long time = Utils.millisecondToSecond(System.currentTimeMillis() - mStartTime);
        } else {
            Toast.makeText(getContext(), getString(R.string.board_incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    public void onShowInstructionsButtonClicked() {
        DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_sudoku);
        dialogInstruction.show(getChildFragmentManager(), "instruction");
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
