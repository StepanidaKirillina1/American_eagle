package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private String itemId;
    private int quantity;
    private double discount;
    private double originalPrice;
    private double price;
    public double tax;
    private boolean discounted;
    private int discountPercent;
    private boolean gwp;
    private String itemType;
    private String productId;
    private String productStyle;
    private String productName;
    private String size;
    private String color;
    private List<Promotion> promotions;
    private String sku;
    private double unitListPrice;
    private double unitSalePrice;
    private boolean available;
    private List<Object> closenessQualifiers;
    private boolean onDemandItem;
    private String brand;
    private boolean editable;
    private int availabilityStatus;
    private DeliveryOptions deliveryOptions;
    private String addItemTime;
    private ProductDetails productDetails;
    private double returnValue;
    private double discountAdjustment;
    private List<PriceDetail> priceDetails;
    private boolean clearance;
    private boolean nonreturnable;
    private boolean hazmat;
}

