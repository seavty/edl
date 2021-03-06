package x_ware.com.edl.modules.appointment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.vistrav.ask.Ask;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.MainActivity;
import x_ware.com.edl.R;
import x_ware.com.edl.helpers.ApiHelper;
import x_ware.com.edl.helpers.CameraHelper;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.helpers.ImageHelper;
import x_ware.com.edl.helpers.StringHelper;
import x_ware.com.edl.networking.api.IAppointmentAPI;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.dto.appointment.AppointmentCheckInDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentCheckOutDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentEditDetailDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentUploadDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentViewDTO;
import x_ware.com.edl.networking.RetrofitProvider;

public class AppointmentDetailActivity extends AppCompatActivity implements AppointmentDetailDialog.AppointmentDetailDialogListener {
    private static final String TAG = "AppointmentDetailActivi";

    private ProgressDialog progress;
    private TextView lblTiming, lblCompanyName, lblCompanyPhoneNumber, lblSubject, lblDetail;
    private TextView lblPersonPhoneNumber, lblPersonTitle;
    private TextView lblContactPerson;
    private Button btnCheckInOrCheckOut;
    private ImageButton imbEditSubject, imbCamera;
    private ImageView imgAction;


    private AppointmentViewDTO appointment;

    static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private Uri fileUri;

