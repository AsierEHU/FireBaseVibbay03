package com.example.asier.vibbay03.Tools;

import android.util.Log;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.Beans.Puja;
import com.example.asier.vibbay03.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by asier on 02/05/2017.
 */

public class ArticleTools {
    public static Articulo selectedArticle;

    public static Puja getUserArtHigherPrice(DataSnapshot pujasArts, String userId, String artId){
        Puja biggerBidClass = null;
        double biggerBid = 0;

                Iterator<DataSnapshot> bids = pujasArts.getChildren().iterator();
                while (bids.hasNext()) {
                    DataSnapshot ds = bids.next();
                    Puja p = ds.getValue(Puja.class);

                    if (p.getPrecio() > biggerBid) {
                        p.setIdArt(artId);
                        p.setIdUsuario(userId);
                        biggerBid = p.getPrecio();
                        biggerBidClass = p;
                    }
                }

        return biggerBidClass;

    }

    public static Puja getHigherBidPrice(Articulo art, DataSnapshot pujas) {

        Puja biggerBidClass = null;
        double biggerBid = 0;

        Iterator<DataSnapshot> users = pujas.getChildren().iterator();
        while (users.hasNext()) {
            DataSnapshot user = users.next();
            String userId = user.getKey();
            Iterator<DataSnapshot> articles = user.getChildren().iterator();
            while (articles.hasNext()) {
                DataSnapshot article = articles.next();
                String artId = article.getKey();
                if (artId.equals(art.getTitulo())) {
                    Iterator<DataSnapshot> bids = article.getChildren().iterator();
                    while (bids.hasNext()) {
                        DataSnapshot ds = bids.next();
                        Puja p = ds.getValue(Puja.class);

                        if (p.getPrecio() > biggerBid) {
                            p.setIdArt(artId);
                            p.setIdUsuario(userId);
                            biggerBid = p.getPrecio();
                            biggerBidClass = p;
                        }
                    }
                }

            }
        }
        return biggerBidClass;
    }

}
