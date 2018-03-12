package x_ware.com.edl.modules.project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {

    private static final String TAG = "ProjectFragment";
    private int currentPage = 1;
    private IProjectAPI projectAPI;
    private Call<GetListModel<ProjectViewModel>> projectCall;

    private ProgressDialog progress;
    private Subscription subscription;
    private CompositeDisposable compositeDisposable;

    private RecyclerView.Adapter projectAdapter;
    private RecyclerView rcvProject;


    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_project, container, false);
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        initializeComponents(view);
        return view;
    }

    // *** private methods *** /

    //-> initializeComponents()

    private void initializeComponents(View view){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        rcvProject = view.findViewById(R.id.rcvProject);
        rcvProject.setHasFixedSize(true);
        rcvProject.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        getProjects();
    }

    //-> getProjects()
    private void getProjects(){
        try {
            RetrofitProvider.get().create(IProjectAPI.class).getProjects(currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleResults, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getAppointments: " + ex.getMessage());
        }
    }

    //-> handleResults
    private void handleResults(Response<GetListModel<ProjectViewModel>> response){
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, Object obj) {
                        Log.d(TAG, "onClick: " + position);
                        ProjectViewModel project = (ProjectViewModel)obj;
                        //startActivity(new Intent(getActivity().getApplicationContext(), ProjectDetailActivity.class));
                        Intent intent =  new Intent(getActivity().getApplicationContext(), ProjectDetailActivity.class);
                        intent.putExtra("ProjectViewModel", (Serializable) project);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position, Object obj) {
                        Log.d(TAG, "onLongClick: ");

                    }
                };
                projectAdapter = new ProjectAdapter((List<ProjectViewModel>) response.body().items, getActivity().getApplicationContext() , listener);
                rcvProject.setAdapter(projectAdapter);
                break;

            case 500:
                Helper.error500(getActivity().getApplicationContext());
                break;

        }
    }

    //-> handleError
    private void handleError(Throwable t){
        progress.dismiss();
        Helper.reuqestError(getActivity().getApplicationContext(), TAG, t);
    }

}
