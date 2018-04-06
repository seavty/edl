package x_ware.com.edl.networking.dto.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 3/6/18.
 */

public class UserModel implements Serializable {

    @SerializedName("userProfile")
    public UserProfileDTO userProfile;

    @SerializedName("token")
    public String token;

}
