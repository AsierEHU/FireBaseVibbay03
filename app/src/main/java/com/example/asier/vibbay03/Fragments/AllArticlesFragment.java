package com.example.asier.vibbay03.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class AllArticlesFragment extends ArticlesFragment {

    int MY_PERMISSIONS_REQUEST;

    public AllArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);

        //Permisions
        String[] permissions;
        ArrayList<String> per = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            per.add(Manifest.permission.CAMERA);

            }
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            per.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        }
        permissions = new String[per.size()];
        for(int i=0; i<per.size(); i++){
            permissions[i] = per.get(i);
        }
        if(permissions.length>0){
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    MY_PERMISSIONS_REQUEST);
        }

        //articles show
        final ArrayList<ArticleViews> articles = new ArrayList<>();
        ArticleLooper.forEachArticlesOnce(new ArticleExec() {
            @Override
            public void execAction(Articulo a) {
                if(a.isEstado()==1) {
                    ArticleViews av = new ArticleViews(a);
                    articles.add(av);
                }
            }
            @Override
            public void onFinish() {
                fl.setAdapter(new ArticleAdapter(articles,getContext()));
            }
        });

    }
}

