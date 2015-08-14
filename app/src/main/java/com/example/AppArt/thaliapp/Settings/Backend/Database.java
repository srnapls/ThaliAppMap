package com.example.AppArt.thaliapp.Settings.Backend;

import com.example.AppArt.thaliapp.Calendar.Backend.EventParser;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.FoodList.Backend.Product;
import com.example.AppArt.thaliapp.FoodList.Backend.ProductCategory;
import com.example.AppArt.thaliapp.FoodList.Backend.ProductParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Class using Singleton pattern that stores all parsed data.
 * Makes parsed data accessible for every class in the project.
 * Data in three categories: Events, Products and Receipts
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
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
        GregorianCalendar nu = (GregorianCalendar) GregorianCalendar.getInstance();
        if(events == null){
            return null;
        }
        for(int i = 0; i< events.size(); i++){
            // Remove all events with an endDate in the past
            while (events.get(i).getEndDate().compareTo(nu) < 0) {
                events.remove(i);
            }
        }
        Collections.sort(events);
        return events;
    }

    /**
     * Downloads a new list of ThaliaEvents using an AsyncTask named EventParser
     */
    public void updateEvents() {
        // icalAddress
        EventParser eventParser = new EventParser();
        eventParser.execute(icalAddress);
        try{
            Thread.sleep(4000);
            events = eventParser.getNewEvents();
        } catch(InterruptedException ex){
            ex.printStackTrace();
        }
        events = eventParser.getNewEvents();
        System.out.println("updateEvents end");
    }

    /**
     * **************************************************************
     * Part handling Products
     * ***************************************************************
     */

    public List<Product> getProduct (ProductCategory cat){
        switch(cat){
            case PIZZA: return productsPizza;
            case FRIES: return productsFries;
            case SANDWICHES: return productsSandwich;
            case SNACKS: return productsSnacks;
            default: return null;
        }
    }
    /**
     * Updates the lists of Products of all categories using the DummyDb
     */
    public void updateProducts() {
        productsFries = productParser.getParsed(ProductCategory.FRIES);
        productsPizza = productParser.getParsed(ProductCategory.PIZZA);
        productsSandwich = productParser.getParsed(ProductCategory.SANDWICHES);
        productsSnacks = productParser.getParsed(ProductCategory.SNACKS);
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
     * Getter for receipts
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
