package com.miarodriguezfo.tuterapia;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddSessionActivity extends AppCompatActivity {
    private EditText editTextDate;
    private EditText editTextTime;
    private EditText comentarios;
    private TextView title;
    private int number;
    private Button add;
    private Button addExercise;
    private FirebaseUser user;
    private DatabaseReference sesionesRef;
    private FirebaseDatabase ref;
    private String patientId;
    private List<EjercicioAsignado> ejercicios;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Añadir sesión");
        ejercicios = new ArrayList<>();
        final Context act = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance();
        number = getIntent().getExtras().getInt("numero");
        patientId = getIntent().getExtras().getString("id");
        editTextDate = (EditText) findViewById(R.id.input_fecha);
        editTextDate.setInputType(InputType.TYPE_NULL);
        title = (TextView) findViewById(R.id.sesion_number);
        editTextTime = (EditText) findViewById(R.id.input_hora);
        editTextTime.setInputType(InputType.TYPE_NULL);
        comentarios = (EditText) findViewById(R.id.input_comentarios);
        title.setText("Sesión" + " " + number);
        add = (Button) findViewById(R.id.addSessionButton);
        addExercise = (Button) findViewById(R.id.addExerciseButton);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "EEEE, MMM dd yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));
                editTextDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                System.out.println("Fecha: " + hour);
                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, minute);
                String myFormat = "HH:mm";
                SimpleDateFormat stf = new SimpleDateFormat(myFormat, Locale.UK);
                editTextTime.setText(stf.format(myCalendar.getTime()));
            }
        };


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(v.getContext(), time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                tp.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDate.getText().toString().trim().length() == 0) {
                    Toast.makeText(act, "Selecciona una fecha", Toast.LENGTH_SHORT).show();
                } else if (editTextTime.getText().toString().trim().length() == 0) {
                    Toast.makeText(act, "Selecciona una hora", Toast.LENGTH_SHORT).show();
                } else if (ejercicios.size() < 1) {
                    Toast.makeText(act, "Debes agregar por lo menos un ejercicio", Toast.LENGTH_SHORT).show();
                } else {
                    Date fecha = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy HH:mm");
                    try {
                        fecha = df.parse(editTextDate.getText().toString() + " " + editTextTime.getText().toString());
                        Calendar cal = Calendar.getInstance();
                        cal.set(fecha.getYear(), fecha.getMonth(), fecha.getDay(), fecha.getHours(), fecha.getMinutes());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sesionesRef = ref.getReference("sesiones");
                    sesionesRef.child(patientId).child(Integer.toString(number)).child("Horario").setValue(fecha);
                    sesionesRef.child(patientId).child(Integer.toString(number)).child("Asignado por").setValue(user.getUid());
                    sesionesRef.child(patientId).child(Integer.toString(number)).child("Comentarios").setValue(comentarios.getText().toString());
                    for(int i =0;i<ejercicios.size();i++){
                        System.out.println("test"+ejercicios.get(i).uid);
                        sesionesRef.child(patientId).child(Integer.toString(number)).child("Ejercicios").child(Integer.toString(i)).setValue(ejercicios.get(i));
                    }
                    sesionesRef.child(patientId).child(Integer.toString(number)).child("Estado").setValue("Pendiente");
                    onBackPressed();
                }

            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ExerciseSelection.class);
                int requestCode = 1;
                startActivityForResult(intent, requestCode);
            }
        });

        rv = (RecyclerView) findViewById(R.id.rve);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        initializeAdapter();
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


    @Override
    protected void onResume() {
        super.onResume();
        if (ejercicios != null) {
            System.out.println("Ejercicios: " + ejercicios.size());

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        if (resultCode != 0) {
            System.out.println("test " + requestCode + " " + resultCode);
            ejercicios.add(new EjercicioAsignado(data.getExtras().getString("uid"),
                    data.getExtras().getString("nombre"),
                    data.getExtras().getString("peso"),
                    data.getExtras().getString("repeticiones"),
                    data.getExtras().getString("duracion"),
                    data.getExtras().getString("indicaciones")));
        }
        initializeAdapter();
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
