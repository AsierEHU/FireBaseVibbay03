package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.example.asier.vibbay03.Views.ArticleViews;
import com.example.asier.vibbay03.Views.BidAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by asier on 06/05/2017.
 */

public class MyBidsFragment  extends Fragment {

    GridView fl;

    public MyBidsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fl =(GridView) inflater.inflate(R.layout.fragment_allarticles, container, false);



        final ArrayList<ArticleViews> bids = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<ArticleViews> articles = new ArrayList<>();
        final HashMap<String,ArrayList<Puja>> pujasDiferentes = new HashMap<>();

        DatabaseReference reference = database.getReference("Pujas").child(LoginFireBaseTool.loggedIn.getEmail());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot ds = it.next();
                    String id = ds.getKey();

                    ArrayList<Puja> pujasArt;
                    pujasArt = new ArrayList<Puja>();
                    pujasDiferentes.put(id,pujasArt);

                    Log.i("Pujas", "Key " + id);
                    Iterator<DataSnapshot> it2 = ds.getChildren().iterator();
                    while(it2.hasNext()){
                        DataSnapshot ds2 = it2.next();
                        Puja p = ds2.getValue(Puja.class);
                        Log.i("Pujas", "Precio " +  String.valueOf(p.getPrecio()));
                        p.setIdUsuario(LoginFireBaseTool.loggedIn.getEmail());
                        p.setIdArt(id);
                        pujasArt.add(p);
                    }
                }
                Log.i("Pujas", pujasDiferentes.toString());
                Log.i("Pujas", pujasDiferentes.keySet().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Login error", databaseError.getDetails());
                Log.i("Login message", databaseError.getMessage());
            }
        });

        DatabaseReference reference2 = database.getReference("Articulos");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot dName = it.next();
                    String usuerId = dName.getKey();
                    Iterator<DataSnapshot> it2 = dName.getChildren().iterator();
                    while(it2.hasNext()){
                        DataSnapshot ds = it2.next();
                        Articulo a = ds.getValue(Articulo.class);
                        a.setUserId(usuerId);
                        for (String entry : pujasDiferentes.keySet()) {
                            if(a.getTitulo().equals(entry)){
                                Log.i("Pujas", "Fin fin "+ a.getTitulo() + a.getPrecio());
                                ArticleViews av = new ArticleViews(a);
                                articles.add(av);
                            }
                        }
                    }
                    fl.setAdapter(new BidAdapter(articles,getContext()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Login error", databaseError.getDetails());
                Log.i("Login message", databaseError.getMessage());
            }
        });



    return fl;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
    }

}
