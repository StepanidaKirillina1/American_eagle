package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryOptions {
    private boolean shippingAvailable;
    private Shipping shipping;
    private boolean pickupAvailable;
    private Pickup pickup;
}
