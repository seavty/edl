package x_ware.com.edl.networking.dto.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/22/18.
 */

public class AppointmentUploadDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("base64")
    public String base64;

}
