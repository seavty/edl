package x_ware.com.edl.networking.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by buneavros on 2/20/18.
 */

public class MetaDataDTO implements Serializable {

    @SerializedName("currentPage")
    public int currentPage;

    @SerializedName("pageSize")
    public int pageSize;

    @SerializedName("totalPage")
    public int totalPage;

    @SerializedName("totalRecord")
    public int totalRecord;

    @SerializedName("orderColumn")
    public String orderColumn;

    @SerializedName("orderBy")
    public String orderBy;

    @SerializedName("search")
    public String search;
}
