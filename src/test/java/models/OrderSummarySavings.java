package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummarySavings {
    private double listPriceTotal;
    private double saleMarkdown;
    private double discounts;
    private double rawShipping;
    private double subTotalBeforeDiscount;
    private double savings;
}
