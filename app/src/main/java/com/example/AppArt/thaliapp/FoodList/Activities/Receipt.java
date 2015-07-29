package com.example.AppArt.thaliapp.FoodList.Activities;

import android.content.Intent;
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
import com.example.AppArt.thaliapp.Settings.Backend.Database;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Receipt extends ActionBarActivity {
    private String[] chosen;
    private Double amount;
    private String name;
    private String[] all;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BonFragment())
                    .commit();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chosen = extras.getStringArray("chosen");
            amount = extras.getDouble("amount", 0.0);
            name = extras.getString("name", " ");
        }
        if (chosen == null) {
            size = 2;
        } else {
            size = chosen.length + 2;
        }
        all = new String[size];
        all[0] = name;
        for (int i = 1; i < size - 1; i++) {
            all[i] = chosen != null ? chosen[i - 1] : null;
        }
        all[size - 1] = Double.toString(amount);
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
        intent.putExtra("chosen", (String[]) null);
        intent.putExtra("amount", 0.0);
        intent.putExtra("name", (String) null);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, Restaurant.class));
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
            Receipt b = (Receipt) getActivity();
            all = b.getAll();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    all);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
