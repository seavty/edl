package x_ware.com.edl.networking.models.project;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectCommunicationViewModel implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("companyName")
    public String companyName;

    @SerializedName("action")
    public String action;

    @SerializedName("details")
    public String details;
}
