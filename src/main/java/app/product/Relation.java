package app.product;


public class Relation {

    private int weightTypeId;
    private int productTypeId;
    private int brandId;

    public Relation(int weightTypeId, int productTypeId, int brandId) {
        this.weightTypeId = weightTypeId;
        this.productTypeId = productTypeId;
        this.brandId = brandId;
    }

    public int getWeightTypeId() {
        return weightTypeId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public int getBrandId() {
        return brandId;
    }
}
