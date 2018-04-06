package x_ware.com.edl.networking.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by buneavros on 2/20/18.
 */

public class GetListDTO<T> implements Serializable {

    @SerializedName("metaData")
    public MetaDataDTO metaData;

    @SerializedName("results")
    public List<T> items;

}
