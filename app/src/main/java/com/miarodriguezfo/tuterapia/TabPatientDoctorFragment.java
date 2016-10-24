package com.miarodriguezfo.tuterapia;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class TabPatientDoctorFragment extends Fragment {

    private FirebaseDatabase ref;
    private DatabaseReference usuarioRef;
    private DatabaseReference solicitudesRef;
    private DatabaseReference doctorRef;
    private FirebaseUser user;
    private TextView textView;
    private CircleImageView doctorProfileImage;
    private Button solButton;
    public static final int SOLICITUD = 0;
    public String doctorUid;
    public String name;
    public String solicitudID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.patient_doctor_tab, container, false);
        textView = (TextView) rootView.findViewById(R.id.doctorName);
        doctorProfileImage = (CircleImageView) rootView.findViewById(R.id.circle_image_profile_doctor);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance();
        usuarioRef = ref.getReference("users/"+user.getUid());
        solButton = (Button) rootView.findViewById(R.id.solicitud_button);
        solButton.setVisibility(View.INVISIBLE);
        usuarioRef.child("Doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("jajaja");
                    String doc=snapshot.getValue(String.class).toString();
                    if (! doc.equals(" ")) {
                        // TODO: handle the case where the data already exists
                        String doctorUID = snapshot.getValue(String.class).toString();
                        doctorRef = ref.getReference("users/" + doctorUID);
                        doctorRef.child("Foto").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // TODO: handle the case where the data already exists
                                    new DownLoadImageTask(doctorProfileImage).execute(snapshot.getValue(String.class).toString());
                                    doctorProfileImage.setVisibility(View.VISIBLE);
                                } else {
                                    // TODO: handle the case where the data does not yet exist
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {
                            }
                        });

                        doctorRef.child("Nombre").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // TODO: handle the case where the data already exists
                                    textView.setText(snapshot.getValue(String.class).toString());

                                } else {
                                    // TODO: handle the case where the data does not yet exist
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {
                            }
                        });
                    }else{
                        solicitudesRef = ref.getReference("solicitudes");
                        Query solicitudes = solicitudesRef.orderByChild("Paciente").equalTo(user.getUid());
                        solicitudes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount()==0){
                                    textView.setText(R.string.no_doctor_message);
                                    solButton.setVisibility(View.INVISIBLE);
                                    doctorProfileImage.setVisibility(View.INVISIBLE);
                                }else{
                                    Iterator<DataSnapshot> temp=dataSnapshot.getChildren().iterator();
                                    DataSnapshot doc=temp.next();
                                    doctorUid=doc.child("Doctor").getValue().toString();
                                    textView.setText(R.string.solicitudes_pendientes);
                                    doctorProfileImage.setVisibility(View.INVISIBLE);
                                    solButton.setVisibility(View.VISIBLE);
                                    solButton.setText("Ver Solicitud");
                                    solicitudID= doc.getKey().toString();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }
                else {
                    // TODO: handle the case where the data does not yet exist
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) { }
        });
        solButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test();
            }
        });
        return rootView;
    }

    public void test(){
        System.out.println("test again");
        Query doc= ref.getReference("users").orderByKey().equalTo(doctorUid);
        doc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name =dataSnapshot.child(doctorUid).child("Nombre").getValue().toString();

                Dialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Aceptar Solicitud");

                builder.setMessage(getResources().getString(R.string.acept_doctor) + "\n"+ name )
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                aceptarSolicitud();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ref.getReference("solicitudes").child(solicitudID).removeValue();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void aceptarSolicitud(){
        System.out.println("test "+ solicitudID);
        usuarioRef.child("Doctor").setValue(doctorUid);
        ref.getReference("users/"+doctorUid).child("Pacientes").child(user.getUid()).setValue(true);
        ref.getReference("solicitudes").child(solicitudID).removeValue();

    }
}
