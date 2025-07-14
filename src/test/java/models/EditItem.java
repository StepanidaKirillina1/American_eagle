package models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditItem {
    private String itemId;
    private int quantity;
    private String skuId;
}
