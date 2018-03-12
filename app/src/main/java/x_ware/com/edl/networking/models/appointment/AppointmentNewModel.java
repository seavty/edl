package x_ware.com.edl.networking.models.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/6/18.
 */

public class AppointmentNewModel implements Serializable {

    @SerializedName("action")
    public String action;

    @SerializedName("subject")
    public String subject;

    @SerializedName("details")
    public String details;

    @SerializedName("dateTimeFrom")
    public String dateTimeFrom;

    @SerializedName("dateTimeTo")
    public String dateTimeTo;

    @SerializedName("comm_brand")
    public String comm_brand;

    @SerializedName("comm_opportunityid")
    public int comm_opportunityid;
}
