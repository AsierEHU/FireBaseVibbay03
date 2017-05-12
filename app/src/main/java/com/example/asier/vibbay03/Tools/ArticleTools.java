package com.example.asier.vibbay03.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.FBLoopers.ArticleExec;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Views.ArticleAdapter;
import com.example.asier.vibbay03.Views.ArticleViews;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by asier on 02/05/2017.
 */

public class ArticleTools {

    public static Articulo selectedArticle;

    public static void onArticleStateChange(final Articulo art, final ArticleExec ae){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Articulos/"+art.getUserId()+"/"+art.getTitulo());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("asd",dataSnapshot.getKey());
                Articulo a = dataSnapshot.getValue(Articulo.class);
                a.setMax_puja(art.getMax_puja());
                a.setUserId(art.getUserId());
                a.setArt_pujas(art.getArt_pujas());
                ae.execAction(a);
                ae.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });
    }


    public static void pujar(Articulo art, double precio){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Long id = new Date().getTime();
        Puja p = new Puja(precio);
        p.setIdArt(art.getTitulo());
        p.setIdUsuario(LoginFireBaseTool.loggedIn.getEmail());

        Map<String, Object> pujaValues = p.toMap();

        Map<String, Object> childPujas = new HashMap<>();
        childPujas.put("/Pujas/"+p.getIdUsuario()+"/"+p.getIdArt()+"/"+id,pujaValues);
        mDatabase.updateChildren(childPujas);
    }




}
