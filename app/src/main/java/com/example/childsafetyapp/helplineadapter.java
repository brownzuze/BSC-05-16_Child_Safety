package com.example.childsafetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class helplineadapter extends RecyclerView.Adapter<helplineadapter.myviewholder>  {

    ArrayList<contactmodel> dataholder;

    public helplineadapter(ArrayList<contactmodel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position) {
        holder.dname.setText(dataholder.get(position).getOrgname());
        holder.dage.setText(dataholder.get(position).getOrgnumber());
        holder.dcontact.setText(dataholder.get(position).getOrgemail());



    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

     class myviewholder extends RecyclerView.ViewHolder {

        TextView dname, dage, dcontact;
        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            dname = itemView.findViewById(R.id.displayname);
            dage = itemView.findViewById(R.id.displaycontact);
            dcontact =itemView.findViewById(R.id.displayemail);

        }
    }
}
