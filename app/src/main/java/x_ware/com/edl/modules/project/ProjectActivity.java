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
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.helpers.Helper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

public class ProjectActivity extends AppCompatActivity {
    private static final String TAG = "ProjectActivity";
    private int currentPage = 1;
    private IProjectAPI projectAPI;
    private Call<GetListModel<ProjectViewModel>> projectCall;

    private ProgressDialog progress;
    private Subscription subscription;
    private CompositeDisposable compositeDisposable;

    private RecyclerView.Adapter projectAdapter;
    private RecyclerView rcvProject;

    private AppointmentViewModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
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
        progress = ProgressDialogHelper.getInstance(this);
        rcvProject = findViewById(R.id.rcvProject);
        rcvProject.setHasFixedSize(true);
        rcvProject.setLayoutManager(new LinearLayoutManager(this));
    }

    //-> getProjects()
    private void getProjects(){
        try {
            RetrofitProvider.get().create(IProjectAPI.class).getProjectsBoundWithCompany(appointment.id, currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetProjectsResult, this::handleGetProjectsError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getAppointments: " + ex.getMessage());
        }
    }

    //-> handleGetProjectsResult
    private void handleGetProjectsResult(Response<GetListModel<ProjectViewModel>> response){
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, Object obj) {
                        ProjectViewModel project = (ProjectViewModel)obj;
                        Intent intent =  new Intent(getApplicationContext(), ProjectDetailActivity.class);
                        intent.putExtra("ProjectViewModel", (Serializable) project);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position, Object obj) {

                    }
                };
                List<ProjectViewModel> projects = response.body().items;
                projectAdapter = new ProjectAdapter(projects, this , listener);
                rcvProject.setAdapter(projectAdapter);
                break;

            case 500:
                Helper.error500(this);
                break;
        }
    }

    //-> handleError
    private void handleGetProjectsError(Throwable t){
        progress.dismiss();
    }
}
