package com.example.asier.vibbay03.Fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.asier.vibbay03.Beans.Usuario;
import com.example.asier.vibbay03.R;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {


    EditText username;
    EditText password;
    Button buttonClick;
    GridLayout fL;
    NavigationView navigation;
    Fragment allArticles;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    testLogin();}
        };
        fL = (GridLayout) inflater.inflate(R.layout.fragment_login, container, false);
        navigation = (NavigationView)container.getRootView().findViewById(R.id.nav_view);
        buttonClick = (Button)fL.findViewById(R.id.buttonAccept);
        buttonClick.setOnClickListener(mClickListener);
        return fL;
    }
    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        Log.i("Fragment","Login fragment terminado");
    }


    public void testLogin(){

        username = (EditText)fL.findViewById(R.id.login_username);
        password = (EditText)fL.findViewById(R.id.login_password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Usuarios");
        reference.child(username.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                Toast loginMessage;
                if (u.getPassword().equals(password.getText().toString())) {
                    Log.i("Login", "Correcto");
                    LoginFireBaseTool.loggedIn = u;
                    loginMessage = Toast.makeText(getContext(), "LOGIN CORRECTO", Toast.LENGTH_SHORT);
                    allArticles = new AllArticlesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    navigation.getMenu().findItem(R.id.nav_login).setVisible(false);
                    navigation.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    navigation.getMenu().findItem(R.id.nav_myArticles).setVisible(true);
                    navigation.getMenu().findItem(R.id.nav_newArticle).setVisible(true);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include_main, allArticles);
                    fragmentTransaction.commit();
                } else {
                    Log.i("Login", "No hay usuario");
                    loginMessage = Toast.makeText(getContext(), "USER OR PASSWORD INVALID", Toast.LENGTH_SHORT);
                }
                loginMessage.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Login error", databaseError.getDetails());
                Log.i("Login message", databaseError.getMessage());
            }
        });
    }
}
