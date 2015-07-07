package com.example.AppArt.thaliapp.Eetlijst.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Database;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Bon extends ActionBarActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private String[] chosen;
    private Double bedrag;
    private String naam;
    private String[] all;
    SharedPreferences sharedpreferences;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_bon);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BonFragment())
                    .commit();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chosen = extras.getStringArray("chosen");
            bedrag = extras.getDouble("bedrag", 0.0);
            naam = extras.getString("naam", "Test");
        }
        if (chosen == null) {
            size = 2;
        } else {
            size = chosen.length + 2;
        }
        all = new String[size];
        all[0] = naam;
        for (int i = 1; i < size - 1; i++) {
            all[i] = chosen != null ? chosen[i - 1] : null;
        }
        all[size - 1] = Double.toString(bedrag);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    public String[] getAll() {
        return all;
    }

    public void send(View v) {
        Database.getDatabase().addReceipt(chosen);
        Intent intent = getIntent();
        chosen = null;
        intent.putExtra("chosen", chosen);
        intent.putExtra("bedrag", 0.0);
        intent.putExtra("naam", (String) null);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, Eetlijst.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class BonFragment extends ListFragment {
        private String[] all;

        public BonFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bon b = (Bon) getActivity();
            all = b.getAll();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    all);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
