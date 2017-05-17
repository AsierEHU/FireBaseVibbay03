package com.example.asier.vibbay03.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewArticleFragment extends Fragment {

    private int GALLERY_REQUEST = 1;
    private int CAMERA_REQUEST = 0;
    private ImageView imageView;
    private ImageView galleryView;
    private ImageView resultImage;
    boolean hasUploadedImage = false;
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
        galleryView = (ImageView) ll.findViewById(R.id.galleryTaken);
        resultImage = (ImageView) ll.findViewById(R.id.resultImage);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(Intent.createChooser(cameraIntent, "Saca una foto"), CAMERA_REQUEST);
            }
        });
        galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent();
                cameraIntent.setType("image/*");
                cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(cameraIntent, "Elige de la galería"), GALLERY_REQUEST);
            }
        });

        return ll;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
    }

    private void addArticle(){
        nombre = (EditText) ll.findViewById(R.id.ArticleName);
        precio = (EditText) ll.findViewById(R.id.ArticlePrice);

        if(nombre.getText().toString().matches("") || precio.getText().toString().matches("") || !hasUploadedImage) {
            Toast.makeText(ll.getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String name = LoginFireBaseTool.loggedIn.getEmail() + new Date().getTime();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://vibbay03-4f198.appspot.com/articulos/"+name+".jpg");

            // Get the data from an ImageView as bytes
            resultImage.setDrawingCacheEnabled(true);
            resultImage.buildDrawingCache();
            Bitmap bitmap = resultImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("ERROR ONUPLOAD",exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //   DecimalFormat df = new DecimalFormat("#.00");
                    //    String pri = df.format(Double.parseDouble(precio.getText().toString()));

                    String pri = precio.getText().toString();

                    Articulo a = new Articulo(nombre.getText().toString(),1,downloadUrl.toString(),Double.parseDouble(pri));
                    Map<String, Object> articuloValues = a.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/Articulos/"+LoginFireBaseTool.loggedIn.getEmail()+"/"+nombre.getText(),articuloValues);
                    mDatabase.updateChildren(childUpdates);

                    nombre.setText("");
                    precio.setText("");
                    hasUploadedImage = false;
                    resultImage.setImageResource(R.drawable.ic_imagebox);
                    Toast toast = Toast.makeText(getContext(), "Nuevo artículo creado", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                if(requestCode == CAMERA_REQUEST ){
                    if(data.getData()==null){
                        bm = (Bitmap)data.getExtras().get("data");
                    }else{
                        bm = ImageTools.getResizedBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri),600);
                    }
                }else{
                    bm = ImageTools.getResizedBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri),600);
                }
                hasUploadedImage = true;
                resultImage.setImageBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}