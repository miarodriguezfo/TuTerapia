package com.miarodriguezfo.tuterapia;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Miguel Rodriguez on 18/11/2016.
 */

public class PatientInformationActivity extends AppCompatActivity {

    private FirebaseDatabase ref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usuariosRef;
    private CircleImageView profileImage;
    private EditText correo;
    private EditText telefono;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_information);
        patientId = getIntent().getExtras().getString("id");
        ref = FirebaseDatabase.getInstance();
        usuariosRef = ref.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        profileImage = (CircleImageView) findViewById(R.id.circle_image_profile_signup);
        final TextView nombre = (TextView) findViewById(R.id.nombre);
        correo = (EditText) findViewById(R.id.correo);
        telefono = (EditText) findViewById(R.id.telefono);
        Query pacientes = usuariosRef.orderByKey().equalTo(patientId);
        pacientes.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre.setText(dataSnapshot.child(patientId).child("Nombre").getValue().toString());
                new DownLoadImageTask(profileImage).execute(dataSnapshot.child(patientId).child("Foto").getValue().toString());
                correo.setText(dataSnapshot.child(patientId).child("Correo").getValue().toString());
                telefono.setText(dataSnapshot.child(patientId).child("Telefono").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
