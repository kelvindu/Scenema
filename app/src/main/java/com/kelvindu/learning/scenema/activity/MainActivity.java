package com.kelvindu.learning.scenema.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.fragment.MovieListFragment;

/*
* Hello me from some distant future
*
* one thing or another maybe you will end up looking at this code again, and as they say don't fall into the same pit twice
* these are some of the comments that will probably going to help you to understanding how to build this long
* tiring app from scratch again, if you choose to do so.
* so sit back enjoy reading this whole spaghetti code that you made a while ago, I know you probably now able to code more decent
* than this one, but in case if you haven't shame on you my future self!
*
* from your beloved past
* Author : Kelvin Du
* */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bundle bundle = new Bundle();//this is the new bundle object to store new arguments to pass it into another fragments/activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //these stuff are templates don't fret about it, and don't delete it.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this is the method you used to fire up the initial views
        if (savedInstanceState == null){
            setViews("now_playing","Now Playing");
        }

        //this is the template for the navigation pane and drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void onBackPressed() {
        //this is the on back pressed listener
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_now_playing:
                setViews("now_playing","Now Playing");
                break;
            case R.id.nav_popular:
                setViews("popular","Popular");
                break;
            case R.id.nav_top_rated:
                setViews("top_rated","Top Rated");
                break;
            case R.id.nav_upcoming:
                setViews("upcoming","Upcoming");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setViews(String category,String title){
        // the set views method is basically just a fragment transaction hooked into the bundle
        // and firing up replacing the id container in the main activity.
        MovieListFragment movieListFragment = new MovieListFragment();
        bundle.putString("title",title);
        bundle.putString("category",category);
        movieListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,movieListFragment);
        fragmentTransaction.commit();
    }
}
