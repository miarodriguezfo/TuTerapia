package com.miarodriguezfo.tuterapia;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Miguel Rodriguez on 22/10/2016.
 */
public class RVAdapterExercise extends RecyclerView.Adapter<RVAdapterExercise.ExViewHolder> implements View.OnClickListener  {

    private View.OnClickListener listener;

    public static class ExViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView category;
        TextView injury;
        TextView description;
        TextView equipment;


        ExViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cve);
            name = (TextView)itemView.findViewById(R.id.ex_name);
            category = (TextView)itemView.findViewById(R.id.ex_category);
            injury = (TextView)itemView.findViewById(R.id.ex_injury);
            description = (TextView)itemView.findViewById(R.id.ex_description);
            equipment = (TextView)itemView.findViewById(R.id.ex_equipment);
        }
    }

    List<Ejercicio> ejercicios;

    RVAdapterExercise(List<Ejercicio> ejercicios){
        this.ejercicios = ejercicios;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ExViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_exercise, viewGroup, false);
        v.setOnClickListener(this);
        ExViewHolder pvh = new ExViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ExViewHolder pacViewHolder, int i) {
        pacViewHolder.name.setText(ejercicios.get(i).name);
        pacViewHolder.category.setText("Categoria: "+ejercicios.get(i).category);
        pacViewHolder.injury.setText("Parte: "+ ejercicios.get(i).injury);
        pacViewHolder.description.setText("Descripción: \n"+ejercicios.get(i).description);
        pacViewHolder.equipment.setText("Equipamento: "+ejercicios.get(i).equipment);
    }

    @Override
    public int getItemCount() {
        return ejercicios.size();
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
