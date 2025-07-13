package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemData {
    private String itemId;
    private String sku;
    private String productId;
    private Integer quantity;
    private String deliveryOption;
}
