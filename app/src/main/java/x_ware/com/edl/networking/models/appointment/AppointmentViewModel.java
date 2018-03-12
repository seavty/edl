package x_ware.com.edl.networking.models.appointment;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by buneavros on 2/20/18.
 */

public class AppointmentViewModel implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("timing")
    public String timing;

    @SerializedName("companyName")
    public String companyName;

    @SerializedName("address")
    public String address;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("subject")
    public String subject;

    @SerializedName("details")
    public String details;

    @SerializedName("checkIncheckOut")
    public String checkIncheckOut;

    @SerializedName("action")
    public String action;
}
