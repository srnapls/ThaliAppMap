package com.example.AppArt.thaliapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.Activity;
import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.Settings.Activities.Overview;
import com.example.AppArt.thaliapp.Settings.Activities.Login;
import com.example.AppArt.thaliapp.Settings.Activities.Notifications;

import java.util.ArrayList;

/**
 * Calendar activity, shows a list of all currently known ThaliaEvents that
 * have yet to end.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class ThaliappActivity extends Activity {
    public static final String MyPREFERENCES = "Settings";

    /**
     * @param savedInstanceState, saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.Calendar:
                startActivity(new Intent(this, Calendar.class));
                break;
            case R.id.Restaurant:
                startActivity(new Intent(this, Restaurant.class));
                break;
            case R.id.Notifications:
                startActivity(new Intent(this, Notifications.class));
                break;
            case R.id.Overview: {
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
        }
        return true;
    }


}
