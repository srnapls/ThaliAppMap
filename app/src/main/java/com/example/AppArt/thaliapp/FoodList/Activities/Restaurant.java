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

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Restaurant extends ActionBarActivity {
    private String name;
    private EditText editName;
    private ArrayList<String> chosen = new ArrayList<>();
    private double amount = 0;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    public void pizza(View v) {
        Intent intentpizza = new Intent(this, FoodList.class);
        intentpizza.putExtra("foodlist", (String[]) Database.getDatabase().getProductsPizza().toArray());
        intentpizza.putExtra("chosen", chosenToString());
        intentpizza.putExtra("amount", amount);
        startActivity(intentpizza);
    }

    public void friet(View v) {
        Intent intentfriet = new Intent(this, FoodList.class);
        intentfriet.putExtra("chosen", chosenToString());
        intentfriet.putExtra("foodlist", (String[]) Database.getDatabase().getProductsFries().toArray());
        intentfriet.putExtra("amount", amount);
        startActivity(intentfriet);
    }

    public void broodjes(View v) {
        Intent intentbr = new Intent(this, FoodList.class);
        intentbr.putExtra("chosen", chosenToString());
        intentbr.putExtra("foodlist", (String[]) Database.getDatabase().getProductsSandwich().toArray());
        intentbr.putExtra("amount", amount);
        startActivity(intentbr);
    }

    public void snacks(View v) {
        Intent intentsnacks = new Intent(this, FoodList.class);
        intentsnacks.putExtra("foodlist", (String[]) Database.getDatabase().getProductsSnacks().toArray());
        intentsnacks.putExtra("chosen", chosenToString());
        intentsnacks.putExtra("amount", amount);
        startActivity(intentsnacks);
    }

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
