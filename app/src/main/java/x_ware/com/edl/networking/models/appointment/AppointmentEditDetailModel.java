package x_ware.com.edl.networking.models.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/1/18.
 */

public class AppointmentEditDetailModel implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("details")
    public String details;
}
