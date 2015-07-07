package com.example.AppArt.thaliapp.Settings;

import com.example.AppArt.thaliapp.Calendar.Backhand.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backhand.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Backhand.Product;
import com.example.AppArt.thaliapp.Eetlijst.Backhand.ProductParser;

import java.util.ArrayList;

/**
 * Class using Singleton pattern that stores all parsed data.
 * Makes parsed data accessible for every class in the project.
 *
 * Created by AppArt on 4-7-2015.
 */
public class Database {
    private static Database database = null;

    private ArrayList<ThaliaEvent> events = new ArrayList<>();

    private ArrayList<Product> productsFries = new ArrayList<>();
    private ArrayList<Product> productsPizza = new ArrayList<>();
    private ArrayList<Product> productsSandwich = new ArrayList<>();
    private ArrayList<Product> productsSnacks = new ArrayList<>();


    private Database(){
        // Hier de dummy db's parsen of doen we dat via update functies?;
    }

    public static Database getDatabase(){
        if(database == null){
            database = new Database();
        }
        return database;
    }

    public void updateEvents(){
        GetiCal getiCal = new GetiCal();
        events = (ArrayList<ThaliaEvent>) getiCal.getNewEvents();
    }

    public void updateProducts(){

    }

    public ArrayList<ThaliaEvent> getEvents(){return events;}
    public ArrayList<Product> getProducts(){return products;}
}
