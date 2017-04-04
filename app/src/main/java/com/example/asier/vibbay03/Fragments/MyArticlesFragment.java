package com.example.asier.vibbay03.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.R;
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

import java.util.Iterator;

public class MyArticlesFragment extends Fragment {

    GridLayout fl;


    public MyArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fl = (GridLayout) inflater.inflate(R.layout.fragment_allarticles, container, false);
        return fl;

    }
    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Articulos").child(LoginFireBaseTool.loggedIn.getEmail());
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fl.removeAllViews();
                Iterator<DataSnapshot> it2 = dataSnapshot.getChildren().iterator();
                while (it2.hasNext()) {
                    DataSnapshot ds = it2.next();
                    Articulo a = ds.getValue(Articulo.class);
                    LinearLayout x = new LinearLayout(getContext());
                    TextView nombre = new TextView(x.getContext());
                    TextView precio = new TextView(x.getContext());
                    final ImageView imagen = new ImageView(x.getContext());
                    nombre.setText(a.getTitulo());
                    StorageReference httpsReference = storage.getReferenceFromUrl(a.getImagen());
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
                    Log.i("imagen", a.getImagen());
                    Log.i("precio", String.valueOf(a.getPrecio()));
                    precio.setText(String.valueOf(a.getPrecio()));

                    x.addView(nombre);
                    x.addView(precio);
                    x.addView(imagen);
                    fl.addView(x);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Login error", databaseError.getDetails());
                Log.i("Login message", databaseError.getMessage());
            }
        });
    }


}
