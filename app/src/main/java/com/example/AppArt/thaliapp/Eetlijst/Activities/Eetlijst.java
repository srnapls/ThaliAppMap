package com.example.AppArt.thaliapp.Eetlijst.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.AppArt.thaliapp.Eetlijst.Backhand.Product;
import com.example.AppArt.thaliapp.Eetlijst.Backhand.ProductParser;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Eetlijst extends ActionBarActivity {
    public static final String MyPREFERENCES = "Eten";
    private String naam;
    private EditText name;
    private ArrayList<String> friet = new ArrayList<>();
    private ArrayList<String> pizza = new ArrayList<>();
    private ArrayList<String> snacks = new ArrayList<>();
    private ArrayList<String> broodjes = new ArrayList<>();
    private ArrayList<String> chosen = new ArrayList<>();
    private String test;
    SharedPreferences sharedpreferences;

    private String[] f;
    private String[] p;
    private String[] s;
    private String[] b;
    private double totaalbedrag = 0;

    protected void addFries(String f) {
        friet.add(f);
    }

    protected void addPizza(String p) {
        pizza.add(p);
    }

    protected void addSnack(String s) {
        snacks.add(s);
    }

    public void addOverig(String o) {
        broodjes.add(o);
    }

    private void saveList() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(f + "_size", s.length);
        for (int i = 0; i < f.length; i++)
            editor.putString(f + "_" + i, f[i]);

        editor.putInt(p + "_size", p.length);
        for (int i = 0; i < p.length; i++)
            editor.putString(p + "_" + i, p[i]);

        editor.putInt(s + "_size", s.length);
        for (int i = 0; i < s.length; i++)
            editor.putString(s + "_" + i, s[i]);

        editor.putInt(b + "_size", b.length);
        for (int i = 0; i < b.length; i++)
            editor.putString(b + "_" + i, b[i]);
    }

    @Override
    protected void onCreate(Bundle savedInstancesharedpreferences) {
        super.onCreate(savedInstancesharedpreferences);
        setContentView(R.layout.activity_eetlijst);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));

        name = (EditText) findViewById(R.id.editText);

        f = new String[sharedpreferences.getInt("f_size", 10)];
        for (int i = 0; i < f.length; i++) {
            f[i] = sharedpreferences.getString("f_" + i, "");
        }
        p = new String[sharedpreferences.getInt("p_size", 10)];
        for (int i = 0; i < p.length; i++) {
            p[i] = sharedpreferences.getString("p_" + i, " ");
        }
        s = new String[sharedpreferences.getInt("s_size", 10)];
        for (int i = 0; i < s.length; i++) {
            s[i] = sharedpreferences.getString("s_" + i, " ");
        }
        b = new String[sharedpreferences.getInt("b_size", 10)];
        for (int i = 0; i < b.length; i++) {
            b[i] = sharedpreferences.getString("b_" + i, " ");
        }
        if (f.length == 10 && b.length == 10 && s.length == 10 && p.length == 10) {
            setLists();
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String[] pizzas = extras.getStringArray("pizzas");
            if (pizzas != null) {
                for (int i = 0; i < pizzas.length; i++) {
                    chosen.add(pizzas[i]);
                }
            }
            totaalbedrag += extras.getDouble("pbedrag");
            String[] snack = extras.getStringArray("snacks");
            if (snack != null) {
                for (int i = 0; i < snack.length; i++) {
                    chosen.add(snack[i]);
                }
            }
            totaalbedrag += extras.getDouble("sbedrag");
            String[] broodje = extras.getStringArray("broodjes");
            if (broodje != null) {
                for (int i = 0; i < broodje.length; i++) {
                    chosen.add(broodje[i]);
                }
            }
            totaalbedrag += extras.getDouble("bbedrag");
            String[] friets = extras.getStringArray("frieten");
            if (friets != null) {
                for (int i = 0; i < friets.length; i++) {
                    chosen.add(friets[i]);
                }
            }
            totaalbedrag += extras.getDouble("fbedrag");
        }
    }

    private void setLists() {
        ProductParser parser = new ProductParser();
        parser.Parsing();
        List<Product> frietlijst = parser.FriesParsing();
        List<Product> pizzalijst = parser.PizzaParsing();
        List<Product> snacklijst = parser.SnackParsing();
        List<Product> broodjeslijst = parser.SandwichParsing();
        f = new String[frietlijst.size()];
        for (int i = 0; i < frietlijst.size(); i++) f[i] = frietlijst.get(i).toString();
        p = new String[pizzalijst.size()];
        for (int i = 0; i < pizzalijst.size(); i++) p[i] = pizzalijst.get(i).toString();
        s = new String[snacklijst.size()];
        for (int i = 0; i < snacklijst.size(); i++) s[i] = snacklijst.get(i).toString();
        b = new String[broodjeslijst.size()];
        for (int i = 0; i < broodjeslijst.size(); i++) b[i] = broodjeslijst.get(i).toString();
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
                Intent intent2 = new Intent(this, Eetlijst.class);
                startActivity(intent2);
                break;
            case R.id.menu4:
                Intent intent4 = new Intent(this, Settings.class);
                startActivity(intent4);
                break;
        }
        return true;
    }
    public void pizza(View v) {
        saveList();
        String[] ch = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            ch[i] = chosen.get(i);
        }
        Intent intentpizza = new Intent(this, Pizza.class);
        intentpizza.putExtra("pizzalijst", p);
        intentpizza.putExtra("chosen", ch);
        intentpizza.putExtra("bedrag",totaalbedrag);
        startActivity(intentpizza);
    }

    public void friet(View v) {
        saveList();
        String[] ch = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            ch[i] = chosen.get(i);
        }
        Intent intentfriet = new Intent(this, Fries.class);
        intentfriet.putExtra("chosen", ch);
        intentfriet.putExtra("frietlijst", f);
        intentfriet.putExtra("bedrag",totaalbedrag);
        startActivity(intentfriet);
    }

    public void broodjes(View v) {
        saveList();
        String[] ch = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            ch[i] = chosen.get(i);
        }
        Intent intentbr = new Intent(this, Broodjes.class);
        intentbr.putExtra("chosen", ch);
        intentbr.putExtra("broodjeslijst", b);
        intentbr.putExtra("bedrag",totaalbedrag);
        startActivity(intentbr);
    }

    public void snacks(View v) {
        saveList();
        String[] ch = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            ch[i] = chosen.get(i);
        }
        Intent intentsnacks = new Intent(this, Snack.class);
        intentsnacks.putExtra("snacklijst", s);
        intentsnacks.putExtra("chosen", ch);
        intentsnacks.putExtra("bedrag",totaalbedrag);
        startActivity(intentsnacks);
    }

    public void bedrag(View v) {
        saveList();
        String[] ch = new String[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            ch[i] = chosen.get(i);
        }
        naam = name.getText().toString();
        Intent intent = new Intent(this, Bon.class);
        intent.putExtra("chosen", ch);
        intent.putExtra("naam", naam);
        intent.putExtra("bedrag",totaalbedrag);
        startActivity(intent);
    }
}
