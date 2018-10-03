package foodfacts.bevilacqua.com.foodfacts.rawmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_name_fr")
    @Expose
    public String productName;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("ingredients")
    @Expose
    public List<Ingredient> ingredients = null;
    @SerializedName("nutriments")
    @Expose
    public Nutriments nutriments;

}
