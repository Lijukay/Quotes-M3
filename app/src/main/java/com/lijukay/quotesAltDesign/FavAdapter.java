package com.lijukay.quotesAltDesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {
    private final Context mContextFav;
    private final ArrayList<FavoritesItem> mFavItem;
    private final RecyclerViewInterface recyclerViewInterface;

    public FavAdapter (Context contextFav, ArrayList<FavoritesItem> favList, RecyclerViewInterface recyclerViewInterface){
        mContextFav = contextFav;
        mFavItem = favList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parentFav, int viewTypeFav) {
        View vF = LayoutInflater.from(mContextFav).inflate(R.layout.favquotes, parentFav, false);
        return new FavViewHolder(vF, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holderFav, int positionFav) {
        FavoritesItem currentItemFav = mFavItem.get(positionFav);

        String FavQuote = currentItemFav.getQuoteFav();
        String FavAuthor = currentItemFav.getAuthorFav();

        holderFav.mQuoteFav.setText(FavQuote);
        holderFav.mAuthorFav.setText(FavAuthor);
    }

    @Override
    public int getItemCount() {
        return mFavItem.size();
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder{
        public final TextView mQuoteFav;
        public final TextView mAuthorFav;


        public FavViewHolder(@NonNull View itemViewFav, RecyclerViewInterface recyclerViewInterface) {
            super(itemViewFav);
            mQuoteFav = itemViewFav.findViewById(R.id.quoteFav);
            mAuthorFav = itemViewFav.findViewById(R.id.authorFav);

            itemViewFav.setOnClickListener(view -> {
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
