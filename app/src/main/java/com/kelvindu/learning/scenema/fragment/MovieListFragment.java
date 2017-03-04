package com.kelvindu.learning.scenema.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.activity.MainActivity;
import com.kelvindu.learning.scenema.adapter.MovieRVAdapter;
import com.kelvindu.learning.scenema.model.api.MovieService;
import com.kelvindu.learning.scenema.model.movielist.MovieList;
import com.kelvindu.learning.scenema.model.movielist.Result;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Wohoo! this is where the actual fun(headache) begins
 *
 * as you can see all my capabilities as I am right now just able to get by with the funcionalities
 * through these spagetti code.
 * hope you can do this one better
 *
 */
public class MovieListFragment extends Fragment {

    //constants are necesarry, you don't want to mistype the string value did you?
    public static final String BASE_URL = "http://api.themoviedb.org/";
    public static final String ARGS_TITLE = "title";
    public static final String ARGS_CATEGORY = "category";

    //declarations for views, recycler view, adapters, optionals: progress dialog, refresh layout, viewcreated and refresh data
    private Toolbar toolbar;
    private View mFragment;
    private ProgressDialog pDialog;
    RecyclerView recyclerView;
    private MovieRVAdapter movieRVAdapter;
    private SwipeRefreshLayout refreshLayout;
    public boolean refreshData,viewCreated;
    //realm just need to get the default instance for declarations
    Realm realm = Realm.getDefaultInstance();

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getArguments().getString(ARGS_TITLE));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        pDialog = new ProgressDialog(getContext());

        //refresh layout is object from swipe refresh layout, refreshing might sounds hard but it's basically
        //just set the listener in a container, set refreshData true and call the retrieve data method
        refreshLayout = (SwipeRefreshLayout) mFragment.findViewById(R.id.refresh_container);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                refreshData = true;
                getMovieList();
            }
        });

        //steps to declare recycler view
        //1. hook the view with ID (like always)
        //2. set the layout manager, sometimes you need linear sometimes grid
        //3. Set adapters but you might want to do it a bit later in time
        //4. set the animator,fixed size, view caches are just basically how you want to caches and manipulate the animation

        recyclerView = (RecyclerView)mFragment.findViewById(R.id.recycler_movies);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        return mFragment;
    }

    //on view created is the "safe way" you want to manipulate things that require you to make views that can be null
    //for example, the toolbar thing you see there, it is an onClick listeners that will manipulate the recyclerview so
    //in sense it must be declared after the recycler view is up and running
    //recycler view can be put in here instead onCreated should you choose to do so

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager llm = (LinearLayoutManager)recyclerView.getLayoutManager();
                llm.smoothScrollToPosition(recyclerView,null,0);
            }
        });
    }

    //on resume is just like on connected etc. it's when the screen returns back to your app
    @Override
    public void onResume() {
        super.onResume();
        getMovieList();
    }

    //this is when the layout is visible to the app A.K.A. when your app is wide opened
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && viewCreated){
            getMovieList();
        }
    }

    //when view destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreated = false;
    }

    private void getMovieList (){
        //the basic steps for canceling out the refresh animation is to set the refreshing from true to false
        if(!refreshLayout.isRefreshing()){//when refreshing is false
            refreshLayout.setRefreshing(true);
        }
        MovieService movieService = retrofitConnect();
        movieService.getMovie(getArguments().getString(ARGS_CATEGORY)).enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                RealmResults<Result> results = getResult();
                //to begin querying up the data into the database you must begin transaction and end it
                realm.beginTransaction();
                results.deleteAllFromRealm();//the alternatives can be called from getResults instead of results
                for (int i = 0; i < movieList.getResults().size(); i++){
                    Result r = realm.createObject(Result.class);
                    r.setId(movieList.getResults().get(i).getId());
                    r.setTitle(movieList.getResults().get(i).getTitle());
                    r.setOriginalTitle(movieList.getResults().get(i).getOriginalTitle());
                    r.setPoster_path(movieList.getResults().get(i).getPoster_path());
                    r.setOverview(movieList.getResults().get(i).getOverview());
                    r.setReleaseDate(movieList.getResults().get(i).getReleaseDate());
                    r.setVoteAverage(movieList.getResults().get(i).getVoteAverage());
                    //this is not in the api that were pulled from the retrofit but you need to classify the movie lists
                    r.setCategory(getArguments().getString(ARGS_CATEGORY));
                }
                realm.commitTransaction();
                //close the transaction
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.e("factory","fail "+t.getMessage());
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        //this is the part where you hook the adapter into the recycler view,
        //though you can actually do that initially but it won't really make any different since the adapter would have
        //reload again with new sets of data
        if(recyclerView.getAdapter() == null || movieRVAdapter == null || refreshData){
            movieRVAdapter = new MovieRVAdapter(getContext(),getResult());
            recyclerView.setAdapter(movieRVAdapter);
        } else
            movieRVAdapter.setTemp(getResult());

        refreshData = false;
        refreshLayout.setRefreshing(false);
    }

    // Retrofit workflow is as following
    // 1. create a new Retrofit.Builder with base url of the api you want to pull
    // 2. build the retrofit object with GsonConverter (maybe a different converter depends on your requirement
    // 3. creating a movie service class, created from retrofit
    // 4. if you required to call those steps above more than one, might as well making it a method on it's own
    // 5. it'll ease you out

    private  MovieService retrofitConnect(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        return movieService;
    }

//    now now, why is this RealmResult queries must be a method on it's own you ask?
//    because this method is like one of the method that you will call ALOT in this class
//    which one you prefer?
//    typing all of those realm.where.blabla().blabla() or to just simply call getResult()?

    private RealmResults<Result> getResult(){
        RealmResults <Result> r = realm.where(Result.class)
                .equalTo(ARGS_CATEGORY,getArguments().getString(ARGS_CATEGORY))
                .findAll();

        return r;
    }
}
