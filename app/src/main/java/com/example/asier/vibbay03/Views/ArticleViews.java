package com.example.asier.vibbay03.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.Fragments.ArticleDetailsFragment;
import com.example.asier.vibbay03.MainActivity;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ArticleTools;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asier on 20/04/2017.
 */

public class ArticleViews {

    private final Articulo art;

    public ArticleViews(Articulo art){
        this.art = art;
    }

    public LinearLayout getTinyView(final Context cont){

        //Main layout
        LinearLayout x = new LinearLayout(cont);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(100, 100, 100, 100);
        x.setLayoutParams(lp);
        x.setOrientation(LinearLayout.VERTICAL);
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        x.setBackground(border);
        x.setPadding(20,20,20,20);


        //nombre
        TextView nombre = new TextView(x.getContext());
        nombre.setText(art.getTitulo());
        if(art.isEstado()==1){
            Log.i("","");
            //nombre.setTextColor(Color.BLACK);
        }
        else{
            nombre.setTextColor(Color.RED);
        }
        nombre.setTypeface(null, Typeface.BOLD);


        //precioBase
        TextView inicial = new TextView(x.getContext());
        inicial.setText("Precio base:");
        inicial.setTypeface(null, Typeface.ITALIC);
        //inicial.setPaintFlags(inicial.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView precioInicial = new TextView(x.getContext());
        precioInicial.setText(String.format("%1$,.2f€", art.getPrecio()));


        View ruler = new View(x.getContext()); ruler.setBackgroundColor(Color.BLACK);


        //precioUltimo

        TextView ultimo = new TextView(x.getContext());
        ultimo.setText("Último:");
        ultimo.setTypeface(null, Typeface.ITALIC);
        //ultimo.setPaintFlags(ultimo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final TextView ultimoPrecio = new TextView(x.getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.i("ArtViews","entra0");
        DatabaseReference BidsReference = database.getReference("Pujas");
        Log.i("ArtViews","entra1");
        BidsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("ArtViews","entra2");
                Puja p = ArticleTools.getHigherBidPrice(art,dataSnapshot);
                if(p!=null){
                    Log.i("ArtViews","entra3");
                    Log.i("ArtViews", p.toString());
                    ultimoPrecio.setText(String.format("%1$,.2f€", p.getPrecio()));

                }else{
                    ultimoPrecio.setText(String.format("Sin pujas"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error", databaseError.getDetails());
            }
        });


        //Imagen
        final ImageView imagen = new ImageView(x.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        imagen.setLayoutParams(layoutParams);
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(art.getImagen());
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imagen.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //add views
        x.addView(nombre);
        x.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
        x.addView(inicial);
        x.addView(precioInicial);
        //

        x.addView(imagen);
        x.addView(ultimo);
        x.addView(ultimoPrecio);
        //x.addView(name);
      //  x.addView(b);


        //
        //x.setOnClickListener();
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("OnClick", "entra1");
                ArticleTools.selectedArticle = art;
                Fragment art = new ArticleDetailsFragment();
                MainActivity.getActualMainActivity().changeFragment(art);
            }
        });

        return x;
    }

    public LinearLayout getBidView(final Context cont){
            //Main layout
            LinearLayout x = new LinearLayout(cont);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(100, 100, 100, 100);
            x.setLayoutParams(lp);
            x.setOrientation(LinearLayout.VERTICAL);
            //use a GradientDrawable with only one color set, to make it a solid color
            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF); //white background
            border.setStroke(1, 0xFF000000); //black border with full opacity
            x.setBackground(border);
            x.setPadding(20,20,20,20);

            //nombre
            TextView nombre = new TextView(x.getContext());
            nombre.setText(art.getTitulo());
            nombre.setTypeface(null, Typeface.BOLD);

            //precioBase
            TextView precio = new TextView(x.getContext());
            precio.setText(String.format("%1$,.2f€", art.getPrecio()));

            //Imagen
            final ImageView imagen = new ImageView(x.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            imagen.setLayoutParams(layoutParams);
            final FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference httpsReference = storage.getReferenceFromUrl(art.getImagen());
            final long ONE_MEGABYTE = 1024 * 1024;
            httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imagen.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            //add views
            x.addView(nombre);
            x.addView(imagen);

            int i = 1;
            for(Puja p: art.getArt_pujas()){
                TextView precioPuja = new TextView(x.getContext());
                precioPuja.setText(i + ": " + String.format("%1$,.2f€", p.getPrecio()));
                i++;
                x.addView(precioPuja);
            }


            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("OnClick", "entra1");
                    ArticleTools.selectedArticle = art;
                    Fragment art = new ArticleDetailsFragment();
                    MainActivity.getActualMainActivity().changeFragment(art);
                }
            });
            return x;
        }
}
