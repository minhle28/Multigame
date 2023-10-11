package com.example.multigame.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.multigame.R;

public class DialogInstruction extends DialogFragment {

    public static String TAG_LAYOUT_ID = "layout_id";
    public static String TAG_WRAP_CONTENT = "TAG_WRAP_CONTENT";
    private CallBack mCallBack;

    public static DialogInstruction newInstance(int layoutID) {
        Bundle args = new Bundle();
        args.putInt(TAG_LAYOUT_ID, layoutID);
        DialogInstruction fragment = new DialogInstruction();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogInstruction newInstance(int layoutID, boolean wrapContent) {
        Bundle args = new Bundle();
        args.putInt(TAG_LAYOUT_ID, layoutID);
        args.putBoolean(TAG_WRAP_CONTENT, wrapContent);
        DialogInstruction fragment = new DialogInstruction();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallBack(CallBack callBack){
        this.mCallBack = callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments().getBoolean(TAG_WRAP_CONTENT,false)){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
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
        int layoutId = getArguments().getInt(TAG_LAYOUT_ID, R.layout.dialog_instruction_tictactoe);
        View root = inflater.inflate(layoutId, null);
        return root;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mCallBack != null){
            mCallBack.onClose();
        }
    }

    public interface CallBack{
        public void onClose();
    }
}

