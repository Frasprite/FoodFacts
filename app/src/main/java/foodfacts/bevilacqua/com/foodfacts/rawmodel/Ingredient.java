package foodfacts.bevilacqua.com.foodfacts.rawmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("rank")
    @Expose
    public int rank;

}
