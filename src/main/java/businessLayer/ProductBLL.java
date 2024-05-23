package businessLayer;

import model.Product;

/**
 * The ProductBLL class provides business logic for operations on Product objects.
 * It includes a method for validating the fields of a Product object.
 */
public class ProductBLL{

    /**
     * Validates the fields of a Product object.
     * It checks if the name and description have an appropriate length.
     * If a field is not valid, it throws an IllegalArgumentException.
     *
     * @param product the Product object to be validated
     * @throws IllegalArgumentException if a field is not valid
     */
    public void validate(Product product) {
        if (product.getName().length() > 44) {
            throw new IllegalArgumentException("Product name is too long!");
        }

        if (product.getDescription().length() > 44) {
            throw new IllegalArgumentException("Product description is too long!");
        }
    }
}