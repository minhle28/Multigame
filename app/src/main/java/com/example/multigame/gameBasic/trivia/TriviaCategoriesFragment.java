package com.example.multigame.gameBasic.trivia;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.databinding.TriviaCategoriesFragmentBinding;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class TriviaCategoriesFragment extends BaseFragment<TriviaCategoriesFragmentBinding> {
    private int selectedDifficulty = 0;
    @Override
    protected int getLayoutID() {
        return R.layout.trivia_categories_fragment;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.trivia_game_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
    }

}