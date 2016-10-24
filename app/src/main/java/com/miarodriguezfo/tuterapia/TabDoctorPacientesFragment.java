package com.miarodriguezfo.tuterapia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class TabDoctorPacientesFragment extends Fragment {

    private FloatingActionButton anadirPacienteButton;
    private RecyclerView rv;
    private ArrayList<Paciente> pacientes;
    private DatabaseReference usuariosRef;
    private FirebaseDatabase ref;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.doctor_pacientes_tab, container, false);
        anadirPacienteButton = (FloatingActionButton) rootView.findViewById(R.id.addButton);
        anadirPacienteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), AddPatientActivity.class);
                startActivity(intent);
            }
        });
        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        pacientes = new ArrayList<Paciente>();

        ref = FirebaseDatabase.getInstance();
        usuariosRef = ref.getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query pacientes = usuariosRef.orderByChild("Tipo").equalTo("Paciente");
        pacientes.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearPatients();
                Iterator<DataSnapshot> temp=dataSnapshot.getChildren().iterator();
                while(temp.hasNext()){

                    DataSnapshot pac=temp.next();
                    if(pac.child("Doctor").getValue().equals(user.getUid())) {
                        addPacient(pac.child("Nombre").getValue().toString(),
                                pac.child("Correo").getValue().toString(),
                                pac.child("Foto").getValue().toString(),
                                pac.getKey());
                    }
                }
                initializeAdapter();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    public void addPacient(String name, String email, String photoUrl, String uid){

        pacientes.add(new Paciente(name,
                email,
                photoUrl,
                uid));

    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(pacientes);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + pacientes.get(rv.getChildPosition(v)).name);
            }
        });
        rv.setAdapter(adapter);

    }

    public void clearPatients(){
        pacientes.clear();
    }
}
