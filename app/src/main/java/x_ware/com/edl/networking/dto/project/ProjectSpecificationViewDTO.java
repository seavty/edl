package x_ware.com.edl.networking.dto.project;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/23/18.
 */

public class ProjectSpecificationViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("specification")
    public String specification;

    @SerializedName("productCode")
    public String productCode;

    @SerializedName("remarks")
    public String remarks;
}
