package x_ware.com.edl.networking.dto.project;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectCommunicationViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("comp_Name")
    public String companyName;

    @SerializedName("action")
    public String action;

    @SerializedName("details")
    public String details;
}
