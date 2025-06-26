package testData;

public enum ProductId {

    PRODUCT_ID_1("0482_8637_600", "0043600840"), PRODUCT_ID_2("0482_8636_900", "0043600824"),
    PRODUCT_ID_3("0482_8865_709", "0043603968"), PRODUCT_ID_4("0482_8790_013", "0043603950"),
    PRODUCT_ID_5("0482_8714_100", "0043600832"), PRODUCT_ID_6("0482_8419_200", "0043219245");

    private final String productId;
    private final String skuId;

    ProductId(String productId, String skuId) {
        this.productId = productId;
        this.skuId = skuId;
    }

    public String getProductId() {
        return productId;
    }

    public String getSkuId() {
        return skuId;
    }
}
