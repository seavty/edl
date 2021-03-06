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
import x_ware.com.edl.networking.dto.project.ProjectSpecificationViewDTO;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectSpecificationAdapter extends RecyclerView.Adapter<ProjectSpecificationAdapter.ViewHolder> {

    private static final String TAG = "ProjectSpecificationAda";
    private List<ProjectSpecificationViewDTO> projectSpecifications;
    private Context context;
    private IRecyclerViewClickListener listener;

    public ProjectSpecificationAdapter(List<ProjectSpecificationViewDTO> projectSpecifications, Context context, IRecyclerViewClickListener listener) {
        this.projectSpecifications = projectSpecifications;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ProjectSpecificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project_specification, parent, false);
        return new ProjectSpecificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectSpecificationAdapter.ViewHolder holder, int position) {
        ProjectSpecificationViewDTO projectSpecification = projectSpecifications.get(position);
        //-- remember id must convert to text first before setting
        holder.lblProductCode.setText(projectSpecification.productCode);
        holder.lblRemarks.setText(projectSpecification.remarks);
    }

    @Override
    public int getItemCount() {
        return projectSpecifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        private TextView lblProductCode, lblRemarks;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            lblProductCode = itemView.findViewById(R.id.lblProductCode);
            lblRemarks = itemView.findViewById(R.id.lblRemarks);

        }

        @Override
        public void onClick(View view) {
            ProjectSpecificationViewDTO projectSpecification = projectSpecifications.get(getAdapterPosition());
            listener.onClick(view, getAdapterPosition(), projectSpecification);
        }

        @Override
        public boolean onLongClick(View view) {
            ProjectSpecificationViewDTO projectSpecification = projectSpecifications.get(getAdapterPosition());
            listener.onLongClick(view, getAdapterPosition(), projectSpecification);
            return true;
        }
    }
}
