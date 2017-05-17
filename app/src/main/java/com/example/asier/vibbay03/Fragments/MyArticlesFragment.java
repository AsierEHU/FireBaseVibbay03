package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.FBLoopers.ArticleExec;
import com.example.asier.vibbay03.FBLoopers.ArticleLooper;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.example.asier.vibbay03.Views.ArticleAdapter;
import com.example.asier.vibbay03.Views.ArticleViews;

import java.util.ArrayList;

public class MyArticlesFragment extends ArticlesFragment {

    public MyArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);

        final ArrayList<ArticleViews> articles = new ArrayList<>();

        ArticleLooper.forEachUserArticlesOnce(new ArticleExec() {
            @Override
            public void execAction(Articulo a) {
                ArticleViews av = new ArticleViews(a);
                articles.add(av);
            }

            @Override
            public void onFinish() {
                fl.setAdapter(new ArticleAdapter(articles,fl.getContext()));
            }
        },LoginFireBaseTool.loggedIn.getEmail());
    }

}
