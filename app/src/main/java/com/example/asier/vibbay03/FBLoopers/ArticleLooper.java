package com.example.asier.vibbay03.FBLoopers;

import android.util.Log;

import com.example.asier.vibbay03.Beans.Articulo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by asier on 12/05/2017.
 */

public class ArticleLooper {


    public static void forEachArticlesOnce(final ArticleExec ae){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Articulos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot dName = it.next();
                    String usuerId = dName.getKey();
                    Iterator<DataSnapshot> it2 = dName.getChildren().iterator();
                    while(it2.hasNext()){
                        DataSnapshot ds = it2.next();
                        final Articulo a = ds.getValue(Articulo.class);
                        a.setUserId(usuerId);
                        ae.execAction(a);
                    }
                }
                ae.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }


    public static void forEachUserArticlesOnce(final ArticleExec ae, final String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Articulos").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it2 = dataSnapshot.getChildren().iterator();
                while (it2.hasNext()) {
                    DataSnapshot ds = it2.next();
                    Articulo a = ds.getValue(Articulo.class);
                    a.setUserId(userId);
                    ae.execAction(a);
                }
                ae.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }

}
