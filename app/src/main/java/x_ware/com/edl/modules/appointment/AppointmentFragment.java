package x_ware.com.edl.modules.appointment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.appointment.AppointmentAdapter;
import x_ware.com.edl.networking.api.IAppointmentAPI;
import x_ware.com.edl.helpers.ApiHelper;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    private static final String TAG = "AppointmentFragment";

    private int currentPage = 1;
    private ProgressDialog progress;

    private EditText txtDate;
    private ImageButton imbCalendar, imbNewAppointment;
    private RecyclerView.Adapter appointmentAdapter;
    private RecyclerView rcvAppointment;

    public String strDate;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        initializeComponents(view);
        return view;
    }

    //-> initializeComponents
    private void initializeComponents(View view) {
        setUpViews(view);
        setUpEvents();
        getAppointments();
    }

    //-> setUpViews
    private void setUpViews(View view) {
        progress = ProgressDialogHelper.getInstance(getActivity());

        txtDate = view.findViewById(R.id.txtDate);
        txtDate.setText(DateTimeHelper.get_CurrentDate_dd_mm_yyyy());
        txtDate.setEnabled(false);
        txtDate.setTextColor(Color.BLACK);

        imbCalendar = view.findViewById(R.id.imbCalendar);
        imbNewAppointment = view.findViewById(R.id.imbNewAppointment);

        rcvAppointment = view.findViewById(R.id.rcvAppointment);
        rcvAppointment.setHasFixedSize(true);
        rcvAppointment.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //-> setUpEvents
    private void setUpEvents() {
        imbCalendar.setOnClickListener(view -> openCalendarDialog());
        imbNewAppointment.setOnClickListener(view -> newAppointment());
    }

    //-> getAppointments
    private void getAppointments() {
        try {
            RetrofitProvider.get(getActivity()).create(IAppointmentAPI.class).searchAppointments(currentPage, DateTimeHelper.convert_dd_mm_yyyy_To_yyyy_mm_dd(txtDate.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetAppointments, this::handleError);
        } catch (Exception ex) {
            Log.d(TAG, "getAppointment: " + ex.getMessage());
        }
    }

    //-> handleGetAppointments
    private void handleGetAppointments(Response<GetListModel<AppointmentViewModel>> response) {
        if(ApiHelper.isSuccessful(getActivity(), response.code())){
            IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position, Object obj) {
                    AppointmentViewModel appointment = (AppointmentViewModel) obj;
                    Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
                    intent.setData(null);
                    intent.putExtra("AppointmentViewModel", appointment);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position, Object obj) {
                    Log.d(TAG, "onLongClick: ");
                }
            };

            List<AppointmentViewModel> appointments = response.body().items;
            appointmentAdapter = new AppointmentAdapter(appointments, getActivity(), listener);
            rcvAppointment.setAdapter(appointmentAdapter);
        }
    }

    //-> newAppointment
    private void newAppointment() {
        Intent intent = new Intent(getActivity(), AppointmentNewActivity.class);
        startActivity(intent);
    }

    //-> openCalendarDialog
    private void openCalendarDialog() {
        dtp(txtDate);
    }

    //-> dtp
    private void dtp(final EditText txtDtp) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                (view, year, month, day) -> {
                    txtDtp.setText(DateTimeHelper.get_dd_mm_yyy(year, (month + 1), day));
                    getAppointments();

                }, mYear, mMonth, mDay);
        dpd.show();
    }

    //-> handleError
    private void handleError(Throwable t) {
        progress.dismiss();
        ApiHelper.unableConnectToServer(getActivity(), TAG, t);
    }
}
