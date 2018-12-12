package com.lambton.daianaiziatov.parkyourcar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class InstructionFragment extends Fragment {

    private WebView webView;


    public InstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View instructionView = inflater.inflate(R.layout.fragment_instruction, container, false);



        return instructionView;
    }

}
