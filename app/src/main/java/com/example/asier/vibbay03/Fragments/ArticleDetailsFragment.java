package com.example.asier.vibbay03.Fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

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
        ip.setText("Precio inicial: " +String.format("%1$,.2f€", articulo.getPrecio()));

        //last Bid
        final TextView lb = (TextView) sv.findViewById(R.id.lastbid);
        final DatabaseReference BidsReference = database.getReference("Pujas");
        BidsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Puja p = ArticleTools.getHigherBidPrice(articulo,dataSnapshot);
                if(p!=null){
                    lb.setText("Puja más alta: "+String.format("%1$,.2f€", p.getPrecio()));
                    articulo.setMax_puja(p.getPrecio());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });


        //My last bid
        if(LoginFireBaseTool.loggedIn != null && !articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
            final TextView mlb = (TextView) sv.findViewById(R.id.mylastbid);
            DatabaseReference bidsArtReference = database.getReference("Pujas/"+LoginFireBaseTool.loggedIn.getEmail()+"/"+articulo.getTitulo());
            bidsArtReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Puja p = ArticleTools.getUserArtHigherPrice(dataSnapshot,LoginFireBaseTool.loggedIn.getEmail(),articulo.getTitulo());
                    if(p!=null){
                        mlb.setText("Mi puja más alta: "+String.format("%1$,.2f€", p.getPrecio()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("error", databaseError.getDetails());
                }
            });
        }


        //Generic Button
        final Button b = (Button) sv.findViewById(R.id.genericbutton);
        //Login
        if(LoginFireBaseTool.loggedIn == null){
            b.setText("Login");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getActualMainActivity().changeFragment(new LoginFragment());
                }
            });
            //Ver pujas
        }else if(articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())){
            b.setText("Ver pujas");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<ArticleViews> articles = new ArrayList<>();
                    final HashMap<String,ArrayList<Puja>> pujasDiferentes = new HashMap<>();
                    b.setEnabled(false);
                    BidsReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Puja> x = ArticleTools.getHistoricalBids(articulo, dataSnapshot);
                            LinearLayout ll = (LinearLayout)sv.findViewById(R.id.bidsLayout);
                            ll.removeAllViews();
                            double lastPrice = -1;
                            for(Puja p:x){
                                TextView tv = new TextView(getContext());
                                tv.setText(p.getIdUsuario()+" - "+String.format("%1$,.2f€", p.getPrecio()));
                                ll.addView(tv,0);
//                                if(p.getPrecio()>lastPrice){
//                                    ll.addView(tv,1);
//                                }else{
//                                    ll.addView(tv,0);
//                                }
                                lastPrice = p.getPrecio();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("error", databaseError.getDetails());
;
                        }
                    });
                }
            });
            //Pujar
        }else{
            b.setText("Pujar");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    alert.setTitle("Nueva puja");
                    alert.setMessage("Indique el precio de su puja");

// Set an EditText view to get user input
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            double price = Double.valueOf(input.getText().toString());
                            if(price<=Math.max(articulo.getMax_puja(),articulo.getPrecio())){
                                Toast toast = Toast.makeText(getContext(), "La puja debe ser mayor que "+String.format("%1$,.2f€",Math.max(articulo.getMax_puja(),articulo.getPrecio())), Toast.LENGTH_SHORT);
                                toast.show();
                            }else{
                                ArticleTools.pujar(articulo, price );
                                Toast toast = Toast.makeText(getContext(), "Puja realizada correctamente", Toast.LENGTH_SHORT);
                                toast.show();
                            }


                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();



                    //comprobar que la puja no es más alta

                }
            });
        }

        //Close Bid button
        if(LoginFireBaseTool.loggedIn != null && articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
            final Button cb = (Button) sv.findViewById(R.id.closebids);
            cb.setVisibility(View.VISIBLE);
            if(articulo.isEstado()==0){
                cb.setText("Pujas cerradas");
                cb.setEnabled(false);
            }else{
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    database.getReference("Articulos/"+ articulo.getUserId()+"/"+articulo.getTitulo()+"/estado").setValue(0);
                    cb.setText("Pujas cerradas");
                    cb.setEnabled(false);
                    }
                });
            }

        }

    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        Log.i("Fragment","Article Details fragment terminado");
    }

}
