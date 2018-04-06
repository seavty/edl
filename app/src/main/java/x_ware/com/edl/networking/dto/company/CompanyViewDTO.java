package x_ware.com.edl.networking.dto.company;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import x_ware.com.edl.networking.dto.address.AddressViewDTO;
import x_ware.com.edl.networking.dto.person.PersonViewDTO;

/**
 * Created by buneavros on 2/22/18.
 */

public class CompanyViewDTO implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public int name;

    @SerializedName("person")
    public PersonViewDTO person;

    @SerializedName("address")
    public AddressViewDTO address;


}
