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

    public static List<Puja> getHistoricalBids(Articulo art, DataSnapshot pujas){
        ArrayList<Puja> pujaslist = new ArrayList<>();
        final HashMap<String,ArrayList<Puja>> pujasDiferentes = new HashMap<>();
        Iterator<DataSnapshot> usuarios = pujas.getChildren().iterator();
        while (usuarios.hasNext()) {
            DataSnapshot usuario = usuarios.next();
            String usu_id = usuario.getKey();
            Iterator<DataSnapshot> articulos = usuario.getChildren().iterator();
            while (articulos.hasNext()){
                DataSnapshot articuloDS = articulos.next();
                String art_id = articuloDS.getKey();
                if(art_id.equals(art.getTitulo())){
                    Iterator<DataSnapshot> pujasDS = articuloDS.getChildren().iterator();
                    while(pujasDS.hasNext()){
                        DataSnapshot puja = pujasDS.next();
                        Puja p = puja.getValue(Puja.class);
                        p.setIdUsuario(usu_id);
                        p.setIdArt(art_id);
                        pujaslist.add(p);
                    }
                }

            }
        }
        return pujaslist;
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
