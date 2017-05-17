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
import com.example.asier.vibbay03.Views.ArticleAdapter;
import com.example.asier.vibbay03.Views.ArticleViews;

import java.util.ArrayList;


public class SearchedArticlesFragment extends ArticlesFragment {

    public SearchedArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
    }

    public void showSearchedArticles(final String query) {

        final ArrayList<ArticleViews> articles = new ArrayList<>();
        ArticleLooper.forEachArticlesOnce(new ArticleExec() {
            @Override
            public void execAction(Articulo a) {
                if(a.isEstado()==1 && a.getTitulo().contains(query)){
                    ArticleViews av = new ArticleViews(a);
                    articles.add(av);
                }
            }
            @Override
            public void onFinish() {
                fl.setAdapter(new ArticleAdapter(articles,fl.getContext()));
            }
        });

    }
}