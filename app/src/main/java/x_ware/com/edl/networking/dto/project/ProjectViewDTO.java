package x_ware.com.edl.networking.dto.project;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("description")
    public String description;

    @SerializedName("stage")
    public String stage;
}
