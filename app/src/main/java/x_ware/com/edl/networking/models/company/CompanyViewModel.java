package x_ware.com.edl.networking.models.company;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import x_ware.com.edl.networking.models.address.AddressViewModel;
import x_ware.com.edl.networking.models.person.PersonViewModel;

/**
 * Created by buneavros on 2/22/18.
 */

public class CompanyViewModel implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public int name;

    @SerializedName("person")
    public PersonViewModel person;

    @SerializedName("address")
    public AddressViewModel address;


}
