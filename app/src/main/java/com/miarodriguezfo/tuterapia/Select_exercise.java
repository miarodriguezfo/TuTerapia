package com.miarodriguezfo.tuterapia;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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

public class Select_exercise extends Fragment {
    private RecyclerView rv;
    private SearchView search;
    private ArrayList<Ejercicio> ejercicios;
    private DatabaseReference ejerciciosRef;
    private FirebaseDatabase ref;
    private FirebaseUser user;
    private CoordinatorLayout coordinatorLayout;
    private Fragment este;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_select_exercise, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rve);
        search= (SearchView) rootView.findViewById(R.id.sve);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        ejercicios = new ArrayList<>();
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.exercises_cl);
        este=this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance();
        ejerciciosRef = ref.getReference("ejercicios");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Query exercises = ejerciciosRef.orderByKey();
                exercises.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        clearExercises();
                        Iterator<DataSnapshot> temp=dataSnapshot.getChildren().iterator();
                        while(temp.hasNext()){
                            DataSnapshot exe=temp.next();
                            if (exe.child("Creador").getValue()!=null &&
                                    exe.child("Nombre").getValue()!=null &&
                                    exe.child("Categoria").getValue()!=null &&
                                    exe.child("Parte").getValue()!=null &&
                                    exe.child("Equipamento").getValue()!=null &&
                                    exe.child("Descripcion").getValue()!=null) {

                                if (exe.child("Nombre").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())
                                        || exe.child("Categoria").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())
                                        || exe.child("Parte").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())
                                        || exe.child("Descripcion").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())
                                        || exe.child("Equipamento").getValue().toString().toLowerCase().contains(search.getQuery().toString().toLowerCase())) {
                                    addExercise(exe.getKey(),
                                            exe.child("Nombre").getValue().toString(),
                                            exe.child("Categoria").getValue().toString(),
                                            exe.child("Parte").getValue().toString(),
                                            exe.child("Descripcion").getValue().toString(),
                                            exe.getKey(),
                                            exe.child("Equipamento").getValue().toString());
                                }
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
        ((ExerciseSelection)getActivity()).setExerciseId("");
        return rootView;
    }

    public void addExercise(String uid, String name, String category, String injury, String description, String equipment, String creator){

        ejercicios.add(new Ejercicio(uid,
                name,
                category,
                injury,
                description,
                equipment,
                creator));

    }

    private void initializeAdapter() {
        RVAdapterExercise adapter = new RVAdapterExercise(ejercicios);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + ejercicios.get(rv.getChildPosition(v)).name);
                ((ExerciseSelection)getActivity()).setExerciseId(ejercicios.get(rv.getChildPosition(v)).getUid());
                ((ExerciseSelection)getActivity()).setExerciseName(ejercicios.get(rv.getChildPosition(v)).name);
                ((ExerciseSelection)getActivity()).changeFragment(1);
            }
        });
        rv.setAdapter(adapter);

    }

    public void clearExercises(){
        ejercicios.clear();
    }
}