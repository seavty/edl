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
import android.widget.TextView;

import org.reactivestreams.Subscription;
import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.project.ProjectCommunicationAdapter;
import x_ware.com.edl.helpers.ApiErrorHelper;
import x_ware.com.edl.helpers.ProgressDialogHelper;
import x_ware.com.edl.networking.api.IProjectAPI;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.project.ProjectCommunicationViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;
import x_ware.com.edl.networking.RetrofitProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectCommunicationFragment extends Fragment {

    private static final String TAG = "ProjectCommunicationFra";
    private int currentPage = 1;

    private ProgressDialog progress;
    private TextView lblTotalRecord;
    private RecyclerView.Adapter projectCommunicationAdapter;
    private RecyclerView rcvProjectCommunication;

    private ProjectViewModel project;

    public ProjectCommunicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_communication, container, false);
        initializeComponents(view);
        return view;
    }

    //private methods
    //-> initializeComponents()
    private void initializeComponents(View view){
        if(getActivity().getIntent() != null && getActivity().getIntent().hasExtra("ProjectViewModel"))
            project = (ProjectViewModel) getActivity().getIntent().getSerializableExtra("ProjectViewModel");

        setUpViews(view);
        getProjectCommunications();
    }

    private void setUpViews(View view) {
        progress = ProgressDialogHelper.getInstance(getActivity());
        rcvProjectCommunication = view.findViewById(R.id.rcvProjectCommunication);
        rcvProjectCommunication.setHasFixedSize(true);
        rcvProjectCommunication.setLayoutManager(new LinearLayoutManager(getActivity()));
        lblTotalRecord = view.findViewById(R.id.lblTotalRecord);
    }

    //-> getProjectCompanies()
    private void getProjectCommunications(){
        try {
            RetrofitProvider.get(getActivity()).create(IProjectAPI.class).getProjectCommunications(project.id,currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(x -> progress.show())
                    .doOnComplete(() -> progress.dismiss())
                    .subscribe(this::handleGetProjectCommunications, this::handleError);
        }
        catch (Exception ex) {
            Log.d(TAG, "getProjectCommunications: " + ex.getMessage());
        }
    }

    //-> handleResults
    private void handleGetProjectCommunications(Response<GetListModel<ProjectCommunicationViewModel>> response){
        switch (response.code()){
            case 200:
                IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, Object obj) {
                        Log.d(TAG, "onClick: ");
                    }

                    @Override
                    public void onLongClick(View view, int position, Object obj) {
                        Log.d(TAG, "onLongClick: ");
                    }
                };
                lblTotalRecord.setText(response.body().metaData.totalRecord + ""); // total record is integer so need to convert it to string
                projectCommunicationAdapter = new ProjectCommunicationAdapter(response.body().items, getActivity(), listener);
                rcvProjectCommunication.setAdapter(projectCommunicationAdapter);
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
