package com.example.AppArt.thaliapp.Eetlijst.Backhand;

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

    private final ProductDummyDb Db = new ProductDummyDb();

    private List<Product> parsedFries = new ArrayList<>();
    private List<Product> parsedPizza = new ArrayList<>();
    private List<Product> parsedSandwich = new ArrayList<>();
    private List<Product> parsedSnacks = new ArrayList<>();

    /**
     * Parses a productdatabase to lists of products
     *
     * @return A list of the parsed products.
     */
    public void Parsing() {
        parsedFries.clear();
        parsedFries.addAll(ProductParsing(Db.friesList, ProductCategory.FRIES));
        parsedPizza.clear();
        parsedPizza.addAll(PizzaParsing(Db.pizzaList));
        parsedSandwich.clear();
        parsedSandwich.addAll(ProductParsing(Db.sandwichList, ProductCategory.SANDWICHES));
        parsedSnacks.clear();
        parsedSnacks.addAll(ProductParsing(Db.snackList, ProductCategory.SNACKS));
    }

    public List<Product> getParsedFries() {
        return parsedFries;
    }

    public List<Product> getParsedPizza() {
        return parsedPizza;
    }

    public List<Product> getParsedSandwich() {
        return parsedSandwich;
    }

    public List<Product> getParsedSnacks() {
        return parsedSnacks;
    }

    /**
     * Parses the Sandwich-, Fries- and SnackLists into Product objects
     *
     * @param SFSList
     * @param cat
     * @return
     */
    public List<Product> ProductParsing(String[] SFSList, ProductCategory cat) {
        // Variables that are about to be filled
        String name;
        Double price;
        List<String> ingredients = new ArrayList<>();

        // Scanning the stringarray and parsing it
        List<Product> newProductList = new ArrayList<>();
        Scanner scan;
        for (String SFSString : SFSList) {
            scan = new Scanner(SFSString);
            scan.useDelimiter("\\n");
            name = scan.next();
            scan.findWithinHorizon("€", 10);
            price = scan.nextDouble();

            Product newProduct = new Product(name, price, cat, ingredients);
            ingredients.clear();
            newProductList.add(newProduct);
        }
        return newProductList;
    }

    /**
     * Parses the pizzaString from the dummy database into Product objects
     *
     * @param pizzaList
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
            price = scan.nextDouble();

            Product newProduct = new Product(name, price, ProductCategory.PIZZA, ingredients);
            ingredients.clear();
            newProductList.add(newProduct);
        }
        return newProductList;
    }
}

