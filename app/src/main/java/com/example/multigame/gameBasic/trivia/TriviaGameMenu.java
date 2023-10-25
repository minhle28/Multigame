package com.example.multigame.gameBasic.trivia;

import android.content.Intent;
import android.os.Bundle;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.databinding.TriviaGameMenuBinding;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
public class TriviaGameMenu extends BaseFragment<TriviaGameMenuBinding> {

    @Override
    protected int getLayoutID() {
        return R.layout.trivia_game_menu;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_trivia_game);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
    }

    public void onPlayGameButtonClicked() {
        findNavController(this).navigate(R.id.action_memoryGameMenu_to_memoryGameFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}