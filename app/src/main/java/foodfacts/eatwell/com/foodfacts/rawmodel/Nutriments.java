package foodfacts.eatwell.com.foodfacts.rawmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nutriments {

    @SerializedName("energy_unit")
    @Expose
    public String energyUnit;
    @SerializedName("energy")
    @Expose
    public String energy;

}
