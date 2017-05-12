package com.example.asier.vibbay03.FBLoopers;

import android.util.Log;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.Tools.ArticleTools;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asier on 12/05/2017.
 */

public class BidLooper {

    public static void forEachBidUserOnce(final BidExec be, final String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Pujas").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot ds = it.next();
                    String id = ds.getKey();
                    Iterator<DataSnapshot> it2 = ds.getChildren().iterator();
                    while(it2.hasNext()){
                        DataSnapshot ds2 = it2.next();
                        Puja p = ds2.getValue(Puja.class);
                        p.setIdUsuario(userId);
                        p.setIdArt(id);
                        be.execAction(p);
                    }
                }
                be.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }

    public static void forEachBidUserArticleOnce(final BidExec be, final Articulo art, final String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bidsArtReference = database.getReference("Pujas/"+userId+"/"+art.getTitulo());
        bidsArtReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> bids = dataSnapshot.getChildren().iterator();
                while (bids.hasNext()) {
                    DataSnapshot ds = bids.next();
                    Puja p = ds.getValue(Puja.class);
                    p.setIdArt(art.getTitulo());
                    p.setIdUsuario(userId);
                    be.execAction(p);
                }

                be.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }


    public static void forEachBidOnChange(final BidExec be){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference BidsReference = database.getReference("Pujas");
        BidsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> users = dataSnapshot.getChildren().iterator();
                while (users.hasNext()) {
                    DataSnapshot user = users.next();
                    String userId = user.getKey();
                    Iterator<DataSnapshot> articles = user.getChildren().iterator();
                    while (articles.hasNext()) {
                        DataSnapshot article = articles.next();
                        String artId = article.getKey();
                        Iterator<DataSnapshot> bids = article.getChildren().iterator();
                        while (bids.hasNext()) {
                            DataSnapshot ds = bids.next();
                            Puja p = ds.getValue(Puja.class);
                            p.setIdArt(artId);
                            p.setIdUsuario(userId);
                            be.execAction(p);
                        }
                    }
                }
                be.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }

}
