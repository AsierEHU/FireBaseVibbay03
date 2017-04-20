package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ScrollView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Views.ArticleAdapter;
import com.example.asier.vibbay03.Views.ArticleViews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class SearchedArticlesFragment extends Fragment {

    GridView fl;

    public SearchedArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fl =(GridView) inflater.inflate(R.layout.fragment_allarticles, container, false);
        return fl;
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        Log.i("Fragment","Todos los artículos fragment terminado");
    }

    public void showSearchedArticles(final String query) {
        Log.i("SEARCH", "Entra metodo con query: "+query);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Articulos");
        final ArrayList<ArticleViews> articles = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    Iterator<DataSnapshot> it2 = it.next().getChildren().iterator();
                    while(it2.hasNext()){
                        DataSnapshot ds = it2.next();
                        Articulo a = ds.getValue(Articulo.class);
                        Log.i("SEARCH", "Entra antes if con artículo " + a.getTitulo());
                        if(a.getTitulo().contains(query)){
                            ArticleViews av = new ArticleViews(a);
                            articles.add(av);
                        }else{
                            Log.i("SEARCH", "El articulo con títutlo "+ a.getTitulo()+ " no contiene la query " + query);
                        }
                    } // while articulos
                } // while usuarios
                fl.setAdapter(new ArticleAdapter(articles,getContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Login error", databaseError.getDetails());
                Log.i("Login message", databaseError.getMessage());
            }
        });
    }
}