    private boolean isCheckOut=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        initializeComponents();
    }

    //-> initializeComponents
    private void initializeComponents() {
        if (getIntent() != null && getIntent().hasExtra("AppointmentViewModel"))
            appointment = (AppointmentViewDTO) getIntent().getSerializableExtra("AppointmentViewModel");

        setUpViews();
        setUpEvents();
        getAppointment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if(requestCode == CameraHelper.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = CameraHelper.getCameraBitmap(data, this);
                String base64 = ImageHelper.convertBitmapToBase64(bitmap);

                AppointmentUploadDTO uploadImage = new AppointmentUploadDTO();
                uploadImage.id = appointment.id;
                uploadImage.base64 = base64;
                RetrofitProvider.get(this).create(IAppointmentAPI.class).uploadImage(appointment.id, uploadImage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> progress.show())
                        .doOnComplete(() -> progress.dismiss())
                        .subscribe(this::handlePostImage, this::handleError);
            }
        }
    }


    //-> handlePostImage
    private void handlePostImage(Response<Void> response){
        Log.d(TAG, "handlePostImage: " + response.code());
        if(ApiHelper.isSuccessful(this, response.code())){
            Toast.makeText(this, "successfully uploaded image", Toast.LENGTH_SHORT).show();
        }
    }


    //-> setUpViews()
    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment Details");

        progress = ProgressDialogHelper.getInstance(this);

        lblTiming = findViewById(R.id.lblTiming);
        lblCompanyName = findViewById(R.id.lblCompanyName);
        lblCompanyPhoneNumber = findViewById(R.id.lblCompanyPhoneNumber);
        lblContactPerson = findViewById(R.id.lblContactPerson);
        lblPersonPhoneNumber = findViewById(R.id.lblPersonPhoneNumber);

        lblSubject = findViewById(R.id.lblSubject);
        lblDetail = findViewById(R.id.lblDetail);
        lblPersonTitle = findViewById(R.id.lblPersonTitle);

        btnCheckInOrCheckOut = findViewById(R.id.btnCheckInOrCheckOut);

        imbEditSubject = findViewById(R.id.imbEditDetail);
        imbCamera = findViewById(R.id.imbCamera);

        imgAction = findViewById(R.id.imgAction);


        imbCamera.setVisibility(View.INVISIBLE);
    }

    //-> setUpEvents
    private void setUpEvents() {
        String myText = "check in";
        if (appointment.checkIncheckOut != null) {
            if (appointment.checkIncheckOut.equals("Checked In"))
                myText = "check out";

        }
        String checkInCheckOutText = myText;
        btnCheckInOrCheckOut.setOnClickListener(view -> new AlertDialog.Builder(AppointmentDetailActivity.this)
                .setMessage("Do you want to " + checkInCheckOutText + " ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> checkInOrCheckOut())
                .setNegativeButton("No", null)
                .show());

        imbEditSubject.setOnClickListener(view -> openDetailDialog());

        imbCamera.setOnClickListener(view -> openCamera());
        
        lblCompanyPhoneNumber.setOnClickListener(view -> phoneCall(lblCompanyPhoneNumber.getText().toString()));

        lblPersonPhoneNumber.setOnClickListener(view -> phoneCall(lblPersonPhoneNumber.getText().toString()));


    }

    private void phoneCall(String phoneNumber) {
        Ask.on(this)
                .id(1001) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.CALL_PHONE)
                .withRationales("Phone call permission needed",
                        "In order to make phone call you need to allow phone call permission") //optional
                .go();
    if (!phoneNumber.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Calling")
                    .setMessage("Do you want to call to this number " + phoneNumber + " ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(AppointmentDetailActivity.this, Manifest.permission.CALL_PHONE) ==
                                PackageManager.PERMISSION_GRANTED) {
                            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            (AppointmentDetailActivity.this).startActivity(callIntent);
                        } else {
                            Toast.makeText(AppointmentDetailActivity.this, "Please enable Phone Call permission", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    //-> openCamera
    private void openCamera(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CameraHelper.REQUEST_TAKE_PHOTO);

    }

    //-> getAppointment()
    private void getAppointment() {
        try {
            RetrofitProvider.get(this).create(IAppointmentAPI.class).getAppointment(appointment.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetAppointment, this::handleError);
        } catch (Exception ex) {
            Log.d(TAG, "getAppointment: " + ex.getMessage());
        }
    }

    //-> handleGetAppointment
    private void handleGetAppointment(Response<AppointmentViewDTO> response) {
        if(ApiHelper.isSuccessful(this, response.code())){
            appointment = response.body();
            displayData();
        }
    }

    //-> handleError
    private void handleError(Throwable t) {
        progress.dismiss();
        ApiHelper.unableConnectToServer(this, TAG, t);
    }

    //-> displayData()
    private void displayData() {

        AppointmentViewDTO tmp = (AppointmentViewDTO) StringHelper.tmp(appointment);

        imbEditSubject.setVisibility(View.INVISIBLE);
        imbCamera.setVisibility(View.INVISIBLE);

        //lblTiming.setText(DateTimeHelper.convert_yyyy_mm_dd_t_hh_mm_ss_To_dd_mm_yyy_hh_mm(appointment.timing));
        lblTiming.setText(DateTimeHelper.convert_yyyy_mm_dd_t_hh_mm_ss_To_hh_mm_With_am_pm(appointment.timing));
        lblCompanyName.setText(StringHelper.nullToEmptyString(appointment.companyName) + ":");
        lblCompanyPhoneNumber.setText(appointment.phoneNumber);

        lblContactPerson.setText("Contact Name: "+ StringHelper.nullToEmptyString(appointment.fullName));
        lblPersonPhoneNumber.setText(appointment.personPhoneNumber);

        lblPersonTitle.setText("Title:" + StringHelper.nullToEmptyString(appointment.personTitle));
        lblSubject.setText("Subject:" + StringHelper.nullToEmptyString(appointment.subject));
        lblDetail.setText("Communication Details: " + StringHelper.nullToEmptyString(appointment.details));

        if (appointment.checkIncheckOut != null) {
            if (appointment.checkIncheckOut.equals("Checked In")) {
                btnCheckInOrCheckOut.setText("Check Out");
                imbEditSubject.setVisibility(View.VISIBLE);
                imbCamera.setVisibility(View.VISIBLE);
            }

            if (appointment.checkIncheckOut.equals("Checked Out")) {
                btnCheckInOrCheckOut.setEnabled(false);
                //startActivity(new Intent(this, MainActivity.class));
                //finish();
            }
        }
        displayDrawable(appointment.action);
    }

    //-> displayDrawable
    private void displayDrawable(String action) {
        switch (action) {
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

    //-> openDetailDialog
    private void openDetailDialog() {
        AppointmentDetailDialog detailDialog = new AppointmentDetailDialog();
        detailDialog.appointment = appointment;
        detailDialog.show(getSupportFragmentManager(), "detailDialog");
    }

    //-> locationPermission
    private boolean locationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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
                        .addOnSuccessListener(location -> {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                Log.d(TAG, "onSuccess: " + location.getLatitude());
                                Log.d(TAG, "onSuccess: " + location.getLongitude());
                            } else {
                                Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        });
            }
        } catch (Exception ex) {
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
                    .setPositiveButton("Yes", (dialog, id1) -> checkInOrCheckOut())
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    //-> checkInOrCheckOut
    private void checkInOrCheckOut() {
        try {
            if (locationPermission()) {
                if (appointment.checkIncheckOut == null)
                    checkIn();
                else
                    checkOut();
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }
    }

    //-> checkOut
    @SuppressLint("MissingPermission")
    private void checkOut() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // GPS location can be null if GPS is switched off
                    if (location != null) {
                        isCheckOut = true;
                        AppointmentCheckOutDTO checkout = new AppointmentCheckOutDTO();
                        checkout.id = appointment.id;
                        checkout.latitude = location.getLatitude();
                        checkout.longitude = location.getLongitude();
                        RetrofitProvider.get(AppointmentDetailActivity.this).create(IAppointmentAPI.class).checkOut(appointment.id, checkout)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(x -> progress.show())
                                .doOnComplete(() -> progress.dismiss())
                                .subscribe(AppointmentDetailActivity.this::handleCheckInOrCheckOut, AppointmentDetailActivity.this::handleError);

                    } else {
                        Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

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
    @SuppressLint("MissingPermission")
    private void checkIn() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            AppointmentCheckInDTO checkInModel = new AppointmentCheckInDTO();
                            checkInModel.id = appointment.id;
                            checkInModel.latitude = location.getLatitude();
                            checkInModel.longitude = location.getLongitude();
                            RetrofitProvider.get(AppointmentDetailActivity.this).create(IAppointmentAPI.class).checkIn(appointment.id, checkInModel)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(x -> progress.show())
                                    .doOnComplete(() -> progress.dismiss())
                                    .subscribe(AppointmentDetailActivity.this::handleCheckInOrCheckOut, AppointmentDetailActivity.this::handleError);

                        } else {
                            Toast.makeText(AppointmentDetailActivity.this, "Unable to get your current location", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("MapDemoActivity", "Error trying to get last GPS location");
                    e.printStackTrace();
                });
    }

    //-> handleCheckInOrCheckOut
    private void handleCheckInOrCheckOut(Response<AppointmentViewDTO> response) {
        if(isCheckOut) {
            Toast.makeText(this, "Successfully checked out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        if(ApiHelper.isSuccessful(this, response.code())){
            Toast.makeText(this, "Successfully checked in", Toast.LENGTH_SHORT).show();
            appointment = response.body();
            displayData();
        }
    }

    @Override
    public void onComplete(AppointmentViewDTO appointment) {
        AppointmentEditDetailDTO editDetail = new AppointmentEditDetailDTO();
        editDetail.id = appointment.id;
        editDetail.details = appointment.details;
        RetrofitProvider.get(this).create(IAppointmentAPI.class).editDetail(appointment.id, editDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(x -> progress.show())
                .doOnComplete(() -> progress.dismiss())
                .subscribe(this::handleEditDetail, this::handleError);
    }

    //-> handleEditDetail
    private void handleEditDetail(Response<AppointmentViewDTO> response) {
        if(ApiHelper.isSuccessful(this, response.code())){
            appointment = response.body();
            displayData();
        }
    }

}



