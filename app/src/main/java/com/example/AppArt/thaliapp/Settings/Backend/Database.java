package com.example.AppArt.thaliapp.Settings.Backend;

import com.example.AppArt.thaliapp.Calendar.Backend.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Backend.Product;
import com.example.AppArt.thaliapp.Eetlijst.Backend.ProductParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
     * It is possible that no events have been parsed yet, or that all events
     * that have been parsed, have ended. In these cases, a call to
     * updateEvents() is required.
     *
     * @return all already parsed events that haven't ended yet
     */
    public List<ThaliaEvent> getEvents() {
        Date nu = new Date();
        int i = 0;
        if(events == null){
            return null;
        }
        while (i < events.size()) {
            while (events.get(i).getGregCalFormat(events.get(i).getEndDate()).getTime().compareTo(nu) < 0) {
                events.remove(i);
            }
            i++;
        }
        Collections.sort(events);
        return events;
    }

    /**
     * Downloads a new list of ThaliaEvents
     */
    public void updateEvents() {
        events = new GetiCal();
    }

    /**
     * **************************************************************
     * Part handling Products
     * ***************************************************************
     */

    public List<Product> getProductsFries() {
        return productsFries;
    }

    public List<Product> getProductsPizza() {
        return productsPizza;
    }

    public List<Product> getProductsSandwich() {
        return productsSandwich;
    }

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
     *
     * @param receipt the to-be-added Receipt
     */
    public void addReceipt(String[] receipt) {
        receipts.add(receipt);
    }

    /**
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
