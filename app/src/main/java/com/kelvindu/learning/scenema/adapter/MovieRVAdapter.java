package com.kelvindu.learning.scenema.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.activity.DetailActivity;
import com.kelvindu.learning.scenema.model.movielist.Result;

import io.realm.RealmResults;

/**
 * When you trying to understand what is adapter, just think it of it as the bridge
 * if you relate it with MVC it's basically the C part of the MVC where you decide the things that should be in the Views
 * so yeah this part here is basically the core logic that will hook your data into the views
 * keep that in mind
 */

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.ViewHolder> {

    //depending on what data you used, you still going to need the context
    private Context context;
    //this could be anyting depend on what kinda method you want to use to hook the data
    private RealmResults<Result> temp;

    //this is when some changes are happening with the database you want to notify the adapter so that
    //the adapter goes through all the process in hooking up the views

    public void setTemp(RealmResults<Result> temp) {
        this.temp = temp;
        notifyDataSetChanged();
    }

    public MovieRVAdapter(Context context, RealmResults<Result> result) {
        this.context = context;
        this.temp = result;
    }

    //you know that android needs the onCreate for everything right? especially for the views
    //this onCreate is the part where it creates those "card looking movie list"
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cards,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieRVAdapter.ViewHolder holder, int position) {
        //this is where you will spend the rest of your building time for adapters (the rest is just a matter of shortcuts
        //small changes, however on this part of method you are required to hook the views that you delcared on ViewHolder into the
        //actual data itself. not hard, just some redundant stuff.
        //to take notes that if the getItemCount returns 0 this method will not going to get called at all

        holder.title.setText(temp.get(position).getTitle());
        holder.date.setText(temp.get(position).getReleaseDate());
        holder.overview.setText(temp.get(position).getOverview());
        Glide.with(context).load(temp.get(position).getPoster_path())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_not_loaded)
                .skipMemoryCache(false)
                .into(holder.poster);
    }

    //if your view doesn't show up you probably messing up with this short line method
    //somehow you make it returning 0 all the time so it's practically useless no matter how much notifyDataSetChanged is happening

    @Override
    public int getItemCount() {
        if(this.temp != null)
            return this.temp.size();
        return 0;
    }

    //ViewHolders can be declared outside the adapter class, but I prefer to do it here, you don't need to think about
    //naming these view holders anyway if you do it in here
    //imagine if you have like maybe 10 - 15 adapters
    //can you still come up with some unique names for each adapter?
    //well I'm sure you can but I think it would make it more easier and private to do it here and call it ViewHolder
    //it functions the same as the ViewHolder anyway so you won't need to worry if something is wrong if you create this
    //object using ViewHolder class

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView date;
        TextView overview;
        ImageView poster;

        //what a ViewHolder does is essentially bind the views and listeners then return it
        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            date = (TextView) itemView.findViewById(R.id.movie_date);
            overview = (TextView) itemView.findViewById(R.id.movie_overview);
            poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        //implements the onClickListener interface or some custom listeners is all up to you
        //and the requirements or needs you need to do
        @Override
        public void onClick(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), DetailActivity.class)
                    .putExtra("movieId",temp.get(getAdapterPosition()).getId()));
        }
    }
}
