package com.example.multigame.gameBasic.sudoku;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.multigame.R;
import com.example.multigame.databinding.ChooseNumberDialogFragmentBinding;

public class ChooseNumberDialogFragment extends DialogFragment {

    ChooseNumberDialogFragmentBinding binding;

    private static String TAG_SELECTED_NUMBER = "selectedNumber";
    private static String TAG_NEW_BOARD = "newBoard";

    Callback mCallback;
    private int selectedNumber = 1;
    private boolean checkBoxChecked = false;
    private boolean newBoardCreating;

    public static ChooseNumberDialogFragment newInstance(int selectedNumber, boolean isNewBoard) {
        Bundle args = new Bundle();
        args.putInt(TAG_SELECTED_NUMBER, selectedNumber);
        args.putBoolean(TAG_NEW_BOARD, isNewBoard);
        ChooseNumberDialogFragment fragment = new ChooseNumberDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.choose_number_dialog_fragment, null);
        binding = DataBindingUtil.bind(root);
        initLayout();
        binding.setActions(this);
        return root;
    }

    private void initLayout() {
        selectedNumber = getArguments().getInt(TAG_SELECTED_NUMBER, 0);
        initializeSpinner();
        binding.spinner.setSelection(selectedNumber - 1);
        newBoardCreating = getArguments().getBoolean(TAG_NEW_BOARD, false);
        if (newBoardCreating) {
            binding.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeSpinner() {
        final Integer numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1, numbers);
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNumber = numbers[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onCheckBoxClicked() {
        checkBoxChecked = binding.checkBox.isChecked();
    }

    public void chooseNumberButtonClicked() {
        if (mCallback != null) {
            mCallback.chooseNumber(selectedNumber, checkBoxChecked);
        }
        dismiss();
    }

    public void onRemovePieceButtonClicked() {
        if (mCallback != null) {
            mCallback.removePiece();
        }
        dismiss();
    }

    interface Callback {
        void chooseNumber(int selectedNumber, boolean isUnsure);

        void removePiece();
    }
}
