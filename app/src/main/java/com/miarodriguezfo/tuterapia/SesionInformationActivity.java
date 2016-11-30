package com.miarodriguezfo.tuterapia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Miguel Rodriguez on 18/11/2016.
 */

public class SesionInformationActivity extends AppCompatActivity {

    private FirebaseDatabase ref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference sesionesRef;
    private FirebaseUser user;
    private TextView numero;
    private TextView fecha;
    private TextView hora;
    private TextView comentarios;
    private RecyclerView rv;
    private List<EjercicioAsignado> ejercicios;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_information);
        num = getIntent().getExtras().getString("id");
        ref = FirebaseDatabase.getInstance();
        sesionesRef = ref.getReference("sesiones");
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        numero = (TextView) findViewById(R.id.numero_tv);
        fecha = (TextView) findViewById(R.id.fecha_tv);
        hora = (TextView) findViewById(R.id.hora_tv);
        ejercicios = new ArrayList<>();
        this.
        comentarios = (TextView) findViewById(R.id.comentarios_tv);
        rv = (RecyclerView) findViewById(R.id.rve);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        Query sesiones = sesionesRef.orderByKey().equalTo(user.getUid());
        sesiones.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Comentarios: "+ dataSnapshot.child(user.getUid()).child(num));
                Date date= new Date(Long.parseLong(dataSnapshot.child(user.getUid()).child(num).child("Horario").child("time").getValue().toString()));
                DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy");
                numero.setText("Sesion "+ num);
                fecha.setText("Fecha: "+df.format(date));
                df = new SimpleDateFormat("HH:mm");
                hora.setText("Hora: "+df.format(date));
                comentarios.setText(dataSnapshot.child(user.getUid()).child(num).child("Comentarios").getValue().toString());
                System.out.println(dataSnapshot.child(user.getUid()).child(num).child("Ejercicios").getChildrenCount());
                Iterator<DataSnapshot> temp = dataSnapshot.child(user.getUid()).child(num).child("Ejercicios").getChildren().iterator();
                while (temp.hasNext()) {

                    DataSnapshot data = temp.next();
                    EjercicioAsignado ej=new EjercicioAsignado();
                    ej.setUid(data.child("uid").getValue().toString());
                    ej.setNombre(data.child("nombre").getValue().toString());
                    ej.setPeso(data.child("peso").getValue().toString());
                    ej.setRepeticiones(data.child("repeticiones").getValue().toString());
                    ej.setDuracion(data.child("duracion").getValue().toString());
                    ej.setIndicaciones(data.child("indicaciones").getValue().toString());
                    ejercicios.add(ej);
                }
                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializeAdapter() {
        RVAdapterExerciseSel adapter = new RVAdapterExerciseSel(ejercicios);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + ejercicios.get(rv.getChildPosition(v)).nombre);
            }
        });
        rv.setAdapter(adapter);

    }
}
