package x_ware.com.edl.networking.models.appointment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/22/18.
 */

public class AppointmentUploadImage implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("base64")
    public String base64;

}
