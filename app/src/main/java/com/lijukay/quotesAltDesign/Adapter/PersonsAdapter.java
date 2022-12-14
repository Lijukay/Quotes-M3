package com.lijukay.quotesAltDesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lijukay.quotesAltDesign.Items.PersonsItem;
import com.lijukay.quotesAltDesign.R;

import java.util.ArrayList;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PViewHolder> {
    private final Context mContextP;
    private final ArrayList<PersonsItem> mPItem;
    private final RecyclerViewClickListener listener;

    public PersonsAdapter (Context contextP, ArrayList<PersonsItem> PList, RecyclerViewClickListener listener){
        mContextP = contextP;
        mPItem = PList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public PViewHolder onCreateViewHolder(@NonNull ViewGroup parentP, int viewTypeP) {
        View vP = LayoutInflater.from(mContextP).inflate(R.layout.persons_item, parentP, false);
        return new PViewHolder(vP);
    }

    @Override
    public void onBindViewHolder(@NonNull PViewHolder holderP, int positionP) {
        PersonsItem currentItemP = mPItem.get(positionP);

        String PAuthor = currentItemP.getAuthorP();
        holderP.mAuthorP.setText(PAuthor);
    }

    @Override
    public int getItemCount() {
        return mPItem.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(int position);
    }

    public class PViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mAuthorP;


        public PViewHolder(@NonNull View itemViewP) {
            super(itemViewP);
            mAuthorP = itemViewP.findViewById(R.id.person);
            itemViewP.setOnClickListener(this);
        }
        @Override
        public void onClick(View viewP) {
            listener.onClick(getAdapterPosition());
        }
    }
}
