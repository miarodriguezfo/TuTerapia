package com.miarodriguezfo.tuterapia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class TabPatientHistorialFragment extends Fragment {
    private ArrayList<Sesion> sesionesLista;
    private RecyclerView rv;
    private FirebaseUser user;
    private DatabaseReference sesionesRef;
    private FirebaseDatabase ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.patient_historial_tab, container, false);
        sesionesLista= new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rv = (RecyclerView) rootView.findViewById(R.id.rvs);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        ref = FirebaseDatabase.getInstance();
        sesionesRef = ref.getReference("sesiones/" + user.getUid());
        Query sesiones = sesionesRef.orderByChild("Estado").equalTo("Completado");
        sesiones.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearSessions();
                Iterator<DataSnapshot> temp = dataSnapshot.getChildren().iterator();
                while (temp.hasNext()) {

                    DataSnapshot pac = temp.next();
                    addSesion(pac.getKey(),
                            new Date(Long.parseLong(pac.child("Horario").child("time").getValue().toString())),
                            pac.child("Comentarios").getValue().toString(),
                            pac.child("Estado").getValue().toString());
                    System.out.println(new Date(Long.parseLong(pac.child("Horario").child("time").getValue().toString())).toString());
                }
                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    private void initializeAdapter() {
        RVAdapterSesiones adapter = new RVAdapterSesiones(sesionesLista);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + sesionesLista.get(rv.getChildPosition(v)).numero);
            }
        });
        rv.setAdapter(adapter);

    }
    private void clearSessions() {
        sesionesLista.clear();
    }

    private void addSesion(String numero, Date fecha, String comentarios, String estado) {
        sesionesLista.add(new Sesion(numero,fecha,comentarios,estado));
    }
}
