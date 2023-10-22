package com.example.multigame.gameBasic.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import com.example.multigame.R;

public class CellGroupFragment extends Fragment {

    private int groupId;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cell_group, container, false);

        // Set textView click listeners
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        for (int textView1 : textViews) {
            TextView textView = view.findViewById(textView1);
            textView.setOnClickListener(view -> {
                if(getParentFragment() instanceof SudokuGamePlayFragment){
                    ((SudokuGamePlayFragment)getParentFragment()).onFragmentInteraction(groupId, Integer.parseInt(view.getTag().toString()), view);
                }else{
                    //((SudokuNewGameFragment)getParentFragment()).onFragmentInteraction(groupId, Integer.parseInt(view.getTag().toString()), view);
                }
            });
        }
        return view;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setValue(int position, int value) {
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        TextView currentView = view.findViewById(textViews[position]);
        currentView.setText(String.valueOf(value));
        currentView.setTextColor(Color.BLACK);
        currentView.setTypeface(null, Typeface.BOLD);
    }

    public boolean checkGroupCorrect() {
        ArrayList<Integer> numbers = new ArrayList<>();
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        for (int textView1 : textViews) {
            TextView textView = view.findViewById(textView1);
            int number = Integer.parseInt(textView.getText().toString());
            if (numbers.contains(number)) {
                return false;
            } else {
                numbers.add(number);
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

