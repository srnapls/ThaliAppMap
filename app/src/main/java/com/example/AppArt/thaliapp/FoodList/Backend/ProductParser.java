package com.example.AppArt.thaliapp.FoodList.Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Generates lists of products per category. This needs to be reworked once a
 * real database is provided.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */
public class ProductParser {

    private final ProductDummyDb productDummyDb = new ProductDummyDb();

    private List<List<Product>> parsedProducts = new ArrayList<>();

    private List<Product> parsedFries = new ArrayList<>();
    private List<Product> parsedPizza = new ArrayList<>();
    private List<Product> parsedSandwich = new ArrayList<>();
    private List<Product> parsedSnacks = new ArrayList<>();

    /**************************************************************************
     * Getters
     *************************************************************************/

    /**
     * Get a list with all products based on category.
     * e.g. want a list with all the products of the FRIES category?
     * parsedProducts.get(ProductCategory.FRIES.ordinal());
     *
     * @return List with for every ProductCategory a list of Products
     */
    public List<List<Product>> getParsedProducts(){
        List<Product> parsedFries = new ArrayList<>();
        parsedFries.clear();
        parsedFries.addAll(ProductParsing(productDummyDb.friesList, ProductCategory.FRIES));
        parsedProducts.set(ProductCategory.FRIES.ordinal(), parsedFries);

        List<Product> parsedPizza = new ArrayList<>();
        parsedPizza.clear();
        parsedPizza.addAll(PizzaParsing(productDummyDb.pizzaList));
        parsedProducts.set(ProductCategory.PIZZA.ordinal(), parsedPizza);

        List<Product> parsedSandwiches = new ArrayList<>();
        parsedSandwiches.clear();
        parsedSandwiches.addAll(ProductParsing(productDummyDb.sandwichList, ProductCategory.SANDWICHES));
        parsedProducts.set(ProductCategory.SANDWICHES.ordinal(), parsedSandwiches);

        List<Product> parsedSnacks = new ArrayList<>();
        parsedSnacks.clear();
        parsedSnacks.addAll(ProductParsing(productDummyDb.snackList, ProductCategory.SNACKS));
        parsedProducts.set(ProductCategory.SNACKS.ordinal(), parsedSnacks);

        return parsedProducts;
    }


    /**
     * Parses the dummyDb input on Pizza
     * @return List of all Pizza Product objects in the dummyDb
     */
    public List<Product> getParsedFries() {
        parsedFries.clear();
        parsedFries.addAll(ProductParsing(productDummyDb.friesList, ProductCategory.FRIES));
        return parsedFries;
    }

    /**
     * Parses the dummyDb input on Pizza
     * @return List of all Pizza Product objects in the dummyDb
     */
    public List<Product> getParsedPizza() {
        parsedPizza.clear();
        parsedPizza.addAll(PizzaParsing(productDummyDb.pizzaList));
        return parsedPizza;
    }

    /**
     * Parses the dummyDb input on Sandwich
     * @return List of all Sandwich Product objects in the dummyDb
     */
    public List<Product> getParsedSandwich() {
        parsedSandwich.clear();
        parsedSandwich.addAll(ProductParsing(productDummyDb.sandwichList, ProductCategory.SANDWICHES));
        return parsedSandwich;
    }

    /**
     * Parses the dummyDb input on Snacks
     * @return List of all Snacks Product objects in the dummyDb
     */
    public List<Product> getParsedSnacks() {
        parsedSnacks.clear();
        parsedSnacks.addAll(ProductParsing(productDummyDb.snackList, ProductCategory.SNACKS));
        return parsedSnacks;
    }

    /**************************************************************************
     * The Parsing
     *************************************************************************/

    /**
     * Parses the Sandwich-, Fries- and SnackLists into Product objects
     *
     * @param FSSList Descriptions of fries, sandwiches or snacks
     * @param cat Category of the given list
     * @return List of Product objects containing all information of FSSList
     */
    public List<Product> ProductParsing(String[] FSSList, ProductCategory cat) {
        // Variables that are about to be filled
        String name;
        Double price;
        List<String> ingredients = new ArrayList<>();

        // Scanning the stringarray and parsing it
        List<Product> newProductList = new ArrayList<>();
        Scanner scan;
        for (String SFSString : FSSList) {
            scan = new Scanner(SFSString);
            scan.useDelimiter("\\n");
            name = scan.next();
            scan.findWithinHorizon("€", 10);
            price = Double.parseDouble(scan.next());

            Product newProduct = new Product(name, price, cat, ingredients);
            ingredients.clear();
            newProductList.add(newProduct);
        }
        return newProductList;
    }

    /**
     * Parses the pizzaString from the dummy database into Product objects
     *
     * @param pizzaList Descriptions of the pizzas
     * @return A list with the new pizzaproducts
     */
    public List<Product> PizzaParsing(String[] pizzaList) {
        // Variables that are about to be filled
        String name;
        Double price;
        List<String> ingredients = new ArrayList<>();

        // Scanning the stringarray and parsing it
        List<Product> newProductList = new ArrayList<>();
        Scanner scan;
        for (String pizzaString : pizzaList) {
            scan = new Scanner(pizzaString);
            scan.useDelimiter("%|, |\\n");
            name = scan.next();
            String ingredient;
            while (scan.hasNext()) {
                ingredient = scan.next();
                ingredients.add(ingredient);
            }
            String priceString = ingredients.get(ingredients.size() - 1);
            ingredients.remove(ingredients.size() - 1);
            scan.close();
            scan = new Scanner(priceString);
            scan.findWithinHorizon("€", 10);
            price = Double.parseDouble(scan.next());

            Product newProduct = new Product(name, price, ProductCategory.PIZZA, ingredients);
            ingredients.clear();
            newProductList.add(newProduct);
        }
        return newProductList;
    }
}

