package x_ware.com.edl.modules.project;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.project.ProjectCompanyAdapter;
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.helpers.Helper;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.project.ProjectCompanyViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectCompanyFragment extends Fragment {

    private static final String TAG = "ProjectCompanyFragment";
    private int currentPage = 1;
    private Call<GetListModel<ProjectCompanyViewModel>> projectCompanyCall;

    private ProgressDialog progress;
    private Subscription subscription;
    private CompositeDisposable compositeDisposable;

    private RecyclerView.Adapter projectCompanyAdapter;
    private RecyclerView rcvProjectCompany;

    private ProjectViewModel project;


    public ProjectCompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_project_company, container, false);

        View view = inflater.inflate(R.layout.fragment_project_company, container, false);
        initializeComponents(view);
        getProjectCompanies();
        return view;
    }

    //private methods
    //-> initializeComponents()
    private void initializeComponents(View view){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        rcvProjectCompany = ((RecyclerView) view.findViewById(R.id.rcvProjectCompany));
        rcvProjectCompany.setHasFixedSize(true);
        rcvProjectCompany.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        if(getActivity().getIntent() != null && getActivity().getIntent().hasExtra("ProjectViewModel")) {
            project = (ProjectViewModel) getActivity().getIntent().getSerializableExtra("ProjectViewModel");
        }

        Log.d(TAG, "initializeComponents: " + TAG);
    }

    //-> getProjectCompanies()
    private void getProjectCompanies(){
        try {
            RetrofitProvider.get().create(IProjectAPI.class).getProjectCompanies(project.id, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleResults, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getProjectCompanies: " + ex.getMessage());
        }
    }

    //-> handleResults
    private void handleResults(Response<GetListModel<ProjectCompanyViewModel>> response){
        Log.d(TAG, "handleResults: " + response.code());

        switch (response.code()) {
            case 200:
                IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, Object obj) {
                        Log.d(TAG, "onClick: " + position);
                        //startActivity(new Intent(getApplicationContext(), ProjectDetailActivity.class));
                    }

                    @Override
                    public void onLongClick(View view, int position, Object obj) {

                    }
                };

                List<ProjectCompanyViewModel> projectCompanies = response.body().items;
                projectCompanyAdapter = new ProjectCompanyAdapter(projectCompanies, getActivity().getApplicationContext() , listener);
                rcvProjectCompany.setAdapter(projectCompanyAdapter);
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
