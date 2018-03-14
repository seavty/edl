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
import x_ware.com.edl.adapters.project.ProjectSpecificationAdapter;
import x_ware.com.edl.helpers.ApiErrorHelper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.project.ProjectCommunicationViewModel;
import x_ware.com.edl.networking.models.project.ProjectSpecificationViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectSpecificationFragment extends Fragment {

    private static final String TAG = "ProjectSpecificationFra";

    private int currentPage = 1;
    private ProgressDialog progress;

    private RecyclerView.Adapter projectSpecificationAdapter;
    private RecyclerView rcvSpecifications;

    private ProjectViewModel project;

    public ProjectSpecificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_specification, container, false);
        initializeComponents(view);
        getProjectSpecifications();
        return view;
    }

    //private methods
    //-> initializeComponents()
    private void initializeComponents(View view){
        progress = ProgressDialogHelper.getInstance(getActivity());

        rcvSpecifications = view.findViewById(R.id.rcvProjectSpecification);
        rcvSpecifications.setHasFixedSize(true);
        rcvSpecifications.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getActivity().getIntent() != null && getActivity().getIntent().hasExtra("ProjectViewModel")) {
            project = (ProjectViewModel) getActivity().getIntent().getSerializableExtra("ProjectViewModel");
        }
    }

    //-> getProjectCompanies()
    private void getProjectSpecifications(){
        try {
            RetrofitProvider.get().create(IProjectAPI.class).getProjectSpecifications(project.id,1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetProjectSpecifications, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getProjectSpecifications: " + ex.getMessage());
        }
    }

    //-> handleResults
    private void handleGetProjectSpecifications(Response<GetListModel<ProjectSpecificationViewModel>> response){
        switch (response.code()){
            case 200:
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
                List<ProjectSpecificationViewModel> specifications = response.body().items;
                projectSpecificationAdapter = new ProjectSpecificationAdapter(specifications, getActivity() , listener);
                rcvSpecifications.setAdapter(projectSpecificationAdapter);
//            if(specifications.size() == 0)
//                Helper.noRecordToast(getActivity().getApplicationContext());
                break;

            case 500:
                ApiErrorHelper.statusCode500(getActivity());
                break;

            default:
                ApiErrorHelper.statusCode500(getActivity());
                break;
        }

    }

    //-> handleError
    private void handleError(Throwable t){
        progress.dismiss();
        ApiErrorHelper.unableConnectToServer(getActivity(), TAG, t);
    }


}
