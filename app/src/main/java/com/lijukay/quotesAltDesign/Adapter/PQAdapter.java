package com.lijukay.quotesAltDesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lijukay.quotesAltDesign.Items.PQItem;
import com.lijukay.quotesAltDesign.R;
import com.lijukay.quotesAltDesign.Others.RecyclerViewInterface;

import java.util.ArrayList;

public class PQAdapter extends RecyclerView.Adapter<PQAdapter.PQViewHolder> {
    private final Context mContextPQ;
    private final ArrayList<PQItem> mPQItem;
    private final RecyclerViewInterface recyclerViewInterface;

    public PQAdapter (Context contextPQ, ArrayList<PQItem> pQList, RecyclerViewInterface recyclerViewInterface){
        mContextPQ = contextPQ;
        mPQItem = pQList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PQViewHolder onCreateViewHolder(@NonNull ViewGroup parentPQ, int viewTypePQ) {
        View vPQ = LayoutInflater.from(mContextPQ).inflate(R.layout.pquotes_item, parentPQ, false);
        return new PQViewHolder(vPQ, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PQViewHolder holderPQ, int positionPQ) {
        PQItem currentItemPQ = mPQItem.get(positionPQ);

        String PQQuote = currentItemPQ.getQuotePQ();
        String PQAuthor = currentItemPQ.getAuthorPQ();

        holderPQ.mQuotePQ.setText(PQQuote);
        holderPQ.mAuthorPQ.setText(PQAuthor);
    }

    @Override
    public int getItemCount() {
        return mPQItem.size();
    }

    public static class PQViewHolder extends RecyclerView.ViewHolder{
        public final TextView mQuotePQ;
        public final TextView mAuthorPQ;


        public PQViewHolder(@NonNull View itemViewPQ, RecyclerViewInterface recyclerViewInterface) {
            super(itemViewPQ);
            mQuotePQ = itemViewPQ.findViewById(R.id.quoteP);
            mAuthorPQ = itemViewPQ.findViewById(R.id.authorP);

            itemViewPQ.setOnClickListener(view -> {
                if (recyclerViewInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });

        }
    }
}
