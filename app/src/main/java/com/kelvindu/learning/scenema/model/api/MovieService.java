package com.kelvindu.learning.scenema.model.api;

import com.kelvindu.learning.scenema.model.comment.Comments;
import com.kelvindu.learning.scenema.model.movielist.MovieList;
import com.kelvindu.learning.scenema.model.trailerlist.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit require you to declare an interface for using it's service, basically this is how the format of the Service
 *
 * GET ("some links you want to add to the base url")
 * Call the method (args, param) that is with data types corresponding with the gson object in models
 * if you think it that way it will be much more make sense to put this together in the models
 *
 */

public interface MovieService {
    @GET("3/movie/{category}?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<MovieList> getMovie(@Path("category") String category);

    @GET("3/movie/{id}/videos?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<TrailerList> getTrailers(@Path("id") String id);

    @GET("3/movie/{id}/reviews?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<Comments> getComments(@Path("id") String id);
}
