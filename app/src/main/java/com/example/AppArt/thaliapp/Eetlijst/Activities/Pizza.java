package com.example.AppArt.thaliapp.Eetlijst.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.AppArt.thaliapp.R;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Pizza extends ListActivity {

    private String[] lijst;
    private ArrayList<String> chosen = new ArrayList<>();
    private double bedrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            lijst = null;
        } else {
            lijst = extras.getStringArray("pizzalijst");
            String[] s = extras.getStringArray("chosen");
            if (s != null) {
                for (int i = 0; i < s.length; i++) {
                    chosen.add(s[i]);
                }
            }
            bedrag = extras.getDouble("bedrag", 0);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lijst);
        setListAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String[] p = new String[chosen.size()];
            for (int i = 0; i < chosen.size(); i++) {
                p[i] = chosen.get(i);
            }
            Intent i = new Intent(this, Eetlijst.class);
            i.putExtra("pizzas", p);
            i.putExtra("pbedrag", bedrag);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String s = l.getItemAtPosition(position).toString();
        Scanner scan = new Scanner(s);
        scan.useDelimiter(":");
        String pizza = scan.next();
        chosen.add(pizza);
        StringBuilder sb = new StringBuilder();
        sb.append("Je hebt gekozen voor een pizza ");
        sb.append(pizza);
        CharSequence cs = sb.toString();
        Toast t = Toast.makeText(this, cs, Toast.LENGTH_SHORT);
        t.show();
        String geld = s.substring(s.lastIndexOf(" : ") + 2, s.lastIndexOf(" : ") + 7);
        double d = Double.parseDouble(geld);
        bedrag += d;
    }
}
