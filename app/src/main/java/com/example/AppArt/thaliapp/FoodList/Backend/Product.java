package com.example.AppArt.thaliapp.FoodList.Backend;


import java.util.ArrayList;
import java.util.List;

/**
 * Contains all information about a product. Has a name, price, a list of
 * ingredients and a category
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Product {

    private final String name;
    private final Double price;
    private final List<String> ingredients;
    private final ProductCategory category;

    /**
     * Initialises the product object
     *
     * @param name        The name of the product
     * @param price       The price of the product
     * @param category    The category of the product
     * @param ingredients A list of Strings with each string being an ingredient
     *                    of the product
     */
    public Product(String name, double price, ProductCategory category, List<String> ingredients) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.ingredients = new ArrayList<>(ingredients);
    }

    /**
     * Makes a string of all of the ingredients
     *
     * @return all ingredients
     */
    public String ingredientsPrinter() {
        StringBuilder ingredientsPrinted = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsPrinted.append(ingredients.get(i));
            if (i != ingredients.size() - 1) {
                ingredientsPrinted.append(", ");
            }
        }
        return ingredientsPrinted.toString();
    }

    /**
     * A toString specification
     *
     * @return 'name': 'price' \n'category'\nIngredients: 'ingredient',
     * 'ingredient', ...
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(": ");
        sb.append(price);
        sb.append(" EUR");
        if (category == ProductCategory.PIZZA) {
            sb.append("\nIngredients:");
            sb.append(ingredientsPrinter());
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }


}
