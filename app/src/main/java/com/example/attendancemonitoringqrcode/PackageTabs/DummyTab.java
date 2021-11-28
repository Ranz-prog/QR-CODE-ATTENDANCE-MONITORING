package com.example.attendancemonitoringqrcode.PackageTabs;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancemonitoringqrcode.R;


public class DummyTab extends Fragment {


    public DummyTab() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy_tab, container, false);
    }


}
