package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sku {
    private int displaySeq;
    private String size;
    private double salePrice;
    private int inventoryStatus;
    private boolean onlineOnly;
    private String sizeCode;
    private String skuId;
    private double listPrice;
}
