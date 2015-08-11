package com.example.AppArt.thaliapp.FoodList.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Backend.Product;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The menu in which you can chose what food to order or watch the receipt
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Restaurant extends ActionBarActivity {
    private String name;
    private EditText editName;
    private ArrayList<String> chosen = new ArrayList<>();
    private double amount = 0;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstancesharedpreferences) {
        super.onCreate(savedInstancesharedpreferences);
        setContentView(R.layout.activity_restaurant);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));

        editName = (EditText) findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] food = extras.getStringArray("chosenfood");
            if (food != null) {
                Collections.addAll(chosen, food);
            }
            amount += extras.getDouble("chosenamount");
        }

        // Connect to the database and update if necessary
        this.database = Database.getDatabase();
        if(database.getProductsFries() == null || database.getProductsPizza() == null
            || database.getProductsSandwiches() == null || database.getProductsSnacks() == null){
            database.updateProducts();
        }
    }

    /**
     * Makes the menu by inflating
     *
     * @param menu
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Decides what to do based on what is clicked
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent1 = new Intent(this, Calendar.class);
                startActivity(intent1);
                break;
            case R.id.menu2:
                Intent intent2 = new Intent(this, Restaurant.class);
                startActivity(intent2);
                break;
            case R.id.menu4:
                Intent intent3 = new Intent(this, Settings.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    /**
     * When there are no fries and you chose fries, it says that there are no more fries
     * If there are fries, shows how many you've chosen of what fries
     *
     * @param v
     */
    public void fries(View v) {
        Intent intentfriet = new Intent(this, FoodList.class);
        if(database.getProductsFries() == null){
            Toast.makeText(this, "Jammer :( \nEr zijn geen frietjes.", Toast.LENGTH_LONG).show();
            return;
        }
        intentfriet.putExtra("chosen", chosenToString());
        intentfriet.putExtra("foodlist", Product.toStringArray(database.getProductsFries()));
        intentfriet.putExtra("amount", amount);
        startActivity(intentfriet);
    }

    /**
     * Same as fries(), but for pizza
     *
     * @param v
     */
    public void pizza(View v) {
        Intent intentpizza = new Intent(this, FoodList.class);
        if(database.getProductsPizza() == null){
            Toast.makeText(this, "Jammer :( \nEr zijn geen pizza's.", Toast.LENGTH_LONG).show();
            return;
        }
        intentpizza.putExtra("foodlist", Product.toStringArray(database.getProductsPizza()));
        intentpizza.putExtra("chosen", chosenToString());
        intentpizza.putExtra("amount", amount);
        startActivity(intentpizza);
    }

    /**
     * Same as fries(), but for snacks
     *
     * @param v
     */
    public void snacks(View v) {
        Intent intentsnacks = new Intent(this, FoodList.class);
        if(database.getProductsSnacks() == null){
            Toast.makeText(this, "Jammer :( \nEr zijn geen snacks.", Toast.LENGTH_LONG).show();
            return;
        }
        intentsnacks.putExtra("foodlist", Product.toStringArray(database.getProductsSnacks()));
        intentsnacks.putExtra("chosen", chosenToString());
        intentsnacks.putExtra("amount", amount);
        startActivity(intentsnacks);
    }

    /**
     * Same as fries(), but for sandwiches
     *
     * @param v
     */
    public void sandwiches(View v) {
        Intent intentbr = new Intent(this, FoodList.class);
        if(database.getProductsSandwiches() == null){
            Toast.makeText(this, "Jammer :( \nEr zijn geen broodjes.", Toast.LENGTH_LONG).show();
            return;
        }
        intentbr.putExtra("chosen", chosenToString());
        intentbr.putExtra("foodlist", Product.toStringArray(database.getProductsSandwiches()));
        intentbr.putExtra("amount", amount);
        startActivity(intentbr);
    }

    /**
     * Saves the total amount together with your name and the food you've chosen
     *
     * @param v
     */
    public void bedrag(View v) {
        name = editName.getText().toString();
        Intent intent = new Intent(this, Receipt.class);
        intent.putExtra("chosen", chosenToString());
        intent.putExtra("name", name);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }

    /**
     * A stringbuilder to make a stringarray of all the chosen food
     *
     * @return stringarray of food thats chosen
     */
    private String[] chosenToString() {
        String[] chosenstring = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            chosenstring[i] = chosen.get(i);
        }
        return chosenstring;
    }
}
