package com.miarodriguezfo.tuterapia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class DoctorSesionesActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nameTextView;
    private TextView emailTextView;
    private CircleImageView profileImage;
    private FloatingActionButton addButton;
    private DatabaseReference sesionesRef;
    private FirebaseDatabase ref;
    private String patientId;
    private int number;
    private ArrayList<Sesion> sesionesLista;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_pacientes_sesiones);
        setToolbar();
        user = FirebaseAuth.getInstance().getCurrentUser();
        patientId = getIntent().getExtras().getString("id");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sesionesLista = new ArrayList<>();
        setTitle(R.string.sesiones);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        nameTextView = (TextView) navHeaderView.findViewById(R.id.username);
        emailTextView = (TextView) navHeaderView.findViewById(R.id.email);
        profileImage = (CircleImageView) navHeaderView.findViewById(R.id.circle_image_profile);
        rv = (RecyclerView) findViewById(R.id.rvs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        addButton = (FloatingActionButton) findViewById(R.id.addSessionButton);
        if (navigationView != null) {
            // Añadir carácteristicas
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.nav_log_out:
                                    logout();
                            }


                            drawerLayout.closeDrawers();

                            return true;
                        }
                    });
        }

        if (savedInstanceState == null) {
            // Seleccionar item
        }


        String name = user.getDisplayName();
        String email = user.getEmail();
        new DownLoadImageTask(profileImage).execute(user.getPhotoUrl().toString());
        nameTextView.setText(name);
        emailTextView.setText(email);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddSessionActivity.class);
                intent.putExtra("numero", number + 1);
                intent.putExtra("id", patientId);
                startActivity(intent);
            }
        });
        ref = FirebaseDatabase.getInstance();
        sesionesRef = ref.getReference("sesiones/" + patientId);
        Query sesiones = sesionesRef.orderByChild("Estado").equalTo("Pendiente");
        sesiones.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearSessions();
                int i = 0;
                Iterator<DataSnapshot> temp = dataSnapshot.getChildren().iterator();
                while (temp.hasNext()) {

                    DataSnapshot pac = temp.next();
                    addSesion(pac.getKey(),
                            new Date(Long.parseLong(pac.child("Horario").child("time").getValue().toString())),
                            pac.child("Comentarios").getValue().toString(),
                            pac.child("Estado").getValue().toString());

                    System.out.println(new Date(Long.parseLong(pac.child("Horario").child("time").getValue().toString())).toString());

                    number = Integer.parseInt(pac.getKey());
                }
                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clearSessions() {
        sesionesLista.clear();
    }

    private void addSesion(String numero, Date fecha, String comentarios, String estado) {
        sesionesLista.add(new Sesion(numero, fecha, comentarios, estado));
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

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
}

