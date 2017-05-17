package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.FBLoopers.BidExec;
import com.example.asier.vibbay03.FBLoopers.BidLooper;
import com.example.asier.vibbay03.FBLoopers.ArticleExec;
import com.example.asier.vibbay03.FBLoopers.ArticleLooper;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.example.asier.vibbay03.Views.ArticleViews;
import com.example.asier.vibbay03.Views.BidAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asier on 06/05/2017.
 */

public class MyBidsFragment extends ArticlesFragment {


    public MyBidsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        final ArrayList<ArticleViews> bids = new ArrayList<>();
        final HashMap<String,ArrayList<Puja>> pujasDiferentes = new HashMap<>();


        //bids loop
        BidLooper.forEachBidUserOnce(new BidExec() {
            @Override
            public void execAction(Puja p) {

                ArrayList<Puja> pujasArt = pujasDiferentes.get(p.getIdArt());
                if(pujasArt==null){
                    pujasArt = new ArrayList<Puja>();
                    pujasDiferentes.put(p.getIdArt(),pujasArt);
                }
                pujasArt.add(p);
            }

            @Override
            public void onFinish() {
            }
        }, LoginFireBaseTool.loggedIn.getEmail());

        //articles show
        final ArrayList<ArticleViews> articles = new ArrayList<>();
        ArticleLooper.forEachArticlesOnce(new ArticleExec() {
            @Override
            public void execAction(Articulo a) {
                for (String entry : pujasDiferentes.keySet()) {
                    if(a.getTitulo().equals(entry)){
                        a.setArt_pujas(pujasDiferentes.get(entry));
                        ArticleViews av = new ArticleViews(a);
                        articles.add(av);
                    }
                }
            }
            @Override
            public void onFinish() {
                fl.setAdapter(new BidAdapter(articles,fl.getContext()));
            }
        });
    }

}
