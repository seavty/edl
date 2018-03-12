package x_ware.com.edl.networking.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by buneavros on 2/20/18.
 */

public class GetListModel<T> implements Serializable {

    @SerializedName("metaData")
    public MetaDataModel metaData;

    @SerializedName("results")
    public List<T> items;

}
