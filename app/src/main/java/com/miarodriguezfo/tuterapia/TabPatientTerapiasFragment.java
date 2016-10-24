package com.miarodriguezfo.tuterapia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Miguel Rodriguez on 17/10/2016.
 */

public class TabPatientTerapiasFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.patient_terapias_tab, container, false);
    }
}
