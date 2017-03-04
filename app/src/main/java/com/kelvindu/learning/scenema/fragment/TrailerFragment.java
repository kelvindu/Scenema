package com.kelvindu.learning.scenema.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.adapter.TrailerRVAdapter;
import com.kelvindu.learning.scenema.model.api.MovieService;
import com.kelvindu.learning.scenema.model.trailerlist.ResultTrailer;
import com.kelvindu.learning.scenema.model.trailerlist.TrailerList;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a list of Items.
 * interface.
 *
 * that comment above is the reminder for you if you think you want ot declare a weird type of fragments
 * in hopes that it will make the job easier, think again!
 * better declare an empty fragment and building it from scratch if you're fragment is not too complex
 *
 * well as for the rest, this one is basically the same stuff with the movie fragment
 *
 */
public class TrailerFragment extends Fragment {

    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String ARGS_ID = "id";
    public static final String MOVIE_ID = "movieId";
    private Realm realm = Realm.getDefaultInstance();
    private RecyclerView recyclerView;
    private TrailerRVAdapter trailerRVAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrailerFragment() {
    }

    /**
     * maybe I have said it countless times, but don't make stuff that can go null in the onCreate if you don't want any
     * unwanted headaches
     * */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailer_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_trailer);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(10);
        getTrailerList(String.valueOf(getArguments().getInt(ARGS_ID)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTrailerList(String.valueOf(getArguments().getInt(ARGS_ID)));
    }

    //you want the details of these stuff? go to MovieListFragment

    private void getTrailerList(final String movieId){
        //if the adapter is not null it doesn't make sense to call this whole retrofit, caching process anyway
        if(trailerRVAdapter == null){
            MovieService movieService = retrofitConnect();
            movieService.getTrailers(movieId).enqueue(new Callback<TrailerList>() {
                @Override
                public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                    TrailerList trailerList = response.body();
                    RealmResults<ResultTrailer> results = getResult();
                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    for (int i = 0; i < trailerList.getResults().size(); i++) {
                        ResultTrailer rt = realm.createObject(ResultTrailer.class);
                        rt.setMovieId(movieId);
                        rt.setId(trailerList.getResults().get(i).getId());
                        rt.setKey(trailerList.getResults().get(i).getKey());
                        rt.setName(trailerList.getResults().get(i).getName());
                    }
                    realm.commitTransaction();
                    trailerRVAdapter.setTemp(getResult());
                }

                @Override
                public void onFailure(Call<TrailerList> call, Throwable t) {
                    Log.e("factory_trailer","No data inserted into the list");
                }
            });

            if(recyclerView.getAdapter() == null || trailerRVAdapter == null){
                trailerRVAdapter = new TrailerRVAdapter(getContext(),getResult());
                recyclerView.setAdapter(trailerRVAdapter);
            }
            else trailerRVAdapter.setTemp(getResult());
        }
    }

    private RealmResults<ResultTrailer> getResult(){
        return realm.where(ResultTrailer.class)
                .equalTo(MOVIE_ID,String.valueOf(getArguments().getInt(ARGS_ID)))
                .findAll();
    }

    private MovieService retrofitConnect(){
        Retrofit r = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService m = r.create(MovieService.class);
        return m;
    }

}
