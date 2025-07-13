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
public class OrderSummary {
    private String id;
    private double shipping;
    private double shippingTax;
    private double subtotal;
    private double discount;
    private double donation;
    private double subtotalMinusDiscount;
    private double tax;
    private double shippingItemsCost;
    private double pickupItemsCost;
    private double total;
    private double amountUntilFreeShipping;
    private double freeShippingThreshold;
    private double giftCardTotal;
    private double giftCardStandardTotal;
    private double giftCardInstantCreditTotal;
    private double roadDeliveryFee;
    private List<Object> appliedCoupons;
    private List<AppliedPromotion> appliedPromotions;
    private OrderSummarySavings orderSummarySavings;
    private double creditSavingsAmount;
    private double netTotal;
}
