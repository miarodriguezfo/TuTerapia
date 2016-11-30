package com.miarodriguezfo.tuterapia;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Miguel Rodriguez on 22/10/2016.
 */
public class RVAdapterExerciseSel extends RecyclerView.Adapter<RVAdapterExerciseSel.ExViewHolder> implements View.OnClickListener  {

    private View.OnClickListener listener;

    public static class ExViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;


        ExViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cve);
            name = (TextView)itemView.findViewById(R.id.ex_name);
        }
    }

    List<EjercicioAsignado> ejercicios;

    RVAdapterExerciseSel(List<EjercicioAsignado> ejercicios){
        this.ejercicios = ejercicios;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ExViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_exercise_asignado, viewGroup, false);
        v.setOnClickListener(this);
        ExViewHolder pvh = new ExViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ExViewHolder pacViewHolder, int i) {
        pacViewHolder.name.setText(ejercicios.get(i).nombre);
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
