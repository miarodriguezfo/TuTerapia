package com.miarodriguezfo.tuterapia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Select_details extends Fragment {

    private Button add;
    private EditText peso;
    private EditText duracion;
    private EditText repeticiones;
    private EditText indicaciones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_select_details, container, false);
        add=(Button)rootView.findViewById(R.id.agregar);
        peso=(EditText)rootView.findViewById(R.id.input_peso);
        duracion=(EditText)rootView.findViewById(R.id.input_duration);
        repeticiones=(EditText)rootView.findViewById(R.id.input_repeticiones);
        indicaciones=(EditText)rootView.findViewById(R.id.input_indicaciones);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ExerciseSelection)getActivity()).getExerciseId().equals("")){
                    ((ExerciseSelection)getActivity()).changeFragment(0);
                    Toast.makeText(getContext(), "Selecciona un ejercicio", Toast.LENGTH_SHORT).show();
                }else{
                    ((ExerciseSelection)getActivity()).createExercise(((ExerciseSelection)getActivity()).getExerciseId(),
                            ((ExerciseSelection)getActivity()).getExerciseName(),
                            peso.getText().toString(),repeticiones.getText().toString(),duracion.getText().toString(),
                            indicaciones.getText().toString());

                }
            }
        });
        return rootView;
    }

}