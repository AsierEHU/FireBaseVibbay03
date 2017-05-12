package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.asier.vibbay03.R;

/**
 * Created by asier on 12/05/2017.
 */

public class ArticlesFragment extends Fragment {

    GridView fl;

    public ArticlesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fl =(GridView) inflater.inflate(R.layout.fragment_allarticles, container, false);
        return fl;

    }



}
