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
public class CartData {
    private String id;
    private int itemCount;
    private Double total;
    private List<ItemData> items;
    private List<Object> appliedCoupons;
}
