package com.example.AppArt.thaliapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.Activity;
import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.support.v4.app.NavUtils;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.Settings.Activities.Overview;
import com.example.AppArt.thaliapp.Settings.Activities.Login;
import com.example.AppArt.thaliapp.Settings.Activities.About;
import com.example.AppArt.thaliapp.Settings.Activities.Notifications;


import java.util.ArrayList;

/**
 * Calendar activity, shows a list of all currently known ThaliaEvents that
 * have yet to end.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class ThaliappDrawerActivity extends ThaliappActivity implements ListView.OnItemClickListener {
    public static final String MyPREFERENCES = "Settings";

    private ActionBarDrawerToggle mDrawerToggle = null;
    private DrawerLayout mDrawerLayout = null;

    /**
     * @param savedInstanceState, saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    protected void prepareDrawer() {
        String[] menuItems = {"Kalender", "Eetlijst", "Inloggen", "Notificaties", "Over"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuItems));

        mDrawerToggle =
            new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0:
                startActivity(new Intent(this, Calendar.class));
                break;
            case 1:
                startActivity(new Intent(this, Restaurant.class));
                break;
            case 3:
                startActivity(new Intent(this, Notifications.class));
                break;
            case 2: {
                SharedPreferences p = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                boolean b = p.getBoolean("access", false);
                if (b) {
                    Intent i = new Intent(this, Overview.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, Login.class);
                    startActivity(i);
                }
            } break;
            case 4:
                startActivity(new Intent(this, About.class));
                break;
        }
        mDrawerLayout.closeDrawers();
    }

}
