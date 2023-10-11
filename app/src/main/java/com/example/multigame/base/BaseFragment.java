package com.example.multigame.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.multigame.R;

public abstract class BaseFragment<BD extends ViewDataBinding> extends Fragment {

    protected BD binding;

    protected abstract int getLayoutID();

    protected abstract String getActionBarTitle();

    protected abstract void subscribeUi();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false);
        View view = binding.getRoot();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!TextUtils.isEmpty(getActionBarTitle())) {
            getActivity().setTitle(getActionBarTitle());
        }
        subscribeUi();
    }

    public void startActivityWithAnimation(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_left_exit);
    }
}

