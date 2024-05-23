package businessLayer;

import model.Orders;
/**
 * The OrderBLL class provides business logic for operations on Orders objects.
 * It includes a method for validating the fields of an Orders object.
 */
public class OrderBLL {

    /**
     * Validates the fields of an Orders object.
     * It checks if the quantity is an integer.
     * If a field is not valid, it throws an IllegalArgumentException.
     *
     * @param orders the Orders object to be validated
     * @throws IllegalArgumentException if a field is not valid
     */
    public void validate(Orders orders) {
        if (orders.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity! The quantity should be a positive integer.");
        }
    }
}