package com.kelvindu.learning.scenema.fragment;


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
import com.kelvindu.learning.scenema.adapter.CommentRVAdapter;
import com.kelvindu.learning.scenema.adapter.MovieRVAdapter;
import com.kelvindu.learning.scenema.model.api.MovieService;
import com.kelvindu.learning.scenema.model.comment.Comments;
import com.kelvindu.learning.scenema.model.comment.ResultComment;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * just like trailer fragment this too is way too similar with the MovieListFragment,
 * just read that one....
 *
 * I'm too lazy to repeat myself here
 */
public class CommentFragment extends Fragment {

    public static final String BASE_URL = "http://api.themoviedb.org/";
    public static final String ARGS_ID = "id";
    RecyclerView recyclerView;
    CommentRVAdapter commentRVAdapter;
    Realm realm = Realm.getDefaultInstance();


    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_comments);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(20);
        getAllComment(String.valueOf(getArguments().getInt(ARGS_ID)));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllComment(String.valueOf(getArguments().getInt(ARGS_ID)));
    }

    private void getAllComment(final String movieId){
        if(commentRVAdapter == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieService movieService = retrofit.create(MovieService.class);
            movieService.getComments(movieId).enqueue(new Callback<Comments>() {
                @Override
                public void onResponse(Call<Comments> call, Response<Comments> response) {
                    Comments comments = response.body();
                    realm.beginTransaction();
                    getResuts().deleteAllFromRealm();
                    for(int i = 0;i<comments.getResults().size();i++){
                        ResultComment rc = realm.createObject(ResultComment.class);
                        rc.setMovieId(movieId);
                        rc.setId(comments.getResults().get(i).getId());
                        rc.setAuthor(comments.getResults().get(i).getAuthor());
                        rc.setContent(comments.getResults().get(i).getContent());
                    }
                    realm.commitTransaction();
                }

                @Override
                public void onFailure(Call<Comments> call, Throwable t) {
                    Log.e("factory","fail "+t.getMessage());
                }
            });

            if (recyclerView.getAdapter() == null || commentRVAdapter == null){
                commentRVAdapter = new CommentRVAdapter(getContext(),getResuts());
                recyclerView.setAdapter(commentRVAdapter);
            }
            else commentRVAdapter.setTemp(getResuts());
        }
    }

    private RealmResults<ResultComment> getResuts(){
        RealmResults<ResultComment> r = realm
                .where(ResultComment.class)
                .equalTo("movieId",String.valueOf(getArguments().getInt("id")))
                .findAll();
        return r;
    }

}
