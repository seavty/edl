package x_ware.com.edl.adapters.appointment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import x_ware.com.edl.R;
import x_ware.com.edl.helpers.DateTimeHelper;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.networking.dto.appointment.AppointmentViewDTO;
import x_ware.com.edl.modules.project.ProjectActivity;

/**
 * Created by buneavros on 2/21/18.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private static final String TAG = "AppointmentAdapter";
    private List<AppointmentViewDTO> appointments;
    private Context context;
    private IRecyclerViewClickListener listener;

    public AppointmentAdapter(List<AppointmentViewDTO> appointments, Context context, IRecyclerViewClickListener listener) {
        this.appointments = appointments;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appointment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        AppointmentViewDTO appointment = appointments.get(position);
        //-- remember id must convert to text first before setting
        //holder.lblTiming.setText(DateTimeHelper.convert_yyyy_mm_dd_t_hh_mm_ss_To_dd_mm_yyy_hh_mm(appointment.timing));
        holder.lblTiming.setText(DateTimeHelper.convert_yyyy_mm_dd_t_hh_mm_ss_To_hh_mm_With_am_pm(appointment.timing));
        holder.lblCompanyName.setText(appointment.companyName);
        holder.lblAddress.setText(appointment.address);

        holder.imgCheck.setVisibility(View.INVISIBLE);
        if(appointment.checkIncheckOut != null) {
            if (appointment.checkIncheckOut.equals("Checked Out"))
               holder.imgCheck.setVisibility(View.VISIBLE);

        }
        switch (appointment.action){
            case "Delivery":
                holder.imgAction.setImageDrawable(context.getResources().getDrawable(R.drawable.delivery));
                break;

            case "Presentation":
                holder.imgAction.setImageDrawable(context.getResources().getDrawable(R.drawable.presentation));
                break;

            case "SampleRequest":
                holder.imgAction.setImageDrawable(context.getResources().getDrawable(R.drawable.sample_request));
                break;

            case "SalesVisit":
                holder.imgAction.setImageDrawable(context.getResources().getDrawable(R.drawable.sales_visit));
                break;

            default:
                holder.imgAction.setImageDrawable(context.getResources().getDrawable(R.drawable.presentation));
                break;
        }

        holder.imbProject.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProjectActivity.class);
            intent.putExtra("AppointmentViewModel", appointment);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        if(appointment.address !=null && !appointment.address.equals("")) {

            holder.lblAddress.setOnClickListener(view -> {
                //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(appointment.address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mapIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView lblTiming, lblCompanyName, lblAddress;
        private ImageView imgAction, imgCheck;
        private ImageButton imbProject;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            lblTiming = itemView.findViewById(R.id.lblTiming);
            lblCompanyName = itemView.findViewById(R.id.lblCompanyName);
            lblAddress = itemView.findViewById(R.id.lblAddress);

            imgAction = itemView.findViewById(R.id.imgAction);
            imgCheck = itemView.findViewById(R.id.imgCheck);

            imbProject = itemView.findViewById(R.id.imbProject);
        }

        @Override
        public void onClick(View view) {
            AppointmentViewDTO appointment = appointments.get(getAdapterPosition());
            listener.onClick(view, getAdapterPosition(), appointment);
        }

        @Override
        public boolean onLongClick(View view) {
            AppointmentViewDTO appointment = appointments.get(getAdapterPosition());
            listener.onLongClick(view, getAdapterPosition(), appointment);
            return true;
        }
    }
}
