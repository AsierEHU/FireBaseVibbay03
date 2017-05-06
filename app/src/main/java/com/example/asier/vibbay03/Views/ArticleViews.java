package com.example.asier.vibbay03.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        nombre.setTypeface(null, Typeface.BOLD);

        //precioBase
        TextView precio = new TextView(x.getContext());
        precio.setText(String.format("%1$,.2f€", art.getPrecio()));

        //precioUltimo
        TextView name = new TextView(x.getContext());
        name.setText(art.getUserId());

        Button b = new Button(x.getContext());
        b.setText("Ver");


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
        x.addView(precio);

        x.addView(imagen);
        //x.addView(name);
        x.addView(b);


        //
/*        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleTools.selectedArticle = art;
                Fragment art = new ArticleDetailsFragment();

                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include_main, art);
                fragmentTransaction.commit();

            }
        });*/

        return x;
    }
}