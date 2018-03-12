package x_ware.com.edl.modules.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        initializeComponents();
    }

    //-> initializeComponents
    private void initializeComponents(){
        setUpViews();
        setUpEvents();
    }

    //-> setUpViews
    private void setUpViews() {
        progress = ProgressDialogHelper.getInstance(this);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnClear = findViewById(R.id.btnClear);

    }

    //-> setUpEvents
    private void setUpEvents() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    //-> clear
    private void clear() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtUserName.requestFocus();
    }

    //-> validation
    private boolean validation(){
        txtUserName.setError(null);
        if(txtUserName.getText().toString().trim().isEmpty()) {
            txtUserName.setError( "User name is required!" );
            return false;
        }
        return true;
    }

    //-> login
    private void login(){
        if(validation()) {
            try {
                UserLoginModel user = new UserLoginModel();
                user.userName = txtUserName.getText().toString().trim();
                user.password = "";
                RetrofitProvider.get().create(IUserAPI.class).login(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> progress.show())
                        .doOnComplete(() -> progress.dismiss())
                        .subscribe(this::handleLoginResults, this::handleLoginError);
            } catch (Exception ex) {
                Log.d(TAG, "login: " + ex.getMessage());
            }
        }
    }

    //-> handleLoginResults
    private void handleLoginResults(Response<UserModel> response) {
        switch (response.code()) {
            case 200:
                PreferenceHelper.setSerializeObject(LoginActivity.this, PreferenceKeyHelper.User, response.body());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

            case 404:
                Toast.makeText(this, "Incorrect user name or password", Toast.LENGTH_SHORT).show();
                break;

            default:
                ApiErrorHelper.error500(getApplicationContext());
                break;
        }
    }

    //-> handleLoginError
    private void handleLoginError(Throwable t){
        progress.dismiss();
        ApiErrorHelper.unableConnectToServer(this, TAG, t);
    }

    @Override
    public void onBackPressed() {

    }
}