package x_ware.com.edl.networking.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/27/18.
 */

public class UserLoginModel implements Serializable{

    @SerializedName("userName")
    public String userName;

    @SerializedName("password")
    public String password;
}
