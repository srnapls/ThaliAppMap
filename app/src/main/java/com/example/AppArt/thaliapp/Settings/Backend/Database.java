package com.example.AppArt.thaliapp.Settings.Backend;

import com.example.AppArt.thaliapp.Calendar.Backend.EventParser;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.FoodList.Backend.Product;
import com.example.AppArt.thaliapp.FoodList.Backend.ProductParser;

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

    private final String icalAddress
            = "https://www.thalia.nu/events/ical/feed.ics";
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

    // TODO Frank: Kill the filthy Thread.sleep(4000);
    /**
     * Downloads a new list of ThaliaEvents using an AsyncTask named EventParser
     */
    public void updateEvents() {
        // icalAddress
        System.out.println("updateEvents begin");
        EventParser eventParser = new EventParser();
        eventParser.execute(icalAddress);
        try{
            Thread.sleep(4000);
            events = eventParser.getNewEvents();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("updateEvents end");
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
