package com.kelvindu.learning.scenema.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvindu.learning.scenema.R;
import com.kelvindu.learning.scenema.model.comment.ResultComment;

import org.w3c.dom.Text;

import io.realm.RealmResults;

/**
 * As for this adapter is basically any adapter you can found in the example code
 * don't bother looking the explanation in here
 */

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.ViewHolder> {

    Context context;
    RealmResults<ResultComment> temp;

    public CommentRVAdapter(Context context, RealmResults<ResultComment> resuts) {
        this.context = context;
        this.temp = resuts;
    }

    public void setTemp(RealmResults<ResultComment> temp) {
        this.temp = temp;
        notifyDataSetChanged();
        Log.e("Comment Adapter",temp.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentRVAdapter.ViewHolder holder, int position) {
        holder.author.setText(temp.get(position).getAuthor());
        holder.content.setText(temp.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if(this.temp != null) return this.temp.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView author,content;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView)itemView.findViewById(R.id.comment_author);
            content = (TextView)itemView.findViewById(R.id.comment_content);
        }
    }
}
