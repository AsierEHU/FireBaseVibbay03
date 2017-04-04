package com.example.asier.vibbay03.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asier.vibbay03.Beans.Articulo;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.ImageTools;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewArticleFragment extends Fragment {

    private int GALLERY_REQUEST = 1;
    private int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    LinearLayout ll;
    EditText nombre;
    EditText precio;
    Button add;
    Bitmap bm;

    public NewArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ll = (LinearLayout) inflater.inflate(R.layout.fragment_newarticle, container, false);
        add = (Button) ll.findViewById(R.id.Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle();
            }
        });
        imageView = (ImageView) ll.findViewById(R.id.imageTaken);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// Con esto la cogemos de la galeria, camera_request tiene que ser 1
                //Intent cameraIntent = new Intent();
                //cameraIntent.setType("image/*");
                //cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(Intent.createChooser(cameraIntent, "Elige"), CAMERA_REQUEST);
                Log.i("CAMARA", "ENTRA A LO SIGUINTE");
            }
        });

        return ll;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        Log.i("Fragment", "Nuevo artículo mostrado");
    }

    private void addArticle(){
        nombre = (EditText) ll.findViewById(R.id.ArticleName);
        precio = (EditText) ll.findViewById(R.id.ArticlePrice);
        String foto64 = ImageTools.encodeToBase64(bm,Bitmap.CompressFormat.JPEG,20);
        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        //String name = LoginFireBaseTool.loggedIn.getEmail().substring(0,LoginFireBaseTool.loggedIn.getEmail().indexOf('@'))+new Date().getTime();
        String name = LoginFireBaseTool.loggedIn.getEmail() + new Date().getTime();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://vibbay03-4f198.appspot.com/articulos/"+name+".jpg");
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i("Link", downloadUrl.toString());

                Articulo a = new Articulo(nombre.getText().toString(),1,downloadUrl.toString(),Double.parseDouble(precio.getText().toString()));
                Map<String, Object> articuloValues = a.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Articulos/"+LoginFireBaseTool.loggedIn.getEmail()+"/"+nombre.getText(),articuloValues);
                mDatabase.updateChildren(childUpdates);

                //mDatabase.child("Articulos").setValue(LoginFireBaseTool.loggedIn.getEmail());
                //mDatabase.child("Articulos").child(LoginFireBaseTool.loggedIn.getEmail()).setValue(nombre.getText().toString());
                //mDatabase.child("Articulos").child(LoginFireBaseTool.loggedIn.getEmail()).child(nombre.getText().toString()).setValue(a);
                nombre.setText("");
                precio.setText("");
                imageView.setImageResource(R.drawable.ic_menu_camera);
                Toast toast = Toast.makeText(getContext(), "Nuevo artículo creado", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("VUELTA","ESTA VOLVIENDO AL MISMO ACTIVITY");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.i("VUELTA",uri.toString());
            try {
                bm = ImageTools.getResizedBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri),600);
                bm = ImageTools.rotateBitmap(bm,-90);
                // Log.d(TAG, String.valueOf(bitmap));
                imageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}