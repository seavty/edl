package x_ware.com.edl.networking.dto.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/6/18.
 */

public class UserProfileDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("userName")
    public String userName;

}
