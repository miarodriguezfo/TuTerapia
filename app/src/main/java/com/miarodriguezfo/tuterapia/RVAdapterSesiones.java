package com.miarodriguezfo.tuterapia;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Miguel Rodriguez on 22/10/2016.
 */
public class RVAdapterSesiones extends RecyclerView.Adapter<RVAdapterSesiones.PacViewHolder> implements View.OnClickListener  {

    private View.OnClickListener listener;

    public static class PacViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView fecha;
        TextView estado;


        PacViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cve);
            name = (TextView)itemView.findViewById(R.id.session_name);
            fecha = (TextView)itemView.findViewById(R.id.session_horario);
            estado = (TextView)itemView.findViewById(R.id.session_estado);
        }
    }

    List<Sesion> sesiones;

    RVAdapterSesiones(List<Sesion> sesiones){
        this.sesiones = sesiones;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PacViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sesion, viewGroup, false);
        v.setOnClickListener(this);
        PacViewHolder pvh = new PacViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PacViewHolder pacViewHolder, int i) {
        pacViewHolder.name.setText("Sesión "+sesiones.get(i).numero);
        DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy HH:mm");
        pacViewHolder.fecha.setText(df.format(sesiones.get(i).fecha));
        pacViewHolder.estado.setText("Estado "+sesiones.get(i).estado);
    }

    @Override
    public int getItemCount() {
        return sesiones.size();
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
