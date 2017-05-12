package com.example.asier.vibbay03.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.FBLoopers.BidExec;
import com.example.asier.vibbay03.FBLoopers.BidLooper;
import com.example.asier.vibbay03.Fragments.ArticleDetailsFragment;
import com.example.asier.vibbay03.MainActivity;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ArticleTools;
import com.example.asier.vibbay03.Tools.BidTools;
import com.example.asier.vibbay03.Tools.ImageTools;
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

import java.util.ArrayList;

/**
 * Created by asier on 20/04/2017.
 */

public class ArticleViews {

    private final Articulo art;

    public ArticleViews(Articulo art) {
        this.art = art;
    }

    private LinearLayout getBase(final Context cont) {
        //Main layout
        LinearLayout x = new LinearLayout(cont);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        x.setLayoutParams(lp);
        x.setOrientation(LinearLayout.VERTICAL);
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        x.setBackground(border);
        x.setPadding(20, 20, 20, 20);

        //nombre
        TextView nombre = new TextView(x.getContext());
        nombre.setText(art.getTitulo());
        if (art.isEstado() == 1) {
            Log.i("", "");
            //nombre.setTextColor(Color.BLACK);
        } else {
            nombre.setTextColor(Color.RED);
        }
        nombre.setTypeface(null, Typeface.BOLD);


        //precioBase
        TextView inicial = new TextView(x.getContext());
        inicial.setText("Precio base:");
        inicial.setTypeface(null, Typeface.ITALIC);
        TextView precioInicial = new TextView(x.getContext());
        precioInicial.setText(String.format("%1$,.2f€", art.getPrecio()));


        //Separador
        View ruler = new View(x.getContext());
        ruler.setBackgroundColor(Color.BLACK);


        //Imagen
        final ImageView imagen = new ImageView(x.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 200);
        imagen.setLayoutParams(layoutParams);
        ImageTools.fillImageBitmap(art.getImagen(),imagen);


        //add views
        x.addView(nombre);
        x.addView(ruler, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        x.addView(inicial);
        x.addView(precioInicial);
        x.addView(imagen);
        //

        //x.setOnClickListener();
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleTools.selectedArticle = art;
                Fragment art = new ArticleDetailsFragment();
                MainActivity.getActualMainActivity().nextFragment(art);
            }
        });


        return x;
    }

    public LinearLayout getTinyView(final Context cont) {

        LinearLayout x = getBase(cont);

        //precioUltimo

        TextView ultimo = new TextView(x.getContext());
        ultimo.setText("Último:");
        ultimo.setTypeface(null, Typeface.ITALIC);

        //last Bid
        final TextView ultimoPrecio = new TextView(x.getContext());

        final ArrayList<Puja> pujasArt = new ArrayList<>();
        BidLooper.forEachBidOnChange(new BidExec() {
            @Override
            public void execAction(Puja p) {
                if(p.getIdArt().equals(art.getTitulo())){
                    pujasArt.add(p);
                }
            }

            @Override
            public void onFinish() {
                Puja p = BidTools.getHigherBidFromList(pujasArt);
                if(p!=null){
                    art.setMax_puja(p.getPrecio());
                    ultimoPrecio.setText(String.format("%1$,.2f€", p.getPrecio()));
                }else{
                    ultimoPrecio.setText("Sin pujas");
                }
            }
        });

        x.addView(ultimo);
        x.addView(ultimoPrecio);
        return x;
    }

    public LinearLayout getBidView(final Context cont) {

        LinearLayout x = getBase(cont);

        //Separador
        View ruler = new View(x.getContext());
        ruler.setBackgroundColor(Color.BLUE);
        x.addView(ruler, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));


        //mis pujas
        TextView inicial = new TextView(x.getContext());
        inicial.setText("Mis pujas:");
        inicial.setTypeface(null, Typeface.BOLD_ITALIC);
        x.addView(inicial);


        //Separador
        View ruler2 = new View(x.getContext());
        ruler2.setBackgroundColor(Color.BLUE);
        x.addView(ruler2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));


        //Pujas
        int i = 1;
        for (Puja p : art.getArt_pujas()) {
            TextView precioPuja = new TextView(x.getContext());
            precioPuja.setText(i + ": " + String.format("%1$,.2f€", p.getPrecio()));
            i++;
            x.addView(precioPuja);
        }


        return x;
    }
}
