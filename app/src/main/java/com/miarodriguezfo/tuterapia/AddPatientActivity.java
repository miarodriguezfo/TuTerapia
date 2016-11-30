package com.miarodriguezfo.tuterapia;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.util.Iterator;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class AddPatientActivity extends Activity {

    private DatabaseReference usuariosRef;
    private DatabaseReference solicitudRef;
    private FirebaseDatabase ref;
    private FirebaseUser user;
    private RecyclerView rv;
    private ArrayList<Paciente> pacientes;
    private SearchView search;
    private Paciente temp;
    static final int ADD_PATIENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_patient);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rv = (RecyclerView) findViewById(R.id.rv);
        search= (SearchView) findViewById(R.id.sv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        pacientes = new ArrayList<Paciente>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*0.97  ), (int)(height*.9));

        ref = FirebaseDatabase.getInstance();
        usuariosRef = ref.getReference("users");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Query pacientes = usuariosRef.orderByChild("Tipo").equalTo("Paciente");
                pacientes.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        clearPatients();
                        Iterator<DataSnapshot> temp=dataSnapshot.getChildren().iterator();
                        while(temp.hasNext()){

                            DataSnapshot pac=temp.next();
                            if(pac.child("Doctor").getValue().equals(" ") &&
                                    pac.child("Nombre").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())) {
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
                return false;
            }
        });
        search.setQuery("",true);
    }

    public void addPacient(String name, String email, String photoUrl, String uid){

        pacientes.add(new Paciente(name,
                email,
                photoUrl,
                uid));

    }

    public void clearPatients(){
        pacientes.clear();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(pacientes);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + pacientes.get(rv.getChildPosition(v)).name);
                temp=pacientes.get(rv.getChildPosition(v));
                showDialog(ADD_PATIENT);
            }
        });
        rv.setAdapter(adapter);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case ADD_PATIENT:

                builder.setTitle("Enviar Solicitud");

                builder.setMessage(R.string.add_patient)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                enviarSolicitud();
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();


                break;
        }

        return dialog;
    }

    private void enviarSolicitud() {

        solicitudRef = ref.getReference("solicitudes");
        solicitudRef.child(user.getUid()+temp.getUid()).child("Doctor").setValue(user.getUid());
        solicitudRef.child(user.getUid()+temp.getUid()).child("Paciente").setValue(temp.getUid());
    }


}

