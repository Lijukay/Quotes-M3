package com.lijukay.quotesAltDesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.QuoteAllHolder> {


    private final Context contextAQ;
    private final ArrayList<AllRV> aqs;

    //Constructor

    public AllAdapter(Context contextAQ, ArrayList<AllRV> aq) {
        this.contextAQ = contextAQ;
        this.aqs = aq;
    }

    @NonNull
    @Override
    public QuoteAllHolder onCreateViewHolder(@NonNull ViewGroup parentAQ, int viewTypeAQ) {
        View viewAQ = LayoutInflater.from(contextAQ).inflate(R.layout.allquotes,parentAQ,false);
        return new QuoteAllHolder(viewAQ);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteAllHolder holderAQ, int positionAQ) {

        AllRV aq = aqs.get(positionAQ);
        holderAQ.setDetails(aq);
    }

    @Override
    public int getItemCount() {
        return aqs.size();
    }

    static class QuoteAllHolder extends RecyclerView.ViewHolder{
        private final TextView quoteAQ;
        private final TextView authorAQ;


        QuoteAllHolder(View itemViewAQ){
            super(itemViewAQ);
            quoteAQ = itemViewAQ.findViewById(R.id.quoteAQ);
            authorAQ = itemViewAQ.findViewById(R.id.authorAQ);
        }

        void setDetails(AllRV aq){
            //Set texts
            quoteAQ.setText(aq.getQuoteAll());
            authorAQ.setText(aq.getAuthorAll());
        }
    }
}