package x_ware.com.edl.modules.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.project.ProjectAdapter;
import x_ware.com.edl.helpers.ApiErrorHelper;
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

public class ProjectActivity extends AppCompatActivity {
    private static final String TAG = "ProjectActivity";

    private int currentPage = 1;
    private ProgressDialog progress;

    private RecyclerView.Adapter projectAdapter;
    private RecyclerView rcvProject;

    private AppointmentViewModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initializeComponents();
    }

//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//    }

    //-> initializeComponents
    private void initializeComponents(){
        if(getIntent() != null && getIntent().hasExtra("AppointmentViewModel"))
            appointment = (AppointmentViewModel) getIntent().getSerializableExtra("AppointmentViewModel");

        setUpViews();
        getProjects();
    }

    //-> setUpViews
    private void setUpViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Project Listing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = ProgressDialogHelper.getInstance(this);
        rcvProject = findViewById(R.id.rcvProject);
        rcvProject.setHasFixedSize(true);
        rcvProject.setLayoutManager(new LinearLayoutManager(this));
    }

    //-> getProjects()
    private void getProjects(){
        try {
            RetrofitProvider.get(this).create(IProjectAPI.class).getProjectsBoundWithCompany(appointment.id, currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetProjects, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getAppointments: " + ex.getMessage());
        }
    }

    //-> handleGetProjectsResult
    private void handleGetProjects(Response<GetListModel<ProjectViewModel>> response){
        switch (response.code()) {
            case 200:
                IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, Object obj) {
                        ProjectViewModel project = (ProjectViewModel)obj;
                        Intent intent =  new Intent(getApplicationContext(), ProjectDetailActivity.class);
                        intent.putExtra("ProjectViewModel", project);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position, Object obj) {
                        Log.d(TAG, "onLongClick: ");
                    }
                };
                List<ProjectViewModel> projects = response.body().items;
                projectAdapter = new ProjectAdapter(projects, this , listener);
                rcvProject.setAdapter(projectAdapter);
                break;

            case 401:
                ApiErrorHelper.statusCode401(this);
                break;

            default:
                ApiErrorHelper.statusCode500(this);
                break;
        }
    }

    //-> handleError
    private void handleError(Throwable t){
        progress.dismiss();
        ApiErrorHelper.unableConnectToServer(this, TAG, t);
    }
}
