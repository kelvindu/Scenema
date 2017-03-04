package com.kelvindu.learning.scenema.adapter;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.model.trailerlist.ResultTrailer;

import io.realm.RealmResults;

/**
 * For the full explanation what in the bloody hell does these set of codes does, go to the Movie adapter
 *
 * the only things you found is different would be on the ViewHolder onclick listener methods
 * */


public class TrailerRVAdapter extends RecyclerView.Adapter<TrailerRVAdapter.ViewHolder> {

    private Context context;
    private RealmResults<ResultTrailer> temp;

    public TrailerRVAdapter(Context context, RealmResults<ResultTrailer> result) {
        this.context = context;
        this.temp = result;
    }

    public void setTemp(RealmResults<ResultTrailer> temp) {
        this.temp = temp;
        notifyDataSetChanged();
    }

    @Override
    public TrailerRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trailer, parent, false);
        return new TrailerRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerRVAdapter.ViewHolder holder, int position) {
        holder.trailerName.setText(temp.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(this.temp != null) return this.temp.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public static final String YOUTUBE_APP_URI = "vnd.youtube";
        public static final String YOUTUBE_WEB_URL = "http://www.youtube.com/watch?v=";
        TextView trailerName;

        ViewHolder(View view) {
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailer_name);
            view.setOnClickListener(this);
        }

        /**
         * The onclick method in here is basically starting an activity outside this app can do
         *
         * remember how Intent works? there are two types of intent, ones to open new activity inside your app
         * another one is to throw stuff into the sea of abundant apps, in hopes maybe some of the fish called "app"
         * get the bait.
         *
         * in here I declared 2 intents, one for the youtube app in the phone and one is just an url that can be opened in browser
         * then I tried to launch the youtube intent first, if that failed, then I fired up the second intent
         * Make sense? don't give yourself a headache over this piece of codes
         *
         * */
        @Override
        public void onClick(View view) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOUTUBE_APP_URI +temp.get(getAdapterPosition()).getKey())),
                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_WEB_URL +temp.get(getAdapterPosition()).getKey()));
            try{
                view.getContext().startActivity(appIntent);
            }catch (ActivityNotFoundException e){
                Log.e("Youtube not found","Hey youtube is nowhere in your phone, so we decide to use chrome instead");
                view.getContext().startActivity(webIntent);
            }
        }
    }
}
