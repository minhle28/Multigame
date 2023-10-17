package com.example.multigame.gameBasic.memory;

import android.content.Intent;
import android.os.Bundle;

import com.example.multigame.R;
import com.example.multigame.base.BaseFragment;
import com.example.multigame.databinding.MemoryGameMenuBinding;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MemoryGameMenu extends BaseFragment<MemoryGameMenuBinding> {

    @Override
    protected int getLayoutID() {
        return R.layout.memory_game_menu;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_memory_game);
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
