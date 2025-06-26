package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetails {
    private String brand;
    private String productName;
    private String productType;
    private String productDept;
    private String productStyle;
    private String productColor;
    private String productClass;
    private String skuSize;
    private String productId;
    private boolean onHold;
    private String taxCode;
    private List<String> productExcludedSites;
    private List<String> skuExcludedSites;
    private List<String> productClearanceSites;
    private boolean productNonReturnable;
    private Object returnInfo;
    @JsonProperty("isProductHazmat")
    private boolean productHazmat;
    @JsonProperty("isProductThirdParty")
    private boolean productThirdParty;
    @JsonProperty("isSkuOnDemand")
    private boolean skuOnDemand;
}
