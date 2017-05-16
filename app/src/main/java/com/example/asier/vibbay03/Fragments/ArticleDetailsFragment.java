package com.example.asier.vibbay03.Fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.asier.vibbay03.FBLoopers.ArticleExec;
import com.example.asier.vibbay03.FBLoopers.BidExec;
import com.example.asier.vibbay03.FBLoopers.BidLooper;
import com.example.asier.vibbay03.MainActivity;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ArticleTools;
import com.example.asier.vibbay03.Tools.BidTools;
import com.example.asier.vibbay03.Tools.ImageTools;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.example.asier.vibbay03.Views.ArticleViews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
        return sv;
    }


    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        SetViewAticle();
    }

    private void SetViewAticle() {
        final Articulo articulo = ArticleTools.selectedArticle;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //title
        TextView l = (TextView) sv.findViewById(R.id.titleArticle);
        l.setText(articulo.getTitulo());

        //image
        final ImageView i = (ImageView) sv.findViewById(R.id.imageArt);
        ImageTools.fillImageBitmap(articulo.getImagen(), i);

        //Price
        TextView ip = (TextView) sv.findViewById(R.id.initialprice);
        ip.setText("Precio inicial: " + String.format("%1$,.2f€", articulo.getPrecio()));

        //last Bid and show bids
        double lastBid = 0;
        final TextView lb = (TextView) sv.findViewById(R.id.lastbid);
        final LinearLayout ll = (LinearLayout) sv.findViewById(R.id.bidsLayout);

        final ArrayList<Puja> pujasArt = new ArrayList<>();
        BidLooper.forEachBidOnChange(new BidExec() {
            @Override
            public void execAction(Puja p) {
                if (p.getIdArt().equals(articulo.getTitulo())) {
                    pujasArt.add(p);
                }
            }

            @Override
            public void onFinish() {

                //last bid
                Puja p = BidTools.getHigherBidFromList(pujasArt);
                if (p != null) {
                    articulo.setMax_puja(p.getPrecio());
                    lb.setText("Puja más alta: " + String.format("%1$,.2f€", p.getPrecio()));
                }

                //show bids
                if (LoginFireBaseTool.loggedIn != null && articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
                    ((TextView) sv.findViewById(R.id.bidshistorical)).setVisibility(View.VISIBLE);
                    BidTools.orderListByPrice(pujasArt);
                    ll.removeAllViews();
                    if (pujasArt.isEmpty()) {
                        TextView tv = new TextView(getContext());
                        tv.setText("No existen pujas");
                        ll.addView(tv);
                    } else {
                        for (Puja p1 : pujasArt) {
                            TextView tv = new TextView(getContext());
                            tv.setText(p1.getIdUsuario() + " - " + String.format("%1$,.2f€", p1.getPrecio()));
                            ll.addView(tv);
                        }
                    }
                }
            }
        });

        //My last bid
        if (LoginFireBaseTool.loggedIn != null && !articulo.getUserId().equals(LoginFireBaseTool.loggedIn.getEmail())) {
            final TextView mlb = (TextView) sv.findViewById(R.id.mylastbid);
            final ArrayList<Puja> pujasUserArt = new ArrayList<>();

            BidLooper.forEachBidUserArticleOnce(new BidExec() {
                @Override
                public void execAction(Puja p) {
                    pujasUserArt.add(p);
                }

                @Override
                public void onFinish() {
                    Puja p = BidTools.getHigherBidFromList(pujasUserArt);
                    if (p != null) {
                        mlb.setText("Mi puja más alta: " + String.format("%1$,.2f€", p.getPrecio()));
                    }
                }
            }, articulo, LoginFireBaseTool.loggedIn.getEmail());
        }


        //Generic Button
        final Button b = (Button) sv.findViewById(R.id.genericbutton);

        //Login ( si no estas loggueado)
        if (LoginFireBaseTool.loggedIn == null) {
            b.setText("Login");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getActualMainActivity().nextFragment(new LoginFragment());
                }
            });

            //Pujar ( si el articulo no es tuyo)
        } else if (!LoginFireBaseTool.loggedIn.getEmail().equals(articulo.getUserId())) {
            final AlertDialog ad = BidTools.createDialog(articulo,getContext());
            ArticleTools.onArticleStateChange(articulo, new ArticleExec() {
                @Override
                public void execAction(Articulo a) {

                    if (a.isEstado() == 1) {
                        b.setText("Pujar");
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.show();
                            }
                        });
                    } else {
                        ad.cancel();
                        b.setText("Pujas cerradas");
                        b.setEnabled(false);
                    }
                }

                @Override
                public void onFinish() {
                }
            });

            //Cerrar pujas ( si el articulo es tuyo)
        } else {
            if (articulo.isEstado() == 0) {
                b.setText("Pujas cerradas");
                b.setEnabled(false);
            } else {
                b.setText("Cerrar pujas");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.getReference("Articulos/" + articulo.getUserId() + "/" + articulo.getTitulo() + "/estado").setValue(0);
                        b.setText("Pujas cerradas");
                        b.setEnabled(false);
                    }
                });
            }
        }

    }

}
