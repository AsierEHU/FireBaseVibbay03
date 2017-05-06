package com.example.asier.vibbay03.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

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

public class AllArticlesFragment extends Fragment {

    GridView fl;
    int MY_PERMISSIONS_REQUEST;

    public AllArticlesFragment() {
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


        final ArrayList<ArticleViews> articles = new ArrayList<>();

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
                        Articulo a = ds.getValue(Articulo.class);
                        a.setUserId(usuerId);
                        ArticleViews av = new ArticleViews(a);
                        articles.add(av);
                    }
                }
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

