package x_ware.com.edl.networking.dto.address;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/22/18.
 */

public class AddressViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("address")
    public int address;
}
