package com.miarodriguezfo.tuterapia;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ExerciseSelection extends AppCompatActivity {

    private ViewPager viewPager;
    private String exerciseId;
    private String exerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.exerciseSelectionVP);
        setupViewPager(viewPager);
        setTitle("Selecciona un ejercicio");
        viewPager.setCurrentItem(0);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Select_exercise(), "Ejercicios");
        adapter.addFragment(new Select_details(), "Detalles");
        viewPager.setAdapter(adapter);
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
    public void changeFragment(int i){
        viewPager.setCurrentItem(i);
    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void createExercise(String uid, String nombre,String peso, String repeticiones, String duracion, String indicaciones){

        Intent intent = new Intent();
        intent.putExtra("uid", uid);
        intent.putExtra("nombre", nombre);
        intent.putExtra("peso", peso);
        intent.putExtra("repeticiones", repeticiones);
        intent.putExtra("duracion", duracion);
        intent.putExtra("indicaciones", indicaciones);
        setResult(RESULT_OK, intent);
        finish();
    }
}
