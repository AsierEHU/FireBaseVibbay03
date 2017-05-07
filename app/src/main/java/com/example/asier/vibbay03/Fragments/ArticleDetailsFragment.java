package com.example.asier.vibbay03.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.MainActivity;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ArticleTools;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
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
import java.util.Iterator;

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
        final Articulo articulo = ArticleTools.selectedArticle;

        //title
        TextView l = (TextView) sv.findViewById(R.id.titleArticle);
        l.setText(articulo.getTitulo());

        //image
        final ImageView i = (ImageView) sv.findViewById(R.id.imageArt);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 600);
        i.setLayoutParams(layoutParams);
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(articulo.getImagen());
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                i.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //Price
        TextView ip = (TextView) sv.findViewById(R.id.initialprice);
        ip.setText(String.format("%1$,.2f€", articulo.getPrecio()));

        //last Bid
        final TextView lb = (TextView) sv.findViewById(R.id.lastbid);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BidsReference = database.getReference("Pujas");
        BidsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Puja p = ArticleTools.getHigherBidPrice(articulo,dataSnapshot);
                if(p!=null){
                    lb.setText(String.format("%1$,.2f€", p.getPrecio()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });


        //My last bid
        if(LoginFireBaseTool.loggedIn != null && articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
            final TextView mlb = (TextView) sv.findViewById(R.id.mylastbid);
            DatabaseReference bidsArtReference = database.getReference("Pujas/"+LoginFireBaseTool.loggedIn.getEmail()+"/"+articulo.getTitulo());
            bidsArtReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Puja p = ArticleTools.getUserArtHigherPrice(dataSnapshot,LoginFireBaseTool.loggedIn.getEmail(),articulo.getTitulo());
                    if(p!=null){
                        mlb.setText(String.format("%1$,.2f€", p.getPrecio()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("error", databaseError.getDetails());
                }
            });
        }


        //Generic Button

        Button b = (Button) sv.findViewById(R.id.genericbutton);
        if(LoginFireBaseTool.loggedIn == null){
            b.setText("Login");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getActualMainActivity().changeFragment(new LoginFragment());
                }
            });
        }else if(articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())){
            b.setText("Ver pujas");
        }else{
            b.setText("Pujar");
        }

        //Close Bid button
        if(LoginFireBaseTool.loggedIn != null && articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
            Button cb = (Button) sv.findViewById(R.id.closebids);
            cb.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        Log.i("Fragment","Article Details fragment terminado");
    }

}
