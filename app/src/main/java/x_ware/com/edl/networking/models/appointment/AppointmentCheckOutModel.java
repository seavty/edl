package x_ware.com.edl.networking.models.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/1/18.
 */

public class AppointmentCheckOutModel implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("comm_Y2")
    public double latitude;

    @SerializedName("comm_X2")
    public double longitude;
}
