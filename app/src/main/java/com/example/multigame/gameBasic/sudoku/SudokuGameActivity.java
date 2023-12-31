package com.example.multigame.gameBasic.sudoku;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.example.multigame.R;
import com.example.multigame.base.BaseActivity;
import com.example.multigame.base.DialogInstruction;
import com.example.multigame.databinding.ActivitySudokuGameBinding;

public class SudokuGameActivity extends BaseActivity<ActivitySudokuGameBinding> {

    private Menu menuList;

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
        return R.layout.activity_sudoku_game;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.sudoku_game_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void subscribeUi() {
        binding.setAction(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuList = menu;
        getMenuInflater().inflate(R.menu.menu_app, menu);
        menuList.findItem(R.id.menu_information).setVisible(true);
        return isHaveRightMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:
                DialogInstruction dialogInstruction = DialogInstruction.newInstance(R.layout.dialog_instruction_sudoku);
                dialogInstruction.show(getSupportFragmentManager(), "instruction");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isHaveBackMenu()) {
            NavDestination currentDestination = Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination();
            String className = ((FragmentNavigator.Destination) currentDestination).getClassName();
            if (className.equals(SudokuGameDifficultyFragment.class.getName())) {
                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);
            } else {
                Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
            }
        }
        return true;
    }
}
