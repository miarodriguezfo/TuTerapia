package com.miarodriguezfo.tuterapia;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class TabDoctorPacientesFragment extends Fragment{

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
                patientOptions(pacientes.get(rv.getChildPosition(v)));
            }
        });
        rv.setAdapter(adapter);

    }

    public void clearPatients(){
        pacientes.clear();
    }

    public void patientOptions(final Paciente paciente){

        CharSequence options[] = new CharSequence[] {"Información", "Historia Clinica","Sesiones", "Seguimiento", "Desvincular"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.paciente));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                            mostrarInformacion(paciente);
                        break;
                    case 1:
                            historiaClinica(paciente);
                        break;
                    case 2:
                            rutinas(paciente);
                        break;
                    case 3:
                            seguimiento(paciente);
                        break;
                    case 4:
                        desvincularPaciente(paciente);
                        break;
                }
            }


        });
        builder.show();
    }

    private void mostrarInformacion(Paciente paciente) {
        Intent intent;
        intent = new Intent(getActivity(), PatientInformationActivity.class);
        intent.putExtra("id",paciente.getUid());
        startActivity(intent);
    }

    private void historiaClinica(Paciente paciente) {

    }

    private void rutinas(Paciente paciente) {
        Intent intent;
        intent = new Intent(getActivity(), DoctorSesionesActivity.class);
        intent.putExtra("id",paciente.getUid());
        startActivity(intent);
    }

    private void seguimiento(Paciente paciente) {
    }


    private void desvincularPaciente(final Paciente paciente) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Desvincular Paciente");

        builder.setMessage(getResources().getString(R.string.unlink_patient) + "\n"+ paciente.name + "?" )
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        usuariosRef.child(paciente.getUid()).child("Doctor").setValue(" ");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
