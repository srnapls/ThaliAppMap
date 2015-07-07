package com.example.AppArt.thaliapp.Settings.Backend;

import com.example.AppArt.thaliapp.Calendar.Backend.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Backend.Product;
import com.example.AppArt.thaliapp.Eetlijst.Backend.ProductParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Class using Singleton pattern that stores all parsed data.
 * Makes parsed data accessible for every class in the project.
 * Data in three categories: Events, Products and Receipts
 *
 * Created by AppArt on 4-7-2015.
 */
public class Database {
    private static Database database = null;

    private final GetiCal getiCal;
    private final ProductParser productParser;

    private List<ThaliaEvent> events;

    private List<Product> productsFries;
    private List<Product> productsPizza;
    private List<Product> productsSandwich;
    private List<Product> productsSnacks;

    private List<String[]> receipts = new ArrayList<>();

    /**
     * Creates Parsers
     */
    private Database() {
        getiCal = new GetiCal();
        productParser = new ProductParser();
    }

    /**
     * Singleton pattern: returns current database, if none existent,
     * it creates one.
     *
     * @return The one and only Database of the ThaliApp
     */
    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    /*****************************************************************
     Part handling Events
     *****************************************************************/

    /**
     * @return Last updated ThaliaEvents of Thalia.nu
     */
    public List<ThaliaEvent> getEvents() {
        return events;
    }

    /**
     * Downloads a new list of ThaliaEvents
     */
    public void updateEvents() {
        events = getiCal.getNewEvents();
    }

    /*****************************************************************
     Part handling Products
     *****************************************************************/

    /**
     *
     * @return a List of all Fries Product objects in the DummyDb
     */
    public List<Product> getProductsFries() {
        return productsFries;
    }

    /**
     *
     * @return a List of all Pizza Product objects in the DummyDb
     */
    public List<Product> getProductsPizza() {
        return productsPizza;
    }

    /**
     *
     * @return a List of all Sandwich Product objects in the DummyDb
     */
    public List<Product> getProductsSandwich() {
        return productsSandwich;
    }

    /**
     *
     * @return a List of all Snacks Product objects in the DummyDb
     */
    public List<Product> getProductsSnacks() {
        return productsSnacks;
    }

    /**
     * Updates the lists of Products of all categories using the DummyDb
     */
    public void updateProducts() {
        productsFries = productParser.getParsedFries();
        productsPizza = productParser.getParsedPizza();
        productsSandwich = productParser.getParsedSandwich();
        productsSnacks = productParser.getParsedSnacks();

    }

    /*****************************************************************
     Part handling Receipts
     *****************************************************************/

    /**
     * Add a Receipt to the database
     * @param receipt the to-be-added Receipt
     */
    public void addReceipt(String[] receipt) {
        receipts.add(receipt);
    }

    /**
     *
     * @return all currently stored Receipts
     */
    public List<String[]> getReceipts() {
        return receipts;
    }

    /**
     * Clears all Receipts
     */
    public void emptyReceipts() {
        receipts.clear();
    }
}
