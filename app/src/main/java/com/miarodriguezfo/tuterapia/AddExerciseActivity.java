package com.miarodriguezfo.tuterapia;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.UUID;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class AddExerciseActivity extends AppCompatActivity {

    private DatabaseReference usuariosRef;
    private DatabaseReference ejerciciosRef;
    private FirebaseDatabase ref;
    private FirebaseUser user;
    private TextView nombre;
    private TextView parte;
    private TextView categoria;
    private TextView equipamento;
    private TextView descripcion;
    private Paciente temp;
    private CoordinatorLayout coordinatorLayout;
    static final int ADD_PATIENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);
        user = FirebaseAuth.getInstance().getCurrentUser();
        View tabView=getLayoutInflater().inflate(R.layout.doctor_ejercicios_tab, null);
        coordinatorLayout = (CoordinatorLayout) tabView.findViewById(R.id.exercises_cl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        setTitle("Agregar Ejercicio");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.85), (int) (height * .80));

        ref = FirebaseDatabase.getInstance();

        nombre = (TextView) findViewById(R.id.input_name);
        parte = (TextView) findViewById(R.id.input_parte);
        categoria = (TextView) findViewById(R.id.input_categoria);
        equipamento = (TextView) findViewById(R.id.input_equipamento);
        descripcion = (TextView) findViewById(R.id.input_desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accept_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_accept) {
            if (nombre.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa un nombre para el ejercicio", Toast.LENGTH_SHORT).show();
            } else if (parte.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa la parte del cuerpo a la cual esta dirigido el ejercicio", Toast.LENGTH_SHORT).show();
            } else if (categoria.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa una categoria para el  ejercicio", Toast.LENGTH_SHORT).show();
            } else if (equipamento.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa el equipamento necesario para el  ejercicio", Toast.LENGTH_SHORT).show();
            } else if (descripcion.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa la descripción del ejercicio", Toast.LENGTH_SHORT).show();
            } else {
                ejerciciosRef = ref.getReference("ejercicios");
                String exerciseID= UUID.randomUUID().toString();
                ejerciciosRef.child(exerciseID).child("Nombre").setValue(nombre.getText().toString());
                ejerciciosRef.child(exerciseID).child("Categoria").setValue(categoria.getText().toString());
                ejerciciosRef.child(exerciseID).child("Parte").setValue(parte.getText().toString());
                ejerciciosRef.child(exerciseID).child("Equipamento").setValue(equipamento.getText().toString());
                ejerciciosRef.child(exerciseID).child("Descripcion").setValue(descripcion.getText().toString());
                ejerciciosRef.child(exerciseID).child("Creador").setValue(user.getUid()); //siempre ultimo

                onBackPressed();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            return true;
        }

        if (item.getItemId() == R.id.action_cancel)

        {
            onBackPressed();
            return true;
        }

        return super.

                onOptionsItemSelected(item);
    }
}

