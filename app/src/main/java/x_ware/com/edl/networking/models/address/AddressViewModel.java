package x_ware.com.edl.networking.models.address;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/22/18.
 */

public class AddressViewModel implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("address")
    public int address;
}
