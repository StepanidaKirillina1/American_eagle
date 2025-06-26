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
public class PriceDetail {
    public boolean discounted;
    public double amount;
    public int quantity;
    public double returnValue;
    public double orderDiscountShare;
    public double highBound;
    public double lowBound;
    public List<Adjustment> adjustments;
}
