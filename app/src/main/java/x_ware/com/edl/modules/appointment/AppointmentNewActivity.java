package x_ware.com.edl.modules.appointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.MainActivity;
import x_ware.com.edl.R;
import x_ware.com.edl.networking.api.IAppointmentAPI;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.helpers.Helper;
import x_ware.com.edl.networking.models.appointment.AppointmentNewModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

public class AppointmentNewActivity extends AppCompatActivity {

    private static final String TAG = AppointmentNewActivity.class.getSimpleName();

    private ProgressDialog progress;
    private EditText txtSubject, txtCommunicationDetails, txtStartTime, txtEndTime;
    private ImageButton imbStatTime, imbEndTime, imbSave;

    private Spinner spnAction;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_new);
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

    private void initializeComponents(){
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        txtSubject = findViewById(R.id.txtSubject);
        txtCommunicationDetails = findViewById(R.id.txtCommunicationDetails);
        txtStartTime = findViewById(R.id.txtStartTime); txtStartTime.setEnabled(false); txtStartTime.setTextColor(Color.BLACK);
        txtEndTime = findViewById(R.id.txtEndTime); txtEndTime.setEnabled(false); txtEndTime.setTextColor(Color.BLACK);

        imbStatTime = findViewById(R.id.imbStartTime);
        imbEndTime = findViewById(R.id.imbEndTime);
        imbSave = findViewById(R.id.imbSave);


        spnAction = findViewById(R.id.spnAction);


        setUpSpinner();
        setUpEvent();
        clearData();
    }

    private void setUpSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_action_array, R.layout.spinner_layout);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
// Apply the adapter to the spinner
        spnAction.setAdapter(adapter);
    }

    private void setUpEvent(){
        imbStatTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeClick();
            }
        });

        imbEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimeClick();
            }
        });

        imbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveClick();
            }
        });
    }

    private void startTimeClick(){
        datePicker(txtStartTime);
    }

    private void endTimeClick(){
        datePicker(txtEndTime);
    }

    private void saveClick(){
        try {
            if(validation()) {
                 //-- use token
                AppointmentNewModel appointmentNew = new AppointmentNewModel();
                String actionStr = spnAction.getSelectedItem().toString();
                if(actionStr.equals("Sample Request"))
                    actionStr = "SampleRequest";
                else if(actionStr.equals("Sales Visit"))
                    actionStr = "SalesVisit";

                appointmentNew.action = actionStr;
                appointmentNew.subject = txtSubject.getText().toString();
                appointmentNew.details = txtCommunicationDetails.getText().toString();
                appointmentNew.dateTimeFrom = DateTimeHelper.convert_dd_mm_yyy_hh_mm_To_yyyy_mm_dd_hh_mm_ss(txtStartTime.getText().toString());
                appointmentNew.dateTimeTo = DateTimeHelper.convert_dd_mm_yyy_hh_mm_To_yyyy_mm_dd_hh_mm_ss(txtEndTime.getText().toString());
                RetrofitProvider.get(getApplicationContext()).create(IAppointmentAPI.class).createNewAppointment(appointmentNew)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(x -> progress.show())
                        .doOnComplete(() -> progress.dismiss())
                        .subscribe(this::handleSaveResults, this::handleSaveError);
            }
        }
        catch (Exception ex) {
            Log.d(TAG, "getAppointment: " + ex.getMessage());
        }
    }



    private boolean validation(){
        txtSubject.setError(null);
        txtCommunicationDetails.setError(null);
        txtStartTime.setError(null);
        txtEndTime.setError(null);
        if(txtSubject.getText().toString().trim().isEmpty()) {
            txtSubject.setError( "Subject is required!" );
            return false;
        }

        if(txtCommunicationDetails.getText().toString().trim().isEmpty()) {
            txtCommunicationDetails.setError( "Comm Detail required!" );
            return false;
        }

        if(txtStartTime.getText().toString().trim().isEmpty()) {
            txtStartTime.setError( "Start Time is required!" );
            return false;
        }

        if(txtEndTime.getText().toString().trim().isEmpty()) {
            txtStartTime.setError( "End Time is required!" );
            return false;
        }
        return true;
    }

    private void datePicker(final EditText txtDtp){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd;
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //api >= 21 time picker show only one time
                        //but api < 21 time picker show two time so we need to use this trick
                        if(count == 0) {
                            timePicker(txtDtp, Helper.DDMMYYYY(year, (month+1), day)); // tmp comment
                            count++;
                        }
                        else {
                            count =0;
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    private void timePicker(final EditText txt, String dateStr){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(AppointmentNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String timeSrr= DateTimeHelper.time_HH_MM(hour, minute);
                txt.setText(dateStr + " " + timeSrr);
            }
        }, mHour, mMinute, true);
        tpd.show();
    }


    private void handleSaveResults(Response<AppointmentViewModel> response) {
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                Log.d(TAG, "handleSaveResults: ");
                Toast.makeText(this, "Successfully created appointment", Toast.LENGTH_SHORT).show();
                clearData();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                //clearData();
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
    private void handleSaveError(Throwable t){
        progress.dismiss();
        //Helper.reuqestError(getApplicationContext(), TAG, t);
    }

    private void clearData(){
        txtSubject.setText("");
        txtCommunicationDetails.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}