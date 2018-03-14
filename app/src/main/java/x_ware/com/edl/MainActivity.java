package x_ware.com.edl;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import x_ware.com.edl.dialogs.DatePickerDialogFragment;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.modules.appointment.AppointmentFragment;
import x_ware.com.edl.modules.auth.LoginActivity;
import x_ware.com.edl.modules.project.ProjectFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = "MainActivity";
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    //-> initializeComponents()
    private void initializeComponents() {
        setUpViews();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        goToAppointmentFragment(DateTimeHelper.get_dd_mm_yyy(year, (month + 1), day));
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment Listing");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            return true;
//            //return true;
//        }

        if (id == R.id.actDatePicker) {
            //Toast.makeText(this, "calendar click", Toast.LENGTH_SHORT).show();
            android.support.v4.app.DialogFragment datePicker = new DatePickerDialogFragment();
            datePicker.show(getSupportFragmentManager(), "DatePickerDialogFragment");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navAppointment:
                fragment = new AppointmentFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment, "AppointmentFragment").commit();
                break;
            case R.id.navLogout:
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id1) -> logout())
                        .setNegativeButton("No", null)
                        .show();
                break;
            default:
                Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-> logout
    private void logout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        goToAppointmentFragment(DateTimeHelper.get_yyyy_mm_dd(i, (i1 + 1), i2));
    }

    private void goToAppointmentFragment(String date) {
        fragment = new AppointmentFragment();
        ((AppointmentFragment) fragment).strDate = date;
        getFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment, "AppointmentFragment").commit();
    }
}
