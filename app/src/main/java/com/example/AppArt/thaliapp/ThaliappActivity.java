package com.example.AppArt.thaliapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;

import java.util.ArrayList;

/**
 * Calendar activity, shows a list of all currently known ThaliaEvents that
 * have yet to end.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class ThaliappActivity extends ActionBarActivity {

    /**
     * @param savedInstanceState, saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu, menu that needs to be made
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_information, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here. The action bar will automatically
     * handle clicks on the Home/Up button, as long as you specify a parent
     * activity in AndroidManifest.xml.
     *
     * @param item on which is clicked
     * @return The action has prevailed!
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
            case R.id.Settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return true;
    }


}
