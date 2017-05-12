package com.example.asier.vibbay03.Tools;

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
