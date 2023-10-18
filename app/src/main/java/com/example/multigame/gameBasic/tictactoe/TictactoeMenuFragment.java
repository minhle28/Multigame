package com.example.multigame.gameBasic.tictactoe;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

import android.os.Bundle;
import com.example.multigame.R;
import android.media.MediaPlayer;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.Window;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.multigame.base.BaseFragment;
import com.example.multigame.base.DialogInstruction;

import com.example.multigame.gameBasic.tictactoe.controller.Common;
import com.google.android.material.card.MaterialCardView;
import com.example.multigame.databinding.TictactoeMenuFragmentBinding;
import androidx.navigation.fragment.NavHostFragment;
import static androidx.navigation.fragment.NavHostFragment.findNavController;
import androidx.navigation.NavController;


public class TictactoeMenuFragment extends BaseFragment<TictactoeMenuFragmentBinding> {
    private MediaPlayer player;
    private boolean play_music;
    private Menu menuList;
    private MaterialCardView clickX, clickO, singlePlayerButton, duoPlayerButton;
    private String playerChoose;
    private Context context;
    private TictactoeMenuFragmentBinding binding;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_tictactoe_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_tictactoe_game);
    }

    @Override
    protected void subscribeUi() {
        player = MediaPlayer.create(requireContext(), R.raw.audio_tictactoe_game);
        player.setLooping(true);
        play_music = true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction.newInstance(R.layout.dialog_instruction_tictactoe).show(getChildFragmentManager(),"tictactoe_instruction");
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

    //Functions for game
    private void init() {
        clickX = binding.getRoot().findViewById(R.id.click_x);
        clickO = binding.getRoot().findViewById(R.id.click_o);
        singlePlayerButton = binding.getRoot().findViewById(R.id.single_player_button);
        duoPlayerButton = binding.getRoot().findViewById(R.id.duo_player_button);
        context = requireContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TictactoeMenuFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clickX = view.findViewById(R.id.click_x);
        clickO = view.findViewById(R.id.click_o);
        singlePlayerButton = view.findViewById(R.id.single_player_button);
        duoPlayerButton = view.findViewById(R.id.duo_player_button);
        context = requireContext();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == clickX) {
                    playerChoose = "x";
                    clickX.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_white_text)));
                    clickO.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.home_bg)));
                } else if (view == clickO) {
                    playerChoose = "o";
                    clickO.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_white_text)));
                    clickX.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.home_bg)));
                }
            }
        };

        clickX.setOnClickListener(clickListener);
        clickO.setOnClickListener(clickListener);

        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerChoose == null || playerChoose.equals("")) {
                    Common.showToast("Please choose 'X' OR 'O'", context);
                } else {
                    Bundle args = new Bundle();
                    args.putString("SingleOrDuo", "single");
                    args.putString("playerChoose", playerChoose);
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_tictactoeMenuFragment_to_tictactoeSingleAndDuoFragment, args);
                }
            }
        });

        duoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerChoose == null || playerChoose.equals("")) {
                    Common.showToast("Please choose 'X' OR 'O'", context);
                } else {
                    Bundle args = new Bundle();
                    args.putString("SingleOrDuo", "duo");
                    args.putString("playerChoose", playerChoose);
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_tictactoeMenuFragment_to_tictactoeSingleAndDuoFragment, args);
                }
            }
        });
    }
}