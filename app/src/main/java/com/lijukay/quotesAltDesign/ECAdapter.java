package com.lijukay.quotesAltDesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ECAdapter extends RecyclerView.Adapter<ECAdapter.QuoteHolder> {

    //ECAdapter Class

    private final Context context;
    private final ArrayList<EC> ecs;

    //Constructor

    public ECAdapter(Context context, ArrayList<EC> ec) {
        this.context = context;
        this.ecs = ec;
    }

    @NonNull
    @Override
    public QuoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.editor_choice,parent,false);
        return new QuoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteHolder holder, int position) {

        EC ec = ecs.get(position);
        holder.setDetails(ec);
    }

    @Override
    public int getItemCount() {
        return ecs.size();
    }

    static class QuoteHolder extends RecyclerView.ViewHolder{
        private final TextView quote;
        private final TextView author;


        QuoteHolder(View itemView){
            super(itemView);
            quote = itemView.findViewById(R.id.quote);
            author = itemView.findViewById(R.id.author);
        }

        void setDetails(EC ec){
            //Set texts
            quote.setText(ec.getQuote());
            author.setText(ec.getAuthor());
        }
    }



}