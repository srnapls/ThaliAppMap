package com.example.AppArt.thaliapp.FoodList.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.AppArt.thaliapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Reads and shows the foodlist
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class FoodList extends ListActivity {

    private String[] list;
    private ArrayList<String> chosen = new ArrayList<>();
    private double amount=0;

    /**
     * Reads te foodlist and shows it
     *
     * @param savedInstanceState, the saved instances given to the class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            list = extras.getStringArray("foodlist");
            String[] temp = extras.getStringArray("chosen");
            if (temp != null) {
                Collections.addAll(chosen, temp);
            }
            amount = extras.getDouble("amount", 0.0);
        }
        assert list != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
    }

    /**
     * If you go back, go to restaurant
     *
     * @param keyCode, the key code
     * @param event, the event of the key being pressed
     * @return whether this succeeded
     */
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String[] all = new String[chosen.size()];
            for (int i = 0; i < chosen.size(); i++) {
                all[i] = chosen.get(i);
            }
            Intent i = new Intent(this, Restaurant.class);
            i.putExtra("chosenfood", all);
            i.putExtra("chosenamount", amount);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Displays what food you chose by clicking on an item
     *
     * @param l listview of the foood
     * @param v the view of the list
     * @param position what item you chose
     * @param id , the id of the item that was clicked
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String s = l.getItemAtPosition(position).toString();
        Scanner scan = new Scanner(s);
        scan.useDelimiter(":");
        String food = scan.next();
        chosen.add(food);
        CharSequence cs = "Je hebt gekozen voor " + food;
        Toast t = Toast.makeText(this, cs, Toast.LENGTH_SHORT);
        t.show();
        String geld = s.substring(s.lastIndexOf("EUR") - 5, s.lastIndexOf("EUR") - 1);
        double d = Double.parseDouble(geld);
        amount += d;
    }
}
