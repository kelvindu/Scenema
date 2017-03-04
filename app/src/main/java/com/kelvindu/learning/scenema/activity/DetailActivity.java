package com.kelvindu.learning.scenema.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.fragment.CommentFragment;
import com.kelvindu.learning.scenema.fragment.TrailerFragment;
import com.kelvindu.learning.scenema.model.movielist.MovieList;
import com.kelvindu.learning.scenema.model.movielist.Result;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;

public class DetailActivity extends AppCompatActivity {

    private Bundle bundle = new Bundle();
    private Call<MovieList> call;

    private Realm realm = Realm.getDefaultInstance();
    Result thisMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrieveDetails();
        setViews(getIntent().getExtras().getInt("movieId"));
    }

    //this method overwrites the default navigate up press and calling the back button which in this case is what we want
    //we want the back button and the navigate up to be functioning the same
    @Override
    public boolean onSupportNavigateUp() {
        if(call != null) call.cancel();
        onBackPressed();
        return true;
    }

    //the real meat of back button lies in this code, in order to NOT fire up the MainActivity and lost the previous list we were in
    //we must use the fragment manager, all it does is simply managing to return to the previous fragment if there are some in the stack
    //nothing really complicated here
    public void onBackPressed() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            Log.i("Fragment Manager","Popping backpack");
            fragmentManager.popBackStack();
        }else{
            Log.i("Fragment Manager","Nothing to pop stackers!");
            super.onBackPressed();
        }
    }

    //this is the method that will retrieve all of the datas in the database and hook it up to the list

    private void retrieveDetails(){

        final TextView title = (TextView)findViewById(R.id.title_detail)
                , releaseDate = (TextView)findViewById(R.id.date_detail)
                , overview = (TextView)findViewById(R.id.overview_detail)
                , rating = (TextView) findViewById(R.id.rating_detail);

        ImageView poster = (ImageView)findViewById(R.id.poster_detail);

        //basically it's like saying SELECT * FROM TABLES WHERE id=id
        //to get the matching rows of the data
        thisMovie = realm.where(Result.class)
                .equalTo("id",getIntent().getExtras().getInt("movieId"))
                .findFirst();

        String temp = thisMovie.getVoteAverage() + "/10";

        getSupportActionBar().setTitle(thisMovie.getTitle());
        title.setText(thisMovie.getOriginalTitle());
        releaseDate.setText(thisMovie.getReleaseDate());
        overview.setText(thisMovie.getOverview());
        rating.setText(temp);

        Glide.with(this).load(thisMovie.getPoster_path())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .placeholder(R.drawable.image_not_loaded)
                .skipMemoryCache(false)
                .into(poster);
    }

//    Okay now you probably asking (no you're not you're confused)
//    why does this method must be separated from the getAllDetails, since this one also basically just hooking up
//    some data to the views
//
//    it turns out that on this side of the code is where we fire up a fragment and from there pull some data
//    (since all the MainActivity does is pulling the movie list not the trailer or the comments)
//    so we have to call on the retrofit once again and this time, we might not going to get the data we want.
//
//    so that the data we cached locally can show up first we basically split the method.
//    Make sense?

    public void setViews(int id){
        bundle.putInt("id",id);
        TrailerFragment trailerFragment = new TrailerFragment();
        CommentFragment commentFragment = new CommentFragment();

        //created trailer fragment to contain a list of the trailers stored
        trailerFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.trailer_container,trailerFragment);
        fragmentTransaction.commit();

        commentFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.comment_container,commentFragment);
        fragmentTransaction.commit();
    }

}
