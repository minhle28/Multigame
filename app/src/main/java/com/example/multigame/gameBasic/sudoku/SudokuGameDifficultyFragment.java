package com.example.multigame.gameBasic.sudoku;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.databinding.SudokuGameDifficultyFragmentBinding;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class SudokuGameDifficultyFragment extends BaseFragment<SudokuGameDifficultyFragmentBinding> {

    private boolean newBoard = false;
    private int selectedDifficulty = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.sudoku_game_difficulty_fragment;
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
    }

    public void onDifficultyRadioButtonsClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonEasy:
                if (checked) {
                    selectedDifficulty = 0;
                }
                break;
            case R.id.radioButtonNormal:
                if (checked) {
                    selectedDifficulty = 1;
                }
                break;
            case R.id.radioButtonHard:
                if (checked) {
                    selectedDifficulty = 2;
                }
                break;
        }
    }

    public void onStartGameButtonClicked(View view) {
        if (newBoard) {
            // Do nothing
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("difficulty", selectedDifficulty);
            findNavController(this).navigate(R.id.action_sudokuGameDifficultyFragment_to_sudokuGamePlayFragment, bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
