package x_ware.com.edl.networking.dto.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/1/18.
 */

public class AppointmentCheckInDTO implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("comm_Y")
    public double latitude;

    @SerializedName("comm_X")
    public double longitude;
}
