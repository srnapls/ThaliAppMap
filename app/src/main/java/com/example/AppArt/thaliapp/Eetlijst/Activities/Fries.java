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

public class Fries extends ListActivity {

    private String[] lijst;
    private ArrayList<String> chosen = new ArrayList<>();
    private double bedrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fries);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            lijst = null;
        } else {
            lijst = extras.getStringArray("frietlijst");
            String[] s = extras.getStringArray("chosen");
            if (s != null) {
                for (int i = 0; i < s.length; i++) {
                    chosen.add(s[i]);
                }
            }
            bedrag = extras.getDouble("bedrag",0);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lijst);
        setListAdapter(adapter);

        chosen = new ArrayList<String>();
        bedrag = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String[] p = new String[chosen.size()];
            for (int i = 0; i < chosen.size(); i++) {
                p[i] = chosen.get(i);
            }
            Intent i = new Intent(this, Eetlijst.class);
            i.putExtra("frieten", p);
            i.putExtra("fbedrag", bedrag);
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
        String friet = scan.next();
        chosen.add(friet);
        StringBuilder sb = new StringBuilder();
        sb.append("Je hebt gekozen voor ");
        sb.append(friet);
        CharSequence cs = sb.toString();
        Toast t = Toast.makeText(this, cs, Toast.LENGTH_SHORT);
        t.show();
        String geld = s.substring(s.lastIndexOf("EUR") - 5, s.lastIndexOf("EUR") - 1);
        double d = Double.parseDouble(geld);
        bedrag += d;
    }
}
