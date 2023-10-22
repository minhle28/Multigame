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
        int selectedDifficulty = getArguments().getInt("difficulty", 0);

        ArrayList<Board> boards = readGameBoards(selectedDifficulty);
        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());

        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[i - 1]);
            thisCellGroupFragment.setGroupId(i);
        }

        // Appear all values from the current board
        CellGroupFragment tempCellGroupFragment;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (CellGroupFragment) getChildFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    tempCellGroupFragment.setValue(groupPosition, currentValue);
                }
            }
        }
    }

    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    private ArrayList<Board> readGameBoards(int difficulty) {
        int minNumbers = 30; // Minimum number of initial numbers
        int maxNumbers = 50; // Maximum number of initial numbers

        if (difficulty == 0) {
            maxNumbers = 50;    //Easy
        } else if (difficulty == 1) {
            maxNumbers = 40;    //Normal
        } else if (difficulty == 2) {
            maxNumbers = 30;    //Hard
        }

        ArrayList<Board> boards = new ArrayList<>();

        // Generate random Sudoku boards with initial numbers
        for (int i = 0; i < 10; i++) {
            Board board = generateRandomBoard(minNumbers, maxNumbers);
            boards.add(board);
        }

        return boards;
    }

    private Board generateRandomBoard(int minNumbers, int maxNumbers) {
        Random random = new Random();
        boolean solved = false;
        Board board = new Board();

        while (!solved) {
            board = new Board();
            solved = solveSudoku(board, 0, 0);
        }

        // Randomly remove numbers from the solved board to get the desired number of initial numbers
        int remainingNumbers = 81 - maxNumbers;

        while (remainingNumbers > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (board.getValue(row, col) != 0) {
                board.setValue(row, col, 0);
                remainingNumbers--;
            }
        }

        return board;
    }

    private boolean solveSudoku(Board board, int row, int col) {
        if (row == 9) {
            row = 0;
            if (++col == 9) {
                return true; // Entire board has been successfully filled
            }
        }

        if (board.getValue(row, col) != 0) {
            return solveSudoku(board, row + 1, col);
        }

        for (int num = 1; num <= 9; num++) {
            if (isValidNumber(board, row, col, num)) {
                board.setValue(row, col, num);
                if (solveSudoku(board, row + 1, col)) {
                    return true;
                }
                board.setValue(row, col, 0); // Backtrack
            }
        }

        return false; // No valid number can be placed
    }

    private boolean isValidNumber(Board board, int row, int col, int num) {
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInBox(board, row - row % 3, col - col % 3, num);
    }

    private boolean isInRow(Board board, int row, int num) {
        for (int i = 0; i < 9; i++) {
            if (board.getValue(row, i) == num) {
                return true;
            }
        }
        return false;
    }

    private boolean isInCol(Board board, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board.getValue(i, col) == num) {
                return true;
            }
        }
        return false;
    }

    private boolean isInBox(Board board, int boxStartRow, int boxStartCol, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getValue(boxStartRow + i, boxStartCol + j) == num) {
                    return true;
                }
            }
        }
        return false;
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
        int column = ((group - 1) % 3) * 3 + (cell % 3);

        // Check if row and column are valid indices
        if (row >= 0 && row < 9 && column >= 0 && column < 9) {
            return startBoard.getValue(row, column) != 0;
        } else {
            // Handle the case where row or column is out of bounds
            return false;
        }
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

    public void onFragmentInteraction(int groupId, int cellId, View view) {

        if (mStartTime == 0) {
            mStartTime = System.currentTimeMillis();
        }

        clickedCell = (TextView) view;
        clickedGroup = groupId;
        clickedCellId = cellId;

        int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
        int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

        if (!isStartPiece(groupId, cellId)) {
            int number = TextUtils.isEmpty(clickedCell.getText().toString()) ? 1 : Integer.parseInt(clickedCell.getText().toString());
            ChooseNumberDialogFragment chooseNumber = ChooseNumberDialogFragment.newInstance(number, false);
            chooseNumber.setCallback(new ChooseNumberDialogFragment.Callback() {
                @Override
                public void chooseNumber(int number, boolean isUnsure) {
                    clickedCell.setText(String.valueOf(number));
                    currentBoard.setValue(row, column, number);
                    if (isUnsure) {
                        clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell_unsure));
                    } else {
                        clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                    }

                    if (currentBoard.isBoardFull()) {
                        binding.buttonCheckBoard.setVisibility(View.VISIBLE);
                    } else {
                        binding.buttonCheckBoard.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void removePiece() {
                    clickedCell.setText("");
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                    currentBoard.setValue(row, column, 0);
                    binding.buttonCheckBoard.setVisibility(View.INVISIBLE);
                }
            });
            chooseNumber.show(getChildFragmentManager(), "chooseNumber");
        } else {
            Toast.makeText(getContext(), getString(R.string.start_piece_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
