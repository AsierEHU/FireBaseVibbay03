package com.example.asier.vibbay03;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asier.vibbay03.Fragments.AllArticlesFragment;
import com.example.asier.vibbay03.Fragments.MyBidsFragment;
import com.example.asier.vibbay03.Fragments.SearchedArticlesFragment;
import com.example.asier.vibbay03.Fragments.LoginFragment;
import com.example.asier.vibbay03.Fragments.MyArticlesFragment;
import com.example.asier.vibbay03.Fragments.NewArticleFragment;
import com.example.asier.vibbay03.Tools.LoginFireBaseTool;

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity ma;
    private NavigationView navigationView;
    Stack<Fragment> framgentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        framgentStack = new Stack<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ma = this;

        nextFragment(new AllArticlesFragment());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout lay = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(lay.isDrawerOpen(GravityCompat.START)){
            lay.closeDrawers();
        }else{
            lastFragment();
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
        if (id == R.id.action_search) {
            SearchView sv = new SearchView(getSupportActionBar().getThemedContext());
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setActionView(item, sv);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    SearchedArticlesFragment frag = new SearchedArticlesFragment();
                    frag.showSearchedArticles(query);
                    nextFragment(frag);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
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
                    nextFragment(frag);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getActualMainActivity() {
        return ma;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId()) {
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
            case R.id.nav_myBids:
                fragment = new MyBidsFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_main:
                fragment = new AllArticlesFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_logout:
                LoginFireBaseTool.loggedIn=null;
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_myBids).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_myArticles).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_newArticle).setVisible(false);
                final TextView textViewChange = (TextView)navigationView.findViewById(R.id.bienvenidaID);
                textViewChange.setText("¡Bienvenido a Vibbay03!");
                fragment = new AllArticlesFragment();
                fragmentTransaction = true;
                break;
        }

        if (fragmentTransaction) {
            nextFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void lastFragment() {

        if(framgentStack.size()>1){
            framgentStack.pop();
            Fragment x = framgentStack.peek();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.include_main, x)
                    .commit();
        }else{
            new AlertDialog.Builder(this)
                    .setMessage("¿Seguro que quieres salir?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void nextFragment(Fragment x) {
        framgentStack.push(x);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.include_main, x)
                .commit();
    }

}
