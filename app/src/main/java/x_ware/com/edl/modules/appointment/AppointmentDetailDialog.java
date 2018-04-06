package x_ware.com.edl.modules.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import x_ware.com.edl.R;
import x_ware.com.edl.networking.dto.appointment.AppointmentViewDTO;

/**
 * Created by buneavros on 3/2/18.
 */

public class AppointmentDetailDialog extends AppCompatDialogFragment {

    private AppointmentDetailDialogListener listener;
    private EditText txtDetail;
    public AppointmentViewDTO appointment;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_communication_detail, null);

        builder.setView(view)
                .setTitle("Detail")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    appointment.details = txtDetail.getText().toString();
                    listener.onComplete(appointment);
                });

        txtDetail = view.findViewById(R.id.txtDetail);
        txtDetail.setText(appointment.details);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AppointmentDetailDialogListener) context;
        }
        catch (ClassCastException ex){
            throw new ClassCastException(context.toString() +
                "must implement AppointmentDetailDialogListener"
            );
        }
    }

    public interface AppointmentDetailDialogListener {
        void onComplete(AppointmentViewDTO appointment);
    }
}
