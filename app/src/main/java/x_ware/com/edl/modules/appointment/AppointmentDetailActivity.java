package x_ware.com.edl.modules.appointment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.MainActivity;
import x_ware.com.edl.R;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.networking.api.IAppointmentAPI;
import x_ware.com.edl.helpers.Helper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.models.appointment.AppointmentCheckInModel;
import x_ware.com.edl.networking.models.appointment.AppointmentCheckOutModel;
import x_ware.com.edl.networking.models.appointment.AppointmentEditDetailModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import x_ware.com.edl.modules.project.ProjectActivity;
import x_ware.com.edl.networking.RetrofitProvider;

public class AppointmentDetailActivity extends AppCompatActivity implements AppointmentDetailDialog.AppointmentDetailDialogListener {
    private static final String TAG = "AppointmentDetailActivi";

    private int currentPage = 1;
    private ProgressDialog progress;
    private TextView lblTiming, lblCompanyName, lblPhoneNumber, lblSubject, lblDetail;
    private Button btnGoToProject, btnCheckInOrCheckOut;
    private ImageButton imbEditSubject;
    private ImageView imgAction;
    private AppointmentViewModel appointment;

    static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    //my test location
    //LocationRequest mLocationRequest;
    //Location mLastLocation;
    //FusedLocationProviderClient mFusedLocationClient_Test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeComponents();
    }

    // *** private methods *** /


    //-> initializeComponents
    private void initializeComponents(){
        if(getIntent() != null && getIntent().hasExtra("AppointmentViewModel"))
            appointment = (AppointmentViewModel) getIntent().getSerializableExtra("AppointmentViewModel");

        setUpViews();
        setUpEvents();
        getAppointments();

        //locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //getLocation();
    }

    //-> setUpViews()
    private void setUpViews(){
        progress = ProgressDialogHelper.getInstance(this);

        lblTiming =  findViewById(R.id.lblTiming);
        lblCompanyName =  findViewById(R.id.lblCompanyName);
        lblPhoneNumber =  findViewById(R.id.lblPhoneNumber);
        lblSubject =  findViewById(R.id.lblSubject);
        lblDetail =  findViewById(R.id.lblDetail);

        btnCheckInOrCheckOut = findViewById(R.id.btnCheckInOrCheckOut);
        btnGoToProject = findViewById(R.id.btnGoToProject);
        imbEditSubject = findViewById(R.id.imbEditDetail);
        imgAction = findViewById(R.id.imgAction);
    }

    //-> setUpEvents
    private void setUpEvents() {
        btnGoToProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProject();
            }
        });

        String myText = "check in";
        if(appointment.checkIncheckOut != null) {
            if (appointment.checkIncheckOut.equals("Checked In"))
                myText = "check out";

        }
        String checkInCheckOutText = myText;
        btnCheckInOrCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AppointmentDetailActivity.this)
                        .setMessage("Do you want to " + checkInCheckOutText + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkInOrCheckOut();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        imbEditSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailDialog();
            }
        });
    }

    //-> getAppointment()
    private void getAppointments(){
        try {
            RetrofitProvider.get(this).create(IAppointmentAPI.class).getAppointment(appointment.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleResults, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getAppointment: " + ex.getMessage());
        }
    }


    //-> locationPermission
    private boolean locationPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    //-> getLocation
    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                Toast.makeText(this, "Please enable location permission", Toast.LENGTH_SHORT).show();
                return;

            } else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // GPS location can be null if GPS is switched off
                                if (location != null) {
                                   Log.d(TAG, "onSuccess: " + location.getLatitude());
                                   Log.d(TAG, "onSuccess: " + location.getLongitude());
                                }
                                else {
                                    Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                e.printStackTrace();
                            }
                        });
            }
        }
        catch (Exception ex){
            Log.d(TAG, "getLocation: " + ex.getMessage());
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actCheckIn) {
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to check in?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            checkInOrCheckOut();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    //-> checkInOrCheckOut
    private void checkInOrCheckOut() {
        try {
            Log.d(TAG, "checkInOrCheckOut: " + appointment.checkIncheckOut);

            if(locationPermission()) {
                if (appointment.checkIncheckOut == null)
                    checkIn();
                else
                    checkOut();
            }
        }
        catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }
    }

    //-> checkOut
    private void checkOut(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            Log.d(TAG, "onSuccess: " + location.getLatitude());
                            Log.d(TAG, "onSuccess: " + location.getLongitude());
                            AppointmentCheckOutModel checkout = new AppointmentCheckOutModel();
                            checkout.id = appointment.id;
                            checkout.latitude = location.getLatitude();
                            checkout.longitude = location.getLongitude();
                            RetrofitProvider.get().create(IAppointmentAPI.class).checkOut(appointment.id, checkout)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(x -> progress.show())
                                    .doOnComplete(() -> progress.dismiss())
                                    .subscribe(AppointmentDetailActivity.this::handleCheckInOrCheckOutResults, AppointmentDetailActivity.this::handleCheckInError);

                        } else {
                            Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    //-> checkIn
    private void checkIn() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            Log.d(TAG, "onSuccess: " + location.getLatitude());
                            Log.d(TAG, "onSuccess: " + location.getLongitude());
                            AppointmentCheckInModel checkInModel = new AppointmentCheckInModel();
                            checkInModel.id = appointment.id;
                            checkInModel.latitude = location.getLatitude();
                            checkInModel.longitude = location.getLongitude();
                            RetrofitProvider.get().create(IAppointmentAPI.class).checkIn(appointment.id, checkInModel)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(x -> progress.show())
                                    .doOnComplete(() -> progress.dismiss())
                                    .subscribe(AppointmentDetailActivity.this::handleCheckInOrCheckOutResults, AppointmentDetailActivity.this::handleCheckInError);

                        } else {
                            Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    //-> handleCheckInOrCheckOutResults
    private void handleCheckInOrCheckOutResults(Response<AppointmentViewModel> response) {
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                appointment = response.body();
                displayData();
                break;
            case 500:
                Helper.error500(getApplicationContext());
                break;

            default:
                Helper.error500(getApplicationContext());
                break;
        }
    }
    //-> handleCheckInError
    private void handleCheckInError(Throwable t){
        progress.dismiss();
        //Helper.reuqestError(getApplicationContext(), TAG, t);
    }




    private void openDetailDialog(){
        AppointmentDetailDialog detailDialog = new AppointmentDetailDialog();
        detailDialog.appointment = appointment;
        detailDialog.show(getSupportFragmentManager(), "detailDialog");
    }


    private void goToProject(){
        Intent intent = new Intent(this, ProjectActivity.class);
        //intent.putExtra("AppointmentViewModel", (Serializable) appointment);
        startActivity(intent);
    }



    //-> handleResults
    private void handleResults(Response<AppointmentViewModel> response) {
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                appointment = response.body();
                displayData();
                break;

            case 404:
                Helper.notFound404(getApplicationContext());
                break;

            case 500:
                Helper.error500(getApplicationContext());
                break;

            default:
                Helper.error500(getApplicationContext());
                break;
        }
    }

    //-> handleError
    private void handleError(Throwable t){
        progress.dismiss();
        Helper.reuqestError(getApplicationContext(), TAG, t);
    }

    private void displayData(){
        Log.d(TAG, "displayData:" + appointment.checkIncheckOut);

        imbEditSubject.setVisibility(View.INVISIBLE);
        lblTiming.setText(DateTimeHelper.convert_yyyy_mm_dd_t_hh_mm_ss_To_dd_mm_yyy_hh_mm(appointment.timing));
        lblCompanyName.setText(appointment.companyName);
        lblPhoneNumber.setText(appointment.phoneNumber);
        lblSubject.setText(appointment.subject);
        lblDetail.setText(appointment.details);

        if(appointment.checkIncheckOut != null) {
            if(appointment.checkIncheckOut.equals("Checked In")) {
                btnCheckInOrCheckOut.setText("Check Out");
                imbEditSubject.setVisibility(View.VISIBLE);
            }
        }

        if(appointment.checkIncheckOut != null){
            if(appointment.checkIncheckOut.equals("Checked Out")) {
                btnCheckInOrCheckOut.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        }

        switch (appointment.action){
            case "Delivery":
                imgAction.setImageDrawable(this.getResources().getDrawable(R.drawable.delivery));
                break;
            case "Presentation":
                imgAction.setImageDrawable(this.getResources().getDrawable(R.drawable.presentation));
                break;
            case "SampleRequest":
                imgAction.setImageDrawable(this.getResources().getDrawable(R.drawable.sample_request));
                break;
            case "SalesVisit":
                imgAction.setImageDrawable(this.getResources().getDrawable(R.drawable.sales_visit));
                break;
                default:
                    imgAction.setImageDrawable(this.getResources().getDrawable(R.drawable.presentation));
                    break;
        }
    }


    @Override
    public void onComplete(AppointmentViewModel appointment) {
        Log.d(TAG, "onComplete: " + appointment.details);

        AppointmentEditDetailModel editDetail = new AppointmentEditDetailModel();
        editDetail.id = appointment.id;
        editDetail.details = appointment.details;
        RetrofitProvider.get().create(IAppointmentAPI.class).editDetail(appointment.id, editDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(x -> progress.show())
                .doOnComplete(() -> progress.dismiss())
                .subscribe(this::handleEditDetailResults, this::handleEditDetailError);
    }

    private void handleEditDetailResults(Response<AppointmentViewModel> response) {
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                appointment = response.body();
                displayData();
                break;
            case 500:
                Helper.error500(getApplicationContext());
                break;

            default:
                Helper.error500(getApplicationContext());
                break;
        }
    }

    //-> handleError
    private void handleEditDetailError(Throwable t){
        progress.dismiss();
        Helper.reuqestError(getApplicationContext(), TAG, t);
    }


}
