package com.example.asier.vibbay03.Tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.google.firebase.database.DataSnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by asier on 12/05/2017.
 */

public class BidTools {

    public static AlertDialog createDialog(final Articulo articulo, final Context cont){
        AlertDialog.Builder alert = new AlertDialog.Builder(cont);
        alert.setTitle("Nueva puja");
        alert.setMessage("Indique el precio de su puja");
        final EditText input = new EditText(cont);
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    double price = Double.valueOf(input.getText().toString());
                    if (price <= Math.max(articulo.getMax_puja(), articulo.getPrecio())) {
                        Toast toast = Toast.makeText(cont, "La puja debe ser mayor que " + String.format("%1$,.2f€", Math.max(articulo.getMax_puja(), articulo.getPrecio())), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        ArticleTools.pujar(articulo, price);
                        Toast toast = Toast.makeText(cont, "Puja realizada correctamente", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (Exception e) {
                    Toast toast = Toast.makeText(cont, "Error, ninguna puja insertada", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        return alert.create();
    }

    public static void orderListByPrice(List<Puja> pujas){
        Collections.sort(pujas, new Comparator<Puja>() {
            @Override
            public int compare(Puja o1, Puja o2) {
                if(o1.getPrecio() > o2.getPrecio()){
                    return 1;
                }else if(o1.getPrecio() < o2.getPrecio()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

     public static Puja getHigherBidFromList(List<Puja> pujas) {
         Puja biggerBidClass = null;
         double biggerBid = 0;

         for(Puja p: pujas){
             if(p.getPrecio() > biggerBid){
                 biggerBid = p.getPrecio();
                 biggerBidClass = p;
             }
         }

         return biggerBidClass;
     }

}
