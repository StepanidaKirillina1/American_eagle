package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedPromotion {
    private String id;
    private String name;
    private String message;
    private double discount;
    private boolean qualified;
    private int type;
    private String discountType;
    private int channel;
    private String description;
}
