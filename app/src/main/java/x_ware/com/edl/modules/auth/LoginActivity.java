package x_ware.com.edl.modules.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.MainActivity;
import x_ware.com.edl.R;
import x_ware.com.edl.networking.api.IUserAPI;
import x_ware.com.edl.helpers.ApiErrorHelper;
import x_ware.com.edl.helpers.PreferenceHelper;
import x_ware.com.edl.helpers.PreferenceKeyHelper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.models.user.UserModel;
import x_ware.com.edl.networking.models.user.UserLoginModel;
import x_ware.com.edl.networking.RetrofitProvider;

public class LoginActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "LoginActivity";
    private EditText txtUserName, txtPassword;
    private Button btnLogin, btnClear;
    private ProgressDialog progress;

    private BroadcastReceiver broadcastReceiver;
    private int count = 0;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private Location location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();
    }

    //-> initializeComponents
    private void initializeComponents() {
        setUpViews();
        setUpEvents();


        /* location
        //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode("Aeon Mall, Phnom Penhb"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

        */

        //testLocation();

        
    }
    private void testLocation(){
        Toast.makeText(this, "test location", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        requestLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation()
    {
        getLocationPermission();
        assert locationManager != null;
        if (locationPermissionGranted)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //-> setUpViews
    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        progress = ProgressDialogHelper.getInstance(this);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnClear = findViewById(R.id.btnClear);
    }

    //-> setUpEvents
    private void setUpEvents() {
        btnClear.setOnClickListener(view -> clear());
        btnLogin.setOnClickListener(view -> login());
    }

    //-> clear
    private void clear() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtUserName.requestFocus();
    }

    //-> validation
    private boolean validation() {
        txtUserName.setError(null);
        if (txtUserName.getText().toString().trim().isEmpty()) {
            txtUserName.setError("User name is required!");
            return false;
        }
        return true;
    }

    //-> login
    private void login() {
        testLocation();
        /*
        if (validation()) {
            try {
                UserLoginModel user = new UserLoginModel();
                user.userName = txtUserName.getText().toString().trim();
                user.password = "";
                RetrofitProvider.get().create(IUserAPI.class).login(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> progress.show())
                        .doOnComplete(() -> progress.dismiss())
                        .subscribe(this::handleLogin, this::handleError);
            } catch (Exception ex) {
                Log.d(TAG, "login: " + ex.getMessage());
            }
        }
        */

    }

    //-> handleLogin
    private void handleLogin(Response<UserModel> response) {
        switch (response.code()) {
            case 200:
                PreferenceHelper.setSerializeObject(LoginActivity.this, PreferenceKeyHelper.User, response.body());
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case 404:
                Toast.makeText(this, "Incorrect user name or password", Toast.LENGTH_SHORT).show();
                break;

            default:
                ApiErrorHelper.statusCode500(this);
                break;
        }
    }

    //-> handleError
    private void handleError(Throwable t) {
        progress.dismiss();
        ApiErrorHelper.unableConnectToServer(this, TAG, t);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        //tvLongitude.setText(String.valueOf(location.getLongitude()));
        //tvLatitude.setText(String.valueOf(location.getLatitude()));
        Log.d(TAG, "onLocationChanged: ");
        Log.d(TAG, "onLocationChanged: " + location.getLongitude());
        Log.d(TAG, "onLocationChanged: " + location.getLatitude());
        
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        locationPermissionGranted = false;
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    locationPermissionGranted = true;
                }
            }
        }
    }
}
