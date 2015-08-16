package com.example.AppArt.thaliapp.FoodList.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Backend.Product;
import com.example.AppArt.thaliapp.FoodList.Backend.ProductCategory;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
import com.example.AppArt.thaliapp.Settings.Backend.Database;
import com.example.AppArt.thaliapp.ThaliappActivity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The menu in which you can chose what food to order or watch the receipt
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Restaurant extends ThaliappActivity {
    private String name;
    private EditText editName;
    private ArrayList<String> chosen = new ArrayList<>();
    private double amount = 0;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstancesharedpreferences) {
        super.onCreate(savedInstancesharedpreferences);
        setContentView(R.layout.activity_restaurant);

        editName = (EditText) findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] food = extras.getStringArray("chosenfood");
            if (food != null) {
                Collections.addAll(chosen, food);
            }
            amount = extras.getDouble("chosenamount");
        }

        // Connect to the database and update if necessary
        this.database = Database.getDatabase();
        for (ProductCategory s : ProductCategory.values()) {
            if (database.getProduct(s) == null) {
                database.updateProducts();
                break;
            }
        }
    }

    /**
     * When there are no fries and you chose fries, it says that there are no more fries
     * If there are fries, shows how many you've chosen of what fries
     *
     * @param v, view of the activity
     */
    public void fries(View v) {
        Intent i = productIntent(ProductCategory.FRIES);
        if (i == null) {
            Toast.makeText(this, "Jammer :( \nEr zijn geen frieten.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(i);
    }

    /**
     * Same as fries(), but for pizza
     *
     * @param v, view of the activity
     */
    public void pizza(View v) {
        Intent i = productIntent(ProductCategory.PIZZA);
        if (i == null) {
            Toast.makeText(this, "Jammer :( \nEr zijn geen pizza's.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(i);
    }

    /**
     * Same as fries(), but for snacks
     *
     * @param v, view of the activity
     */
    public void snacks(View v) {
        Intent i = productIntent(ProductCategory.SNACKS);
        if (i == null) {
            Toast.makeText(this, "Jammer :( \nEr zijn geen snacks.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(i);
    }

    /**
     * Same as fries(), but for sandwiches
     *
     * @param v, the view of the activity
     */
    public void sandwiches(View v) {
        Intent i = productIntent(ProductCategory.SANDWICHES);
        if (i == null) {
            Toast.makeText(this, "Jammer :( \nEr zijn geen broodjes.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(i);
    }

    /**
     * Creates the intent that corresponds with the ProductCategory
     * @param cat, the product category
     * @return intent
     */
    public Intent productIntent(ProductCategory cat){
        Intent intent = new Intent(this, FoodList.class);
        if (database.getProduct(cat) == null) return null;
        intent.putExtra("chosen", chosenToString());
        intent.putExtra("foodlist", Product.toStringArray(database.getProduct(cat)));
        intent.putExtra("amount", amount);
        return intent;
    }

    /**
     * Saves the total amount together with your name and the food you've chosen
     *
     * @param v, view of the activity
     */
    public void amount(View v) {
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
