package x_ware.com.edl.adapters.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import x_ware.com.edl.R;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.models.project.ProjectCommunicationViewModel;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectCommunicationAdapter extends RecyclerView.Adapter<ProjectCommunicationAdapter.ViewHolder> {

    private static final String TAG = "ProjectCommunicationAda";
    private List<ProjectCommunicationViewModel> projectCommunications;
    private Context context;
    private IRecyclerViewClickListener listener;

    public ProjectCommunicationAdapter(List<ProjectCommunicationViewModel> projectCommunications, Context context, IRecyclerViewClickListener listener) {
        this.projectCommunications = projectCommunications;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ProjectCommunicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project_communication, parent, false);
        return new ProjectCommunicationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectCommunicationAdapter.ViewHolder holder, int position) {
        ProjectCommunicationViewModel projectCommunication = projectCommunications.get(position);
        //-- remember id must convert to text first before setting

        holder.lblCompanyName.setText(projectCommunication.companyName);
        holder.lblAction.setText(projectCommunication.action);
        holder.lblDetails.setText(projectCommunication.details);
    }

    @Override
    public int getItemCount() {
        return projectCommunications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView lblCompanyName, lblAction, lblDetails;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            lblCompanyName = itemView.findViewById(R.id.lblCompanyName);
            lblAction = itemView.findViewById(R.id.lblAction);
            lblDetails = itemView.findViewById(R.id.lblDetails);

        }

        @Override
        public void onClick(View view) {
            ProjectCommunicationViewModel projectCommunication = projectCommunications.get(getAdapterPosition());
            listener.onClick(view, getAdapterPosition(), projectCommunication);
        }

        @Override
        public boolean onLongClick(View view) {
            ProjectCommunicationViewModel projectCommunication = projectCommunications.get(getAdapterPosition());
            listener.onLongClick(view, getAdapterPosition(), projectCommunication);
            return true;
        }
    }
}
