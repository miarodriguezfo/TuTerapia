package com.miarodriguezfo.tuterapia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SignUpActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private FirebaseDatabase ref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usuariosRef;
    private CircleImageView profileImage;
    private EditText correo;
    private EditText telefono;
    private FirebaseUser user;
    private SegmentedGroup tipo;
    private String tipoUsuario;
    private String foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ref = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        profileImage = (CircleImageView) findViewById(R.id.circle_image_profile_signup);
        TextView nombre = (TextView) findViewById(R.id.nombre);
        correo = (EditText) findViewById(R.id.correo);
        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText("");
        tipoUsuario="";
        tipo = (SegmentedGroup) findViewById(R.id.tipo);
        tipo.setTintColor(Color.parseColor("#009688"));
        tipo.setOnCheckedChangeListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            foto= photoUrl.toString();
            new DownLoadImageTask(profileImage).execute(user.getPhotoUrl().toString());
            nombre.setText(name);
            correo.setText(email);
        } else {
            goLoginScreen();
        }
    }

    public void goMainScreen() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final Context app=this;
        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            DatabaseReference userType = ref.getReference("users/"+user.getUid());
            final String[] typeUser = new String[1];
            userType.child("Tipo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // TODO: handle the case where the data already exists
                        typeUser[0] =snapshot.getValue(String.class).toString();
                        Intent intent;
                        switch(typeUser[0]){
                            case "Paciente":
                                intent = new Intent(app, MainPatientActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                            case "Doctor":
                                intent = new Intent(app, MainDoctorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                        }
                    }
                    else {
                        // TODO: handle the case where the data does not yet exist
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) { }
            });
        }   else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
        goLoginScreen();
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
            if (telefono.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ingresa tu número de telefono", Toast.LENGTH_SHORT).show();

            }else if (tipoUsuario==""){
                Toast.makeText(this, "Selecciona tu tipo de usuario", Toast.LENGTH_SHORT).show();
            }else {


                usuariosRef = ref.getReference("users");
                usuariosRef.child(user.getUid()).child("Nombre").setValue(user.getDisplayName());
                usuariosRef.child(user.getUid()).child("Correo").setValue(user.getEmail());
                usuariosRef.child(user.getUid()).child("Tipo").setValue(tipoUsuario);
                usuariosRef.child(user.getUid()).child("Telefono").setValue(telefono.getText().toString());
                usuariosRef.child(user.getUid()).child("Foto").setValue(foto);
                if (tipoUsuario=="Paciente")
                    usuariosRef.child(user.getUid()).child("Doctor").setValue(" ");
                //ToDo Add clinical History's Data from providern(Age, Gender, Birthdate...)
                goMainScreen();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_cancel ) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                            }
                        }
                    });
            goLoginScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.paciente:
                tipoUsuario="Paciente";
                break;
            case R.id.doctor:
                tipoUsuario="Doctor";
                break;
            default:
                // Nothing to do
        }
    }
    @Override
    public void onClick(View v) {
    }
}
