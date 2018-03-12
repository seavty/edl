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
import x_ware.com.edl.networking.models.project.ProjectViewModel;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private static final String TAG = "ProjectAdapter";
    private List<ProjectViewModel> projects;
    private Context context;
    private IRecyclerViewClickListener listener;

    public ProjectAdapter(List<ProjectViewModel> projects, Context context, IRecyclerViewClickListener listener) {
        this.projects = projects;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project, parent, false);
        return new ProjectAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectAdapter.ViewHolder holder, int position) {
        ProjectViewModel project = projects.get(position);
        //-- remember id must convert to text first before setting
        holder.lblProjectName.setText(project.description);
        holder.lblStage.setText(project.stage); //tmp use other field // becos api not yet ready
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblProjectName, lblStage;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            lblProjectName = itemView.findViewById(R.id.lblProjectName);
            lblStage = itemView.findViewById(R.id.lblStage);

        }

        @Override
        public void onClick(View view) {
            ProjectViewModel project = projects.get(getAdapterPosition());
            listener.onClick(view, getAdapterPosition(), project);
        }
    }
}
