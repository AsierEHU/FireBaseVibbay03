package com.example.asier.vibbay03;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.asier.vibbay03.Fragments.AllArticlesFragment;
import com.example.asier.vibbay03.Fragments.SearchedArticlesFragment;
import com.example.asier.vibbay03.Fragments.LoginFragment;
import com.example.asier.vibbay03.Fragments.MyArticlesFragment;
import com.example.asier.vibbay03.Fragments.NewArticleFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Llamar a AllArticlesFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.include_main, new AllArticlesFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id== R.id.action_search){
            SearchView sv = new SearchView(getSupportActionBar().getThemedContext());
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setActionView(item, sv);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    SearchedArticlesFragment frag = new SearchedArticlesFragment();
                    frag.showSearchedArticles(query);
                    Log.i("Cambios","ENTRA AL SUBIR");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.include_main, frag)
                            .commit();
                    Log.i("Cambios","sale del subir");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    /*SearchedArticlesFragment frag = new SearchedArticlesFragment();
                    frag.showSearchedArticles(newText);
                    Log.i("Cambios","ENTRA AL SUBIR");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.include_main, frag)
                            .commit();*/
                    Log.i("Cambios","sale del subir");
                    return false;
                }
            });
            sv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    AllArticlesFragment frag = new AllArticlesFragment();
                    Log.i("Cambios","ENTRA AL cerrar");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.include_main, frag)
                            .commit();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_login:
                fragment = new LoginFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_myArticles:
                fragment = new MyArticlesFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_newArticle:
                fragment = new NewArticleFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_main:
                fragment = new AllArticlesFragment();
                fragmentTransaction = true;
                break;
        }

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.include_main, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
