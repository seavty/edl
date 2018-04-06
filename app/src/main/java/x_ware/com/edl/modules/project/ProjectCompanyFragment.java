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

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.project.ProjectCompanyAdapter;
import x_ware.com.edl.helpers.ApiHelper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.dto.GetListDTO;
import x_ware.com.edl.networking.dto.project.ProjectCompanyViewDTO;
import x_ware.com.edl.networking.dto.project.ProjectViewDTO;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectCompanyFragment extends Fragment {

    private static final String TAG = "ProjectCompanyFragment";

    private int currentPage = 1;

    private ProgressDialog progress;
    private RecyclerView.Adapter projectCompanyAdapter;
    private RecyclerView rcvProjectCompany;

    private ProjectViewDTO project;


    public ProjectCompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_company, container, false);
        initializeComponents(view);
        getProjectCompanies();
        return view;
    }

    //private methods
    //-> initializeComponents()
    private void initializeComponents(View view){
        progress = ProgressDialogHelper.getInstance(getActivity());

        rcvProjectCompany = view.findViewById(R.id.rcvProjectCompany);
        rcvProjectCompany.setHasFixedSize(true);
        rcvProjectCompany.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getActivity().getIntent() != null && getActivity().getIntent().hasExtra("ProjectViewModel")) {
            project = (ProjectViewDTO) getActivity().getIntent().getSerializableExtra("ProjectViewModel");
        }
    }

    //-> getProjectCompanies()
    private void getProjectCompanies(){
        try {
            RetrofitProvider.get(getActivity()).create(IProjectAPI.class).getProjectCompanies(project.id, currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetProjectCompanies, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getProjectCompanies: " + ex.getMessage());
        }
    }

    //-> handleResults
    private void handleGetProjectCompanies(Response<GetListDTO<ProjectCompanyViewDTO>> response){
        if(ApiHelper.isSuccessful(getActivity(), response.code())){
            IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position, Object obj) {
                    Log.d(TAG, "onClick: " + position);
                }

                @Override
                public void onLongClick(View view, int position, Object obj) {
                    Log.d(TAG, "onLongClick: ");
                }
            };

            List<ProjectCompanyViewDTO> projectCompanies = response.body().items;
            projectCompanyAdapter = new ProjectCompanyAdapter(projectCompanies, getActivity(), listener);
            rcvProjectCompany.setAdapter(projectCompanyAdapter);
        }
    }

    //-> handleError
    private void handleError(Throwable t){
        progress.dismiss();
        ApiHelper.unableConnectToServer(getActivity(), TAG, t);
    }
}
