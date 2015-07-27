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
import com.example.AppArt.thaliapp.Eetlijst.Activities.Restaurant;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Settings extends ActionBarActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Settings";

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

    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
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
                Intent intent4 = new Intent(this, Settings.class);
                startActivity(intent4);
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

        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            String s = l.getItemAtPosition(position).toString();
            switch (s) {
                case "Notificaties":
                    Intent intent;
                    intent = new Intent(getActivity(), Notifications.class);
/*Fail*/                    startActivity(intent);
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
