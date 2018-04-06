package x_ware.com.edl.networking.dto.project;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectCompanyViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("projectName")
    public String projectName;

    @SerializedName("affiliate")
    public String affiliate;

    @SerializedName("relationship")
    public String relationship;
}
