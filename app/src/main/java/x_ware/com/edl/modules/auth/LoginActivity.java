package x_ware.com.edl.modules.auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText txtUserName, txtPassword;
    private Button btnLogin, btnClear;
    private ProgressDialog progress;

    private BroadcastReceiver broadcastReceiver;

    private int count = 0;

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
}
