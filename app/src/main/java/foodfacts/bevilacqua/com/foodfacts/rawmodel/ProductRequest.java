package foodfacts.bevilacqua.com.foodfacts.rawmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductRequest {

    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("product")
    @Expose
    public RawProduct rawProduct;
    @SerializedName("status_verbose")
    @Expose
    public String statusVerbose;

}
