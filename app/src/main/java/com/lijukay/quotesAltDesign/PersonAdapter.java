package com.lijukay.quotesAltDesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder> {

    private final Context contextP;
    private final ArrayList<PersonRV> persons;

    //Constructor

    public PersonAdapter(Context contextP, ArrayList<PersonRV> person) {
        this.contextP = contextP;
        this.persons = person;
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parentP, int viewTypeP) {
        View viewP = LayoutInflater.from(contextP).inflate(R.layout.persons_item,parentP,false);
        return new PersonHolder(viewP);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holderP, int positionP) {

        PersonRV person = persons.get(positionP);
        holderP.setDetailsP(person);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    static class PersonHolder extends RecyclerView.ViewHolder{
        private final TextView personsP;


        PersonHolder(View itemViewP){
            super(itemViewP);
            personsP = itemViewP.findViewById(R.id.person);
        }

        void setDetailsP(PersonRV person){
            //Set texts
            personsP.setText(person.getPerson());
        }
    }
}