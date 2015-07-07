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
    private ArrayList<ThaliaEvent> events = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<String[]> receipts = new ArrayList<>();


    private Database() {
        // Hier de dummy db's parsen of doen we dat via update functies?;
    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public void updateEvents() {
        GetiCal getiCal = new GetiCal();
        events = (ArrayList<ThaliaEvent>) getiCal.getNewEvents();
    }

    public void updateProducts() {
        ProductParser productParser = new ProductParser();
//TODO
    }

    public void addReceipt(String[] receipt) {
        receipts.add(receipt);
    }

    public ArrayList<ThaliaEvent> getEvents() {
        return events;
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
}
