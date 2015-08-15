package com.example.AppArt.thaliapp.Settings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Class for the settings options
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Settings extends ActionBarActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Settings";

    /**
     * @param savedInstanceState, saved instanced
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_instellingen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ListFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * Getter for shared preference
     *
     * @return the preferences
     */
    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu, the menu that needs to be created
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item, that was clicked
     * @return if action succeeded
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Calendar:
                startActivity(new Intent(this, Calendar.class));
                break;
            case R.id.Restaurant:
                startActivity(new Intent(this, Restaurant.class));
                break;
        }
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ListFragment extends android.support.v4.app.ListFragment {
        String[] lijst = new String[]{"Notificaties", "Login", "Update kalender"};
        SharedPreferences sharedpreferences;

        public ListFragment() {
        }

        /**
         * Decides what to do depending on what item had been clicked on
         *
         * @param l        listview of the items
         * @param v        view of the fragment
         * @param position of the item
         * @param id       of the item
         */
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            String s = l.getItemAtPosition(position).toString();
            switch (s) {
                case "Notificaties":
                    Intent intent;
                    intent = new Intent(getActivity(), Notifications.class);
                    startActivity(intent);
                    break;
                case "Login":
                    boolean b = sharedpreferences.getBoolean("access", false);
                    if (b) {
                        Intent i = new Intent(getActivity(), Overview.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), Login.class);
                        startActivity(i);
                    }
                    break;
                case "Update kalender":
                    Database.getDatabase().updateEvents();
                    Toast.makeText(this.getActivity(), "Kalender geupdate", LENGTH_SHORT).show();
            }
        }

        /**
         * @param inflater,           the inflater
         * @param container,          container where the frament needs to go
         * @param savedInstanceState, the saved instances
         * @return the view
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Settings s = (Settings) getActivity();
            sharedpreferences = s.getSharedpreferences();
            ArrayAdapter<String> adapt = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, lijst);
            setListAdapter(adapt);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
