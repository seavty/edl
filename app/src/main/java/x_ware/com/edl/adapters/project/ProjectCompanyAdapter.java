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
import x_ware.com.edl.networking.dto.project.ProjectCompanyViewDTO;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectCompanyAdapter extends RecyclerView.Adapter<ProjectCompanyAdapter.ViewHolder> {

    private static final String TAG = "ProjectCompanyAdapter";
    private List<ProjectCompanyViewDTO> projectCompanies;
    private Context context;
    private IRecyclerViewClickListener listener;

    public ProjectCompanyAdapter(List<ProjectCompanyViewDTO> projectCompanies, Context context, IRecyclerViewClickListener listener) {
        this.projectCompanies = projectCompanies;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ProjectCompanyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project_company, parent, false);
        return new ProjectCompanyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectCompanyAdapter.ViewHolder holder, int position) {
        ProjectCompanyViewDTO projectCompany = projectCompanies.get(position);
        //-- remember id must convert to text first before setting
        holder.lblAffiliate.setText(projectCompany.affiliate);
        holder.lblRelationship.setText(projectCompany.relationship);
    }

    @Override
    public int getItemCount() {
        return projectCompanies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView lblProjectName, lblAffiliate, lblRelationship;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            lblAffiliate = itemView.findViewById(R.id.lblAffiliate);
            lblRelationship = itemView.findViewById(R.id.lblRelationship);

        }

        @Override
        public void onClick(View view) {
            ProjectCompanyViewDTO projectCompany = projectCompanies.get(getAdapterPosition());
            listener.onClick(view, getAdapterPosition(), projectCompany);
        }

        @Override
        public boolean onLongClick(View view) {
            ProjectCompanyViewDTO projectCompany = projectCompanies.get(getAdapterPosition());
            listener.onLongClick(view, getAdapterPosition(), projectCompany);
            return true;
        }
    }
}
