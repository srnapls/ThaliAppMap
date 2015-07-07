package com.example.AppArt.thaliapp.Settings;

import com.example.AppArt.thaliapp.Calendar.Backhand.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backhand.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Backhand.Product;
import com.example.AppArt.thaliapp.Eetlijst.Backhand.ProductParser;

import java.util.ArrayList;

/**
 * Class using Singleton pattern that stores all parsed data.
 * Makes parsed data accessible for every class in the project.
 * <p/>
 * Created by AppArt on 4-7-2015.
 */
public class Database {
    private static Database database = null;
    private final GetiCal getiCal;
    private final ProductParser productParser;

    private ArrayList events = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<String[]> receipts = new ArrayList<>();

    private ArrayList productsFries = new ArrayList<>();
    private ArrayList productsPizza = new ArrayList<>();
    private ArrayList productsSandwich = new ArrayList<>();
    private ArrayList productsSnacks = new ArrayList<>();


    private Database() {
        getiCal = new GetiCal();
        productParser = new ProductParser();
    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public void updateEvents() {
        events = (ArrayList) getiCal.getNewEvents();
    }

    public void updateProducts() {
        productParser.Parsing();
        productsFries = (ArrayList) productParser.getParsedFries();
        productsPizza = (ArrayList) productParser.getParsedPizza();
        productsSandwich = (ArrayList) productParser.getParsedSandwich();
        productsSnacks = (ArrayList) productParser.getParsedSnacks();

    }

    public void addReceipt(String[] receipt) {
        receipts.add(receipt);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<String[]> getReceipts() {
        return receipts;
    }

    public void emptyReceipts() {
        receipts.clear();
    }

    public ArrayList<ThaliaEvent> getEvents() {
        return events;
    }

    public ArrayList getProductsFries() {
        return productsFries;
    }

    public ArrayList getProductsPizza() {
        return productsPizza;
    }

    public ArrayList getProductsSandwich() {
        return productsSandwich;
    }

    public ArrayList getProductsSnacks() {
        return productsSnacks;
    }
}
