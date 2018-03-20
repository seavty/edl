package x_ware.com.edl.modules.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mindorks.paracamera.Camera;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.MainActivity;
import x_ware.com.edl.R;
import x_ware.com.edl.helpers.ImageHelper;
import x_ware.com.edl.networking.api.IUserAPI;
import x_ware.com.edl.helpers.ApiHelper;
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


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private Location location;

    //--for test camera
    Camera camera;

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
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);

        try {
            camera.takePicture();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
    private void testLocation(){
        Toast.makeText(this, "test location", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        requestLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        getLocationPermission();
        assert locationManager != null;
        if (locationPermissionGranted)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
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

        //testLocation();

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
                Toast.makeText(this, "Error occurred while processing your request!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //-> handleError
    private void handleError(Throwable t) {
        progress.dismiss();
        ApiHelper.unableConnectToServer(this, TAG, t);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        Log.d(TAG, "onLocationChanged: " + location.toString());
        //tvLongitude.setText(String.valueOf(location.getLongitude()));
        //tvLatitude.setText(String.valueOf(location.getLatitude()));
        Log.d(TAG, "onLocationChanged: ");
        Log.d(TAG, "onLocationChanged: " + location.getLongitude());
        Log.d(TAG, "onLocationChanged: " + location.getLatitude());

        Toast.makeText(this, "location.getLongitude()" + location.getLongitude() , Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // permission for location
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
            Bitmap bitmap = camera.getCameraBitmap();
            if(bitmap != null) {
                Toast.makeText(this, "photo ok", Toast.LENGTH_SHORT).show();
                //convert the bitmap to bytes
                byte[] bytesArray =  ImageHelper.bitmapToBytes(camera.getCameraBitmap(), ImageHelper.PNG);

                //convert the bytes to string 64, with this form is easly to send by web service or store data in DB
                String imageBase64 = ImageHelper.bytesToStringBase64(bytesArray);
                //Log.d(TAG, "onActivityResult: " + imageBase64);

            }
            else{
                Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
