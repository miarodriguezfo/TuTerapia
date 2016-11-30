package com.miarodriguezfo.tuterapia;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.List;

public class Complete_exercises extends AppCompatActivity {

    private FirebaseDatabase ref;
    private DatabaseReference ejerciciosRef;
    private DatabaseReference exercicesRef;
    private FirebaseUser user;
    private String sesion;
    private TextView descripcion;
    private TextView numero;
    private TextView categoria;
    private TextView parte;
    private TextView equipamento;
    private TextView peso;
    private TextView duracion;
    private TextView repeticiones;
    private Query ejercicios;
    private List<EjercicioAsignado> ejerciciosList;
    private LinearLayout all;
    private RelativeLayout rel;
    private int index;
    private Context este;
    private FloatingActionButton complete;
    private boolean completeSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_exercises);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Completar Sesión");
        completeSession = false;
        ejerciciosList = new ArrayList<>();
        index = 0;
        este = this;
        sesion = getIntent().getExtras().getString("id");
        all = (LinearLayout) findViewById(R.id.activity_complete_exercises);
        rel = (RelativeLayout) findViewById(R.id.relative);
        numero = (TextView) findViewById(R.id.numero_tv);
        descripcion = (TextView) findViewById(R.id.ex_description);
        categoria = (TextView) findViewById(R.id.ex_category);
        parte = (TextView) findViewById(R.id.ex_parte);
        equipamento = (TextView) findViewById(R.id.ex_equipment);
        peso = (TextView) findViewById(R.id.ex_peso);
        duracion = (TextView) findViewById(R.id.ex_duracion);
        repeticiones = (TextView) findViewById(R.id.ex_repeticiones);
        complete = (FloatingActionButton) findViewById(R.id.completeExerciseButton);
        ref = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ejerciciosRef = ref.getReference("sesiones").child(user.getUid()).child(sesion).child("Ejercicios");
        exercicesRef = ref.getReference("ejercicios");
        ejercicios = ejerciciosRef.orderByKey();
        ejercicios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ejerciciosList.clear();
                completeSession = true;
                Iterator<DataSnapshot> temp = dataSnapshot.getChildren().iterator();
                while (temp.hasNext()) {

                    DataSnapshot data = temp.next();
                    EjercicioAsignado ej = new EjercicioAsignado();
                    ej.setUid(data.child("uid").getValue().toString());
                    ej.setNombre(data.child("nombre").getValue().toString());
                    ej.setPeso(data.child("peso").getValue().toString());
                    ej.setRepeticiones(data.child("repeticiones").getValue().toString());
                    ej.setDuracion(data.child("duracion").getValue().toString());
                    ej.setIndicaciones(data.child("indicaciones").getValue().toString());
                    ej.setEstado(data.child("estado").getValue().toString());
                    ejerciciosList.add(ej);
                    if (data.child("estado").getValue().toString().equals("Programado")) {
                        completeSession = false;
                    }
                }
                if (completeSession) {
                    ref.getReference("sesiones").child(user.getUid()).child(sesion).child("Estado").setValue("Completado");
                    AlertDialog alertDialog = new AlertDialog.Builder(Complete_exercises.this).create();
                    alertDialog.setTitle("Sesión Terminada");
                    alertDialog.setMessage("Felicitaciones has completado la Sesión "+sesion);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONTINUAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();
                }
                setDisplay(index);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        OnSwipeTouchListener touch = new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeLeft() {
                if (index + 1 < ejerciciosList.size()) {
                    index++;
                    setDisplay(index);
                }
            }

            public void onSwipeRight() {
                if (index > 0) {
                    index--;
                    setDisplay(index);
                }
            }

            public void onSwipeBottom() {
            }
        };
        all.setOnTouchListener(touch);
        rel.setOnTouchListener(touch);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(ejerciciosList.get(index).getEstado());
                if (ejerciciosList.get(index).getEstado().equals("Programado")) {
                    ejerciciosList.get(index).setEstado("Completo");
                    ref.getReference("sesiones").child(user.getUid()).child(sesion).child("Ejercicios").child(Integer.toString(index)).child("estado").setValue("Completo");
                    index++;
                    index = index % ejerciciosList.size();
                    setDisplay(index);

                } else {
                    ejerciciosList.get(index).setEstado("Programado");
                    ref.getReference("sesiones").child(user.getUid()).child(sesion).child("Ejercicios").child(Integer.toString(index)).child("estado").setValue("Programado");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDisplay(int i) {
        System.out.println("funciona");
        numero.setText(ejerciciosList.get(i).nombre);
        if (!ejerciciosList.get(i).repeticiones.equals(""))
            repeticiones.setText("Repeticiones: " + ejerciciosList.get(i).getRepeticiones());

        if (!ejerciciosList.get(i).peso.equals(""))
            peso.setText("Peso: " + ejerciciosList.get(i).getPeso() + " Lbs");
        if (!ejerciciosList.get(i).getDuracion().equals(""))
            duracion.setText("Duración: " + ejerciciosList.get(i).getDuracion() + " segundos");
        System.out.println("test " + ejerciciosList.get(i).getEstado());
        if (ejerciciosList.get(i).getEstado().equals("Programado")) {
            complete.setImageResource(R.drawable.ic_check_black);
            complete.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#FFFFFF")));
        } else {
            complete.setImageResource(R.drawable.ic_check_white);
            complete.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#25b005")));
        }
        exercicesRef.child(ejerciciosList.get(i).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                descripcion.setText(dataSnapshot.child("Descripcion").getValue().toString());
                categoria.setText("Categoria: " + dataSnapshot.child("Categoria").getValue().toString());
                parte.setText("Parte: " + dataSnapshot.child("Parte").getValue().toString());
                equipamento.setText("Equipamento: " + dataSnapshot.child("Equipamento").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
