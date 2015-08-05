package com.example.AppArt.thaliapp.FoodList.Backend;

/**
 * Defines all possible categories that a product can have.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public enum ProductCategory {

    FRIES, PIZZA, SANDWICHES, SNACKS;

    /**
     * @return The productcategory starting with a capital letter and further
     * only containing small letters. e.g. PIZZA -> Pizza
     */
    @Override
    public String toString() {
        return this.name().substring(0, 1)
                + this.name().substring(1).toLowerCase();
    }
}
