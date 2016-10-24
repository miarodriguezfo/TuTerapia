package com.miarodriguezfo.tuterapia;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Miguel Rodriguez on 22/10/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PacViewHolder> implements View.OnClickListener  {

    private View.OnClickListener listener;

    public static class PacViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView email;
        CircleImageView profileImage;


        PacViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.pac_name);
            email = (TextView)itemView.findViewById(R.id.pac_email);
            profileImage = (CircleImageView)itemView.findViewById(R.id.circle_image_profile_pac);
        }
    }

    List<Paciente> pacientes;

    RVAdapter(List<Paciente> pacientes){
        this.pacientes = pacientes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PacViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        v.setOnClickListener(this);
        PacViewHolder pvh = new PacViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PacViewHolder pacViewHolder, int i) {
        pacViewHolder.name.setText(pacientes.get(i).name);
        pacViewHolder.email.setText(pacientes.get(i).email);
        new DownLoadImageTask(pacViewHolder.profileImage).execute(pacientes.get(i).photoUrl);
    }

    @Override
    public int getItemCount() {
        return pacientes.size();
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
