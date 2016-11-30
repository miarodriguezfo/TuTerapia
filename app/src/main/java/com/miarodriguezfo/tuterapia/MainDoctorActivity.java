package com.miarodriguezfo.tuterapia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.miarodriguezfo.tuterapia.Utilidades.*;

public class MainDoctorActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nameTextView;
    private TextView emailTextView;
    private CircleImageView profileImage;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckPlayServices(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(R.layout.activity_doctor_main);
        tabLayout = (TabLayout) findViewById(R.id.tabsDoc);
        viewPager = (ViewPager) findViewById(R.id.viewpagerDoc);
        setupViewPager(viewPager);

        setToolbar();

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setTitle(R.string.app_name);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        nameTextView = (TextView) navHeaderView.findViewById(R.id.username);
        emailTextView = (TextView) navHeaderView.findViewById(R.id.email);
        profileImage = (CircleImageView) navHeaderView.findViewById(R.id.circle_image_profile);
        //stub = (ViewStub) findViewById(R.id.layout_main_stub);
        if (navigationView != null) {
            // Añadir carácteristicas
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.nav_log_out:
                                    logout();
                                    break;
                                case R.id.nav_pacientes:

                                    break;
                                case R.id.nav_ejercicios:

                                    break;
                            }


                            drawerLayout.closeDrawers();

                            return true;
                        }
                    });
        }

        if (savedInstanceState == null) {
            // Seleccionar item
        }



        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            new DownLoadImageTask(profileImage).execute(user.getPhotoUrl().toString());
            //stub.setLayoutResource(R.layout.content_paciente_main);
            //View inflated = stub.inflate();
            nameTextView.setText(name);
            emailTextView.setText(email);

        } else {
            goLoginScreen();
        }
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabDoctorPacientesFragment(), "Pacientes");
        adapter.addFragment(new TabDoctorEjerciciosFragment(), "Ejercicios");
        viewPager.setAdapter(adapter);
    }
}

