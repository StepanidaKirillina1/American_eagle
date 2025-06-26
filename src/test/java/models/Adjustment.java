package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adjustment {
    private double quantityAdjusted;
    private double adjustment;
    private String adjustmentDescription;
    private double totalAdjustment;
    private double returnValueAdjustmentAmount;
    private String promotionId;
}
