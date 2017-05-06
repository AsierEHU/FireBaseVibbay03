package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ArticleTools;

/**
 * Created by asier on 02/05/2017.
 */

public class ArticleDetailsFragment extends Fragment {

    ScrollView sv;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sv = (ScrollView) inflater.inflate(R.layout.fragment_articledetails, container, false);
        SetViewAticle();
        return sv;
    }

    private void SetViewAticle() {
        Articulo a = ArticleTools.selectedArticle;
        TextView l = (TextView) sv.findViewById(R.id.titleArticle);
        l.setText(a.getTitulo());
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        Log.i("Fragment","Article Details fragment terminado");
    }

}
